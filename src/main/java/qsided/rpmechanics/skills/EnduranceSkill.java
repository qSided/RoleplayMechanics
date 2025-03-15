package qsided.rpmechanics.skills;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import qsided.rpmechanics.PlayerData;
import qsided.rpmechanics.StateManager;
import qsided.rpmechanics.events.PlayerHurtByEntityCallback;
import qsided.rpmechanics.events.IncreaseSkillExperienceCallback;

public class EnduranceSkill {
    public static void register() {
        PlayerHurtByEntityCallback.EVENT.register((player, source, dmgDealt, dmgTaken, wasBlocked) -> {
            PlayerData state = StateManager.getPlayerState(player);
            
            if (!wasBlocked) {
                IncreaseSkillExperienceCallback.EVENT.invoker().increaseExp((ServerPlayerEntity) player, state, "endurance", 3 + (dmgTaken));
            } else {
                IncreaseSkillExperienceCallback.EVENT.invoker().increaseExp((ServerPlayerEntity) player, state, "endurance", 6 + (dmgDealt));
            }
            
            return ActionResult.PASS;
        });
    }
}
