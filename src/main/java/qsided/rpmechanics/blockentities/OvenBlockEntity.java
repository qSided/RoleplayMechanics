package qsided.rpmechanics.blockentities;

import com.google.common.collect.Lists;
import io.wispforest.owo.util.ImplementedInventory;
import it.unimi.dsi.fastutil.objects.Reference2IntMap;
import it.unimi.dsi.fastutil.objects.Reference2IntOpenHashMap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.ExperienceOrbEntity;
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
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.ServerRecipeManager;
import net.minecraft.recipe.input.SingleStackRecipeInput;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import qsided.rpmechanics.PlayerData;
import qsided.rpmechanics.RoleplayMechanicsCommon;
import qsided.rpmechanics.StateManager;
import qsided.rpmechanics.events.IncreaseSkillExperienceCallback;
import qsided.rpmechanics.gui.OvenScreenHandler;
import qsided.rpmechanics.items.QuesComponents;
import qsided.rpmechanics.recipes.QuesRecipeTypes;

import java.util.*;

import static qsided.rpmechanics.RoleplayMechanicsCommon.OWO_CONFIG;

public class OvenBlockEntity extends BlockEntity implements NamedScreenHandlerFactory, ImplementedInventory {
    
    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(7, ItemStack.EMPTY);
    private static final int FUEL = 0;
    protected final PropertyDelegate propertyDelegate;
    int litTimeRemaining;
    int litTimeTotal;
    int cookingTimeSpent;
    int cookingTimeTotal;
    UUID chefUUID;
    private final Reference2IntOpenHashMap<RegistryKey<Recipe<?>>> recipesUsed = new Reference2IntOpenHashMap<>();
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
    
    private boolean hasAtLeastOneIngredient(List<ItemStack> stacks) {
        return stacks.stream().filter(stack -> !stack.isEmpty()).count() > 0 && stacks.stream().allMatch(stack -> !stack.getComponents().contains(QuesComponents.COOK_QUALITY));
    }
    
    public static void tick(World world, BlockPos pos, BlockState blockState, OvenBlockEntity blockEntity) {
        if (!world.isClient()) {
            boolean shouldBeLit = blockEntity.isBurning();
            boolean isDone = false;
            if (blockEntity.isBurning()) {
                blockEntity.litTimeRemaining--;
            }
            List<ItemStack> ingredients = new ArrayList<>();
            ingredients.add(blockEntity.inventory.get(1));
            ingredients.add(blockEntity.inventory.get(2));
            ingredients.add(blockEntity.inventory.get(3));
            ingredients.add(blockEntity.inventory.get(4));
            ingredients.add(blockEntity.inventory.get(5));
            ingredients.add(blockEntity.inventory.get(6));
            ItemStack fuel = blockEntity.inventory.get(FUEL);
            boolean hasFuel = !fuel.isEmpty();
            if (blockEntity.isBurning() || (blockEntity.hasAtLeastOneIngredient(ingredients) && hasFuel)) {
                int i = blockEntity.getMaxCountPerStack();
                if (!blockEntity.isBurning() && ingredients.stream().anyMatch(stack -> !stack.getComponents().contains(QuesComponents.COOK_QUALITY))) {
                    blockEntity.litTimeRemaining = blockEntity.getFuelTime(world.getFuelRegistry(), fuel);
                    blockEntity.litTimeTotal = blockEntity.getFuelTime(world.getFuelRegistry(), fuel);
                    if (blockEntity.isBurning() && ingredients.stream().allMatch(stack -> !stack.isEmpty() && !stack.getComponents().contains(QuesComponents.COOK_QUALITY))) {
                        isDone = true;
                        if (hasFuel) {
                            Item item = fuel.getItem();
                            fuel.decrement(1);
                            if (fuel.isEmpty()) {
                                blockEntity.inventory.set(0, item.getRecipeRemainder());
                            }
                        }
                    }
                }
                
                if (blockEntity.isBurning() &&
                        ingredients.stream().anyMatch(stack -> !stack.getComponents().contains(QuesComponents.COOK_QUALITY) && !stack.isEmpty())) {
                    if (world.getPlayerByUuid(blockEntity.getChefUUID()) != null) {
                        PlayerData state = StateManager.getPlayerState(Objects.requireNonNull(world.getPlayerByUuid(blockEntity.getChefUUID())));
                        blockEntity.cookingTimeSpent = (int) Math.min(blockEntity.cookingTimeSpent + 1 + ((state.skillLevels.getOrDefault("cooking", 1) * OWO_CONFIG.skillOptions.cookingSettings.speed())), 720);
                    } else {
                        blockEntity.cookingTimeSpent++;
                    }
                    if (blockEntity.cookingTimeSpent >= blockEntity.cookingTimeTotal) {
                        blockEntity.cookingTimeSpent = 0;
                        blockEntity.cookingTimeTotal = getCookTime((ServerWorld) world, blockEntity);
                        ingredients.forEach(stack -> {
                            SingleStackRecipeInput singleStackRecipeInput = new SingleStackRecipeInput(stack);
                            RecipeEntry<? extends AbstractCookingRecipe> recipeEntry;
                            if (!stack.isEmpty()) {
                                recipeEntry = blockEntity.matchGetter.getFirstMatch(singleStackRecipeInput, (ServerWorld) world).orElse(null);
                            } else {
                                recipeEntry = null;
                            }
                            
                            craftRecipe(world, blockEntity.getChefUUID() ,world.getRegistryManager(), recipeEntry, singleStackRecipeInput, ingredients.indexOf(stack)+1, blockEntity.inventory, i);
                            
                            if (recipeEntry != null) {
                                blockEntity.recipesUsed.addTo(recipeEntry.id(), 1);
                            }
                            
                        });
                        
                        
                        isDone = true;
                    }
                } else {
                    blockEntity.cookingTimeSpent = 0;
                }
            } else if (!blockEntity.isBurning() && blockEntity.cookingTimeSpent > 0) {
                blockEntity.cookingTimeSpent = MathHelper.clamp(blockEntity.cookingTimeSpent - 2, 0, blockEntity.cookingTimeTotal);
            }
            
            if (shouldBeLit != blockEntity.isBurning()) {
                isDone = true;
                blockState = blockState.with(Properties.LIT, blockEntity.isBurning());
                world.setBlockState(pos, blockState, Block.NOTIFY_ALL);
            }
            
            if (isDone) {
                markDirty(world, pos, blockState);
            }
        }
    }
    
