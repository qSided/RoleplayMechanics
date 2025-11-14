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
import qsided.rpmechanics.events.IncreaseSkillExperienceCallback;

@Mixin(PlayerEntity.class)
public abstract class PlayerFallMixin {

    @Inject(
            method = "handleFallDamage",
            at = @At("TAIL")
    )
    private void rpmechanics$giveAgilityFromFall(float fallDistance,
                                                 float damageMultiplier,
                                                 DamageSource damageSource,
                                                 CallbackInfoReturnable<Boolean> cir) {
        if (!(((Object) this) instanceof ServerPlayerEntity player)) return;

        // Optional: only reward XP if damage actually happened
        if (!cir.getReturnValue()) return;

        PlayerData state = StateManager.getPlayerState(player);

        // Simple formula, tweak to taste
        float raw = (fallDistance - 3.0F) * damageMultiplier * 0.5F;
        int xp = Math.max(0, Math.round(raw));

        if (xp > 0) {
            IncreaseSkillExperienceCallback.EVENT.invoker()
                    .increaseExp(player, state, "agility", (float) xp);
        }
    }
}
