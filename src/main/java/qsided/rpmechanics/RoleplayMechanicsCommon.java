package qsided.rpmechanics;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.PlacedFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import qsided.rpmechanics.blocks.QuesBlocks;
import qsided.rpmechanics.commands.SkillsCommand;
import qsided.rpmechanics.config.ConfigGenerator;
import qsided.rpmechanics.config.RpMechanicsConfig;
import qsided.rpmechanics.config.experience_values.BlockExperience;
import qsided.rpmechanics.config.requirements.ItemCraftingRequirement;
import qsided.rpmechanics.config.roleplay_classes.RoleplayClass;
import qsided.rpmechanics.events.IncreaseSkillExperienceCallback;
import qsided.rpmechanics.events.RoleplayClassSelectedCallback;
import qsided.rpmechanics.items.QuesItems;
import qsided.rpmechanics.items.materials.QuesArmorMaterials;
import qsided.rpmechanics.networking.*;
import qsided.rpmechanics.skills.*;
import qsided.rpmechanics.skills.leveling.ExperienceUp;
import qsided.rpmechanics.skills.leveling.LevelUp;
import qsided.rpmechanics.tags.blocks.QuesBlockTags;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class RoleplayMechanicsCommon implements ModInitializer {
 
	public static final Logger LOGGER = LoggerFactory.getLogger("rpmechanics");
	public static final String MOD_ID = "rpmechanics";
	public static final RegistryKey<PlacedFeature> MYTHRIL_DEBRIS_FEATURE = RegistryKey.of(RegistryKeys.PLACED_FEATURE, Identifier.of(MOD_ID, "mythril_debris_feature"));
    public static final RpMechanicsConfig OWO_CONFIG = RpMechanicsConfig.createAndLoad();
    
    public static final File RP_CLASSES_FILE = new File(FabricLoader.getInstance().getConfigDir() + "/rpmechanics/classes/classes.json");
    public static final File MINING_XP_VALUES_FILE = new File(FabricLoader.getInstance().getConfigDir() + "/rpmechanics/skills/mining.json");
    public static final File CRAFTING_REQS_FILE = new File(FabricLoader.getInstance().getConfigDir() + "/rpmechanics/skills/crafting.json");
    public static final File FARMING_XP_VALUES_FILE = new File(FabricLoader.getInstance().getConfigDir() + "/rpmechanics/skills/farming.json");
    
    static Map<Integer, RoleplayClass> RP_CLASSES;
    static List<BlockExperience> MINING_XP_VALUES;
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
    
    @Override
	public void onInitialize() {
        QuesItems.initialize();
        QuesArmorMaterials.initialize();
        QuesBlockTags.initialize();
        QuesBlocks.initialize();
        MobScaling.initialize();
        
        ObjectMapper mapper = new ObjectMapper();
        CollectionType miningRef = TypeFactory.defaultInstance().constructCollectionType(List.class, BlockExperience.class);
        CollectionType craftingRef = TypeFactory.defaultInstance().constructCollectionType(List.class, ItemCraftingRequirement.class);
        try {
            ConfigGenerator.genReqsConfig();
            ConfigGenerator.genWoodcuttingConfig();
            ConfigGenerator.genMiningConfig();
            ConfigGenerator.genDefaultClasses();
            ConfigGenerator.genPassiveMobs();
            ConfigGenerator.genCraftingConfig();
            ConfigGenerator.genFarmingConfig();
            
            Map<Integer, RoleplayClass> rpClasses = mapper.readValue(RP_CLASSES_FILE, new TypeReference<Map<Integer, RoleplayClass>>() {});
            List<BlockExperience> miningXpValues = mapper.readValue(MINING_XP_VALUES_FILE, miningRef);
            List<BlockExperience> farmingXpValues = mapper.readValue(FARMING_XP_VALUES_FILE, miningRef);
            List<ItemCraftingRequirement> craftingReqs = mapper.readValue(CRAFTING_REQS_FILE, craftingRef);
            
            setRpClasses(rpClasses);
            setMiningXpValues(miningXpValues);
            setCraftingReqs(craftingReqs);
            setFarmingXpValues(farmingXpValues);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        MiningSkill.register();
        EnchantingSkill.register();
        CombatSkill.register();
        WoodcuttingSkill.register();
        EnduranceSkill.register();
        FarmingSkill.register();
        SkillCheckHandler.register();
        
        LevelUp.onLevelUp();
        ExperienceUp.onExperienceUp();
        RoleplayClasses.initialize();
        
        SkillsCommand.register();
        
        BiomeModifications.addFeature(BiomeSelectors.foundInTheEnd(), GenerationStep.Feature.UNDERGROUND_ORES, MYTHRIL_DEBRIS_FEATURE);
        
        PayloadTypeRegistry.playS2C().register(LevelUpPayload.ID, LevelUpPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(RequestSkillsPayload.ID, RequestSkillsPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(SendSkillsLevelsPayload.ID, SendSkillsLevelsPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(SendSkillsExperiencePayload.ID, SendSkillsExperiencePayload.CODEC);
        PayloadTypeRegistry.playC2S().register(SendPlayerFallPayload.ID, SendPlayerFallPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(SendPlayerJumpPayload.ID, SendPlayerJumpPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(SendClassSelectedPayload.ID, SendClassSelectedPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(PlayerFirstJoinPayload.ID, PlayerFirstJoinPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(SendClassAndLevelPayload.ID, SendClassAndLevelPayload.CODEC);
        
        ServerPlayNetworking.registerGlobalReceiver(RequestSkillsPayload.ID, (payload, context) -> {
            PlayerData playerState = StateManager.getPlayerState(context.player());
            
            sendSkillData(playerState, context.player());
        });
        
        ServerPlayNetworking.registerGlobalReceiver(SendPlayerFallPayload.ID, (payload, context) -> {
            PlayerData state = StateManager.getPlayerState(context.player());
            IncreaseSkillExperienceCallback.EVENT.invoker().increaseExp(context.player(), state, "agility", (float) Math.min((payload.integer() / 100), 50));
        });
        
        ServerPlayNetworking.registerGlobalReceiver(SendPlayerJumpPayload.ID, (payload, context) -> {
            PlayerData state = StateManager.getPlayerState(context.player());
            IncreaseSkillExperienceCallback.EVENT.invoker().increaseExp(context.player(), state, "agility", (float) (payload.integer()));
        });
        
        ServerPlayNetworking.registerGlobalReceiver(SendClassSelectedPayload.ID, (payload, context) -> {
            RoleplayClassSelectedCallback.EVENT.invoker().selectClass(context.player(), getPlayerState(context.player()), payload.rpClassId());
        });
        
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            PlayerData state = getPlayerState(handler.getPlayer());
            
            //if (!state.hasJoinedBefore) {
            //    ServerPlayNetworking.send(handler.getPlayer(), new PlayerFirstJoinPayload(1));
            //    System.out.println("Player joined for the first time!");
            //    state.hasJoinedBefore = true;
            //}
            
            if (OWO_CONFIG.displayJoinMessage()) {
                handler.getPlayer().sendMessage(Text.translatable("rpmechanics.player_joined"));
            }
            
            sendSkillData(state, handler.getPlayer());
        });
        
        LootTableEvents.MODIFY.register((key, tableBuilder, source, registries) -> {
            if (key.equals(LootTables.END_CITY_TREASURE_CHEST)) {
                LootPool.Builder poolBuilder = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .conditionally(RandomChanceLootCondition.builder(0.75f))
                        .with(ItemEntry.builder(QuesItems.MYTHRIL_UPGRADE_TEMPLATE))
                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1f, 1f)).build());
                tableBuilder.pool(poolBuilder.build());
            }
        });
        
        ServerPlayerEvents.AFTER_RESPAWN.register((oldPlayer, player, alive) -> {
            PlayerData state = StateManager.getPlayerState(player);
            
            Identifier combatModifier = Identifier.of(RoleplayMechanicsCommon.MOD_ID, "combat_modifier");
            player.getAttributeInstance(EntityAttributes.ATTACK_DAMAGE).overwritePersistentModifier(
                    new EntityAttributeModifier(combatModifier, state.skillLevels.getOrDefault("combat", 1) * .18,
                            EntityAttributeModifier.Operation.ADD_VALUE)
            );
            player.getAttributeInstance(EntityAttributes.ATTACK_SPEED).overwritePersistentModifier(
                    new EntityAttributeModifier(combatModifier, state.skillLevels.getOrDefault("combat", 1) * .03,
                            EntityAttributeModifier.Operation.ADD_VALUE)
            );
            
            Identifier enduranceModifier = Identifier.of(RoleplayMechanicsCommon.MOD_ID, "endurance_modifier");
            player.getAttributeInstance(EntityAttributes.MAX_HEALTH).overwritePersistentModifier(
                    new EntityAttributeModifier(enduranceModifier, state.skillLevels.getOrDefault("endurance", 1) * 2,
                            EntityAttributeModifier.Operation.ADD_VALUE)
            );
            
            Identifier agilityModifier = Identifier.of(RoleplayMechanicsCommon.MOD_ID, "agility_modifier");
            player.getAttributeInstance(EntityAttributes.MOVEMENT_SPEED).overwritePersistentModifier(
                    new EntityAttributeModifier(agilityModifier, state.skillLevels.getOrDefault("agility", 1) * 0.001, EntityAttributeModifier.Operation.ADD_VALUE)
            );
            player.getAttributeInstance(EntityAttributes.SAFE_FALL_DISTANCE).overwritePersistentModifier(
                    new EntityAttributeModifier(agilityModifier, state.skillLevels.getOrDefault("agility", 1) * 0.1, EntityAttributeModifier.Operation.ADD_VALUE)
            );
            player.getAttributeInstance(EntityAttributes.JUMP_STRENGTH).overwritePersistentModifier(
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
		Integer combatLevel = playerState.skillLevels.getOrDefault("combat", 1);
		Integer woodcuttingLevel = playerState.skillLevels.getOrDefault("woodcutting", 1);
		Integer enduranceLevel = playerState.skillLevels.getOrDefault("endurance", 1);
        Integer agilityLevel = playerState.skillLevels.getOrDefault("agility", 1);
		Integer craftingLevel = playerState.skillLevels.getOrDefault("crafting", 1);
		Integer smithingLevel = playerState.skillLevels.getOrDefault("smithing", 1);
		
		Float miningExp = playerState.skillExperience.getOrDefault("mining", 0F);
		Float enchantingExp = playerState.skillExperience.getOrDefault("enchanting", 0F);
		Float combatExp = playerState.skillExperience.getOrDefault("combat", 0F);
		Float woodcuttingExp = playerState.skillExperience.getOrDefault("woodcutting", 0F);
		Float enduranceExp = playerState.skillExperience.getOrDefault("endurance", 0F);
        Float agilityExp = playerState.skillExperience.getOrDefault("agility", 0F);
		Float craftingExp = playerState.skillExperience.getOrDefault("crafting", 0F);
		Float smithingExp = playerState.skillExperience.getOrDefault("smithing", 0F);
        
        ServerPlayNetworking.send(player, new SendClassAndLevelPayload(playerState.rpClass, playerState.rpClassLevel, playerState.rpClassExp));
		
		ServerPlayNetworking.send(player, new SendSkillsLevelsPayload(miningLevel, enchantingLevel, combatLevel, woodcuttingLevel, enduranceLevel, agilityLevel, craftingLevel, smithingLevel));
		ServerPlayNetworking.send(player, new SendSkillsExperiencePayload(miningExp, enchantingExp, combatExp, woodcuttingExp, enduranceExp, agilityExp, craftingExp, smithingExp));
	}
}