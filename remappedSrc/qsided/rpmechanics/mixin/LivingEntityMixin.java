package qsided.rpmechanics.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.advancement.criterion.EntityHurtPlayerCriterion;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import qsided.rpmechanics.events.PlayerHurtByEntityCallback;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @WrapOperation(method = "damage", at = @At(value = "INVOKE", target = "Lnet/minecraft/advancement/criterion/EntityHurtPlayerCriterion;trigger(Lnet/minecraft/server/network/ServerPlayerEntity;Lnet/minecraft/entity/damage/DamageSource;FFZ)V"))
    public void damage(EntityHurtPlayerCriterion instance, ServerPlayerEntity player, DamageSource source, float dealt, float taken, boolean blocked, Operation<Void> original) {
        PlayerHurtByEntityCallback.EVENT.invoker().onDamaged(player, source, dealt, taken, blocked);
    }
}
