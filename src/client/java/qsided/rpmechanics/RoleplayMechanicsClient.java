package qsided.rpmechanics;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.lwjgl.glfw.GLFW;
import qsided.rpmechanics.config.requirements.ItemCraftingRequirement;
import qsided.rpmechanics.config.requirements.ItemWithRequirements;
import qsided.rpmechanics.config.roleplay_classes.RoleplayClass;
import qsided.rpmechanics.gui.other.ClassSelectionScreen;
import qsided.rpmechanics.gui.skills.*;
import qsided.rpmechanics.networking.*;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class RoleplayMechanicsClient implements ClientModInitializer {
	public static String lastScreenOpen = "";
	
	public static String getPlayerClassName() {
		return playerClassName;
	}
	
	public static void setPlayerClassName(String playerClassName) {
		RoleplayMechanicsClient.playerClassName = playerClassName;
	}
	
	public static Integer getPlayerClassLevel() {
		return playerClassLevel;
	}
	
	public static void setPlayerClassLevel(Integer playerClassLevel) {
		RoleplayMechanicsClient.playerClassLevel = playerClassLevel;
	}
	
	public static String playerClassName = "";
	
	public static Integer playerClassLevel = 1;
	
	public static String getLastScreenOpen() {
		return lastScreenOpen;
	}
	
	public static void setLastScreenOpen(String lastScreenOpen) {
		RoleplayMechanicsClient.lastScreenOpen = lastScreenOpen;
	}
	
	public static List<ItemCraftingRequirement> itemCraftingReqs;
	public static List<ItemWithRequirements> itemUseReqs;
	static Map<Integer, RoleplayClass> rpClasses;
	
	public static List<ItemCraftingRequirement> getItemCraftingReqs() {
		return itemCraftingReqs;
	}
	
	public static void setItemCraftingReqs(List<ItemCraftingRequirement> itemCraftingReqs) {
		RoleplayMechanicsClient.itemCraftingReqs = itemCraftingReqs;
	}
	
	public static List<ItemWithRequirements> getItemUseReqs() {
		return itemUseReqs;
	}
	
	public static void setItemUseReqs(List<ItemWithRequirements> itemUseReqs) {
		RoleplayMechanicsClient.itemUseReqs = itemUseReqs;
	}
	
	public static Map<Integer, RoleplayClass> getRpClasses() {
		return rpClasses;
	}
	
	public static void setRpClasses(Map<Integer, RoleplayClass> rpClasses) {
		RoleplayMechanicsClient.rpClasses = rpClasses;
	}
	
	@Override
	public void onInitializeClient() {
		
		MinecraftClient client = MinecraftClient.getInstance();
		
		
		ObjectMapper mapper = new ObjectMapper();
		CollectionType useRef = TypeFactory.defaultInstance().constructCollectionType(List.class, ItemWithRequirements.class);
		CollectionType craftingRef = TypeFactory.defaultInstance().constructCollectionType(List.class, ItemCraftingRequirement.class);
		try {
			List<ItemWithRequirements> itemUseReqs = mapper.readValue(new File(FabricLoader.getInstance().getConfigDir() + "/rpmechanics/reqs.json"), useRef);
			List<ItemCraftingRequirement> itemCraftReqs = mapper.readValue(new File(FabricLoader.getInstance().getConfigDir() + "/rpmechanics/skills/crafting.json"), craftingRef);
			Map<Integer, RoleplayClass> rpClasses = mapper.readValue(new File(FabricLoader.getInstance().getConfigDir() + "/rpmechanics/classes/classes.json"), new TypeReference<Map<Integer, RoleplayClass>>() {});
		
			setItemUseReqs(itemUseReqs);
			setItemCraftingReqs(itemCraftReqs);
			setRpClasses(rpClasses);
		} catch (IOException e) {
            throw new RuntimeException(e);
        }
        
        KeyBinding openSkills = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.rpmechanics.open_skills", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_RIGHT_ALT, "key.category.rpmechanics"));
		KeyBinding openClassSelection = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.rpmechanics.open_class_selection", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_M, "key.category.rpmechanics"));
		
		ClientTickEvents.END_CLIENT_TICK.register(client1 -> {
			while (openSkills.wasPressed()) {
				ClientPlayNetworking.send(new RequestSkillsPayload(client.player.getUuid().toString()));
				
				switch (getLastScreenOpen()) {
                    case "enchanting" -> client.setScreen(new EnchantingSkillScreen());
					case "combat" -> client.setScreen(new CombatSkillScreen());
					case "woodcutting" -> client.setScreen(new WoodcuttingSkillScreen());
                    case "endurance" -> client.setScreen(new EnduranceSkillScreen());
					case "agility" -> client.setScreen(new AgilitySkillScreen());
					case "crafting" -> client.setScreen(new CraftingSkillScreen());
					case "smithing" -> client.setScreen(new SmithingSkillScreen());
                    default -> client.setScreen(new MiningSkillScreen());
                }
			}
			while (openClassSelection.wasPressed()) {
				client.setScreen(new ClassSelectionScreen());
			}
		});
		
		ClientPlayNetworking.registerGlobalReceiver(SendClassAndLevelPayload.ID, (payload, context) -> {
			setPlayerClassName(payload.rpClassId());
			setPlayerClassLevel(payload.level());
		});
		
		ClientPlayNetworking.registerGlobalReceiver(LevelUpPayload.ID, (payload, context) -> {
			context.client().execute(() -> {
				ClientPlayNetworking.send(new RequestSkillsPayload(client.player.getUuid().toString()));
				if (payload.shouldMessage()) {
					context.player().sendMessage(Text.translatable("skills.level_up.rpmechanics." + payload.skill()).append(String.valueOf(payload.level())), false);
				}
			});
		});
		
		ClientPlayNetworking.registerGlobalReceiver(PlayerFirstJoinPayload.ID, (payload, context) -> {
			context.client().setScreen(new ClassSelectionScreen());
		});
		
		ItemTooltipCallback.EVENT.register((stack, tooltipContext, tooltipType, lines) -> {
			
			if (RoleplayMechanicsCommon.OWO_CONFIG.enableRequirements()) {
				getItemCraftingReqs().forEach(item -> {
					if (stack.getItem().toString().equals(item.getItemId()) && !(item.getLevelReq() <= 1)) {
						lines.add(Text.translatable("tooltip.rpmechanics.requirements")
								.append(Text.translatable("skills.rpmechanics.crafting"))
								.append(" " + item.getLevelReq()).formatted(Formatting.WHITE)
								.append(Text.translatable("tooltip.rpmechanics.craft_requirements_two")));
					}
				});
				getItemUseReqs().forEach(item -> {
					if (stack.getItem().toString().equals(item.getItemId())) {
						if (!(item.getRequirements().getSkillLevel() <= 1)) {
							lines.add(Text.translatable("tooltip.rpmechanics.requirements")
									.append(Text.translatable("skills.rpmechanics." + item.getRequirements().getSkill()))
									.append(" " + item.getRequirements().getSkillLevel()).formatted(Formatting.WHITE)
									.append(Text.translatable("tooltip.rpmechanics.requirements_two")));
						}
						if (!(item.getRequirements().getRpClassId() < 0)) {
							lines.add(Text.translatable("tooltip.rpmechanics.class_requirements")
									.append(Text.translatable(RoleplayMechanicsCommon.getRpClasses().getOrDefault(item.getRequirements().getRpClassId(), new RoleplayClass("", "", "", null, null, null)).getName()).getWithStyle(Style.EMPTY.withColor(Color.decode(RoleplayMechanicsCommon.getRpClasses().get(item.getRequirements().getRpClassId()).getColor()).getRGB())).getFirst())
									.append(Text.translatable("tooltip.rpmechanics.requirements_two")));
						}
					}
				});
			}
        });
		
		ClientPlayNetworking.registerGlobalReceiver(SendSkillsLevelsPayload.ID, (payload, context) -> {
			MiningSkillScreen.setMiningLevel(payload.mining());
			EnchantingSkillScreen.setEnchantingLevel(payload.enchanting());
			CombatSkillScreen.setCombatLevel(payload.combat());
			WoodcuttingSkillScreen.setWoodcuttingLevel(payload.woodcutting());
			EnduranceSkillScreen.setEnduranceLevel(payload.endurance());
			EnduranceSkillScreen.setMaxHealth(context.player().getMaxHealth());
			AgilitySkillScreen.setAgilityLevel(payload.agility());
			AgilitySkillScreen.setJumpStrength(context.player().getAttributeInstance(EntityAttributes.JUMP_STRENGTH).getValue());
			AgilitySkillScreen.setSafeDistance(context.player().getAttributeInstance(EntityAttributes.SAFE_FALL_DISTANCE).getValue());
			CraftingSkillScreen.setCraftingLevel(payload.crafting());
			SmithingSkillScreen.setSmithingLevel(payload.smithing());
			});
		ClientPlayNetworking.registerGlobalReceiver(SendSkillsExperiencePayload.ID, (payload, context) -> {
			MiningSkillScreen.setMiningExperience(payload.mining());
			EnchantingSkillScreen.setEnchantingExperience(payload.enchanting());
			CombatSkillScreen.setCombatExperience(payload.combat());
			WoodcuttingSkillScreen.setWoodcuttingExperience(payload.woodcutting());
			EnduranceSkillScreen.setEnduranceExperience(payload.endurance());
			EnduranceSkillScreen.setMaxHealth(context.player().getMaxHealth());
			AgilitySkillScreen.setAgilityExperience(payload.agility());
			AgilitySkillScreen.setJumpStrength(context.player().getAttributeInstance(EntityAttributes.JUMP_STRENGTH).getValue());
			AgilitySkillScreen.setSafeDistance(context.player().getAttributeInstance(EntityAttributes.SAFE_FALL_DISTANCE).getValue());
			CraftingSkillScreen.setCraftingExperience(payload.crafting());
			SmithingSkillScreen.setSmithingExperience(payload.smithing());
			});
	}
	
	public MinecraftClient getClient() {
        return MinecraftClient.getInstance();
    }
}