    public static String getQuality(double chanceForPerfect, double chanceForBurnt, String perfect, String burnt, String average) {
        if (chanceForPerfect + chanceForBurnt > 1 || chanceForPerfect < 0 || chanceForBurnt < 0) {
            throw new IllegalArgumentException("Probabilities must be non-negative and sum up to at most 1");
        }
        
        Random random = new Random();
        double randomNumber = random.nextDouble();
        
        if (randomNumber < chanceForPerfect) {
            return perfect;
        } else if (randomNumber < chanceForPerfect + chanceForBurnt) {
            return burnt;
        } else {
            return average;
        }
    }
    
    public void dropExperienceForRecipesUsed(ServerPlayerEntity player) {
        List<RecipeEntry<?>> list = this.getRecipesUsedAndDropExperience(player.getServerWorld(), player.getPos());
        player.unlockRecipes(list);
        
        for (RecipeEntry<?> recipeEntry : list) {
            if (recipeEntry != null) {
                player.onRecipeCrafted(recipeEntry, this.inventory);
            }
        }
        
        this.recipesUsed.clear();
    }
    
    public List<RecipeEntry<?>> getRecipesUsedAndDropExperience(ServerWorld world, Vec3d pos) {
        List<RecipeEntry<?>> list = Lists.newArrayList();
        
        for (Reference2IntMap.Entry<RegistryKey<Recipe<?>>> entry : this.recipesUsed.reference2IntEntrySet()) {
            world.getRecipeManager().get(entry.getKey()).ifPresent(recipe -> {
                list.add(recipe);
                dropExperience(world, pos, entry.getIntValue(), ((AbstractCookingRecipe)recipe.value()).getExperience());
            });
        }
        
        return list;
    }
    
    private static void dropExperience(ServerWorld world, Vec3d pos, int multiplier, float experience) {
        int i = MathHelper.floor((float)multiplier * experience);
        float f = MathHelper.fractionalPart((float)multiplier * experience);
        if (f != 0.0F && Math.random() < (double)f) {
            i++;
        }
        
        ExperienceOrbEntity.spawn(world, pos, i);
    }
    
