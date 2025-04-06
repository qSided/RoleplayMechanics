package qsided.rpmechanics.config;

import io.wispforest.owo.config.Option;
import io.wispforest.owo.config.annotation.Config;
import io.wispforest.owo.config.annotation.Modmenu;
import io.wispforest.owo.config.annotation.Nest;
import io.wispforest.owo.config.annotation.Sync;
import qsided.rpmechanics.RoleplayMechanicsCommon;

@Modmenu(modId = RoleplayMechanicsCommon.MOD_ID)
@Config(name = "rpmechanics", wrapperName = "RpMechanicsConfig")
public class QuesConfigModel {
    
    @Sync(Option.SyncMode.OVERRIDE_CLIENT)
    public boolean displayJoinMessage = true;
    @Sync(Option.SyncMode.OVERRIDE_CLIENT)
    public boolean enableRequirements = true;
    
    @Nest
    public ExperienceOptions experienceOptions = new ExperienceOptions();
    
    @Nest
    public SkillOptions skillOptions = new SkillOptions();
    public static class SkillOptions {
        @Sync(Option.SyncMode.OVERRIDE_CLIENT)
        public boolean randomToggle = true;
        @Nest
        public AgilitySettings agilitySettings = new AgilitySettings();
        public static class AgilitySettings {
            @Sync(Option.SyncMode.OVERRIDE_CLIENT)
            public double movementSpeed = 0.01;
            public double safeFall = 0.1;
            public double jumpStrength = 0.0052;
        }
        @Nest
        public BowsSettings bowsSettings = new BowsSettings();
        public static class BowsSettings {
            @Sync(Option.SyncMode.OVERRIDE_CLIENT)
            public double speed = 0.5;
            public double accuracy = 0.01;
        }
        @Nest
        public SwordsSettings swordsSettings = new SwordsSettings();
        public static class SwordsSettings {
            @Sync(Option.SyncMode.OVERRIDE_CLIENT)
            public double damage = 0.18;
            public double speed = 0.03;
        }
        @Nest
        public AxesSettings axesSettings = new AxesSettings();
        public static class AxesSettings {
            @Sync(Option.SyncMode.OVERRIDE_CLIENT)
            public double damage = 0.18;
            public double speed = 0.03;
        }
        @Nest
        public EnduranceSettings enduranceSettings = new EnduranceSettings();
        public static class EnduranceSettings {
            @Sync(Option.SyncMode.OVERRIDE_CLIENT)
            public double health = 1;
        }
        @Nest
        public FarmingSettings farmingSettings = new FarmingSettings();
        public static class FarmingSettings {
            @Sync(Option.SyncMode.OVERRIDE_CLIENT)
            public int levelForRightClickHarvest = 10;
        }
        @Nest
        public MiningSettings miningSettings = new MiningSettings();
        public static class MiningSettings {
            @Sync(Option.SyncMode.OVERRIDE_CLIENT)
            public int levelForVeinMining = 10;
            @Sync(Option.SyncMode.OVERRIDE_CLIENT)
            public double miningSpeed = 0.5;
        }
        @Nest
        public WoodcuttingSettings woodcuttingSettings = new WoodcuttingSettings();
        public static class WoodcuttingSettings {
            @Sync(Option.SyncMode.OVERRIDE_CLIENT)
            public int levelForTreeChopping = 10;
            @Sync(Option.SyncMode.OVERRIDE_CLIENT)
            public double choppingSpeed = 0.5;
        }
    }
    
