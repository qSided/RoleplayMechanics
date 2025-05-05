package qsided.rpmechanics.skills;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import qsided.rpmechanics.PlayerData;
import qsided.rpmechanics.StateManager;
import qsided.rpmechanics.events.IncreaseSkillExperienceCallback;
import qsided.rpmechanics.networking.SendPlayerFallPayload;
import qsided.rpmechanics.networking.SendPlayerJumpPayload;

public class AgilitySkill {
    
    public static void register() {
        ServerPlayNetworking.registerGlobalReceiver(SendPlayerFallPayload.ID, (payload, context) -> {
            PlayerData state = StateManager.getPlayerState(context.player());
            IncreaseSkillExperienceCallback.EVENT.invoker().increaseExp(context.player(), state, "agility", (float) Math.min((payload.integer() / 100), 50));
        });
        
        ServerPlayNetworking.registerGlobalReceiver(SendPlayerJumpPayload.ID, (payload, context) -> {
            PlayerData state = StateManager.getPlayerState(context.player());
            IncreaseSkillExperienceCallback.EVENT.invoker().increaseExp(context.player(), state, "agility", (float) (payload.integer()));
        });
    }
    
}
