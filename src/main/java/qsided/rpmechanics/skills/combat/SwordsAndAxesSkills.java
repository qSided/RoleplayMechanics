package qsided.rpmechanics.skills.combat;

import net.fabricmc.fabric.api.entity.event.v1.ServerEntityCombatEvents;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.server.network.ServerPlayerEntity;
import qsided.rpmechanics.PlayerData;
import qsided.rpmechanics.StateManager;
import qsided.rpmechanics.events.IncreaseSkillExperienceCallback;

public class SwordsAndAxesSkills {
    
    public static void register() {
        ServerEntityCombatEvents.AFTER_KILLED_OTHER_ENTITY.register((world, entity, killedEntity) -> {
            if (entity instanceof ServerPlayerEntity player) {
                
                PlayerData state = StateManager.getPlayerState(player);
                float combatExp = state.skillExperience.getOrDefault("combat", 0F);
                int combatLevel = state.skillLevels.getOrDefault("combat", 1);
                
                if (combatLevel < 100) {
                    IncreaseSkillExperienceCallback.EVENT.invoker().increaseExp(player, state, "combat", (12 + (killedEntity.getMaxHealth() / 4)));
                }
            }
        });
    }
    
}
