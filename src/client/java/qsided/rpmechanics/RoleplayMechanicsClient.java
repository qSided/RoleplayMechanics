package qsided.rpmechanics;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.lwjgl.glfw.GLFW;
import qsided.rpmechanics.blockentities.QuesBlockEntityTypes;
import qsided.rpmechanics.blocks.QuesBlocks;
import qsided.rpmechanics.config.experience_values.BlockExperience;
import qsided.rpmechanics.config.requirements.ItemCraftingRequirement;
import qsided.rpmechanics.config.requirements.ItemWithRequirements;
import qsided.rpmechanics.config.roleplay_classes.RoleplayClass;
import qsided.rpmechanics.gui.blocks.OvenHandledScreen;
import qsided.rpmechanics.gui.blocks.SkillEnabledEnchantingTableBlockEntityRenderer;
import qsided.rpmechanics.gui.blocks.SkillEnabledEnchantingTableHandledScreen;
import qsided.rpmechanics.gui.other.ClassSelectionScreen;
import qsided.rpmechanics.gui.skills.*;
import qsided.rpmechanics.items.QuesComponents;
import qsided.rpmechanics.networking.*;
import qsided.rpmechanics.skills.milestones.SkillMilestone;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class RoleplayMechanicsClient implements ClientModInitializer {

	public static String lastScreenOpen = "";
	public static String playerClassName = "";
	public static Integer playerClassLevel = 1;

	public static List<ItemCraftingRequirement> itemCraftingReqs = java.util.Collections.emptyList();
	public static List<ItemWithRequirements> itemUseReqs         = java.util.Collections.emptyList();

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

	public static String getLastScreenOpen() {
		return lastScreenOpen;
	}

	public static void setLastScreenOpen(String lastScreenOpen) {
		RoleplayMechanicsClient.lastScreenOpen = lastScreenOpen;
	}

	public static List<ItemCraftingRequirement> getItemCraftingReqs() {
		return itemCraftingReqs != null ? itemCraftingReqs : java.util.Collections.emptyList();
	}

	public static void setItemCraftingReqs(List<ItemCraftingRequirement> itemCraftingReqs) {
		RoleplayMechanicsClient.itemCraftingReqs = itemCraftingReqs;
	}

	public static List<ItemWithRequirements> getItemUseReqs() {
		return itemUseReqs != null ? itemUseReqs : java.util.Collections.emptyList();
	}

	public static void setItemUseReqs(List<ItemWithRequirements> itemUseReqs) {
		RoleplayMechanicsClient.itemUseReqs = itemUseReqs;
	}

	@Override
	public void onInitializeClient() {
		MinecraftClient client = MinecraftClient.getInstance();

		HandledScreens.register(QuesBlocks.OVEN_SCREEN_HANDLER, OvenHandledScreen::new);
		HandledScreens.register(QuesBlocks.SE_ENCHANTING_TABLE_SCREEN_HANDLER, SkillEnabledEnchantingTableHandledScreen::new);
		BlockEntityRendererFactories.register(QuesBlockEntityTypes.ENCHANTING_TABLE_BLOCK, SkillEnabledEnchantingTableBlockEntityRenderer::new);

		ObjectMapper mapper = new ObjectMapper();
		var typeFactory = TypeFactory.defaultInstance();
		CollectionType useRef      = typeFactory.constructCollectionType(List.class, ItemWithRequirements.class);
		CollectionType craftingRef = typeFactory.constructCollectionType(List.class, ItemCraftingRequirement.class);
		CollectionType miningRef   = typeFactory.constructCollectionType(List.class, BlockExperience.class);

		File useFile      = new File(FabricLoader.getInstance().getConfigDir() + "/rpmechanics/item_use_reqs.json");
		File craftingFile = new File(FabricLoader.getInstance().getConfigDir() + "/rpmechanics/skills/crafting_level_reqs.json");

		try {
			if (useFile.exists()) {
				List<ItemWithRequirements> itemUseReqs = mapper.readValue(useFile, useRef);
				setItemUseReqs(itemUseReqs);
			} else {
				RoleplayMechanicsCommon.LOGGER.warn("[RPMechanics-Client] item_use_reqs.json not found; skipping load");
			}

			if (craftingFile.exists()) {
				List<ItemCraftingRequirement> itemCraftReqs = mapper.readValue(craftingFile, craftingRef);
				setItemCraftingReqs(itemCraftReqs);
			} else {
				RoleplayMechanicsCommon.LOGGER.warn("[RPMechanics-Client] crafting_level_reqs.json not found; skipping load");
			}
		} catch (IOException e) {
			RoleplayMechanicsCommon.LOGGER.error("[RPMechanics-Client] Failed to load requirement configs", e);
		}

		KeyBinding openSkills = KeyBindingHelper.registerKeyBinding(
				new KeyBinding("key.rpmechanics.open_skills", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_RIGHT_ALT, "key.category.rpmechanics"));
		KeyBinding openClassSelection = KeyBindingHelper.registerKeyBinding(
				new KeyBinding("key.rpmechanics.open_class_selection", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_M, "key.category.rpmechanics"));

		ClientTickEvents.END_CLIENT_TICK.register(client1 -> {
			while (openSkills.wasPressed()) {
				ClientPlayNetworking.send(new RequestSkillsPayload(client.player.getUuid().toString()));

				switch (getLastScreenOpen()) {
					case "enchanting" -> client.setScreen(new EnchantingScreen());
					case "swords" -> client.setScreen(new SwordsScreen());
					case "axes" -> client.setScreen(new AxesScreen());
					case "bows" -> client.setScreen(new BowsScreen());
					case "woodcutting" -> client.setScreen(new WoodcuttingScreen());
					case "endurance" -> client.setScreen(new EnduranceScreen());
					case "agility" -> client.setScreen(new MiningScreen());
					case "crafting" -> client.setScreen(new CraftingScreen());
					case "smithing" -> client.setScreen(new SmithingScreen());
					case "farming" -> client.setScreen(new FarmingScreen());
					case "cooking" -> client.setScreen(new CookingScreen());
					case "swimming" -> client.setScreen(new SwimmingScreen());
					default -> client.setScreen(new AgilityScreen());
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

		ClientPlayNetworking.registerGlobalReceiver(SendPlayerS2CPayload.ID, (payload, context) -> {
			SkillEnabledEnchantingTableHandledScreen.setEnchantingLevel(payload.enchantingLevel());
		});

		// Classes and Milestones sync
		ClientPlayNetworking.registerGlobalReceiver(SyncConfigPayload.ID, (payload, context) -> {
			context.client().execute(() -> {
				try {
					ObjectMapper syncMapper = new ObjectMapper();

					Map<Integer, RoleplayClass> rpClasses =
							syncMapper.readValue(payload.rpClassesJson(),
									new com.fasterxml.jackson.core.type.TypeReference<Map<Integer, RoleplayClass>>() {});

					var milestoneRef = typeFactory.constructCollectionType(List.class, SkillMilestone.class);
					List<SkillMilestone> milestones =
							syncMapper.readValue(payload.milestonesJson(), milestoneRef);

					RoleplayMechanicsCommon.setRpClasses(rpClasses);
					RoleplayMechanicsCommon.setMilestones(milestones);

					RoleplayMechanicsCommon.LOGGER.info(
							"[RPMechanics-Client] Synced config: classes={}, milestones={}",
							rpClasses.size(), milestones.size()
					);
				} catch (Exception e) {
					RoleplayMechanicsCommon.LOGGER.error("[RPMechanics-Client] Failed to handle SyncConfigPayload", e);
				}
			});
		});

		// Item use requirements sync
		ClientPlayNetworking.registerGlobalReceiver(SyncItemUseReqsPayload.ID, (payload, context) -> {
			context.client().execute(() -> {
				try {
					String json = RoleplayMechanicsCommon.decompressToString(payload.compressedJson());
					if (json == null || json.isEmpty()) return;

					List<ItemWithRequirements> useReqs = mapper.readValue(json, useRef);
					RoleplayMechanicsClient.setItemUseReqs(useReqs);

					RoleplayMechanicsCommon.LOGGER.info(
							"[RPMechanics-Client] Synced item use requirements: {} entries",
							useReqs.size()
					);
				} catch (Exception e) {
					RoleplayMechanicsCommon.LOGGER.error(
							"[RPMechanics-Client] Failed to handle SyncItemUseReqsPayload", e);
				}
			});
		});

		// Crafting requirements sync
		ClientPlayNetworking.registerGlobalReceiver(SyncCraftingReqsPayload.ID, (payload, context) -> {
			context.client().execute(() -> {
				try {
					String json = RoleplayMechanicsCommon.decompressToString(payload.compressedJson());
					if (json == null || json.isEmpty()) return;

					List<ItemCraftingRequirement> craftReqs = mapper.readValue(json, craftingRef);
					RoleplayMechanicsClient.setItemCraftingReqs(craftReqs);

					RoleplayMechanicsCommon.LOGGER.info(
							"[RPMechanics-Client] Synced crafting requirements: {} entries",
							craftReqs.size()
					);
				} catch (Exception e) {
					RoleplayMechanicsCommon.LOGGER.error(
							"[RPMechanics-Client] Failed to handle SyncCraftingReqsPayload", e);
				}
			});
		});

		// Mining XP values sync
		ClientPlayNetworking.registerGlobalReceiver(SyncMiningXpPayload.ID, (payload, context) -> {
			context.client().execute(() -> {
				try {
					String json = RoleplayMechanicsCommon.decompressToString(payload.compressedJson());
					if (json == null || json.isEmpty()) return;

					List<BlockExperience> miningXp =
							mapper.readValue(json, miningRef);

					RoleplayMechanicsCommon.setMiningXpValues(miningXp);

					RoleplayMechanicsCommon.LOGGER.info(
							"[RPMechanics-Client] Synced mining XP values: {} entries",
							miningXp.size()
					);
				} catch (Exception e) {
					RoleplayMechanicsCommon.LOGGER.error(
							"[RPMechanics-Client] Failed to handle SyncMiningXpPayload", e);
				}
			});
		});

		ClientPlayNetworking.registerGlobalReceiver(LevelUpPayload.ID, (payload, context) -> {
			context.client().execute(() -> {
				ClientPlayNetworking.send(new RequestSkillsPayload(client.player.getUuid().toString()));
				if (payload.shouldMessage()) {
					context.player().sendMessage(
							Text.translatable("skills.level_up.rpmechanics." + payload.skill())
									.append(String.valueOf(payload.level())),
							false
					);
				}
			});
		});

		ClientPlayNetworking.registerGlobalReceiver(PlayerFirstJoinPayload.ID, (payload, context) -> {
			context.client().setScreen(new ClassSelectionScreen());
		});

		ItemTooltipCallback.EVENT.register((stack, tooltipContext, tooltipType, lines) -> {

			if (stack.getComponents().contains(QuesComponents.COOK_QUALITY)) {
				switch (stack.getOrDefault(QuesComponents.COOK_QUALITY, "average")) {
					case "perfect" -> lines.add(Text.translatable("tooltip.rpmechanics.perfect_quality"));
					case "burnt" -> lines.add(Text.translatable("tooltip.rpmechanics.burnt_quality"));
					case "average" -> lines.add(Text.translatable("tooltip.rpmechanics.average_quality"));
				}
			}

			if (RoleplayMechanicsCommon.OWO_CONFIG.enableRequirements()) {
				List<ItemCraftingRequirement> craftingReqs = getItemCraftingReqs();
				List<ItemWithRequirements> useReqs        = getItemUseReqs();
				Map<Integer, RoleplayClass> classes       = RoleplayMechanicsCommon.getRpClasses();

				if (craftingReqs != null) {
					craftingReqs.forEach(item -> {
						if (stack.getItem().toString().equals(item.getItemId()) && item.getLevelReq() > 1) {
							lines.add(Text.translatable("tooltip.rpmechanics.requirements")
									.append(Text.translatable("skills.rpmechanics.crafting"))
									.append(" " + item.getLevelReq()).formatted(Formatting.WHITE)
									.append(Text.translatable("tooltip.rpmechanics.craft_requirements_two")));
						}
					});
				}

				if (useReqs != null) {
					useReqs.forEach(item -> {
						if (!stack.getItem().toString().equals(item.getItemId())) return;

						// Skill requirement
						if (item.getRequirements().getSkillLevel() > 1) {
							lines.add(Text.translatable("tooltip.rpmechanics.requirements")
									.append(Text.translatable("skills.rpmechanics." + item.getRequirements().getSkill()))
									.append(" " + item.getRequirements().getSkillLevel()).formatted(Formatting.WHITE)
									.append(Text.translatable("tooltip.rpmechanics.requirements_two")));
						}

						// Class requirement
						if (item.getRequirements().getRpClassId() >= 0) {
							RoleplayClass clazz = classes.get(item.getRequirements().getRpClassId());
							if (clazz != null && clazz.getColor() != null) {
								int rgb = Color.decode(clazz.getColor()).getRGB();
								Text classNameText = Text.translatable(clazz.getName())
										.setStyle(Style.EMPTY.withColor(rgb));

								lines.add(Text.translatable("tooltip.rpmechanics.class_requirements")
										.append(classNameText)
										.append(Text.translatable("tooltip.rpmechanics.requirements_two")));
							}
						}
					});
				}
			}
		});

		ClientPlayNetworking.registerGlobalReceiver(SendLevelsPayload.ID, ((payload, context) -> {
			AgilityScreen.setLevel(payload.levels().getInt("agility"));
			AgilityScreen.setSafeDistance(context.player().getAttributeValue(EntityAttributes.GENERIC_SAFE_FALL_DISTANCE));
			AxesScreen.setLevel(payload.levels().getInt("axes"));
			BowsScreen.setLevel(payload.levels().getInt("bows"));
			CookingScreen.setLevel(payload.levels().getInt("cooking"));
			CraftingScreen.setLevel(payload.levels().getInt("crafting"));
			EnchantingScreen.setLevel(payload.levels().getInt("enchanting"));
			EnduranceScreen.setLevel(payload.levels().getInt("endurance"));
			FarmingScreen.setLevel(payload.levels().getInt("farming"));
			MiningScreen.setLevel(payload.levels().getInt("mining"));
			SmithingScreen.setLevel(payload.levels().getInt("smithing"));
			SwimmingScreen.setLevel(payload.levels().getInt("swimming"));
			SwordsScreen.setLevel(payload.levels().getInt("swords"));
			WoodcuttingScreen.setLevel(payload.levels().getInt("woodcutting"));
		}));

		ClientPlayNetworking.registerGlobalReceiver(SendExperiencePayload.ID, ((payload, context) -> {
			AgilityScreen.setExp(payload.experience().getFloat("agility"));
			AgilityScreen.setSafeDistance(context.player().getAttributeValue(EntityAttributes.GENERIC_SAFE_FALL_DISTANCE));
			AxesScreen.setExp(payload.experience().getFloat("axes"));
			BowsScreen.setExp(payload.experience().getFloat("bows"));
			CookingScreen.setExp(payload.experience().getFloat("cooking"));
			CraftingScreen.setExp(payload.experience().getFloat("crafting"));
			EnchantingScreen.setExp(payload.experience().getFloat("enchanting"));
			EnduranceScreen.setExp(payload.experience().getFloat("endurance"));
			FarmingScreen.setExp(payload.experience().getFloat("farming"));
			MiningScreen.setExp(payload.experience().getFloat("mining"));
			SmithingScreen.setExp(payload.experience().getFloat("smithing"));
			SwimmingScreen.setExp(payload.experience().getFloat("swimming"));
			SwordsScreen.setExp(payload.experience().getFloat("swords"));
			WoodcuttingScreen.setExp(payload.experience().getFloat("woodcutting"));
		}));
	}
}