    private static void craftRecipe(
            World world,
            UUID chefUUID,
            DynamicRegistryManager dynamicRegistryManager,
            @Nullable RecipeEntry<? extends AbstractCookingRecipe> recipe,
            SingleStackRecipeInput input,
            int slotId,
            DefaultedList<ItemStack> inventory,
            int maxCount
    ) {
        if (recipe != null) {
            ItemStack itemToCraft = recipe.value().craft(input, dynamicRegistryManager);
            PlayerData state = StateManager.getPlayerState(Objects.requireNonNull(world.getPlayerByUuid(chefUUID)));
            double chanceForPerfect = 0 + (state.skillLevels.getOrDefault("cooking", 1) * OWO_CONFIG.skillOptions.cookingSettings.perfectChanceIncrease());
            double chanceForBurnt = Math.max(0, OWO_CONFIG.skillOptions.cookingSettings.baseBurnChance() - (state.skillLevels.getOrDefault("cooking", 1) * OWO_CONFIG.skillOptions.cookingSettings.burnChanceDecrease()));
            
            String quality = getQuality(chanceForPerfect, chanceForBurnt, "perfect", "burnt", "average");
            
            switch (quality) {
                case "perfect" -> {
                    itemToCraft.set(QuesComponents.COOK_QUALITY, quality);
                    itemToCraft.set(DataComponentTypes.FOOD, new FoodComponent(6, 0.6f, true));
                    IncreaseSkillExperienceCallback.EVENT.invoker().increaseExp((ServerPlayerEntity) world.getPlayerByUuid(chefUUID), state, "cooking", 16F);
                }
                case "burnt" -> {
                    itemToCraft.set(QuesComponents.COOK_QUALITY, quality);
                    itemToCraft.set(DataComponentTypes.FOOD, new FoodComponent(2, 0.1f, true));
                    IncreaseSkillExperienceCallback.EVENT.invoker().increaseExp((ServerPlayerEntity) world.getPlayerByUuid(chefUUID), state, "cooking", 8F);
                }
                case "average" -> {
                    itemToCraft.set(QuesComponents.COOK_QUALITY, quality);
                    IncreaseSkillExperienceCallback.EVENT.invoker().increaseExp((ServerPlayerEntity) world.getPlayerByUuid(chefUUID), state, "cooking", 8F);
                }
                
            }
            
            inventory.set(slotId, itemToCraft);
        }
    }
    
    protected int getFuelTime(FuelRegistry fuelRegistry, ItemStack stack) {
        return fuelRegistry.getFuelTicks(stack);
    }
    
    private static int getCookTime(ServerWorld world, OvenBlockEntity furnace) {
        //SingleStackRecipeInput singleStackRecipeInput = new SingleStackRecipeInput(furnace.getStack(RECIPE));
        //List<ItemStack> ingredients = new ArrayList<>();
        //ingredients.add(furnace.inventory.get(1));
        //ingredients.add(furnace.inventory.get(2));
        //ingredients.add(furnace.inventory.get(3));
        //ingredients.add(furnace.inventory.get(4));
        //ingredients.add(furnace.inventory.get(5));
        //ingredients.add(furnace.inventory.get(6));
        //PlayerData state = StateManager.getPlayerState(Objects.requireNonNull(world.getPlayerByUuid(furnace.getChefUUID())));
        //int speedTimesAmount = Math.max(120, (int) (120 * ingredients.stream().filter(stack -> !stack.isEmpty()).count()));
        return 200;
        //return Math.max(120, (int) (speedTimesAmount - (speedTimesAmount * (state.skillLevels.getOrDefault("cooking", 1) * 0.1))));
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
        stack.capCount(64);
        if (slot == 0 && !bl && this.world instanceof ServerWorld serverWorld) {
            cookingTimeTotal = getCookTime(serverWorld, this);
            cookingTimeSpent = 0;
            this.markDirty();
        }
    }
    
    @Override
    public boolean isValid(int slot, ItemStack stack) {
        if (slot != 0) {
            return false;
        } else {
            ItemStack itemStack = this.inventory.get(0);
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
        Inventories.readNbt(nbt, inventory, registries);
        litTimeRemaining = nbt.getInt("oven.lit_time_remaining");
        litTimeTotal = nbt.getInt("oven.lit_time_total");
        cookingTimeSpent = nbt.getInt("oven.cooking_time_spent");
        cookingTimeTotal = nbt.getInt("oven.cooking_time_total");
        chefUUID = nbt.getUuid("oven.chef");
    }
    
    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        nbt.putInt("oven.lit_time_remaining", litTimeRemaining);
        nbt.putInt("oven.lit_time_total", litTimeTotal);
        nbt.putInt("oven.cooking_time_spent", cookingTimeSpent);
        nbt.putInt("oven.cooking_time_total", cookingTimeTotal);
        nbt.putUuid("oven.chef", chefUUID);
        Inventories.writeNbt(nbt, inventory, registries);
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
    
    public UUID getChefUUID() {
        return chefUUID;
    }
    
    public void setChefUUID(UUID chefUUID) {
        this.chefUUID = chefUUID;
    }
}
