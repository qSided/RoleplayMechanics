package qsided.rpmechanics.gui.blocks;

import com.google.common.collect.Lists;
import io.wispforest.owo.ui.base.BaseOwoHandledScreen;
import io.wispforest.owo.ui.component.ButtonComponent;
import io.wispforest.owo.ui.component.Components;
import io.wispforest.owo.ui.container.Containers;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.container.ScrollContainer;
import io.wispforest.owo.ui.core.*;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.random.Random;
import org.jetbrains.annotations.NotNull;
import qsided.rpmechanics.RoleplayMechanicsCommon;
import qsided.rpmechanics.gui.EnchantButtonComponent;
import qsided.rpmechanics.gui.EnchantScrollContainer;
import qsided.rpmechanics.gui.SkillEnabledEnchantingTableScreenHandler;
import qsided.rpmechanics.items.QuesItems;

import java.util.List;
import java.util.Optional;

public class SkillEnabledEnchantingTableHandledScreen extends BaseOwoHandledScreen<FlowLayout, SkillEnabledEnchantingTableScreenHandler>{
    private static final Identifier ENCHANTMENT_SLOT_DISABLED_TEXTURE = Identifier.of(RoleplayMechanicsCommon.MOD_ID, "textures/gui/container/enchanting_table/disabled.png");
    private static final Identifier TEXTURE = Identifier.ofVanilla("textures/gui/container/enchanting_table.png");
    private final Random random = Random.create();
    private final String[] phrases = new String[]{"the", "elder", "scrolls", "klaatu", "berata", "niktu", "xyzzy", "bless", "curse", "light", "darkness", "fire", "air", "earth", "water", "hot", "dry", "cold", "wet", "ignite", "snuff", "embiggen", "twist", "shorten", "stretch", "fiddle", "destroy", "imbue", "galvanize", "enchant", "free", "limited", "range", "of", "towards", "inside", "sphere", "cube", "self", "other", "ball", "mental", "physical", "grow", "shrink", "demon", "elemental", "spirit", "animal", "creature", "beast", "humanoid", "undead", "fresh", "stale", "phnglui", "mglwnafh", "cthulhu", "rlyeh", "wgahnagl", "fhtagn", "baguette"};
    public int ticks;
    public ItemStack currentItem = ItemStack.EMPTY;
    public ItemStack prevItem = ItemStack.EMPTY;
    public int currentLapisCount = 0;
    public int prevLapisCount = 0;
    public static Integer enchantingLevel;
    
    public SkillEnabledEnchantingTableHandledScreen(SkillEnabledEnchantingTableScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }
    
    @Override
    public @NotNull OwoUIAdapter<FlowLayout> createAdapter() {
        return OwoUIAdapter.create(this, Containers::verticalFlow);
    }
    
    @Override
    protected void handledScreenTick() {
        super.handledScreenTick();
        ticks++;
        currentItem = handler.getSlot(0).getStack();
        currentLapisCount = handler.getLapisCount();
        if (ticks >= 5) {
            ticks = 0;
            if (currentItem != prevItem || currentLapisCount != prevLapisCount) {
                uiAdapter.rootComponent.clearChildren();
                build(uiAdapter.rootComponent);
                prevItem = handler.getSlot(0).getStack();
                prevLapisCount = handler.getLapisCount();
            }
        }
    }
    
    public void setSeed(long seed) {
        this.random.setSeed(seed);
    }
    
    public MutableText generatePhrase() {
        StringBuilder stringBuilder = new StringBuilder();
        int i = this.random.nextInt(2) + 3;
        
        for(int j = 0; j < i; ++j) {
            if (j != 0) {
                stringBuilder.append(" ");
            }
            
            stringBuilder.append(Util.getRandom(this.phrases, this.random));
        }
        
        return Text.literal(textRenderer.getTextHandler().trimToWidth(Text.literal(stringBuilder.toString()), 70, Style.EMPTY).getString()).fillStyle(Style.EMPTY.withFont(Identifier.ofVanilla("alt")));
    }
    
