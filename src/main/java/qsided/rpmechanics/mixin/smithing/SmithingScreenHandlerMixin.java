package qsided.rpmechanics.mixin.smithing;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.SmithingScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import qsided.rpmechanics.PlayerData;
import qsided.rpmechanics.StateManager;
import qsided.rpmechanics.events.IncreaseSkillExperienceCallback;

import java.util.Random;

@Mixin(SmithingScreenHandler.class)
public class SmithingScreenHandlerMixin {
    
    @Unique
    public PlayerEntity getUser() {
        return user;
    }
    
    @Unique
    public void setUser(PlayerEntity user) {
        this.user = user;
    }
    
    @Unique
    public PlayerEntity user;
    
    @WrapMethod(method = "onTakeOutput")
    public void onTake(PlayerEntity player, ItemStack stack, Operation<Void> original) {
        setUser(player);
        if (player instanceof ServerPlayerEntity serverPlayer) {
            PlayerData state = StateManager.getPlayerState(serverPlayer);
            IncreaseSkillExperienceCallback.EVENT.invoker().increaseExp(serverPlayer, state, "smithing", 50F);
        }
        original.call(player, stack);
    }
    
    @WrapOperation(method = "onTakeOutput", at = @At(value = "INVOKE", target = "Lnet/minecraft/screen/SmithingScreenHandler;decrementStack(I)V"))
    public void decrement(SmithingScreenHandler instance, int slot, Operation<Void> original) {
        Random r = new Random();
        int randomInt = r.nextInt(100) + 1;
        if (getUser() instanceof ServerPlayerEntity player) {
            PlayerData state = StateManager.getPlayerState(player);
            if (state.skillLevels.getOrDefault("smithing", 1) >= randomInt) {
                if (slot != 2) {
                    original.call(instance, slot);
                }
            } else {
                original.call(instance, slot);
            }
        }
        
    }
    
}
