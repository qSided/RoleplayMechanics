package qsided.rpmechanics.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.Identifier;
import qsided.rpmechanics.RoleplayClasses;
import qsided.rpmechanics.config.experience_values.BlockExperience;
import qsided.rpmechanics.config.requirements.ItemCraftingRequirement;
import qsided.rpmechanics.config.requirements.ItemWithRequirements;
import qsided.rpmechanics.config.requirements.Requirements;
import qsided.rpmechanics.config.roleplay_classes.RoleplayClass;
import qsided.rpmechanics.skills.milestones.SkillMilestone;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class ConfigGenerator {

    public static void genReqsConfig() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        File reqs = new File(FabricLoader.getInstance().getConfigDir() + "/rpmechanics/item_use_reqs.json");
        File dir = reqs.getParentFile();

        // Use map keyed by item id to avoid duplicates and preserve user overrides
        Map<String, ItemWithRequirements> itemMap = new LinkedHashMap<>();

        // 1) Load existing config if it exists
        if (reqs.exists() && !reqs.isDirectory()) {
            try {
                ItemWithRequirements[] existing = mapper.readValue(reqs, ItemWithRequirements[].class);
                for (ItemWithRequirements entry : existing) {
                    if (entry != null && entry.getItemId() != null) {
                        itemMap.put(entry.getItemId(), entry);
                    }
                }
            } catch (Exception e) {
                // if parsing fails, log and fall back to re-generating defaults
                System.err.println("[RPMechanics] Failed to parse item_use_reqs.json, regenerating defaults");
                e.printStackTrace();
                itemMap.clear();
            }
        } else {
            // 2) No file yet → seed with your manual defaults
            // Wooden tier
            itemMap.put("minecraft:wooden_sword", new ItemWithRequirements("minecraft:wooden_sword", new Requirements("swords", -1, 1)));
            itemMap.put("minecraft:wooden_pickaxe", new ItemWithRequirements("minecraft:wooden_pickaxe", new Requirements("mining", -1, 1)));
            itemMap.put("minecraft:wooden_axe", new ItemWithRequirements("minecraft:wooden_axe", new Requirements("woodcutting", -1, 1)));
            itemMap.put("minecraft:wooden_shovel", new ItemWithRequirements("minecraft:wooden_shovel", new Requirements("mining", -1, 1)));
            itemMap.put("minecraft:wooden_hoe", new ItemWithRequirements("minecraft:wooden_hoe", new Requirements("farming", -1, 1)));
            itemMap.put("minecraft:leather_helmet", new ItemWithRequirements("minecraft:leather_helmet", new Requirements("endurance", -1, 1)));
            itemMap.put("minecraft:leather_chestplate", new ItemWithRequirements("minecraft:leather_chestplate", new Requirements("endurance", -1, 1)));
            itemMap.put("minecraft:leather_leggings", new ItemWithRequirements("minecraft:leather_leggings", new Requirements("endurance", -1, 1)));
            itemMap.put("minecraft:leather_boots", new ItemWithRequirements("minecraft:leather_boots", new Requirements("endurance", -1, 1)));

            // Stone + chainmail
            itemMap.put("minecraft:stone_sword", new ItemWithRequirements("minecraft:stone_sword", new Requirements("swords", -1, 4)));
            itemMap.put("minecraft:stone_pickaxe", new ItemWithRequirements("minecraft:stone_pickaxe", new Requirements("mining", -1, 4)));
            itemMap.put("minecraft:stone_axe", new ItemWithRequirements("minecraft:stone_axe", new Requirements("woodcutting", -1, 4)));
            itemMap.put("minecraft:stone_shovel", new ItemWithRequirements("minecraft:stone_shovel", new Requirements("mining", -1, 4)));
            itemMap.put("minecraft:stone_hoe", new ItemWithRequirements("minecraft:stone_hoe", new Requirements("farming", -1, 4)));
            itemMap.put("minecraft:chainmail_helmet", new ItemWithRequirements("minecraft:chainmail_helmet", new Requirements("endurance", -1, 10)));
            itemMap.put("minecraft:chainmail_chestplate", new ItemWithRequirements("minecraft:chainmail_chestplate", new Requirements("endurance", -1, 10)));
            itemMap.put("minecraft:chainmail_leggings", new ItemWithRequirements("minecraft:chainmail_leggings", new Requirements("endurance", -1, 10)));
            itemMap.put("minecraft:chainmail_boots", new ItemWithRequirements("minecraft:chainmail_boots", new Requirements("endurance", -1, 10)));

            // Iron
            itemMap.put("minecraft:iron_sword", new ItemWithRequirements("minecraft:iron_sword", new Requirements("swords", -1, 12)));
            itemMap.put("minecraft:iron_pickaxe", new ItemWithRequirements("minecraft:iron_pickaxe", new Requirements("mining", -1, 12)));
            itemMap.put("minecraft:iron_axe", new ItemWithRequirements("minecraft:iron_axe", new Requirements("woodcutting", -1, 12)));
            itemMap.put("minecraft:iron_shovel", new ItemWithRequirements("minecraft:iron_shovel", new Requirements("mining", -1, 12)));
            itemMap.put("minecraft:iron_hoe", new ItemWithRequirements("minecraft:iron_hoe", new Requirements("farming", -1, 12)));
            itemMap.put("minecraft:iron_helmet", new ItemWithRequirements("minecraft:iron_helmet", new Requirements("endurance", -1, 10)));
            itemMap.put("minecraft:iron_chestplate", new ItemWithRequirements("minecraft:iron_chestplate", new Requirements("endurance", -1, 10)));
            itemMap.put("minecraft:iron_leggings", new ItemWithRequirements("minecraft:iron_leggings", new Requirements("endurance", -1, 10)));
            itemMap.put("minecraft:iron_boots", new ItemWithRequirements("minecraft:iron_boots", new Requirements("endurance", -1, 10)));

            // Gold
            itemMap.put("minecraft:golden_sword", new ItemWithRequirements("minecraft:golden_sword", new Requirements("swords", -1, 15)));
            itemMap.put("minecraft:golden_pickaxe", new ItemWithRequirements("minecraft:golden_pickaxe", new Requirements("mining", -1, 15)));
            itemMap.put("minecraft:golden_axe", new ItemWithRequirements("minecraft:golden_axe", new Requirements("woodcutting", -1, 15)));
            itemMap.put("minecraft:golden_shovel", new ItemWithRequirements("minecraft:golden_shovel", new Requirements("mining", -1, 15)));
            itemMap.put("minecraft:golden_hoe", new ItemWithRequirements("minecraft:golden_hoe", new Requirements("farming", -1, 15)));
            itemMap.put("minecraft:golden_helmet", new ItemWithRequirements("minecraft:golden_helmet", new Requirements("endurance", -1, 15)));
            itemMap.put("minecraft:golden_chestplate", new ItemWithRequirements("minecraft:golden_chestplate", new Requirements("endurance", -1, 15)));
            itemMap.put("minecraft:golden_leggings", new ItemWithRequirements("minecraft:golden_leggings", new Requirements("endurance", -1, 15)));
            itemMap.put("minecraft:golden_boots", new ItemWithRequirements("minecraft:golden_boots", new Requirements("endurance", -1, 15)));

            // Diamond
            itemMap.put("minecraft:diamond_sword", new ItemWithRequirements("minecraft:diamond_sword", new Requirements("swords", -1, 25)));
            itemMap.put("minecraft:diamond_pickaxe", new ItemWithRequirements("minecraft:diamond_pickaxe", new Requirements("mining", -1, 25)));
            itemMap.put("minecraft:diamond_axe", new ItemWithRequirements("minecraft:diamond_axe", new Requirements("woodcutting", -1, 25)));
            itemMap.put("minecraft:diamond_shovel", new ItemWithRequirements("minecraft:diamond_shovel", new Requirements("mining", -1, 25)));
            itemMap.put("minecraft:diamond_hoe", new ItemWithRequirements("minecraft:diamond_hoe", new Requirements("farming", -1, 25)));
            itemMap.put("minecraft:diamond_helmet", new ItemWithRequirements("minecraft:diamond_helmet", new Requirements("endurance", -1, 20)));
            itemMap.put("minecraft:diamond_chestplate", new ItemWithRequirements("minecraft:diamond_chestplate", new Requirements("endurance", -1, 20)));
            itemMap.put("minecraft:diamond_leggings", new ItemWithRequirements("minecraft:diamond_leggings", new Requirements("endurance", -1, 20)));
            itemMap.put("minecraft:diamond_boots", new ItemWithRequirements("minecraft:diamond_boots", new Requirements("endurance", -1, 20)));

            // Netherite
            itemMap.put("minecraft:netherite_sword", new ItemWithRequirements("minecraft:netherite_sword", new Requirements("swords", -1, 40)));
            itemMap.put("minecraft:netherite_pickaxe", new ItemWithRequirements("minecraft:netherite_pickaxe", new Requirements("mining", -1, 40)));
            itemMap.put("minecraft:netherite_axe", new ItemWithRequirements("minecraft:netherite_axe", new Requirements("woodcutting", -1, 40)));
            itemMap.put("minecraft:netherite_shovel", new ItemWithRequirements("minecraft:netherite_shovel", new Requirements("mining", -1, 40)));
            itemMap.put("minecraft:netherite_hoe", new ItemWithRequirements("minecraft:netherite_hoe", new Requirements("farming", -1, 40)));
            itemMap.put("minecraft:netherite_helmet", new ItemWithRequirements("minecraft:netherite_helmet", new Requirements("endurance", -1, 30)));
            itemMap.put("minecraft:netherite_chestplate", new ItemWithRequirements("minecraft:netherite_chestplate", new Requirements("endurance", -1, 30)));
            itemMap.put("minecraft:netherite_leggings", new ItemWithRequirements("minecraft:netherite_leggings", new Requirements("endurance", -1, 30)));
            itemMap.put("minecraft:netherite_boots", new ItemWithRequirements("minecraft:netherite_boots", new Requirements("endurance", -1, 30)));

            // Mythril
            itemMap.put("rpmechanics:mythril_sword", new ItemWithRequirements("rpmechanics:mythril_sword", new Requirements("swords", -1, 50)));
            itemMap.put("rpmechanics:mythril_pickaxe", new ItemWithRequirements("rpmechanics:mythril_pickaxe", new Requirements("mining", -1, 50)));
            itemMap.put("rpmechanics:mythril_axe", new ItemWithRequirements("rpmechanics:mythril_axe", new Requirements("woodcutting", -1, 50)));
            itemMap.put("rpmechanics:mythril_shovel", new ItemWithRequirements("rpmechanics:mythril_shovel", new Requirements("mining", -1, 50)));
            itemMap.put("rpmechanics:mythril_hoe", new ItemWithRequirements("rpmechanics:mythril_hoe", new Requirements("farming", -1, 50)));
            itemMap.put("rpmechanics:mythril_helmet", new ItemWithRequirements("rpmechanics:mythril_helmet", new Requirements("endurance", -1, 40)));
            itemMap.put("rpmechanics:mythril_chestplate", new ItemWithRequirements("rpmechanics:mythril_chestplate", new Requirements("endurance", -1, 40)));
            itemMap.put("rpmechanics:mythril_leggings", new ItemWithRequirements("rpmechanics:mythril_leggings", new Requirements("endurance", -1, 40)));
            itemMap.put("rpmechanics:mythril_boots", new ItemWithRequirements("rpmechanics:mythril_boots", new Requirements("endurance", -1, 40)));
        }

        // 3) Auto-generate new entries for other mods’ gear
        Registries.ITEM.forEach(item -> {
            Identifier id = Registries.ITEM.getId(item);
            if (id != null && !"minecraft".equals(id.getNamespace()) && !"rpmechanics".equals(id.getNamespace())) {
                System.out.println("[RPMechanics] Saw modded item: " + id);
            }
            if (id == null) return;

            String itemId = id.toString();

            // Skip vanilla + your own
            if ("minecraft".equals(id.getNamespace()) || "rpmechanics".equals(id.getNamespace())) return;

            // Don’t touch items already in config
            if (itemMap.containsKey(itemId)) return;

            Requirements req = inferRequirementsFromItem(item);
            if (req != null) {
                itemMap.put(itemId, new ItemWithRequirements(itemId, req));
            }
        });

        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        if (!dir.exists()) {
            //noinspection ResultOfMethodCallIgnored
            dir.mkdirs();
        }

        // 4) Always write merged result back
        mapper.writeValue(reqs, new ArrayList<>(itemMap.values()));
    }

    public static void genWoodcuttingConfig() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        List<BlockExperience> woodcutting = new ArrayList<>();

        Registries.BLOCK.forEach(block -> {
            String idStr = block.asItem().toString();
            if (idStr.contains("log") ||
                    idStr.contains("wood") ||
                    idStr.contains("roots") ||
                    idStr.contains("bamboo")) {
                woodcutting.add(new BlockExperience(idStr,
                        Map.of("woodcutting", 20F)));
            }
        });

        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        File dir = new File(FabricLoader.getInstance().getConfigDir() + "/rpmechanics/skills");
        File reqs = new File(FabricLoader.getInstance().getConfigDir() + "/rpmechanics/skills/woodcutting_xp_values.json");
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
            Identifier id = Registries.BLOCK.getId(block);
            if (id == null) return;

            String blockId = id.toString();
            BlockState state = block.getDefaultState();
            float hardness = block.getHardness();

            int toolTier = getRequiredToolTier(state);
            if (toolTier == 0) {
                // not really a mining target, skip
                return;
            }

            boolean ore = isOre(state)
                    || blockId.contains("ore")
                    || blockId.contains("debris")
                    || blockId.contains("mythril");

            // Base XP by required tool tier
            float baseXp;
            switch (toolTier) {
                case 4 -> baseXp = ore ? 20F : 12F;  // diamond-level stuff
                case 3 -> baseXp = ore ? 16F : 10F;  // iron-level
                case 2 -> baseXp = ore ? 12F : 8F;   // stone-level
                case 1 -> baseXp = ore ? 8F : 5F;   // wood-level
                default -> baseXp = 5F;
            }

            float xp = baseXp + (hardness / 3F);

            mining.add(new BlockExperience(
                    blockId,
                    Map.of("mining", xp)
            ));
        });

        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        File dir = new File(FabricLoader.getInstance().getConfigDir() + "/rpmechanics/skills");
        File reqs = new File(FabricLoader.getInstance().getConfigDir() + "/rpmechanics/skills/mining_xp_values.json");
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
            String idStr = block.asItem().toString();
            if (idStr.contains("beet") ||
                    idStr.contains("wheat") ||
                    idStr.contains("potato") ||
                    idStr.contains("carrot") ||
                    idStr.contains("cactus") ||
                    idStr.contains("sugarcane") ||
                    (idStr.contains("mushroom") && !idStr.contains("carved"))) {
                farming.add(new BlockExperience(idStr, Map.of("farming", 5F)));
            } else if (idStr.contains("pumpkin") || idStr.contains("melon")) {
                farming.add(new BlockExperience(idStr, Map.of("farming", 8F)));
            }
        });

        farming.add(new BlockExperience("minecraft:chorus_fruit", exp));

        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        File dir = new File(FabricLoader.getInstance().getConfigDir() + "/rpmechanics/skills");
        File reqs = new File(FabricLoader.getInstance().getConfigDir() + "/rpmechanics/skills/farming_xp_values.json");
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
        classes.put(3, RoleplayClasses.ARTISAN);
        classes.put(4, RoleplayClasses.FARMER);

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

        // LinkedHashSet keeps insertion order and guarantees uniqueness
        Set<String> mobs = new LinkedHashSet<>();

        // Vanilla baseline
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

        // Add modded passives from registry
        Registries.ENTITY_TYPE.forEach(type -> {
            Identifier id = Registries.ENTITY_TYPE.getId(type);
            if (id == null) return;

            SpawnGroup group = type.getSpawnGroup();

            // Heuristic: treat non-monsters / non-misc as passive-ish
            if (group == SpawnGroup.MONSTER || group == SpawnGroup.MISC) {
                return;
            }

            // You currently store just the path (e.g. "sheep"), so keep that style
            mobs.add(id.getPath());
        });

        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        File dir = new File(FabricLoader.getInstance().getConfigDir() + "/rpmechanics");
        File passiveMobs = new File(FabricLoader.getInstance().getConfigDir() + "/rpmechanics/passive_mobs.json");
        if (!passiveMobs.exists() && !passiveMobs.isDirectory()) {
            try {
                dir.mkdirs();
                mapper.writeValue(passiveMobs, new ArrayList<>(mobs));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void genCraftingConfig() {
        ObjectMapper mapper = new ObjectMapper();

        // Key by itemId to avoid duplicates and allow overrides
        Map<String, ItemCraftingRequirement> craftingMap = new LinkedHashMap<>();

        // --- Heuristics from registry for generic crafting XP ---
        Registries.ITEM.forEach(item -> {
            Identifier id = Registries.ITEM.getId(item);
            if (id == null) return;

            String itemId = id.toString();

            if (itemId.contains("plank") || itemId.contains("log")) {
                craftingMap.putIfAbsent(itemId, new ItemCraftingRequirement(itemId, 0, 0.8F));
            }
            if (itemId.contains("door") || itemId.contains("trapdoor")) {
                craftingMap.putIfAbsent(itemId, new ItemCraftingRequirement(itemId, 0, 14F));
            }
            if ((itemId.contains("emerald")
                    || itemId.contains("diamond")
                    || itemId.contains("iron")
                    || itemId.contains("coal")
                    || itemId.contains("lapis")
                    || itemId.contains("redstone")
                    || itemId.contains("gold"))
                    && itemId.contains("block")) {
                craftingMap.putIfAbsent(itemId, new ItemCraftingRequirement(itemId, 0, 1F));
            }
        });

        // --- Hand-tuned entries (these override heuristics) ---
        craftingMap.put("minecraft:stick", new ItemCraftingRequirement("minecraft:stick", 0, 0.4F));
        craftingMap.put("minecraft:beacon", new ItemCraftingRequirement("minecraft:beacon", 0, 400F));
        craftingMap.put("minecraft:chest", new ItemCraftingRequirement("minecraft:chest", 0, 8F));
        craftingMap.put("minecraft:anvil", new ItemCraftingRequirement("minecraft:anvil", 0, 15F));
        craftingMap.put("minecraft:crafting_table", new ItemCraftingRequirement("minecraft:crafting_table", 0, 4F));
        craftingMap.put("minecraft:smithing_table", new ItemCraftingRequirement("minecraft:smithing_table", 0, 10F));
        craftingMap.put("minecraft:bow", new ItemCraftingRequirement("minecraft:bow", 8, 28F));
        craftingMap.put("minecraft:flint_and_steel", new ItemCraftingRequirement("minecraft:flint_and_steel", 10, 20F));
        craftingMap.put("minecraft:netherite_ingot", new ItemCraftingRequirement("minecraft:netherite_ingot", 30, 30F));
        craftingMap.put("rpmechanics:mythril_ingot", new ItemCraftingRequirement("rpmechanics:mythril_ingot", 45, 40F));

        // Wooden gear
        craftingMap.put("minecraft:wooden_sword", new ItemCraftingRequirement("minecraft:wooden_sword", 0, 35F));
        craftingMap.put("minecraft:wooden_pickaxe", new ItemCraftingRequirement("minecraft:wooden_pickaxe", 0, 35F));
        craftingMap.put("minecraft:wooden_axe", new ItemCraftingRequirement("minecraft:wooden_axe", 0, 35F));
        craftingMap.put("minecraft:wooden_shovel", new ItemCraftingRequirement("minecraft:wooden_shovel", 0, 35F));
        craftingMap.put("minecraft:wooden_hoe", new ItemCraftingRequirement("minecraft:wooden_hoe", 0, 35F));
        craftingMap.put("minecraft:leather_helmet", new ItemCraftingRequirement("minecraft:leather_helmet", 0, 35F));
        craftingMap.put("minecraft:leather_chestplate", new ItemCraftingRequirement("minecraft:leather_chestplate", 0, 35F));
        craftingMap.put("minecraft:leather_leggings", new ItemCraftingRequirement("minecraft:leather_leggings", 0, 35F));
        craftingMap.put("minecraft:leather_boots", new ItemCraftingRequirement("minecraft:leather_boots", 0, 35F));

        // Stone + chainmail
        craftingMap.put("minecraft:stone_sword", new ItemCraftingRequirement("minecraft:stone_sword", 3, 40F));
        craftingMap.put("minecraft:stone_pickaxe", new ItemCraftingRequirement("minecraft:stone_pickaxe", 3, 40F));
        craftingMap.put("minecraft:stone_axe", new ItemCraftingRequirement("minecraft:stone_axe", 3, 40F));
        craftingMap.put("minecraft:stone_shovel", new ItemCraftingRequirement("minecraft:stone_shovel", 3, 40F));
        craftingMap.put("minecraft:stone_hoe", new ItemCraftingRequirement("minecraft:stone_hoe", 3, 40F));
        craftingMap.put("minecraft:chainmail_helmet", new ItemCraftingRequirement("minecraft:chainmail_helmet", 15, 40F));
        craftingMap.put("minecraft:chainmail_chestplate", new ItemCraftingRequirement("minecraft:chainmail_chestplate", 15, 40F));
        craftingMap.put("minecraft:chainmail_leggings", new ItemCraftingRequirement("minecraft:chainmail_leggings", 15, 40F));
        craftingMap.put("minecraft:chainmail_boots", new ItemCraftingRequirement("minecraft:chainmail_boots", 15, 40F));

        // Gold gear
        craftingMap.put("minecraft:golden_sword", new ItemCraftingRequirement("minecraft:golden_sword", 10, 40F));
        craftingMap.put("minecraft:golden_pickaxe", new ItemCraftingRequirement("minecraft:golden_pickaxe", 10, 40F));
        craftingMap.put("minecraft:golden_axe", new ItemCraftingRequirement("minecraft:golden_axe", 10, 40F));
        craftingMap.put("minecraft:golden_shovel", new ItemCraftingRequirement("minecraft:golden_shovel", 10, 40F));
        craftingMap.put("minecraft:golden_hoe", new ItemCraftingRequirement("minecraft:golden_hoe", 10, 40F));
        craftingMap.put("minecraft:golden_helmet", new ItemCraftingRequirement("minecraft:golden_helmet", 10, 40F));
        craftingMap.put("minecraft:golden_chestplate", new ItemCraftingRequirement("minecraft:golden_chestplate", 10, 40F));
        craftingMap.put("minecraft:golden_leggings", new ItemCraftingRequirement("minecraft:golden_leggings", 10, 40F));
        craftingMap.put("minecraft:golden_boots", new ItemCraftingRequirement("minecraft:golden_boots", 10, 40F));

        // Iron gear
        craftingMap.put("minecraft:iron_sword", new ItemCraftingRequirement("minecraft:iron_sword", 8, 40F));
        craftingMap.put("minecraft:iron_pickaxe", new ItemCraftingRequirement("minecraft:iron_pickaxe", 8, 40F));
        craftingMap.put("minecraft:iron_axe", new ItemCraftingRequirement("minecraft:iron_axe", 8, 40F));
        craftingMap.put("minecraft:iron_shovel", new ItemCraftingRequirement("minecraft:iron_shovel", 8, 40F));
        craftingMap.put("minecraft:iron_hoe", new ItemCraftingRequirement("minecraft:iron_hoe", 8, 40F));
        craftingMap.put("minecraft:iron_helmet", new ItemCraftingRequirement("minecraft:iron_helmet", 8, 40F));
        craftingMap.put("minecraft:iron_chestplate", new ItemCraftingRequirement("minecraft:iron_chestplate", 8, 40F));
        craftingMap.put("minecraft:iron_leggings", new ItemCraftingRequirement("minecraft:iron_leggings", 8, 40F));
        craftingMap.put("minecraft:iron_boots", new ItemCraftingRequirement("minecraft:iron_boots", 8, 40F));

        // Diamond gear
        craftingMap.put("minecraft:diamond_sword", new ItemCraftingRequirement("minecraft:diamond_sword", 24, 50F));
        craftingMap.put("minecraft:diamond_pickaxe", new ItemCraftingRequirement("minecraft:diamond_pickaxe", 24, 50F));
        craftingMap.put("minecraft:diamond_axe", new ItemCraftingRequirement("minecraft:diamond_axe", 24, 50F));
        craftingMap.put("minecraft:diamond_shovel", new ItemCraftingRequirement("minecraft:diamond_shovel", 24, 50F));
        craftingMap.put("minecraft:diamond_hoe", new ItemCraftingRequirement("minecraft:diamond_hoe", 24, 50F));
        craftingMap.put("minecraft:diamond_helmet", new ItemCraftingRequirement("minecraft:diamond_helmet", 24, 50F));
        craftingMap.put("minecraft:diamond_chestplate", new ItemCraftingRequirement("minecraft:diamond_chestplate", 24, 50F));
        craftingMap.put("minecraft:diamond_leggings", new ItemCraftingRequirement("minecraft:diamond_leggings", 24, 50F));
        craftingMap.put("minecraft:diamond_boots", new ItemCraftingRequirement("minecraft:diamond_boots", 24, 50F));

        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        // --- Auto-generate crafting reqs for other mods’ tools/weapons/armor ---
        Registries.ITEM.forEach(item -> {
            Identifier id = Registries.ITEM.getId(item);
            if (id == null) return;

            String itemId = id.toString();

            // skip vanilla + your own if you only want modded
            if ("minecraft".equals(id.getNamespace()) || "rpmechanics".equals(id.getNamespace())) return;

            // only gear
            if (!(item instanceof SwordItem || item instanceof AxeItem ||
                    item instanceof PickaxeItem || item instanceof ShovelItem ||
                    item instanceof HoeItem || item instanceof ArmorItem ||
                    item instanceof BowItem || item instanceof CrossbowItem ||
                    item instanceof ShieldItem || item instanceof TridentItem)) {
                return;
            }

            Requirements req = inferRequirementsFromItem(item);
            if (req == null) return;

            int levelReq = req.getSkillLevel();
            float expWorth = levelReq * 2.0F; // tweak to taste

            // Don’t override manual tuning
            craftingMap.putIfAbsent(itemId, new ItemCraftingRequirement(itemId, levelReq, expWorth));
        });

        File skillsDir = new File(FabricLoader.getInstance().getConfigDir() + "/rpmechanics/skills");
        File craftingReqs = new File(FabricLoader.getInstance().getConfigDir() + "/rpmechanics/skills/crafting_level_reqs.json");
        if (!craftingReqs.exists() && !craftingReqs.isDirectory()) {
            try {
                skillsDir.mkdirs();
                mapper.writeValue(craftingReqs, new ArrayList<>(craftingMap.values()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void genDefaultMilestones() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);

        List<SkillMilestone> milestones = new ArrayList<>();

        milestones.add(new SkillMilestone("-1 Enchant Cost", "enchanting", SkillMilestone.Type.ENCHANT_COST_REDUCTION.toString(), 1, 20));
        milestones.add(new SkillMilestone("+3 Enchant Power", "enchanting", SkillMilestone.Type.ENCHANT_POWER.toString(), 3, 33));
        milestones.add(new SkillMilestone("-1 Enchant Cost", "enchanting", SkillMilestone.Type.ENCHANT_COST_REDUCTION.toString(), 1, 40));
        milestones.add(new SkillMilestone("-1 Enchant Cost", "enchanting", SkillMilestone.Type.ENCHANT_COST_REDUCTION.toString(), 1, 60));
        milestones.add(new SkillMilestone("+3 Enchant Power", "enchanting", SkillMilestone.Type.ENCHANT_POWER.toString(), 3, 66));
        milestones.add(new SkillMilestone("-1 Enchant Cost", "enchanting", SkillMilestone.Type.ENCHANT_COST_REDUCTION.toString(), 1, 80));
        milestones.add(new SkillMilestone("+3 Enchant Power", "enchanting", SkillMilestone.Type.ENCHANT_POWER.toString(), 3, 99));
        milestones.add(new SkillMilestone("-1 Enchant Cost", "enchanting", SkillMilestone.Type.ENCHANT_COST_REDUCTION.toString(), 1, 100));

        File dir = new File(FabricLoader.getInstance().getConfigDir() + "/rpmechanics");
        File milestonesFile = new File(FabricLoader.getInstance().getConfigDir() + "/rpmechanics/milestones.json");

        if (!milestonesFile.exists() && !milestonesFile.isDirectory()) {
            try {
                dir.mkdirs();
                mapper.writeValue(milestonesFile, milestones);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    // Helper Methods

    /**
     * Read base attack damage of an item from its attribute modifiers.
     * Works for vanilla + modded weapons that use the standard attribute.
     */
    private static double getBaseAttackDamage(Item item) {
        ItemStack stack = new ItemStack(item);
        final double[] max = {0.0};

        stack.applyAttributeModifiers(EquipmentSlot.MAINHAND,
                (RegistryEntry<EntityAttribute> attr, EntityAttributeModifier mod) -> {
                    if (attr.value() == EntityAttributes.GENERIC_ATTACK_DAMAGE) {
                        // just take the biggest modifier we see
                        double val = mod.value();
                        if (val > max[0]) max[0] = val;
                    }
                });

        return max[0];
    }

    // 0 = no pick needed / not really a mining block
    // 1 = wood
    // 2 = stone
    // 3 = iron
    // 4 = diamond / netherite
    private static int getRequiredToolTier(BlockState state) {
        if (state.isIn(BlockTags.NEEDS_DIAMOND_TOOL)) return 4;
        if (state.isIn(BlockTags.NEEDS_IRON_TOOL)) return 3;
        if (state.isIn(BlockTags.NEEDS_STONE_TOOL)) return 2;

        // Mineable with a pickaxe but no “needs_*_tool” tag → wood / stone-ish
        if (state.isIn(BlockTags.PICKAXE_MINEABLE)) return 1;

        return 0;
    }

    private static int clampLevel(int value) {
        if (value < 1) return 1;
        if (value > 100) return 100;
        return value;
    }

    /**
     * Generic scaling based on durability + enchantability of a fresh stack.
     * Works for armor, tools, weapons, etc. without poking at ToolMaterial.
     */
    private static int scaleLevelFromStack(Item item, int base) {
        ItemStack stack = new ItemStack(item);

        int durability = stack.getMaxDamage();   // 0 if not damageable
        boolean enchFlag = stack.isEnchantable();  // true for most gear

        int level = base
                + durability / 128               // more durable -> higher
                + (enchFlag ? 5 : 0);            // enchantable items get a bump

        return clampLevel(level);
    }

    private static Requirements inferRequirementsFromItem(Item item) {
        // Armor: scale with protection + durability
        if (item instanceof ArmorItem armor) {
            int protection = armor.getProtection();
            int base = 3 + protection * 2; // iron/diamond/etc naturally higher
            int level = scaleLevelFromStack(item, base);
            return new Requirements("endurance", -1, level);
        }

        // Swords
        if (item instanceof SwordItem) {
            double dmg = getBaseAttackDamage(item);   // e.g. 7.0 for strong swords
            int base = 6 + (int) Math.round(dmg * 2.0); // damage heavily influences level
            int level = scaleLevelFromStack(item, base);
            return new Requirements("swords", -1, level);
        }

        // Axes – woodcutting
        if (item instanceof AxeItem) {
            int level = scaleLevelFromStack(item, 10);
            return new Requirements("woodcutting", -1, level);
        }

        // Pickaxes – mining
        if (item instanceof PickaxeItem) {
            int level = scaleLevelFromStack(item, 8);
            return new Requirements("mining", -1, level);
        }

        // Shovels – mining, slightly lower
        if (item instanceof ShovelItem) {
            int level = scaleLevelFromStack(item, 6);
            return new Requirements("mining", -1, level);
        }

        // Hoes – farming
        if (item instanceof HoeItem) {
            int level = scaleLevelFromStack(item, 6);
            return new Requirements("farming", -1, level);
        }

        // Bows / crossbows – ranged
        if (item instanceof BowItem || item instanceof CrossbowItem) {
            int level = scaleLevelFromStack(item, 10);
            return new Requirements("ranged", -1, level);
        }

        // Shields – endurance
        if (item instanceof ShieldItem) {
            int level = scaleLevelFromStack(item, 8);
            return new Requirements("endurance", -1, level);
        }

        // Tridents – strong melee
        if (item instanceof TridentItem) {
            int level = scaleLevelFromStack(item, 15);
            return new Requirements("swords", -1, level);
        }

        // Everything else: no automatic requirement
        return null;
    }

    private static boolean isOre(BlockState state) {
        return state.isIn(BlockTags.GOLD_ORES)
                || state.isIn(BlockTags.IRON_ORES)
                || state.isIn(BlockTags.DIAMOND_ORES)
                || state.isIn(BlockTags.REDSTONE_ORES)
                || state.isIn(BlockTags.LAPIS_ORES)
                || state.isIn(BlockTags.COAL_ORES)
                || state.isIn(BlockTags.EMERALD_ORES)
                || state.isIn(BlockTags.COPPER_ORES);
    }
}
