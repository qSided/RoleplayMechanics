package qsided.rpmechanics.skills;

import net.minecraft.util.ActionResult;
import qsided.rpmechanics.PlayerData;
import qsided.rpmechanics.StateManager;
import qsided.rpmechanics.events.EnchantItemCallback;
import qsided.rpmechanics.events.IncreaseSkillExperienceCallback;

public class EnchantingSkill {
    
    public static void register() {
        EnchantItemCallback.EVENT.register((player, stack, levelsSpent) -> {
            
            PlayerData state = StateManager.getPlayerState(player);
            int enchantingLevel = state.skillLevels.getOrDefault("enchanting", 1);
            
            if (enchantingLevel < 100) {
                IncreaseSkillExperienceCallback.EVENT.invoker().increaseExp(player, state, "enchanting", (float) (24 * levelsSpent));
            }
            
            return ActionResult.PASS;
        });
    }
}
