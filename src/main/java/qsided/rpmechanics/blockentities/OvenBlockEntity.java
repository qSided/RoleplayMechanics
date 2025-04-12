package qsided.rpmechanics.blockentities;

import it.unimi.dsi.fastutil.objects.Reference2IntOpenHashMap;
import net.minecraft.block.AbstractFurnaceBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.FuelRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.*;
import net.minecraft.recipe.input.SingleStackRecipeInput;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class OvenBlockEntity extends AbstractFurnaceBlockEntity {
    
    protected static final int INPUT_SLOT_INDEX = 0;
    protected static final int FUEL_SLOT_INDEX = 1;
    protected static final int OUTPUT_SLOT_INDEX = 2;
    public static final int BURN_TIME_PROPERTY_INDEX = 0;
    private static final int[] TOP_SLOTS = new int[]{0};
    private static final int[] BOTTOM_SLOTS = new int[]{2, 1};
    private static final int[] SIDE_SLOTS = new int[]{1};
    public static final int FUEL_TIME_PROPERTY_INDEX = 1;
    public static final int COOK_TIME_PROPERTY_INDEX = 2;
    public static final int COOK_TIME_TOTAL_PROPERTY_INDEX = 3;
    public static final int PROPERTY_COUNT = 4;
    public static final int DEFAULT_COOK_TIME = 200;
    public static final int field_31295 = 2;
    protected DefaultedList<ItemStack> inventory = DefaultedList.ofSize(3, ItemStack.EMPTY);
    int litTimeRemaining;
    int litTotalTime;
    int cookingTimeSpent;
    int cookingTotalTime;
    protected final PropertyDelegate propertyDelegate = new PropertyDelegate() {
        @Override
        public int get(int index) {
            switch (index) {
                case 0:
                    return OvenBlockEntity.this.litTimeRemaining;
                case 1:
                    return OvenBlockEntity.this.litTotalTime;
                case 2:
                    return OvenBlockEntity.this.cookingTimeSpent;
                case 3:
                    return OvenBlockEntity.this.cookingTotalTime;
                default:
                    return 0;
            }
        }
        
        @Override
        public void set(int index, int value) {
            switch (index) {
                case 0:
                    OvenBlockEntity.this.litTimeRemaining = value;
                    break;
                case 1:
                    OvenBlockEntity.this.litTotalTime = value;
                    break;
                case 2:
                    OvenBlockEntity.this.cookingTimeSpent = value;
                    break;
                case 3:
                    OvenBlockEntity.this.cookingTotalTime = value;
            }
        }
        
        @Override
        public int size() {
            return 4;
        }
    };
    private final Reference2IntOpenHashMap<RegistryKey<Recipe<?>>> recipesUsed = new Reference2IntOpenHashMap<>();
    private final ServerRecipeManager.MatchGetter<SingleStackRecipeInput, ? extends AbstractCookingRecipe> matchGetter;
    
    protected OvenBlockEntity(BlockEntityType<?> blockEntityType, BlockPos pos, BlockState state, RecipeType<? extends AbstractCookingRecipe> recipeType, ServerRecipeManager.MatchGetter<SingleStackRecipeInput, ? extends AbstractCookingRecipe> matchGetter) {
        super(blockEntityType, pos, state, recipeType);
        this.matchGetter = ServerRecipeManager.createCachedMatchGetter(recipeType);
    }
    
    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        super.readNbt(nbt, registries);
        this.inventory = DefaultedList.ofSize(this.size(), ItemStack.EMPTY);
        Inventories.readNbt(nbt, this.inventory, registries);
        this.cookingTimeSpent = nbt.getShort("cooking_time_spent");
        this.cookingTotalTime = nbt.getShort("cooking_total_time");
        this.litTimeRemaining = nbt.getShort("lit_time_remaining");
        this.litTotalTime = nbt.getShort("lit_total_time");
        NbtCompound nbtCompound = nbt.getCompound("RecipesUsed");
        
        for (String string : nbtCompound.getKeys()) {
            this.recipesUsed.put(RegistryKey.of(RegistryKeys.RECIPE, Identifier.of(string)), nbtCompound.getInt(string));
        }
    }
    
    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        super.writeNbt(nbt, registries);
        nbt.putShort("cooking_time_spent", (short)this.cookingTimeSpent);
        nbt.putShort("cooking_total_time", (short)this.cookingTotalTime);
        nbt.putShort("lit_time_remaining", (short)this.litTimeRemaining);
        nbt.putShort("lit_total_time", (short)this.litTotalTime);
        Inventories.writeNbt(nbt, this.inventory, registries);
        NbtCompound nbtCompound = new NbtCompound();
        this.recipesUsed.forEach((recipeKey, count) -> nbtCompound.putInt(recipeKey.getValue().toString(), count));
        nbt.put("RecipesUsed", nbtCompound);
    }
    
    public static void tick(World world, BlockPos pos, BlockState state, OvenBlockEntity blockEntity) {
        if (!world.isClient) {
            boolean isBurning = blockEntity.isBurning();
            boolean bl2 = false;
            if (blockEntity.isBurning()) {
                blockEntity.litTimeRemaining--;
            }
            
            ItemStack ingredient = blockEntity.inventory.get(1);
            ItemStack fuel = blockEntity.inventory.get(0);
            boolean hasIngredient = !fuel.isEmpty();
            boolean hasFuel = !ingredient.isEmpty();
            if (blockEntity.isBurning() || hasFuel && hasIngredient) {
                SingleStackRecipeInput singleStackRecipeInput = new SingleStackRecipeInput(fuel);
                RecipeEntry<? extends AbstractCookingRecipe> recipeEntry;
                if (hasIngredient) {
                    recipeEntry = (RecipeEntry<? extends AbstractCookingRecipe>) blockEntity.matchGetter.getFirstMatch(singleStackRecipeInput, (ServerWorld) world).orElse(null);
                } else {
                    recipeEntry = null;
                }
                
                int i = blockEntity.getMaxCountPerStack();
                if (!blockEntity.isBurning() && canAcceptRecipeOutput(world.getRegistryManager(), recipeEntry, singleStackRecipeInput, blockEntity.inventory, i)) {
                    blockEntity.litTimeRemaining = blockEntity.getFuelTime(world.getFuelRegistry(), ingredient);
                    blockEntity.litTotalTime = blockEntity.litTimeRemaining;
                    if (blockEntity.isBurning()) {
                        bl2 = true;
                        if (hasFuel) {
                            Item item = ingredient.getItem();
                            ingredient.decrement(1);
                            if (ingredient.isEmpty()) {
                                blockEntity.inventory.set(1, item.getRecipeRemainder());
                            }
                        }
                    }
                }
                
                if (blockEntity.isBurning() && canAcceptRecipeOutput(world.getRegistryManager(), recipeEntry, singleStackRecipeInput, blockEntity.inventory, i)) {
                    blockEntity.cookingTimeSpent++;
                    if (blockEntity.cookingTimeSpent == blockEntity.cookingTotalTime) {
                        blockEntity.cookingTimeSpent = 0;
                        blockEntity.cookingTotalTime = getCookTime((ServerWorld) world, blockEntity);
                        if (craftRecipe(world.getRegistryManager(), recipeEntry, singleStackRecipeInput, blockEntity.inventory, i)) {
                            blockEntity.setLastRecipe(recipeEntry);
                        }
                        
                        bl2 = true;
                    }
                } else {
                    blockEntity.cookingTimeSpent = 0;
                }
            } else if (!blockEntity.isBurning() && blockEntity.cookingTimeSpent > 0) {
                blockEntity.cookingTimeSpent = MathHelper.clamp(blockEntity.cookingTimeSpent - 2, 0, blockEntity.cookingTotalTime);
            }
            
            if (isBurning != blockEntity.isBurning()) {
                bl2 = true;
                state = state.with(AbstractFurnaceBlock.LIT, Boolean.valueOf(blockEntity.isBurning()));
                world.setBlockState(pos, state, Block.NOTIFY_ALL);
            }
            
            if (bl2) {
                markDirty(world, pos, state);
            }
        }
    }
    
    @Override
    protected Text getContainerName() {
        return Text.translatable(getCachedState().getBlock().getTranslationKey());
    }
    
    @Override
    protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
        return null;
    }
    
    private boolean isBurning() {
        return this.litTimeRemaining > 0;
    }
    
    private static boolean canAcceptRecipeOutput(
            DynamicRegistryManager dynamicRegistryManager,
            @Nullable RecipeEntry<? extends AbstractCookingRecipe> recipe,
            SingleStackRecipeInput input,
            DefaultedList<ItemStack> inventory,
            int maxCount
    ) {
        if (!inventory.get(0).isEmpty() && recipe != null) {
            ItemStack itemStack = recipe.value().craft(input, dynamicRegistryManager);
            if (itemStack.isEmpty()) {
                return false;
            } else {
                ItemStack itemStack2 = inventory.get(2);
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
    
    private static boolean craftRecipe(
            DynamicRegistryManager dynamicRegistryManager,
            @Nullable RecipeEntry<? extends AbstractCookingRecipe> recipe,
            SingleStackRecipeInput input,
            DefaultedList<ItemStack> inventory,
            int maxCount
    ) {
        if (recipe != null && canAcceptRecipeOutput(dynamicRegistryManager, recipe, input, inventory, maxCount)) {
            ItemStack itemStack = inventory.get(0);
            ItemStack itemStack2 = recipe.value().craft(input, dynamicRegistryManager);
            ItemStack itemStack3 = inventory.get(2);
            if (itemStack3.isEmpty()) {
                inventory.set(2, itemStack2.copy());
            } else if (ItemStack.areItemsAndComponentsEqual(itemStack3, itemStack2)) {
                itemStack3.increment(1);
            }
            
            if (itemStack.isOf(Blocks.WET_SPONGE.asItem()) && !inventory.get(1).isEmpty() && inventory.get(1).isOf(Items.BUCKET)) {
                inventory.set(1, new ItemStack(Items.WATER_BUCKET));
            }
            
            itemStack.decrement(1);
            return true;
        } else {
            return false;
        }
    }
    
    protected int getFuelTime(FuelRegistry fuelRegistry, ItemStack stack) {
        return fuelRegistry.getFuelTicks(stack);
    }
    
    private static int getCookTime(ServerWorld world, OvenBlockEntity furnace) {
        SingleStackRecipeInput singleStackRecipeInput = new SingleStackRecipeInput(furnace.getStack(0));
        return (Integer)furnace.matchGetter
                .getFirstMatch(singleStackRecipeInput, world)
                .map(recipe -> ((AbstractCookingRecipe)recipe.value()).getCookingTime())
                .orElse(200);
    }
}
