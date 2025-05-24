package qsided.rpmechanics;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.resource.ResourceType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import qsided.rpmechanics.attributes.RoleplayMechanicsAttributes;
import qsided.rpmechanics.blockentities.QuesBlockEntityTypes;
import qsided.rpmechanics.blocks.QuesBlocks;
import qsided.rpmechanics.commands.SkillsCommand;
import qsided.rpmechanics.config.ConfigGenerator;
import qsided.rpmechanics.config.RpMechanicsConfig;
import qsided.rpmechanics.config.experience_values.BlockExperience;
import qsided.rpmechanics.config.requirements.ItemCraftingRequirement;
import qsided.rpmechanics.config.roleplay_classes.RoleplayClass;
import qsided.rpmechanics.events.RoleplayClassSelectedCallback;
import qsided.rpmechanics.gui.SkillEnabledEnchantingTableScreenHandler;
import qsided.rpmechanics.items.QuesComponents;
import qsided.rpmechanics.items.QuesItems;
import qsided.rpmechanics.networking.*;
import qsided.rpmechanics.skills.*;
import qsided.rpmechanics.skills.combat.ArcherySkill;
import qsided.rpmechanics.skills.combat.SwordsAndAxesSkills;
import qsided.rpmechanics.skills.leveling.ExperienceUp;
import qsided.rpmechanics.skills.leveling.LevelUp;
import qsided.rpmechanics.skills.milestones.SkillMilestone;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class RoleplayMechanicsCommon implements ModInitializer {
 
	public static final Logger LOGGER = LoggerFactory.getLogger("rpmechanics");
	public static final String MOD_ID = "rpmechanics";
    public static final RpMechanicsConfig OWO_CONFIG = RpMechanicsConfig.createAndLoad();
    
    public static final File RP_CLASSES_FILE = new File(FabricLoader.getInstance().getConfigDir() + "/rpmechanics/classes/classes.json");
    public static final File MILESTONES_FILE = new File(FabricLoader.getInstance().getConfigDir() + "/rpmechanics/milestones.json");
    public static final File MINING_XP_VALUES_FILE = new File(FabricLoader.getInstance().getConfigDir() + "/rpmechanics/skills/mining_xp_values.json");
    public static final File CRAFTING_REQS_FILE = new File(FabricLoader.getInstance().getConfigDir() + "/rpmechanics/skills/crafting_level_reqs.json");
    public static final File FARMING_XP_VALUES_FILE = new File(FabricLoader.getInstance().getConfigDir() + "/rpmechanics/skills/farming_xp_values.json");
    public static final File WOODCUTTING_XP_VALUES_FILE = new File(FabricLoader.getInstance().getConfigDir() + "/rpmechanics/skills/woodcutting_xp_values.json");
    
    static Map<Integer, RoleplayClass> RP_CLASSES;
    static List<BlockExperience> MINING_XP_VALUES;
    static List<SkillMilestone> MILESTONES;
    
    public static List<BlockExperience> getWoodcuttingXpValues() {
        return WOODCUTTING_XP_VALUES;
    }
    
    public static void setWoodcuttingXpValues(List<BlockExperience> woodcuttingXpValues) {
        WOODCUTTING_XP_VALUES = woodcuttingXpValues;
    }
    
    static List<BlockExperience> WOODCUTTING_XP_VALUES;
    static List<ItemCraftingRequirement> CRAFTING_REQS;
    
    public static List<BlockExperience> getFarmingXpValues() {
        return FARMING_XP_VALUES;
    }
    
    public static void setFarmingXpValues(List<BlockExperience> farmingXpValues) {
        FARMING_XP_VALUES = farmingXpValues;
    }
    
    static List<BlockExperience> FARMING_XP_VALUES;
    
    public static Map<Integer, RoleplayClass> getRpClasses() {
        return RP_CLASSES;
    }
    
    public void setRpClasses(Map<Integer, RoleplayClass> rpClasses) {
        RoleplayMechanicsCommon.RP_CLASSES = rpClasses;
    }
    
    public static List<BlockExperience> getMiningXpValues() {
        return MINING_XP_VALUES;
    }
    
    public static void setMiningXpValues(List<BlockExperience> miningXpValues) {
        RoleplayMechanicsCommon.MINING_XP_VALUES = miningXpValues;
    }
    
    public static List<ItemCraftingRequirement> getCraftingReqs() {
        return CRAFTING_REQS;
    }
    
    public static void setCraftingReqs(List<ItemCraftingRequirement> craftingReqsFile) {
        RoleplayMechanicsCommon.CRAFTING_REQS = craftingReqsFile;
    }
    
    public static List<SkillMilestone> getMilestones() {
        return MILESTONES;
    }
    
    public static void setMilestones(List<SkillMilestone> milestones) {
        RoleplayMechanicsCommon.MILESTONES = milestones;
    }
    
    @Override
	public void onInitialize() {
        PayloadTypeRegistry.playS2C().register(LevelUpPayload.ID, LevelUpPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(RequestSkillsPayload.ID, RequestSkillsPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(SendSkillsLevelsPayload.ID, SendSkillsLevelsPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(SendLevelsPayload.ID, SendLevelsPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(SendExperiencePayload.ID, SendExperiencePayload.CODEC);
        PayloadTypeRegistry.playS2C().register(SendSkillsLevelsTwoPayload.ID, SendSkillsLevelsTwoPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(SendSkillsExperiencePayload.ID, SendSkillsExperiencePayload.CODEC);
        PayloadTypeRegistry.playS2C().register(SendSkillsExperienceTwoPayload.ID, SendSkillsExperienceTwoPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(SendPlayerFallPayload.ID, SendPlayerFallPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(SendPlayerJumpPayload.ID, SendPlayerJumpPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(SendClassSelectedPayload.ID, SendClassSelectedPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(PlayerFirstJoinPayload.ID, PlayerFirstJoinPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(SendClassAndLevelPayload.ID, SendClassAndLevelPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(SendPlayerS2CPayload.ID, SendPlayerS2CPayload.CODEC);
        
        QuesItems.initialize();
        QuesBlocks.initialize();
        QuesBlockEntityTypes.initialize();
        QuesComponents.initialize();
        
        ObjectMapper mapper = new ObjectMapper();
        CollectionType miningRef = TypeFactory.defaultInstance().constructCollectionType(List.class, BlockExperience.class);
        CollectionType craftingRef = TypeFactory.defaultInstance().constructCollectionType(List.class, ItemCraftingRequirement.class);
        CollectionType milestoneRef = TypeFactory.defaultInstance().constructCollectionType(List.class, SkillMilestone.class);
        try {
            ConfigGenerator.genReqsConfig();
            ConfigGenerator.genWoodcuttingConfig();
            ConfigGenerator.genMiningConfig();
            ConfigGenerator.genDefaultClasses();
            ConfigGenerator.genPassiveMobs();
            ConfigGenerator.genCraftingConfig();
            ConfigGenerator.genFarmingConfig();
            ConfigGenerator.genDefaultMilestones();
            
            Map<Integer, RoleplayClass> rpClasses = mapper.readValue(RP_CLASSES_FILE, new TypeReference<Map<Integer, RoleplayClass>>() {});
            List<BlockExperience> miningXpValues = mapper.readValue(MINING_XP_VALUES_FILE, miningRef);
            List<BlockExperience> farmingXpValues = mapper.readValue(FARMING_XP_VALUES_FILE, miningRef);
            List<BlockExperience> woodcuttingXpValues = mapper.readValue(WOODCUTTING_XP_VALUES_FILE, miningRef);
            List<ItemCraftingRequirement> craftingReqs = mapper.readValue(CRAFTING_REQS_FILE, craftingRef);
            List<SkillMilestone> milestones = mapper.readValue(MILESTONES_FILE, milestoneRef);
            
            setRpClasses(rpClasses);
            setMiningXpValues(miningXpValues);
            setCraftingReqs(craftingReqs);
            setFarmingXpValues(farmingXpValues);
            setWoodcuttingXpValues(woodcuttingXpValues);
            setMilestones(milestones);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        MiningSkill.register();
        EnchantingSkill.register();
        SwordsAndAxesSkills.register();
        WoodcuttingSkill.register();
        EnduranceSkill.register();
        AgilitySkill.register();
        FarmingSkill.register();
        ArcherySkill.register();
        SwordsAndAxesSkills.register();
        SkillCheckHandler.register();
        Harvesting.initialize();
        
        LevelUp.onLevelUp();
        ExperienceUp.onExperienceUp();
        RoleplayClasses.initialize();
        RoleplayMechanicsAttributes.initialize();
        MobScaling.initialize();
        
        SkillsCommand.register();
        
        //BiomeModifications.addFeature(BiomeSelectors.foundInTheEnd(), GenerationStep.Feature.UNDERGROUND_ORES, MYTHRIL_DEBRIS_FEATURE);
        
        ServerPlayNetworking.registerGlobalReceiver(RequestSkillsPayload.ID, (payload, context) -> {
            PlayerData playerState = StateManager.getPlayerState(context.player());
            
            sendSkillData(playerState, context.player());
        });
        
        ServerPlayNetworking.registerGlobalReceiver(SendClassSelectedPayload.ID, (payload, context) -> {
            RoleplayClassSelectedCallback.EVENT.invoker().selectClass(context.player(), getPlayerState(context.player()), payload.rpClassId());
        });
        
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            PlayerData state = getPlayerState(handler.getPlayer());
            
            if (!state.hasJoinedBefore) {
                state.hasJoinedBefore = true;
                ServerPlayNetworking.send(handler.getPlayer(), new PlayerFirstJoinPayload(1));
                LOGGER.info(handler.getPlayer().getNameForScoreboard() + " has joined for the first time!");
            }
            
            if (OWO_CONFIG.displayJoinMessage()) {
                handler.getPlayer().sendMessage(Text.translatable("rpmechanics.player_joined"));
            }
            
            sendSkillData(state, handler.getPlayer());
        });
        
        //LootTableEvents.MODIFY.register((key, tableBuilder, source, registries) -> {
        //    if (key.equals(LootTables.END_CITY_TREASURE_CHEST)) {
        //        LootPool.Builder poolBuilder = LootPool.builder()
        //                .rolls(ConstantLootNumberProvider.create(1))
        //                .conditionally(RandomChanceLootCondition.builder(0.75f))
        //                .with(ItemEntry.builder(QuesItems.MYTHRIL_UPGRADE_TEMPLATE))
        //                .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1f, 1f)).build());
        //        tableBuilder.pool(poolBuilder.build());
        //    }
        //});
        
        ServerPlayerEvents.AFTER_RESPAWN.register((oldPlayer, player, alive) -> {
            PlayerData state = StateManager.getPlayerState(player);
            
            Identifier combatModifier = Identifier.of(RoleplayMechanicsCommon.MOD_ID, "combat_modifier");
            player.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).overwritePersistentModifier(
                    new EntityAttributeModifier(combatModifier, state.skillLevels.getOrDefault("combat", 1) * .18,
                            EntityAttributeModifier.Operation.ADD_VALUE)
            );
            player.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_SPEED).overwritePersistentModifier(
                    new EntityAttributeModifier(combatModifier, state.skillLevels.getOrDefault("combat", 1) * .03,
                            EntityAttributeModifier.Operation.ADD_VALUE)
            );
            
            Identifier enduranceModifier = Identifier.of(RoleplayMechanicsCommon.MOD_ID, "endurance_modifier");
            player.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).overwritePersistentModifier(
                    new EntityAttributeModifier(enduranceModifier, state.skillLevels.getOrDefault("endurance", 1) * 2,
                            EntityAttributeModifier.Operation.ADD_VALUE)
            );
            
            Identifier agilityModifier = Identifier.of(RoleplayMechanicsCommon.MOD_ID, "agility_modifier");
            player.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).overwritePersistentModifier(
                    new EntityAttributeModifier(agilityModifier, state.skillLevels.getOrDefault("agility", 1) * 0.001, EntityAttributeModifier.Operation.ADD_VALUE)
            );
            player.getAttributeInstance(EntityAttributes.GENERIC_SAFE_FALL_DISTANCE).overwritePersistentModifier(
                    new EntityAttributeModifier(agilityModifier, state.skillLevels.getOrDefault("agility", 1) * 0.1, EntityAttributeModifier.Operation.ADD_VALUE)
            );
            player.getAttributeInstance(EntityAttributes.GENERIC_JUMP_STRENGTH).overwritePersistentModifier(
                    new EntityAttributeModifier(agilityModifier, state.skillLevels.getOrDefault("agility", 1) * 0.0058, EntityAttributeModifier.Operation.ADD_VALUE)
            );
        });
        
        LOGGER.info("Que's mod loaded!");
    }
	
	public PlayerData getPlayerState(ServerPlayerEntity player) {
		return StateManager.getPlayerState(player);
	}
	
	public static void sendSkillData(PlayerData playerState, ServerPlayerEntity player) {
		Integer miningLevel = playerState.skillLevels.getOrDefault("mining", 1);
		Integer enchantingLevel = playerState.skillLevels.getOrDefault("enchanting", 1);
		Integer swordsLevel = playerState.skillLevels.getOrDefault("swords", 1);
		Integer axesLevel = playerState.skillLevels.getOrDefault("axes", 1);
		Integer bowsLevel = playerState.skillLevels.getOrDefault("bows", 1);
		Integer woodcuttingLevel = playerState.skillLevels.getOrDefault("woodcutting", 1);
		Integer enduranceLevel = playerState.skillLevels.getOrDefault("endurance", 1);
        Integer agilityLevel = playerState.skillLevels.getOrDefault("agility", 1);
		Integer craftingLevel = playerState.skillLevels.getOrDefault("crafting", 1);
		Integer smithingLevel = playerState.skillLevels.getOrDefault("smithing", 1);
		Integer farmingLevel = playerState.skillLevels.getOrDefault("farming", 1);
		Integer cookingLevel = playerState.skillLevels.getOrDefault("cooking", 1);
		Integer swimmingLevel = playerState.skillLevels.getOrDefault("swimming", 1);
		
		Float miningExp = playerState.skillExperience.getOrDefault("mining", 0F);
		Float enchantingExp = playerState.skillExperience.getOrDefault("enchanting", 0F);
		Float swordsExp = playerState.skillExperience.getOrDefault("swords", 0F);
		Float axesExp = playerState.skillExperience.getOrDefault("axes", 0F);
		Float bowsExp = playerState.skillExperience.getOrDefault("bows", 0F);
		Float woodcuttingExp = playerState.skillExperience.getOrDefault("woodcutting", 0F);
		Float enduranceExp = playerState.skillExperience.getOrDefault("endurance", 0F);
        Float agilityExp = playerState.skillExperience.getOrDefault("agility", 0F);
		Float craftingExp = playerState.skillExperience.getOrDefault("crafting", 0F);
		Float smithingExp = playerState.skillExperience.getOrDefault("smithing", 0F);
        Float farmingExp = playerState.skillExperience.getOrDefault("farming", 0F);
        Float cookingExp = playerState.skillExperience.getOrDefault("cooking", 0F);
        Float swimmingExp = playerState.skillExperience.getOrDefault("swimming", 0F);
        
        ServerPlayNetworking.send(player, new SendClassAndLevelPayload(playerState.rpClass, playerState.rpClassLevel, playerState.rpClassExp));
        
        NbtCompound levels = new NbtCompound();
        levels.putInt("agility", agilityLevel);
		levels.putInt("axes", axesLevel);
		levels.putInt("bows", bowsLevel);
		levels.putInt("cooking", cookingLevel);
		levels.putInt("crafting", craftingLevel);
		levels.putInt("enchanting", enchantingLevel);
		levels.putInt("endurance", enduranceLevel);
		levels.putInt("farming", farmingLevel);
		levels.putInt("mining", miningLevel);
		levels.putInt("smithing", smithingLevel);
		levels.putInt("swimming", swimmingLevel);
		levels.putInt("swords", swordsLevel);
		levels.putInt("woodcutting", woodcuttingLevel);
        
        NbtCompound experience = new NbtCompound();
        experience.putFloat("agility", agilityExp);
		experience.putFloat("axes", axesExp);
		experience.putFloat("bows", bowsExp);
		experience.putFloat("cooking", cookingExp);
		experience.putFloat("crafting", craftingExp);
		experience.putFloat("enchanting", enchantingExp);
		experience.putFloat("endurance", enduranceExp);
		experience.putFloat("farming", farmingExp);
		experience.putFloat("mining", miningExp);
		experience.putFloat("smithing", smithingExp);
		experience.putFloat("swimming", swimmingExp);
		experience.putFloat("swords", swordsExp);
		experience.putFloat("woodcutting", woodcuttingExp);
        
        ServerPlayNetworking.send(player, new SendLevelsPayload(levels));
		ServerPlayNetworking.send(player, new SendExperiencePayload(experience));
		
		//ServerPlayNetworking.send(player, new SendSkillsLevelsPayload(miningLevel, enchantingLevel, swordsLevel, woodcuttingLevel, enduranceLevel, agilityLevel));
		//ServerPlayNetworking.send(player, new SendSkillsLevelsTwoPayload(farmingLevel, smithingLevel, craftingLevel, axesLevel, bowsLevel, cookingLevel));
		//ServerPlayNetworking.send(player, new SendSkillsExperiencePayload(miningExp, enchantingExp, swordsExp, woodcuttingExp, enduranceExp, agilityExp));
        //ServerPlayNetworking.send(player, new SendSkillsExperienceTwoPayload(farmingExp, smithingExp, craftingExp, axesExp, bowsExp, cookingExp));
	}
}