package qsided.rpmechanics.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import qsided.rpmechanics.RoleplayClasses;
import qsided.rpmechanics.config.experience_values.BlockExperience;
import qsided.rpmechanics.config.requirements.ItemCraftingRequirement;
import qsided.rpmechanics.config.requirements.ItemWithRequirements;
import qsided.rpmechanics.config.requirements.Requirements;
import qsided.rpmechanics.config.roleplay_classes.RoleplayClass;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigGenerator {
    
    public static void genReqsConfig() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        List<ItemWithRequirements> items = new ArrayList<>();
        
        items.add(new ItemWithRequirements("minecraft:wooden_sword", new Requirements("combat", -1, 1)));
        items.add(new ItemWithRequirements("minecraft:wooden_pickaxe", new Requirements("farming", -1, 1)));
        items.add(new ItemWithRequirements("minecraft:wooden_axe", new Requirements("woodcutting", -1, 1)));
        items.add(new ItemWithRequirements("minecraft:wooden_shovel", new Requirements("farming", -1, 1)));
        //items.add(new ItemWithRequirements("minecraft:wooden_hoe", new Requirements("agility", 1)));
        items.add(new ItemWithRequirements("minecraft:leather_helmet", new Requirements("endurance", -1, 1)));
        items.add(new ItemWithRequirements("minecraft:leather_chestplate", new Requirements("endurance", -1, 1)));
        items.add(new ItemWithRequirements("minecraft:leather_leggings", new Requirements("endurance", -1, 1)));
        items.add(new ItemWithRequirements("minecraft:leather_boots", new Requirements("endurance", -1, 1)));
        
        items.add(new ItemWithRequirements("minecraft:stone_sword", new Requirements("combat", -1, 4)));
        items.add(new ItemWithRequirements("minecraft:stone_pickaxe", new Requirements("farming", -1, 4)));
        items.add(new ItemWithRequirements("minecraft:stone_axe", new Requirements("woodcutting", -1, 4)));
        items.add(new ItemWithRequirements("minecraft:stone_shovel", new Requirements("farming", -1, 4)));
        //items.add(new ItemWithRequirements("minecraft:stone_hoe", new Requirements("agility", 4)));
        items.add(new ItemWithRequirements("minecraft:chainmail_helmet", new Requirements("endurance", -1, 10)));
        items.add(new ItemWithRequirements("minecraft:chainmail_chestplate", new Requirements("endurance", -1, 10)));
        items.add(new ItemWithRequirements("minecraft:chainmail_leggings", new Requirements("endurance", -1, 10)));
        items.add(new ItemWithRequirements("minecraft:chainmail_boots", new Requirements("endurance", -1, 10)));
        
        items.add(new ItemWithRequirements("minecraft:iron_sword", new Requirements("combat", -1, 12)));
        items.add(new ItemWithRequirements("minecraft:iron_pickaxe", new Requirements("farming", -1, 12)));
        items.add(new ItemWithRequirements("minecraft:iron_axe", new Requirements("woodcutting", -1, 12)));
        items.add(new ItemWithRequirements("minecraft:iron_shovel", new Requirements("farming", -1, 12)));
        //items.add(new ItemWithRequirements("minecraft:iron_hoe", new Requirements("agility", 12)));
        items.add(new ItemWithRequirements("minecraft:iron_helmet", new Requirements("endurance", -1, 10)));
        items.add(new ItemWithRequirements("minecraft:iron_chestplate", new Requirements("endurance", -1, 10)));
        items.add(new ItemWithRequirements("minecraft:iron_leggings", new Requirements("endurance", -1, 10)));
        items.add(new ItemWithRequirements("minecraft:iron_boots", new Requirements("endurance", -1, 10)));
        
        items.add(new ItemWithRequirements("minecraft:golden_sword", new Requirements("combat", -1, 15)));
        items.add(new ItemWithRequirements("minecraft:golden_pickaxe", new Requirements("farming", -1, 15)));
        items.add(new ItemWithRequirements("minecraft:golden_axe", new Requirements("woodcutting", -1, 15)));
        items.add(new ItemWithRequirements("minecraft:golden_shovel", new Requirements("farming", -1, 15)));
        //items.add(new ItemWithRequirements("minecraft:gold_hoe", new Requirements("agility", 15)));
        items.add(new ItemWithRequirements("minecraft:golden_helmet", new Requirements("endurance", -1, 15)));
        items.add(new ItemWithRequirements("minecraft:golden_chestplate", new Requirements("endurance", -1, 15)));
        items.add(new ItemWithRequirements("minecraft:golden_leggings", new Requirements("endurance", -1, 15)));
        items.add(new ItemWithRequirements("minecraft:golden_boots", new Requirements("endurance", -1, 15)));
        
        items.add(new ItemWithRequirements("minecraft:diamond_sword", new Requirements("combat", -1, 25)));
        items.add(new ItemWithRequirements("minecraft:diamond_pickaxe", new Requirements("farming", -1, 25)));
        items.add(new ItemWithRequirements("minecraft:diamond_axe", new Requirements("woodcutting", -1, 25)));
        items.add(new ItemWithRequirements("minecraft:diamond_shovel", new Requirements("farming", -1, 25)));
        //items.add(new ItemWithRequirements("minecraft:diamond_hoe", new Requirements("agility", 25)));
        items.add(new ItemWithRequirements("minecraft:diamond_helmet", new Requirements("endurance", -1, 20)));
        items.add(new ItemWithRequirements("minecraft:diamond_chestplate", new Requirements("endurance", -1, 20)));
        items.add(new ItemWithRequirements("minecraft:diamond_leggings", new Requirements("endurance", -1, 20)));
        items.add(new ItemWithRequirements("minecraft:diamond_boots", new Requirements("endurance", -1, 20)));
        
        items.add(new ItemWithRequirements("minecraft:netherite_sword", new Requirements("combat", -1, 40)));
        items.add(new ItemWithRequirements("minecraft:netherite_pickaxe", new Requirements("farming", -1, 40)));
        items.add(new ItemWithRequirements("minecraft:netherite_axe", new Requirements("woodcutting", -1, 40)));
        items.add(new ItemWithRequirements("minecraft:netherite_shovel", new Requirements("farming", -1, 40)));
        //items.add(new ItemWithRequirements("minecraft:netherite_hoe", new Requirements("agility", 40)));
        items.add(new ItemWithRequirements("minecraft:netherite_helmet", new Requirements("endurance", -1, 30)));
        items.add(new ItemWithRequirements("minecraft:netherite_chestplate", new Requirements("endurance", -1, 30)));
        items.add(new ItemWithRequirements("minecraft:netherite_leggings", new Requirements("endurance", -1, 30)));
        items.add(new ItemWithRequirements("minecraft:netherite_boots", new Requirements("endurance", -1, 30)));
        
        items.add(new ItemWithRequirements("rpmechanics:mythril_sword", new Requirements("combat", -1, 50)));
        items.add(new ItemWithRequirements("rpmechanics:mythril_pickaxe", new Requirements("farming", -1, 50)));
        items.add(new ItemWithRequirements("rpmechanics:mythril_axe", new Requirements("woodcutting", -1, 50)));
        items.add(new ItemWithRequirements("rpmechanics:mythril_shovel", new Requirements("farming", -1, 50)));
        //items.add(new ItemWithRequirements("rpmechanics:mythril_hoe", new Requirements("agility", 40)));
        items.add(new ItemWithRequirements("rpmechanics:mythril_helmet", new Requirements("endurance", -1, 40)));
        items.add(new ItemWithRequirements("rpmechanics:mythril_chestplate", new Requirements("endurance", -1, 40)));
        items.add(new ItemWithRequirements("rpmechanics:mythril_leggings", new Requirements("endurance", -1, 40)));
        items.add(new ItemWithRequirements("rpmechanics:mythril_boots", new Requirements("endurance", -1, 40)));
        
        
        
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        
        File dir = new File(FabricLoader.getInstance().getConfigDir() + "/rpmechanics");
        File reqs = new File(FabricLoader.getInstance().getConfigDir() + "/rpmechanics/reqs.json");
        if (!reqs.exists() && !reqs.isDirectory()) {
            try {
                dir.mkdirs();
                mapper.writeValue(reqs, items);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    
    public static void genWoodcuttingConfig() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        List<BlockExperience> woodcutting = new ArrayList<>();
        
        Registries.BLOCK.forEach(block -> {
            if (block.asItem().toString().contains("log") ||
                    block.asItem().toString().contains("wood") ||
                    block.asItem().toString().contains("roots") ||
                    block.asItem().toString().contains("bamboo")) {
                woodcutting.add(new BlockExperience(block.asItem().toString(),
                        Map.of(
                                "woodcutting", 20F
                               )));
            }
        });
        
        
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        
        File dir = new File(FabricLoader.getInstance().getConfigDir() + "/rpmechanics/skills");
        File reqs = new File(FabricLoader.getInstance().getConfigDir() + "/rpmechanics/skills/woodcutting.json");
        if (!reqs.exists() && !reqs.isDirectory()) {
            try {
                dir.mkdirs();
                mapper.writeValue(reqs, woodcutting);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        
        
    }
    
    public static void genMiningConfig() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        List<BlockExperience> mining = new ArrayList<>();
        
        Registries.BLOCK.forEach(block -> {
            if (block.asItem().toString().contains("stone") ||
                    block.asItem().toString().contains("andesite") ||
                    block.asItem().toString().contains("diorite") ||
                    block.asItem().toString().contains("granite") ||
                    block.asItem().toString().contains("slate") ||
                    block.asItem().toString().contains("dirt") ||
                    block.asItem().toString().contains("grass")) {
                mining.add(new BlockExperience(block.asItem().toString(),
                        Map.of(
                                "mining", 5 + (block.getHardness() / 3))));
            } else if (
                    block.asItem().toString().contains("ore") ||
                            block.asItem().toString().contains("debris") ||
                            block.asItem().toString().contains("mythril")) {
                mining.add(new BlockExperience(block.asItem().toString(),
                        Map.of(
                                "mining", 9 + (block.getHardness() / 3))));
            }
        });
        
        
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        
        File dir = new File(FabricLoader.getInstance().getConfigDir() + "/rpmechanics/skills");
        File reqs = new File(FabricLoader.getInstance().getConfigDir() + "/rpmechanics/skills/mining.json");
        if (!reqs.exists() && !reqs.isDirectory()) {
            try {
                dir.mkdirs();
                mapper.writeValue(reqs, mining);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    
    public static void genFarmingConfig() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        List<BlockExperience> farming = new ArrayList<>();
        Map<String, Float> exp = new HashMap<>();
        exp.put("farming", 1.4F);
        
        Registries.BLOCK.forEach(block -> {
            if (block.asItem().toString().contains("beet") ||
                    block.asItem().toString().contains("wheat") ||
                    block.asItem().toString().contains("potato") ||
                    block.asItem().toString().contains("carrot") ||
                    block.asItem().toString().contains("cactus") ||
                    block.asItem().toString().contains("sugarcane") ||
                    block.asItem().toString().contains("stem") &&
                    !block.asItem().toString().contains("carved")) {
                farming.add(new BlockExperience(block.asItem().toString(), Map.of("farming", 5F)));
            } else if (block.asItem().toString().contains("pumpkin") || block.asItem().toString().contains("melon")) {
                farming.add(new BlockExperience(block.asItem().toString(), Map.of("farming", 8F)));
            }
        });
        
        farming.add(new BlockExperience("minecraft:chorus_fruit", exp));
        
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        
        File dir = new File(FabricLoader.getInstance().getConfigDir() + "/rpmechanics/skills");
        File reqs = new File(FabricLoader.getInstance().getConfigDir() + "/rpmechanics/skills/farming.json");
        if (!reqs.exists() && !reqs.isDirectory()) {
            try {
                dir.mkdirs();
                mapper.writeValue(reqs, farming);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    
    public static void genDefaultClasses() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Map<Integer, RoleplayClass> classes = new HashMap<>();
        
        classes.put(0, RoleplayClasses.PALADIN);
        classes.put(1, RoleplayClasses.RANGER);
        classes.put(2, RoleplayClasses.FIGHTER);
        
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        
        File dir = new File(FabricLoader.getInstance().getConfigDir() + "/rpmechanics/classes");
        File rpClasses = new File(FabricLoader.getInstance().getConfigDir() + "/rpmechanics/classes/classes.json");
        if (!rpClasses.exists() && !rpClasses.isDirectory()) {
            try {
                dir.mkdirs();
                mapper.writeValue(rpClasses, classes);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    
    public static void genPassiveMobs() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        List<String> mobs = new ArrayList<>();
        
        mobs.add("sheep");
        mobs.add("cow");
        mobs.add("chicken");
        mobs.add("mooshroom");
        mobs.add("squid");
        mobs.add("glow_squid");
        mobs.add("pig");
        mobs.add("rabbit");
        mobs.add("agent");
        mobs.add("allay");
        mobs.add("armadillo");
        mobs.add("axolotl");
        mobs.add("bat");
        mobs.add("cat");
        mobs.add("camel");
        mobs.add("cod");
        mobs.add("donkey");
        mobs.add("horse");
        mobs.add("frog");
        mobs.add("mule");
        mobs.add("ocelot");
        mobs.add("parrot");
        mobs.add("pufferfish");
        mobs.add("salmon");
        mobs.add("skeleton_horse");
        mobs.add("sniffer");
        mobs.add("strider");
        mobs.add("tadpole");
        mobs.add("tropical_fish");
        mobs.add("turtle");
        mobs.add("villager");
        mobs.add("wandering_trader");
        mobs.add("zombie_horse");
        mobs.add("dolphin");
        
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        
        File dir = new File(FabricLoader.getInstance().getConfigDir() + "/rpmechanics");
        File passiveMobs = new File(FabricLoader.getInstance().getConfigDir() + "/rpmechanics/passive_mobs.json");
        if (!passiveMobs.exists() && !passiveMobs.isDirectory()) {
            try {
                dir.mkdirs();
                mapper.writeValue(passiveMobs, mobs);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    
    public static void genCraftingConfig() {
        ObjectMapper mapper = new ObjectMapper();
        List<ItemCraftingRequirement> items = new ArrayList<>();
        
        Registries.ITEM.forEach(item -> {
            if (item.toString().contains("plank") ||
                    item.toString().contains("log")) {
                items.add(new ItemCraftingRequirement(item.toString(), 0, 0.8F));
            }
            if (item.toString().contains("door") ||
                    item.toString().contains("trapdoor")) {
                items.add(new ItemCraftingRequirement(item.toString(), 0, 14F));
            }
            if (item.toString().contains("emerald")
                    || item.toString().contains("diamond")
                    || item.toString().contains("iron")
                    || item.toString().contains("coal")
                    || item.toString().contains("lapis")
                    || item.toString().contains("redstone")
                    || item.toString().contains("gold")
                    && item.toString().contains("block")) {
                items.add(new ItemCraftingRequirement(item.toString(), 0, 1F));
            }
        });
        
        items.add(new ItemCraftingRequirement("minecraft:stick", 0, 0.4F));
        items.add(new ItemCraftingRequirement("minecraft:beacon", 0, 400F));
        items.add(new ItemCraftingRequirement("minecraft:chest", 0, 8F));
        items.add(new ItemCraftingRequirement("minecraft:anvil", 0, 15F));
        items.add(new ItemCraftingRequirement("minecraft:crafting_table", 0, 4F));
        items.add(new ItemCraftingRequirement("minecraft:smithing_table", 0, 10F));
        items.add(new ItemCraftingRequirement("minecraft:bow", 8, 28F));
        items.add(new ItemCraftingRequirement("minecraft:flint_and_steel", 10, 20F));
        items.add(new ItemCraftingRequirement("minecraft:netherite_ingot", 30, 30F));
        items.add(new ItemCraftingRequirement("rpmechanics:mythril_ingot", 45, 40F));
        
        items.add(new ItemCraftingRequirement("minecraft:wooden_sword", 0, 35F));
        items.add(new ItemCraftingRequirement("minecraft:wooden_pickaxe", 0, 35F));
        items.add(new ItemCraftingRequirement("minecraft:wooden_axe", 0, 35F));
        items.add(new ItemCraftingRequirement("minecraft:wooden_shovel", 0, 35F));
        items.add(new ItemCraftingRequirement("minecraft:wooden_hoe", 0, 35F));
        items.add(new ItemCraftingRequirement("minecraft:leather_helmet", 0, 35F));
        items.add(new ItemCraftingRequirement("minecraft:leather_chestplate", 0, 35F));
        items.add(new ItemCraftingRequirement("minecraft:leather_leggings", 0, 35F));
        items.add(new ItemCraftingRequirement("minecraft:leather_boots", 0, 35F));
        
        items.add(new ItemCraftingRequirement("minecraft:stone_sword", 3, 40F));
        items.add(new ItemCraftingRequirement("minecraft:stone_pickaxe", 3, 40F));
        items.add(new ItemCraftingRequirement("minecraft:stone_axe", 3, 40F));
        items.add(new ItemCraftingRequirement("minecraft:stone_shovel", 3, 40F));
        items.add(new ItemCraftingRequirement("minecraft:stone_hoe", 3, 40F));
        items.add(new ItemCraftingRequirement("minecraft:chainmail_helmet", 15, 40F));
        items.add(new ItemCraftingRequirement("minecraft:chainmail_chestplate", 15, 40F));
        items.add(new ItemCraftingRequirement("minecraft:chainmail_leggings", 15, 40F));
        items.add(new ItemCraftingRequirement("minecraft:chainmail_boots", 15, 40F));
        
        items.add(new ItemCraftingRequirement("minecraft:golden_sword", 10, 40F));
        items.add(new ItemCraftingRequirement("minecraft:golden_pickaxe", 10, 40F));
        items.add(new ItemCraftingRequirement("minecraft:golden_axe", 10, 40F));
        items.add(new ItemCraftingRequirement("minecraft:golden_shovel", 10, 40F));
        items.add(new ItemCraftingRequirement("minecraft:golden_hoe", 10, 40F));
        items.add(new ItemCraftingRequirement("minecraft:golden_helmet", 10, 40F));
        items.add(new ItemCraftingRequirement("minecraft:golden_chestplate", 10, 40F));
        items.add(new ItemCraftingRequirement("minecraft:golden_leggings", 10, 40F));
        items.add(new ItemCraftingRequirement("minecraft:golden_boots", 10, 40F));
        
        items.add(new ItemCraftingRequirement("minecraft:iron_sword", 8, 40F));
        items.add(new ItemCraftingRequirement("minecraft:iron_pickaxe", 8, 40F));
        items.add(new ItemCraftingRequirement("minecraft:iron_axe", 8, 40F));
        items.add(new ItemCraftingRequirement("minecraft:iron_shovel", 8, 40F));
        items.add(new ItemCraftingRequirement("minecraft:iron_hoe", 8, 40F));
        items.add(new ItemCraftingRequirement("minecraft:iron_helmet", 8, 40F));
        items.add(new ItemCraftingRequirement("minecraft:iron_chestplate", 8, 40F));
        items.add(new ItemCraftingRequirement("minecraft:iron_leggings", 8, 40F));
        items.add(new ItemCraftingRequirement("minecraft:iron_boots", 8, 40F));
        
        items.add(new ItemCraftingRequirement("minecraft:diamond_sword", 24, 50F));
        items.add(new ItemCraftingRequirement("minecraft:diamond_pickaxe", 24, 50F));
        items.add(new ItemCraftingRequirement("minecraft:diamond_axe", 24, 50F));
        items.add(new ItemCraftingRequirement("minecraft:diamond_shovel", 24, 50F));
        items.add(new ItemCraftingRequirement("minecraft:diamond_hoe", 24, 50F));
        items.add(new ItemCraftingRequirement("minecraft:diamond_helmet", 24, 50F));
        items.add(new ItemCraftingRequirement("minecraft:diamond_chestplate", 24, 50F));
        items.add(new ItemCraftingRequirement("minecraft:diamond_leggings", 24, 50F));
        items.add(new ItemCraftingRequirement("minecraft:diamond_boots", 24, 50F));
        
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        
        File dir = new File(FabricLoader.getInstance().getConfigDir() + "/rpmechanics");
        File dir2 = new File(FabricLoader.getInstance().getConfigDir() + "/rpmechanics/skills");
        File craftingReqs = new File(FabricLoader.getInstance().getConfigDir() + "/rpmechanics/skills/crafting.json");
        if (!craftingReqs.exists() && !craftingReqs.isDirectory()) {
            try {
                dir.mkdirs();
                mapper.writeValue(craftingReqs, items);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
