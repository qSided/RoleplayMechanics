package qsided.rpmechanics.skills.combat;

import net.fabricmc.fabric.api.entity.event.v1.ServerEntityCombatEvents;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import qsided.rpmechanics.PlayerData;
import qsided.rpmechanics.StateManager;
import qsided.rpmechanics.events.IncreaseSkillExperienceCallback;
import qsided.rpmechanics.events.ShootBowCallback;

public class ArcherySkill {
    
    public static void register() {
        
        ShootBowCallback.EVENT.register((world, player, hand) -> {
            
            if (player instanceof ServerPlayerEntity || !world.isClient()) {
                PlayerData state = StateManager.getPlayerState(player);
                assert player instanceof ServerPlayerEntity;
                IncreaseSkillExperienceCallback.EVENT.invoker().increaseExp((ServerPlayerEntity) player, state, "bows", 4F);
            }
            
            return ActionResult.PASS;
        });
        
        ServerEntityCombatEvents.AFTER_KILLED_OTHER_ENTITY.register((serverWorld, attacker, killed) -> {
            if (attacker instanceof ServerPlayerEntity player) {
                PlayerData state = StateManager.getPlayerState(player);
                if (killed.getRecentDamageSource().isOf(DamageTypes.ARROW)) {
                    IncreaseSkillExperienceCallback.EVENT.invoker().increaseExp(player, state, "bows", 12 + (killed.getMaxHealth() / 4));
                }
            }
        });
        
    }
    
}
