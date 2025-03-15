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
    
    //@Nest
    //public SkillOptions skillOptions = new SkillOptions();
    //public static class SkillOptions {
    //    @Sync(Option.SyncMode.OVERRIDE_CLIENT)
    //    public boolean enableAgility = true;
    //    @Nest
    //    public AgilitySettings agilitySettings = new AgilitySettings();
    //    public static class AgilitySettings {
    //        @Sync(Option.SyncMode.OVERRIDE_CLIENT)
    //        public double jumpHeightIncrease = 0.0058;
    //        @Sync(Option.SyncMode.OVERRIDE_CLIENT)
    //        public double safeFallDistanceIncrease = 0.1;
    //        @Sync(Option.SyncMode.OVERRIDE_CLIENT)
    //        public double movementSpeedIncrease = 0.001;
    //    }
    //}
    
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
        public CombatOptions combatOptions = new CombatOptions();
        public static class CombatOptions {
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
