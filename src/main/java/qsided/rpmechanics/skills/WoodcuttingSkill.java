package qsided.rpmechanics.skills;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.server.network.ServerPlayerEntity;
import qsided.rpmechanics.PlayerData;
import qsided.rpmechanics.config.experience_values.BlockExperience;
import qsided.rpmechanics.events.IncreaseSkillExperienceCallback;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;

import static qsided.rpmechanics.StateManager.getPlayerState;

public class WoodcuttingSkill {
    
    public static void register() {
        ObjectMapper mapper = new ObjectMapper();
        CollectionType typeReference = TypeFactory.defaultInstance().constructCollectionType(List.class, BlockExperience.class);
        PlayerBlockBreakEvents.AFTER.register((world, player, pos, state, blockEntity) -> {
            PlayerData data = getPlayerState(player);
            try {
                List<BlockExperience> experience = mapper.readValue(new File(FabricLoader.getInstance().getConfigDir()
                        + "/rpmechanics/skills/woodcutting.json"), typeReference);
                experience.forEach(block -> {
                    if (state.getBlock().asItem().toString().equals(block.getId())) {
                        
                        
                        block.getExperience().forEach((skill, value) -> {
                            IncreaseSkillExperienceCallback.EVENT.invoker()
                                    .increaseExp((ServerPlayerEntity) player, data, skill, value);
                        });
                        
                    }
                });
                
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Random r = new Random();
            int randomInt = r.nextInt(100) + 1;
            
            if (player.getEquippedStack(EquipmentSlot.MAINHAND).isIn(ItemTags.AXES) &&
                    randomInt <= data.skillLevels.getOrDefault("woodcutting", 1)) {
                player.dropItem(state.getBlock().asItem().getDefaultStack(), false, true);
            }
            
        });
    }
}