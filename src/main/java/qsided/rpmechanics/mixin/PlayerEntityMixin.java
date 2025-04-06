package qsided.rpmechanics.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import qsided.rpmechanics.PlayerData;
import qsided.rpmechanics.StateManager;
import qsided.rpmechanics.attributes.RoleplayMechanicsAttributes;
import qsided.rpmechanics.events.IncreaseSkillExperienceCallback;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
    
    @WrapOperation(method = "createPlayerAttributes", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;createLivingAttributes()Lnet/minecraft/entity/attribute/DefaultAttributeContainer$Builder;"))
    private static DefaultAttributeContainer.Builder addCustomAttributes(Operation<DefaultAttributeContainer.Builder> original) {
        
        return original.call()
                .add(RoleplayMechanicsAttributes.BOW_PROJECTILE_SPEED, 0.0)
                .add(RoleplayMechanicsAttributes.BOW_PROJECTILE_ACCURACY, 0.0);
    }
    
    @WrapOperation(method = "jump", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;incrementStat(Lnet/minecraft/util/Identifier;)V"))
    public void jump(PlayerEntity instance, Identifier stat, Operation<Void> original) {
        if (instance instanceof ServerPlayerEntity player) {
            PlayerData state = StateManager.getPlayerState(player);
            IncreaseSkillExperienceCallback.EVENT.invoker().increaseExp(player, state, "agility", 5F);
        }
        original.call(instance, stat);
    }
    
}
