package qsided.rpmechanics.mixin.smithing;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.screen.*;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import qsided.rpmechanics.PlayerData;
import qsided.rpmechanics.StateManager;
import qsided.rpmechanics.events.IncreaseSkillExperienceCallback;

import java.util.Random;

@Mixin(AnvilScreenHandler.class)
public class AnvilScreenHandlerMixin {
    
    @Shadow @Final private Property levelCost;
    
    
    public PlayerEntity getUser() {
        return user;
    }
    
    public void setUser(PlayerEntity user) {
        this.user = user;
    }
    
    @Unique
    public PlayerEntity user;
    
    @WrapMethod(method = "onTakeOutput")
    public void onTake(PlayerEntity player, ItemStack stack, Operation<Void> original) {
        setUser(player);
        original.call(player, stack);
    }
    
    @WrapOperation(method = "onTakeOutput", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;addExperienceLevels(I)V"))
    public void chargeExp(PlayerEntity instance, int levels, Operation<Void> original) {
        if (getUser() instanceof ServerPlayerEntity player) {
            PlayerData state = StateManager.getPlayerState(player);
            Random r = new Random();
            int randomInt = r.nextInt(100) + 1;
            if (state.skillLevels.getOrDefault("smithing", 1) >= randomInt) {
                System.out.println("Didn't charge");
                IncreaseSkillExperienceCallback.EVENT.invoker().increaseExp(player, state, "smithing", (float) levelCost.get());
            } else {
                System.out.println("Charged");
                player.addExperienceLevels(-levelCost.get());
                IncreaseSkillExperienceCallback.EVENT.invoker().increaseExp(player, state, "smithing", (float) levelCost.get());
            }
        }
    }
    
    @WrapOperation(method = "onTakeOutput", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;decrement(I)V"))
    public void decrement1(ItemStack instance, int amount, Operation<Void> original) {
    
    }
    
    @WrapOperation(method = "onTakeOutput", at = @At(value = "INVOKE", target = "Lnet/minecraft/inventory/Inventory;setStack(ILnet/minecraft/item/ItemStack;)V"))
    public void decrement(Inventory inv, int slotId, ItemStack itemToBe, Operation<Void> original) {
        if (slotId == 1) {
            ItemStack item = inv.getStack(slotId);
            if (item.getCount() >= 1 && itemToBe.equals(ItemStack.EMPTY)) {
                if (getUser() instanceof ServerPlayerEntity player) {
                    PlayerData state = StateManager.getPlayerState(player);
                    Random r = new Random();
                    int randomInt = r.nextInt(100) + 1;
                    if (state.skillLevels.getOrDefault("smithing", 1) >= randomInt) {
                        if (!item.isIn(ItemTags.SWORDS) &&
                                !item.isIn(ItemTags.PICKAXES) &&
                                !item.isIn(ItemTags.AXES) &&
                                !item.isIn(ItemTags.SHOVELS) &&
                                !item.isIn(ItemTags.HOES) &&
                                !item.isIn(ItemTags.HEAD_ARMOR) &&
                                !item.isIn(ItemTags.HEAD_ARMOR_ENCHANTABLE) &&
                                !item.isIn(ItemTags.CHEST_ARMOR) &&
                                !item.isIn(ItemTags.CHEST_ARMOR_ENCHANTABLE) &&
                                !item.isIn(ItemTags.LEG_ARMOR) &&
                                !item.isIn(ItemTags.LEG_ARMOR_ENCHANTABLE) &&
                                !item.isIn(ItemTags.FOOT_ARMOR) &&
                                !item.isIn(ItemTags.FOOT_ARMOR_ENCHANTABLE) && !item.isOf(Items.ENCHANTED_BOOK)) {
                            inv.setStack(slotId, item);
                        } else {
                            original.call(inv, slotId, itemToBe);
                        }
                    } else {
                        original.call(inv, slotId, itemToBe);
                    }
                }
            }
        } else if (slotId == 0) {
            original.call(inv, slotId, itemToBe);
        }
    }
}
