package qsided.rpmechanics.mixin;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import qsided.rpmechanics.PlayerData;
import qsided.rpmechanics.StateManager;

@Mixin(PlayerEntity.class)
public abstract class PlayerFallDamageScaleMixin {

    @Inject(
            method = "handleFallDamage",
            at = @At("HEAD"),
            cancellable = true
    )
    private void rpmechanics$scaleFallDamage(float fallDistance,
                                             float damageMultiplier,
                                             DamageSource damageSource,
                                             CallbackInfoReturnable<Boolean> cir) {
        if (!(((Object) this) instanceof ServerPlayerEntity player)) return;

        // Vanilla-style base damage
        float baseDamage = Math.max(0.0F, fallDistance - 3.0F) * damageMultiplier;
        if (baseDamage <= 0.0F) {
            cir.setReturnValue(false);
            return;
        }

        PlayerData state = StateManager.getPlayerState(player);
        int agilityLevel = state.skillLevels.getOrDefault("agility", 1);

        float reduction = Math.min(0.5F, agilityLevel * 0.01F); // up to 50% reduction
        float finalDamage = baseDamage * (1.0F - reduction);

        if (finalDamage <= 0.0F) {
            cir.setReturnValue(false);
            return;
        }

        player.damage(damageSource, finalDamage);
        cir.setReturnValue(true);
    }
}
