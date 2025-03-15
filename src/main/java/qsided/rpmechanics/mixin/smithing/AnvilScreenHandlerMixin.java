package qsided.rpmechanics.mixin.smithing;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.screen.AnvilScreenHandler;
import net.minecraft.screen.Property;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import qsided.rpmechanics.PlayerData;
import qsided.rpmechanics.StateManager;
import qsided.rpmechanics.events.IncreaseSkillExperienceCallback;

import java.util.Random;

@Mixin(AnvilScreenHandler.class)
public class AnvilScreenHandlerMixin {
    
    @Shadow @Final private Property levelCost;
    
    @Shadow private int repairItemUsage;
    
    @Shadow private boolean keepSecondSlot;
    
    @Unique
    private static PlayerData getPlayerToUse() {
        return playerToUse;
    }
    
    @Unique
    private static void setPlayerToUse(PlayerData playerToUse) {
        AnvilScreenHandlerMixin.playerToUse = playerToUse;
    }
    
    private static PlayerData playerToUse = null;
    
    @WrapMethod(method = "onTakeOutput")
    public void onTake(PlayerEntity player, ItemStack stack, Operation<Void> original) {
        if (player instanceof ServerPlayerEntity serverPlayer) {
            PlayerData state = StateManager.getPlayerState(serverPlayer);
            setPlayerToUse(state);
            original.call(player, stack);
        }
        
    }
    
    @WrapOperation(method = "onTakeOutput", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;addExperienceLevels(I)V"))
    public void onAddExp(PlayerEntity instance, int levels, Operation<Void> original) {
        original.call(instance, levels);
        if (instance instanceof ServerPlayerEntity player) {
            PlayerData state = StateManager.getPlayerState(instance);
            Random r = new Random();
            int randomInt = r.nextInt(100) + 1;
            if (state.skillLevels.getOrDefault("smithing", 1) >= randomInt) {
                IncreaseSkillExperienceCallback.EVENT.invoker().increaseExp(player, state, "smithing", (float) -levels);
            } else {
                player.addExperienceLevels(levelCost.get());
                IncreaseSkillExperienceCallback.EVENT.invoker().increaseExp(player, state, "smithing", (float) -levels);
            }
        }
    }
    
    @WrapOperation(method = "onTakeOutput", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;decrement(I)V"))
    public void saveCountGreaterThanOne(ItemStack instance, int amount, Operation<Void> original) {
        original.call(instance, amount);
        Random r = new Random();
        int randomInt = r.nextInt(100) + 1;
        System.out.println("Decrement");
        if (getPlayerToUse().skillLevels.getOrDefault("smithing", 1) >= randomInt) {
            if (
                    !instance.isIn(ItemTags.SWORDS) ||
                            !instance.isIn(ItemTags.PICKAXES) ||
                            !instance.isIn(ItemTags.AXES) ||
                            !instance.isIn(ItemTags.SHOVELS) ||
                            !instance.isIn(ItemTags.HOES) ||
                            !instance.isIn(ItemTags.HEAD_ARMOR) ||
                            !instance.isIn(ItemTags.HEAD_ARMOR_ENCHANTABLE) ||
                            !instance.isIn(ItemTags.CHEST_ARMOR) ||
                            !instance.isIn(ItemTags.CHEST_ARMOR_ENCHANTABLE) ||
                            !instance.isIn(ItemTags.LEG_ARMOR) ||
                            !instance.isIn(ItemTags.LEG_ARMOR_ENCHANTABLE) ||
                            !instance.isIn(ItemTags.FOOT_ARMOR) ||
                            !instance.isIn(ItemTags.FOOT_ARMOR_ENCHANTABLE)) {
                System.out.println("Recovered materials");
                instance.increment(amount);
            }
            System.out.println("Consumed materials");
        }
    }
    
    @WrapOperation(method = "onTakeOutput", at = @At(value = "INVOKE", target = "Lnet/minecraft/inventory/Inventory;setStack(ILnet/minecraft/item/ItemStack;)V"))
    public void saveCountOne(Inventory instance, int i, ItemStack stack, Operation<Void> original) {
        if (repairItemUsage > 0 ) {
            Random r = new Random();
            int randomInt = r.nextInt(100) + 1;
            if (getPlayerToUse().skillLevels.getOrDefault("smithing", 1) >= randomInt && instance.getStack(1).getCount() == 1) {
                if (stack.isIn(ItemTags.SWORDS) ||
                        stack.isIn(ItemTags.PICKAXES) ||
                        stack.isIn(ItemTags.AXES) ||
                        stack.isIn(ItemTags.SHOVELS) ||
                        stack.isIn(ItemTags.HOES) ||
                        stack.isIn(ItemTags.HEAD_ARMOR) ||
                        stack.isIn(ItemTags.HEAD_ARMOR_ENCHANTABLE) ||
                        stack.isIn(ItemTags.CHEST_ARMOR) ||
                        stack.isIn(ItemTags.CHEST_ARMOR_ENCHANTABLE) ||
                        stack.isIn(ItemTags.LEG_ARMOR) ||
                        stack.isIn(ItemTags.LEG_ARMOR_ENCHANTABLE) ||
                        stack.isIn(ItemTags.FOOT_ARMOR) ||
                        stack.isIn(ItemTags.FOOT_ARMOR_ENCHANTABLE)) {
                    instance.setStack(1, ItemStack.EMPTY);
                }
                instance.setStack(1, stack);
            } else {
                instance.setStack(1, ItemStack.EMPTY);
            }
            instance.setStack(0, ItemStack.EMPTY);
        } else if (!keepSecondSlot) {
            instance.setStack(0, ItemStack.EMPTY);
        }
    }
}
