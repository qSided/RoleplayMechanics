package qsided.rpmechanics.blockentities;

import io.wispforest.owo.util.ImplementedInventory;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.FuelRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.recipe.AbstractCookingRecipe;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.ServerRecipeManager;
import net.minecraft.recipe.input.SingleStackRecipeInput;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import qsided.rpmechanics.blocks.OvenBlock;
import qsided.rpmechanics.gui.OvenScreenHandler;
import qsided.rpmechanics.recipes.QuesRecipeTypes;

public class OvenBlockEntity extends BlockEntity implements NamedScreenHandlerFactory, ImplementedInventory {
    
    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(3, ItemStack.EMPTY);
    private static final int FUEL = 0;
    private static final int RECIPE = 1;
    private static final int OUTPUT = 2;
    protected final PropertyDelegate propertyDelegate;
    private static int litTimeRemaining;
    private static int litTimeTotal;
    private static int cookingTimeSpent;
    private static int cookingTimeTotal;
    private final ServerRecipeManager.MatchGetter<SingleStackRecipeInput, ? extends AbstractCookingRecipe> matchGetter;
    
    public OvenBlockEntity(BlockPos pos, BlockState state) {
        super(QuesBlockEntityTypes.OVEN_BLOCK, pos, state);
        this.propertyDelegate = new PropertyDelegate() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> litTimeRemaining;
                    case 1 -> litTimeTotal;
                    case 2 -> cookingTimeSpent;
                    case 3 -> cookingTimeTotal;
                    default -> 0;
                };
            }
            
            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0: litTimeRemaining = value;
                    case 1: litTimeTotal = value;
                    case 2: cookingTimeSpent = value;
                    case 3: cookingTimeTotal = value;
                }
            }
            
            @Override
            public int size() {
                return 4;
            }
        };
        this.matchGetter = ServerRecipeManager.createCachedMatchGetter(QuesRecipeTypes.OVEN_RECIPE_TYPE);
    }
    
    private boolean isBurning() {
        return litTimeRemaining > 0;
    }
    
    public static void tick(World world, BlockPos pos, BlockState blockState, OvenBlockEntity blockEntity) {
        if (!world.isClient()) {
            boolean shouldBeLit = blockEntity.isBurning();
            boolean isDone = false;
            if (blockEntity.isBurning()) {
                litTimeRemaining--;
            }
            ItemStack ingredient = blockEntity.inventory.get(RECIPE);
            ItemStack fuel = blockEntity.inventory.get(FUEL);
            boolean hasFuel = !fuel.isEmpty();
            boolean hasIngredient = !ingredient.isEmpty();
            if (blockEntity.isBurning() || hasIngredient && hasFuel) {
                SingleStackRecipeInput singleStackRecipeInput = new SingleStackRecipeInput(fuel);
                RecipeEntry<? extends AbstractCookingRecipe> recipeEntry;
                if (hasFuel) {
                    recipeEntry = blockEntity.matchGetter.getFirstMatch(singleStackRecipeInput, (ServerWorld) world).orElse(null);
                } else {
                    recipeEntry = null;
                }
                
                int i = blockEntity.getMaxCountPerStack();
                if (!blockEntity.isBurning() && canAcceptRecipeOutput(world.getRegistryManager(), recipeEntry, singleStackRecipeInput, blockEntity.inventory, i)) {
                    litTimeRemaining = blockEntity.getFuelTime(world.getFuelRegistry(), ingredient);
                    litTimeTotal = litTimeRemaining;
                    if (blockEntity.isBurning()) {
                        isDone = true;
                        if (hasIngredient) {
                            Item item = ingredient.getItem();
                            ingredient.decrement(1);
                            if (ingredient.isEmpty()) {
                                blockEntity.inventory.set(1, item.getRecipeRemainder());
                            }
                        }
                    }
                }
                
                if (blockEntity.isBurning() && canAcceptRecipeOutput(world.getRegistryManager(), recipeEntry, singleStackRecipeInput, blockEntity.inventory, i)) {
                    cookingTimeSpent++;
                    if (cookingTimeSpent == cookingTimeTotal) {
                        cookingTimeSpent = 0;
                        cookingTimeTotal = getCookTime((ServerWorld) world, blockEntity);
                        craftRecipe(world.getRegistryManager(), recipeEntry, singleStackRecipeInput, blockEntity.inventory, i);
                        
                        isDone = true;
                    }
                } else {
                    cookingTimeSpent = 0;
                }
            } else if (!blockEntity.isBurning() && cookingTimeSpent > 0) {
                cookingTimeSpent = MathHelper.clamp(cookingTimeSpent - 2, 0, cookingTimeTotal);
            }
            
            if (shouldBeLit != blockEntity.isBurning()) {
                isDone = true;
                blockState = blockState.with(OvenBlock.LIT, Boolean.valueOf(blockEntity.isBurning()));
                world.setBlockState(pos, blockState, Block.NOTIFY_ALL);
            }
            
            if (isDone) {
                markDirty(world, pos, blockState);
            }
        }
    }
    
    private static boolean canAcceptRecipeOutput(
            DynamicRegistryManager dynamicRegistryManager,
            @Nullable RecipeEntry<? extends AbstractCookingRecipe> recipe,
            SingleStackRecipeInput input,
            DefaultedList<ItemStack> inventory,
            int maxCount
    ) {
        if (!inventory.get(FUEL).isEmpty() && recipe != null) {
            ItemStack itemStack = recipe.value().craft(input, dynamicRegistryManager);
            if (itemStack.isEmpty()) {
                return false;
            } else {
                ItemStack itemStack2 = inventory.get(OUTPUT);
                if (itemStack2.isEmpty()) {
                    return true;
                } else if (!ItemStack.areItemsAndComponentsEqual(itemStack2, itemStack)) {
                    return false;
                } else {
                    return itemStack2.getCount() < maxCount && itemStack2.getCount() < itemStack2.getMaxCount() ? true : itemStack2.getCount() < itemStack.getMaxCount();
                }
            }
        } else {
            return false;
        }
    }
    
    private static void craftRecipe(
            DynamicRegistryManager dynamicRegistryManager,
            @Nullable RecipeEntry<? extends AbstractCookingRecipe> recipe,
            SingleStackRecipeInput input,
            DefaultedList<ItemStack> inventory,
            int maxCount
    ) {
        if (recipe != null && canAcceptRecipeOutput(dynamicRegistryManager, recipe, input, inventory, maxCount)) {
            ItemStack itemStack = inventory.get(FUEL);
            ItemStack itemStack2 = recipe.value().craft(input, dynamicRegistryManager);
            ItemStack itemStack3 = inventory.get(OUTPUT);
            if (itemStack3.isEmpty()) {
                inventory.set(OUTPUT, itemStack2.copy());
            } else if (ItemStack.areItemsAndComponentsEqual(itemStack3, itemStack2)) {
                itemStack3.increment(1);
            }
            
            if (itemStack.isOf(Blocks.WET_SPONGE.asItem()) && !inventory.get(RECIPE).isEmpty() && inventory.get(RECIPE).isOf(Items.BUCKET)) {
                inventory.set(RECIPE, new ItemStack(Items.WATER_BUCKET));
            }
            
            itemStack.decrement(1);
        }
    }
    
    protected int getFuelTime(FuelRegistry fuelRegistry, ItemStack stack) {
        return fuelRegistry.getFuelTicks(stack);
    }
    
    private static int getCookTime(ServerWorld world, OvenBlockEntity furnace) {
        SingleStackRecipeInput singleStackRecipeInput = new SingleStackRecipeInput(furnace.getStack(RECIPE));
        return furnace.matchGetter
                .getFirstMatch(singleStackRecipeInput, world)
                .map(recipe -> (recipe.value()).getCookingTime())
                .orElse(120);
    }
    
    @Override
    public int size() {
        return this.inventory.size();
    }
    
    @Override
    public void setStack(int slot, ItemStack stack) {
        ItemStack itemStack = this.inventory.get(slot);
        boolean bl = !stack.isEmpty() && ItemStack.areItemsAndComponentsEqual(itemStack, stack);
        this.inventory.set(slot, stack);
        stack.capCount(this.getMaxCount(stack));
        if (slot == 0 && !bl && this.world instanceof ServerWorld serverWorld) {
            cookingTimeTotal = getCookTime(serverWorld, this);
            cookingTimeSpent = 0;
            this.markDirty();
        }
    }
    
    @Override
    public boolean isValid(int slot, ItemStack stack) {
        if (slot == 2) {
            return false;
        } else if (slot != 1) {
            return true;
        } else {
            ItemStack itemStack = this.inventory.get(1);
            return this.world.getFuelRegistry().isFuel(stack) || stack.isOf(Items.BUCKET) && !itemStack.isOf(Items.BUCKET);
        }
    }
    
    @Override
    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }
    
    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        super.readNbt(nbt, registries);
        //Inventories.readNbt(nbt, inventory, registries);
        litTimeRemaining = nbt.getInt("oven.lit_time_remaining");
        litTimeTotal = nbt.getInt("oven.lit_time_total");
        cookingTimeSpent = nbt.getInt("oven.cooking_time_spent");
        cookingTimeTotal = nbt.getInt("oven.cooking_time_total");
    }
    
    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        nbt.putInt("oven.lit_time_remaining", litTimeRemaining);
        nbt.putInt("oven.lit_time_total", litTimeTotal);
        nbt.putInt("oven.cooking_time_spent", cookingTimeSpent);
        nbt.putInt("oven.cooking_time_total", cookingTimeTotal);
        //Inventories.writeNbt(nbt, inventory, registries);
        super.writeNbt(nbt, registries);
    }
    
    @Override
    public Text getDisplayName() {
        return Text.translatable(getCachedState().getBlock().getTranslationKey());
    }
    
    @Override
    public @Nullable ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new OvenScreenHandler(syncId, playerInventory, this, this.propertyDelegate);
    }
    
    @Override
    public @Nullable Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }
    
    @Override
    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registries) {
        return createNbt(registries);
    }
}