    public static class ExperienceOptions {
        @Sync(Option.SyncMode.OVERRIDE_CLIENT)
        public boolean useGlobal = true;
        @Nest
        public GlobalOptions globalOptions = new GlobalOptions();
        public static class GlobalOptions {
            @Sync(Option.SyncMode.OVERRIDE_CLIENT)
            public float baseExperience = 60;
            @Sync(Option.SyncMode.OVERRIDE_CLIENT)
            public Choices multiplicativeOrAdditive = Choices.MULTIPLY;
            @Sync(Option.SyncMode.OVERRIDE_CLIENT)
            public double amount = 3.4;
        }
        @Nest
        public AgilityOptions agilityOptions = new AgilityOptions();
        public static class AgilityOptions {
            @Sync(Option.SyncMode.OVERRIDE_CLIENT)
            public float baseExperience = 60;
            @Sync(Option.SyncMode.OVERRIDE_CLIENT)
            public Choices multiplicativeOrAdditive = Choices.MULTIPLY;
            @Sync(Option.SyncMode.OVERRIDE_CLIENT)
            public double amount = 3.4;
        }
        @Nest
        public BowsOptions bowsOptions = new BowsOptions();
        public static class BowsOptions {
            @Sync(Option.SyncMode.OVERRIDE_CLIENT)
            public float baseExperience = 60;
            @Sync(Option.SyncMode.OVERRIDE_CLIENT)
            public Choices multiplicativeOrAdditive = Choices.MULTIPLY;
            @Sync(Option.SyncMode.OVERRIDE_CLIENT)
            public double amount = 3.4;
        }
        @Nest
        public SwordsOptions swordsOptions = new SwordsOptions();
        public static class SwordsOptions {
            @Sync(Option.SyncMode.OVERRIDE_CLIENT)
            public float baseExperience = 60;
            @Sync(Option.SyncMode.OVERRIDE_CLIENT)
            public Choices multiplicativeOrAdditive = Choices.MULTIPLY;
            @Sync(Option.SyncMode.OVERRIDE_CLIENT)
            public double amount = 3.4;
        }
        @Nest
        public AxesOptions axesOptions = new AxesOptions();
        public static class AxesOptions {
            @Sync(Option.SyncMode.OVERRIDE_CLIENT)
            public float baseExperience = 60;
            @Sync(Option.SyncMode.OVERRIDE_CLIENT)
            public Choices multiplicativeOrAdditive = Choices.MULTIPLY;
            @Sync(Option.SyncMode.OVERRIDE_CLIENT)
            public double amount = 3.4;
        }
        @Nest
        public CraftingOptions craftingOptions = new CraftingOptions();
        public static class CraftingOptions {
            @Sync(Option.SyncMode.OVERRIDE_CLIENT)
            public float baseExperience = 60;
            @Sync(Option.SyncMode.OVERRIDE_CLIENT)
            public Choices multiplicativeOrAdditive = Choices.MULTIPLY;
            @Sync(Option.SyncMode.OVERRIDE_CLIENT)
            public double amount = 3.4;
        }
        @Nest
        public EnchantingOptions enchantingOptions = new EnchantingOptions();
        public static class EnchantingOptions {
            @Sync(Option.SyncMode.OVERRIDE_CLIENT)
            public float baseExperience = 60;
            @Sync(Option.SyncMode.OVERRIDE_CLIENT)
            public Choices multiplicativeOrAdditive = Choices.MULTIPLY;
            @Sync(Option.SyncMode.OVERRIDE_CLIENT)
            public double amount = 3.4;
        }
        @Nest
        public EnduranceOptions enduranceOptions = new EnduranceOptions();
        public static class EnduranceOptions {
            @Sync(Option.SyncMode.OVERRIDE_CLIENT)
            public float baseExperience = 60;
            @Sync(Option.SyncMode.OVERRIDE_CLIENT)
            public Choices multiplicativeOrAdditive = Choices.MULTIPLY;
            @Sync(Option.SyncMode.OVERRIDE_CLIENT)
            public double amount = 3.4;
        }
        @Nest
        public FarmingOptions farmingOptions = new FarmingOptions();
        public static class FarmingOptions {
            @Sync(Option.SyncMode.OVERRIDE_CLIENT)
            public float baseExperience = 60;
            @Sync(Option.SyncMode.OVERRIDE_CLIENT)
            public Choices multiplicativeOrAdditive = Choices.MULTIPLY;
            @Sync(Option.SyncMode.OVERRIDE_CLIENT)
            public double amount = 3.4;
        }
        @Nest
        public MiningOptions miningOptions = new MiningOptions();
        public static class MiningOptions {
            @Sync(Option.SyncMode.OVERRIDE_CLIENT)
            public float baseExperience = 60;
            @Sync(Option.SyncMode.OVERRIDE_CLIENT)
            public Choices multiplicativeOrAdditive = Choices.MULTIPLY;
            @Sync(Option.SyncMode.OVERRIDE_CLIENT)
            public double amount = 3.4;
        }
        @Nest
        public SmithingOptions smithingOptions = new SmithingOptions();
        public static class SmithingOptions {
            @Sync(Option.SyncMode.OVERRIDE_CLIENT)
            public float baseExperience = 60;
            @Sync(Option.SyncMode.OVERRIDE_CLIENT)
            public Choices multiplicativeOrAdditive = Choices.MULTIPLY;
            @Sync(Option.SyncMode.OVERRIDE_CLIENT)
            public double amount = 3.4;
        }
        @Nest
        public WoodcuttingOptions woodcuttingOptions = new WoodcuttingOptions();
        public static class WoodcuttingOptions {
            @Sync(Option.SyncMode.OVERRIDE_CLIENT)
            public float baseExperience = 60;
            @Sync(Option.SyncMode.OVERRIDE_CLIENT)
            public Choices multiplicativeOrAdditive = Choices.MULTIPLY;
            @Sync(Option.SyncMode.OVERRIDE_CLIENT)
            public double amount = 3.4;
        }
    }
    
    public enum Choices {
        ADD, MULTIPLY;
    }
}