    @Override
    public void build(FlowLayout root) {
        
        root.surface(Surface.VANILLA_TRANSLUCENT);
        
        root.child(
                Containers.verticalFlow(Sizing.fixed(177), Sizing.fixed(167))
                        .child(
                                Components.texture(TEXTURE,
                                        0,
                                        0,
                                        176,
                                        166)
                        )
                        .child(
                                Components.label(Text.translatable("container.inventory"))
                                        .color(Color.ofFormatting(Formatting.DARK_GRAY))
                                        .positioning(Positioning.absolute(7, 72))
                        )
                        .child(
                                Components.label(Text.translatable("container.enchant"))
                                        .color(Color.ofFormatting(Formatting.DARK_GRAY))
                                        .positioning(Positioning.absolute(7, 5))
                        )
                        .positioning(Positioning.relative(50, 50))
                        .id("gui")
        );
        
        setSeed(handler.getSeed());
        
        int lapisCount = (this.handler).getLapisCount();
        
        root.childById(FlowLayout.class, "gui").child(
                new EnchantScrollContainer(ScrollContainer.ScrollDirection.VERTICAL, Sizing.fixed(108), Sizing.fixed(57),
                                Containers.verticalFlow(Sizing.fill(), Sizing.fixed(19 * 6))
                                        .id("buttons")
                        )
                        .positioning(Positioning.absolute(60, 14))
                        .id("options")
        );
        
        for(int l = 0; l < 6; ++l) {
            
            Optional<RegistryEntry.Reference<Enchantment>> enchantment = this.client.world.getRegistryManager().get(RegistryKeys.ENCHANTMENT).getEntry((this.handler).enchantmentId[l]);
            List<Text> buttonTooltip = Lists.newArrayList();
            int enchantmentPower = (this.handler).enchantmentPower[l];
            if (enchantment.isPresent()) {
                int level = (this.handler).enchantmentLevel[l];
                int amount = l + 1 - (int) RoleplayMechanicsCommon.getMilestones().stream().filter(skillMilestone -> skillMilestone.hasMet(getEnchantingLevel()) && skillMilestone.getRewardType().equals("ENCHANT_COST_REDUCTION")).count();
                
                if (amount < 1) {
                    amount = 1;
                }
                
                buttonTooltip.add(Text.translatable("container.enchant.clue", Enchantment.getName(enchantment.get(), level)).formatted(Formatting.WHITE));
                if (!this.client.player.getAbilities().creativeMode) {
                    buttonTooltip.add(ScreenTexts.EMPTY);
                    if (this.client.player.experienceLevel < enchantmentPower) {
                        buttonTooltip.add(Text.translatable("container.enchant.level.requirement", (this.handler).enchantmentPower[l]).formatted(Formatting.RED));
                    } else {
                        
                        MutableText lapisCost;
                        if (amount == 1) {
                            lapisCost = Text.translatable("container.enchant.lapis.one");
                        } else {
                            lapisCost = Text.translatable("container.enchant.lapis.many", amount);
                        }
                        
                        buttonTooltip.add(lapisCost.formatted(lapisCount >= amount ? Formatting.GRAY : Formatting.RED));
                        MutableText levelCost;
                        if (amount == 1) {
                            levelCost = Text.translatable("container.enchant.level.one");
                        } else {
                            levelCost = Text.translatable("container.enchant.level.many", amount);
                        }
                        
                        buttonTooltip.add(levelCost.formatted(Formatting.GRAY));
                        
                        buttonTooltip.add(Text.translatable("container.enchant.power", (this.handler).enchantmentPower[l]).formatted(Formatting.GOLD));
                        }
                }
            }
            
            if (enchantmentPower == 0) {
                
                root.childById(FlowLayout.class, "buttons").child(
                        new EnchantButtonComponent(Text.empty(), onPress -> {
                        
                        })
                                .active(false)
                                .renderer(ButtonComponent.Renderer.texture(ENCHANTMENT_SLOT_DISABLED_TEXTURE, 0, 0, 108, 19))
                                .sizing(Sizing.fixed(108), Sizing.fixed(19))
                                .positioning(Positioning.absolute(0, 19 * l))
                );
                
            } else {
                MutableText phrase = generatePhrase();
                if ((lapisCount < l + 1 || this.client.player.experienceLevel < enchantmentPower) && !this.client.player.getAbilities().creativeMode) {
                    
                    root.childById(FlowLayout.class, "buttons").child(
                            Components.button(phrase.formatted(Formatting.GRAY), onPress -> {
                            
                            })
                                    .renderer(ButtonComponent.Renderer.texture(ENCHANTMENT_SLOT_DISABLED_TEXTURE, 0, 0, 108, 19))
                                    .tooltip(buttonTooltip)
                                    .sizing(Sizing.fixed(108), Sizing.fixed(19))
                                    .positioning(Positioning.absolute(0, 19 * l))
                    );
                    
                    root.childById(FlowLayout.class, "buttons").child(
                            Components.item(QuesItems.EXPERIENCE_DISABLED.getDefaultStack())
                                    .sizing(Sizing.fixed(16), Sizing.fixed(16))
                                    .positioning(Positioning.absolute(1, 2 + 19 * l))
                    );
                    
                } else {
                    
                    int finalL = l;
                    root.childById(FlowLayout.class, "buttons").child(
                            new EnchantButtonComponent(phrase, onPress -> {
                                this.client.interactionManager.clickButton((this.handler).syncId, finalL);
                            })
                                    .active(true)
                                    .tooltip(buttonTooltip)
                                    .sizing(Sizing.fixed(108), Sizing.fixed(19))
                                    .positioning(Positioning.absolute(0, 19 * l))
                    );
                    
                    root.childById(FlowLayout.class, "buttons").child(
                            Components.item(QuesItems.EXPERIENCE.getDefaultStack())
                                    .sizing(Sizing.fixed(16), Sizing.fixed(16))
                                    .positioning(Positioning.absolute(1, 2 + 19 * l))
                    );
                }
            }
        }
    }
    
    public static Integer getEnchantingLevel() {
        return enchantingLevel;
    }
    
    public static void setEnchantingLevel(Integer enchantingLevel) {
        SkillEnabledEnchantingTableHandledScreen.enchantingLevel = enchantingLevel;
    }
}
