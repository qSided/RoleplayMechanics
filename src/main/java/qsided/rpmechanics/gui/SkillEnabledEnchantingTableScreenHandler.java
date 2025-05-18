package qsided.rpmechanics.gui;

import com.mojang.datafixers.util.Pair;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.registry.tag.EnchantmentTags;
import net.minecraft.screen.*;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.collection.IndexedIterable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import qsided.rpmechanics.PlayerData;
import qsided.rpmechanics.RoleplayMechanicsCommon;
import qsided.rpmechanics.StateManager;
import qsided.rpmechanics.blocks.QuesBlocks;
import qsided.rpmechanics.blocks.SkillEnabledEnchantingTable;
import qsided.rpmechanics.events.IncreaseSkillExperienceCallback;
import qsided.rpmechanics.skills.milestones.SkillMilestone;

import java.util.List;
import java.util.Optional;

public class SkillEnabledEnchantingTableScreenHandler extends ScreenHandler {
    
    static final Identifier EMPTY_LAPIS_SLOT_TEXTURE = Identifier.ofVanilla("item/empty_slot_lapis_lazuli");
    private final Inventory inventory;
    private final ScreenHandlerContext context;
    private final Random random;
    private final Property seed;
    public final int[] enchantmentPower;
    public final int[] enchantmentId;
    public final int[] enchantmentLevel;
    
    public SkillEnabledEnchantingTableScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, ScreenHandlerContext.EMPTY);
    }
    
    public SkillEnabledEnchantingTableScreenHandler(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(QuesBlocks.SE_ENCHANTING_TABLE_SCREEN_HANDLER, syncId);
        this.inventory = new SimpleInventory(2) {
            public void markDirty() {
                super.markDirty();
                SkillEnabledEnchantingTableScreenHandler.this.onContentChanged(this);
            }
        };
        this.random = Random.create();
        this.seed = Property.create();
        this.enchantmentPower = new int[6];
        this.enchantmentId = new int[]{-1, -1, -1, -1, -1, -1};
        this.enchantmentLevel = new int[]{-1, -1, -1, -1, -1, -1};
        this.context = context;
        this.addSlot(new Slot(this.inventory, 0, 15, 47) {
            public int getMaxItemCount() {
                return 1;
            }
        });
        this.addSlot(new Slot(this.inventory, 1, 35, 47) {
            public boolean canInsert(ItemStack stack) {
                return stack.isOf(Items.LAPIS_LAZULI);
            }
            
            public Pair<Identifier, Identifier> getBackgroundSprite() {
                return Pair.of(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, SkillEnabledEnchantingTableScreenHandler.EMPTY_LAPIS_SLOT_TEXTURE);
            }
        });
        
        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
        
        for(int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
        
        this.addProperty(Property.create(this.enchantmentPower, 0));
        this.addProperty(Property.create(this.enchantmentPower, 1));
        this.addProperty(Property.create(this.enchantmentPower, 2));
        this.addProperty(Property.create(this.enchantmentPower, 3));
        this.addProperty(Property.create(this.enchantmentPower, 4));
        this.addProperty(Property.create(this.enchantmentPower, 5));
        this.addProperty(this.seed).set(playerInventory.player.getEnchantmentTableSeed());
        this.addProperty(Property.create(this.enchantmentId, 0));
        this.addProperty(Property.create(this.enchantmentId, 1));
        this.addProperty(Property.create(this.enchantmentId, 2));
        this.addProperty(Property.create(this.enchantmentId, 3));
        this.addProperty(Property.create(this.enchantmentId, 4));
        this.addProperty(Property.create(this.enchantmentId, 5));
        this.addProperty(Property.create(this.enchantmentLevel, 0));
        this.addProperty(Property.create(this.enchantmentLevel, 1));
        this.addProperty(Property.create(this.enchantmentLevel, 2));
        this.addProperty(Property.create(this.enchantmentLevel, 3));
        this.addProperty(Property.create(this.enchantmentLevel, 4));
        this.addProperty(Property.create(this.enchantmentLevel, 5));
    }
    
    public void onContentChanged(Inventory inventory) {
        if (inventory == this.inventory) {
            ItemStack itemStack = inventory.getStack(0);
            if (!itemStack.isEmpty() && itemStack.isEnchantable()) {
                this.context.run((world, pos) -> {
                    IndexedIterable<RegistryEntry<Enchantment>> indexedIterable = world.getRegistryManager().get(RegistryKeys.ENCHANTMENT).getIndexedEntries();
                    int i = 0;
                    
                    for(BlockPos blockPos : SkillEnabledEnchantingTable.POWER_PROVIDER_OFFSETS) {
                        if (SkillEnabledEnchantingTable.canAccessPowerProvider(world, pos, blockPos)) {
                            ++i;
                        }
                    }
                    
                    this.random.setSeed(this.seed.get());
                    
                    for(int j = 0; j < 6; ++j) {
                        this.enchantmentPower[j] = EnchantmentHelper.calculateRequiredExperienceLevel(this.random, j, i, itemStack);
                        this.enchantmentId[j] = -1;
                        this.enchantmentLevel[j] = -1;
                        if (this.enchantmentPower[j] < j + 1) {
                            this.enchantmentPower[j] = 0;
                        }
                    }
                    
                    for(int j = 0; j < 6; ++j) {
                        if (this.enchantmentPower[j] > 0) {
                            if (player() instanceof ServerPlayerEntity player) {
                                PlayerData state = StateManager.getPlayerState(player);
                                int enchantingLevel = state.skillLevels.getOrDefault("enchanting", 1);
                                
                                int enchantPowerModifier = 0;
                                for (SkillMilestone skillMilestone : RoleplayMechanicsCommon.getMilestones()) {
                                    if (skillMilestone.hasMet(enchantingLevel) &&
                                            skillMilestone.getSkill().equals("enchanting") &&
                                            skillMilestone.getRewardType().equals("ENCHANT_POWER") &&
                                            skillMilestone.getRewardAmount() != null) {
                                        enchantPowerModifier += skillMilestone.getRewardAmount();
                                    }
                                }
                                this.enchantmentPower[j] = this.enchantmentPower[j] + enchantPowerModifier;
                            }
                            List<EnchantmentLevelEntry> list = this.generateEnchantments(world.getRegistryManager(), itemStack, j, this.enchantmentPower[j]);
                            if (list != null && !list.isEmpty()) {
                                EnchantmentLevelEntry enchantmentLevelEntry = list.get(this.random.nextInt(list.size()));
                                this.enchantmentId[j] = indexedIterable.getRawId(enchantmentLevelEntry.enchantment);
                                this.enchantmentLevel[j] = enchantmentLevelEntry.level;
                            }
                        }
                    }
                    
                    this.sendContentUpdates();
                });
            } else {
                for(int i = 0; i < 6; ++i) {
                    this.enchantmentPower[i] = 0;
                    this.enchantmentId[i] = -1;
                    this.enchantmentLevel[i] = -1;
                }
            }
        }
        
    }
    
    public boolean onButtonClick(PlayerEntity player, int id) {
        if (id >= 0 && id < this.enchantmentPower.length) {
            ItemStack itemToEnchant = this.inventory.getStack(0);
            ItemStack lapis = this.inventory.getStack(1);
            int i = id + 1;
            if ((lapis.isEmpty() || lapis.getCount() < i) && !player.isInCreativeMode()) {
                return false;
            } else if (this.enchantmentPower[id] <= 0 || itemToEnchant.isEmpty() || (player.experienceLevel < i || player.experienceLevel < this.enchantmentPower[id]) && !player.getAbilities().creativeMode) {
                return false;
            } else {
                this.context.run((world, pos) -> {
                    PlayerData state = StateManager.getPlayerState(player);
                    int enchantingLevel = state.skillLevels.getOrDefault("enchanting", 1);
                    
                    int enchantPowerModifier = 0;
                    for (SkillMilestone skillMilestone : RoleplayMechanicsCommon.getMilestones()) {
                        if (skillMilestone.hasMet(enchantingLevel) && skillMilestone.getSkill().equals("enchanting") && skillMilestone.getRewardType().equals("ENCHANT_POWER")) {
                            enchantPowerModifier += skillMilestone.getRewardAmount();
                        }
                    }
                    this.enchantmentPower[id] = this.enchantmentPower[id] + enchantPowerModifier;
                    ItemStack enchantedItem = itemToEnchant;
                    List<EnchantmentLevelEntry> enchantments = this.generateEnchantments(world.getRegistryManager(), itemToEnchant, id, this.enchantmentPower[id]);
                    if (!enchantments.isEmpty()) {
                        
                        int cost = 0;
                        for (SkillMilestone skillMilestone : RoleplayMechanicsCommon.getMilestones()) {
                            if (skillMilestone.hasMet(enchantingLevel) &&
                                    skillMilestone.getSkill().equals("enchanting") &&
                                    skillMilestone.getRewardType().equals("ENCHANT_COST_REDUCTION") &&
                                    skillMilestone.getRewardAmount() != null) {
                                cost += skillMilestone.getRewardAmount();
                            }
                        }
                        
                        if (cost < 1) {
                            cost = 1;
                        }
                        
                        player.applyEnchantmentCosts(itemToEnchant, cost);
                        if (itemToEnchant.isOf(Items.BOOK)) {
                            enchantedItem = itemToEnchant.withItem(Items.ENCHANTED_BOOK);
                            this.inventory.setStack(0, enchantedItem);
                        }
                        
                        for(EnchantmentLevelEntry enchantmentLevelEntry : enchantments) {
                            enchantedItem.addEnchantment(enchantmentLevelEntry.enchantment, enchantmentLevelEntry.level);
                        }
                        
                        lapis.decrementUnlessCreative(cost, player);
                        if (lapis.isEmpty()) {
                            this.inventory.setStack(1, ItemStack.EMPTY);
                        }
                        
                        player.incrementStat(Stats.ENCHANT_ITEM);
                        if (player instanceof ServerPlayerEntity) {
                            IncreaseSkillExperienceCallback.EVENT.invoker().increaseExp((ServerPlayerEntity) player, state, "enchanting", (float) enchantmentPower[id]);
                            Criteria.ENCHANTED_ITEM.trigger((ServerPlayerEntity)player, enchantedItem, i);
                        }
                        
                        this.inventory.markDirty();
                        this.seed.set(player.getEnchantmentTableSeed());
                        this.onContentChanged(this.inventory);
                        world.playSound(null, pos, SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.BLOCKS, 1.0F, world.random.nextFloat() * 0.1F + 0.9F);
                    }
                    
                });
                return true;
            }
        } else {
            String playerName = String.valueOf(player.getName());
            Util.error(playerName + " pressed invalid button id: " + id);
            return false;
        }
    }
    
    private List<EnchantmentLevelEntry> generateEnchantments(DynamicRegistryManager registryManager, ItemStack stack, int slot, int level) {
        this.random.setSeed((this.seed.get() + slot));
        Optional<RegistryEntryList.Named<Enchantment>> optional = registryManager.get(RegistryKeys.ENCHANTMENT).getEntryList(EnchantmentTags.IN_ENCHANTING_TABLE);
        if (optional.isEmpty()) {
            return List.of();
        } else {
            List<EnchantmentLevelEntry> list = EnchantmentHelper.generateEnchantments(this.random, stack, level, ((RegistryEntryList.Named)optional.get()).stream());
            if (stack.isOf(Items.BOOK) && list.size() > 1) {
                list.remove(this.random.nextInt(list.size()));
            }
            
            return list;
        }
    }
    
    public int getLapisCount() {
        ItemStack itemStack = this.inventory.getStack(1);
        return itemStack.isEmpty() ? 0 : itemStack.getCount();
    }
    
    public int getSeed() {
        return this.seed.get();
    }
    
    public void onClosed(PlayerEntity player) {
        super.onClosed(player);
        this.context.run((world, pos) -> this.dropInventory(player, this.inventory));
    }
    
    public boolean canUse(PlayerEntity player) {
        return canUse(this.context, player, QuesBlocks.ENCHANTING_TABLE);
    }
    
    public ItemStack quickMove(PlayerEntity player, int slot) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot2 = this.slots.get(slot);
        
        if (slot2 != null && slot2.hasStack()) {
            ItemStack itemStack2 = slot2.getStack();
            itemStack = itemStack2.copy();
            if (slot == 0) {
                if (!this.insertItem(itemStack2, 2, 38, true)) {
                    return ItemStack.EMPTY;
                }
            } else if (slot == 1) {
                if (!this.insertItem(itemStack2, 2, 38, true)) {
                    return ItemStack.EMPTY;
                }
            } else if (itemStack2.isOf(Items.LAPIS_LAZULI)) {
                if (!this.insertItem(itemStack2, 1, 2, true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                if ((this.slots.get(0)).hasStack() || !(this.slots.get(0)).canInsert(itemStack2)) {
                    return ItemStack.EMPTY;
                }
                
                ItemStack itemStack3 = itemStack2.copyWithCount(1);
                itemStack2.decrement(1);
                (this.slots.get(0)).setStack(itemStack3);
            }
            
            if (itemStack2.isEmpty()) {
                slot2.setStack(ItemStack.EMPTY);
            } else {
                slot2.markDirty();
            }
            
            if (itemStack2.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }
            
            slot2.onTakeItem(player, itemStack2);
        }
        return itemStack;
    }
}
