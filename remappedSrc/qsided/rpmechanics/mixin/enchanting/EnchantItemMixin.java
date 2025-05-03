package qsided.rpmechanics.mixin.enchanting;

import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.screen.EnchantmentScreenHandler;
import net.minecraft.screen.Property;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Util;
import org.spongepowered.asm.mixin.*;
import qsided.rpmechanics.PlayerData;
import qsided.rpmechanics.StateManager;
import qsided.rpmechanics.events.EnchantItemCallback;

import java.util.List;
import java.util.Random;

@Mixin(EnchantmentScreenHandler.class)
public abstract class EnchantItemMixin {
    
    @Shadow @Final public int[] enchantmentPower;
    
    @Shadow @Final private Inventory inventory;
    
    @Shadow @Final private ScreenHandlerContext context;
    
    @Shadow protected abstract List<EnchantmentLevelEntry> generateEnchantments(DynamicRegistryManager registryManager, ItemStack stack, int slot, int level);
    
    @Shadow @Final private Property seed;
    
    @Shadow public abstract void onContentChanged(Inventory inventory);
    
    @Unique
    private static boolean isBetween(int x, int lower, int upper) {
        return lower <= x && x <= upper;
    }
    
    /**
     * @author qSided
     * @reason completely rework the way enchanting works based on player's Enchanting Skill, also changed some variable names to better clarify
     */
    @Overwrite
    public boolean onButtonClick(PlayerEntity player, int id) {
        if (id >= 0 && id < enchantmentPower.length) {
            ItemStack itemStack = inventory.getStack(0);
            ItemStack lapis = inventory.getStack(1);
            int i = id + 1;
            if ((lapis.isEmpty() || lapis.getCount() < i) && !player.isInCreativeMode()) {
                return false;
            } else if (enchantmentPower[id] <= 0 || itemStack.isEmpty() || (player.experienceLevel < i || player.experienceLevel < enchantmentPower[id]) && !player.getAbilities().creativeMode) {
                return false;
            } else {
                context.run((world, pos) -> {
                    int enchantingPower;
                    PlayerData state = StateManager.getPlayerState(player);
                    
                    if (isBetween(state.skillLevels.getOrDefault("enchantingLevel", 1), 20, 39)) {
                        enchantingPower = 1;
                    } else if (isBetween(state.skillLevels.getOrDefault("enchantingLevel", 1), 40, 59)) {
                        enchantingPower = 2;
                    } else if (isBetween(state.skillLevels.getOrDefault("enchantingLevel", 1), 60, 79)) {
                        enchantingPower = 3;
                    } else if (isBetween(state.skillLevels.getOrDefault("enchantingLevel", 1), 80, 99)) {
                        enchantingPower = 4;
                    } else if (state.skillLevels.getOrDefault("enchantingLevel", 1) == 100) {
                        enchantingPower = 5;
                    } else {
                        enchantingPower = 0;
                    }
                    
                    ItemStack itemToEnchant = itemStack;
                    List<EnchantmentLevelEntry> list = generateEnchantments(world.getRegistryManager(), itemToEnchant, id, enchantmentPower[id]);
                    if (!list.isEmpty()) {
                        if (isBetween(state.skillLevels.getOrDefault("enchantingLevel", 1), 0, 32)) {
                            player.applyEnchantmentCosts(itemToEnchant, i);
                            lapis.decrementUnlessCreative(i, player);
                        } else if (isBetween(state.skillLevels.getOrDefault("enchantingLevel", 1), 33, 65)) {
                            player.applyEnchantmentCosts(itemToEnchant, i - 1);
                            lapis.decrementUnlessCreative(Math.max(i-1, 1), player);
                        } else if (isBetween(state.skillLevels.getOrDefault("enchantingLevel", 1), 66, 100)) {
                            player.applyEnchantmentCosts(itemToEnchant, Math.max(i - 2, 0));
                            lapis.decrementUnlessCreative(Math.max(i-2, 1), player);
                        }
                        
                        if (itemToEnchant.isOf(Items.BOOK)) {
                            itemToEnchant = itemStack.withItem(Items.ENCHANTED_BOOK);
                            inventory.setStack(0, itemToEnchant);
                        }
                        
                        Random r = new Random();
                        int randomInt = r.nextInt(100) + 1;
                        
                        for (EnchantmentLevelEntry enchantmentLevelEntry : list) {
                            if (randomInt <= state.skillLevels.getOrDefault("enchanting", 1)) {
                                if (state.skillLevels.getOrDefault("enchanting", 1) == 100) {
                                    int levelIncrease = r.nextInt(2, enchantingPower + 1);
                                    itemToEnchant.addEnchantment(enchantmentLevelEntry.enchantment, enchantmentLevelEntry.level + levelIncrease);
                                } else {
                                    int levelIncrease = r.nextInt(enchantingPower + 1);
                                    itemToEnchant.addEnchantment(enchantmentLevelEntry.enchantment, enchantmentLevelEntry.level + levelIncrease);
                                }
                            } else {
                                itemToEnchant.addEnchantment(enchantmentLevelEntry.enchantment, enchantmentLevelEntry.level);
                            }
                        }
                        
                        
                        if (lapis.isEmpty()) {
                            inventory.setStack(1, ItemStack.EMPTY);
                        }
                        
                        player.incrementStat(Stats.ENCHANT_ITEM);
                        if (player instanceof ServerPlayerEntity) {
                            Criteria.ENCHANTED_ITEM.trigger((ServerPlayerEntity)player, itemToEnchant, i);
                            EnchantItemCallback.EVENT.invoker().interact((ServerPlayerEntity) player, itemToEnchant, id);
                        }
                        inventory.markDirty();
                        seed.set(player.getEnchantingTableSeed());
                        onContentChanged(inventory);
                        world.playSound((PlayerEntity)null, pos, SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.BLOCKS, 1.0F, world.random.nextFloat() * 0.1F + 0.9F);
                    }
                });
                return true;
            }
        } else {
            String username = String.valueOf(player.getName());
            Util.logErrorOrPause(username + " pressed invalid button id: " + id);
            return false;
        }
    }
}
