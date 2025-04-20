package qsided.rpmechanics.gui.other;

import io.wispforest.owo.ui.base.BaseOwoHandledScreen;
import io.wispforest.owo.ui.component.Components;
import io.wispforest.owo.ui.container.Containers;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.core.*;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.NotNull;
import qsided.rpmechanics.blocks.QuesBlocks;
import qsided.rpmechanics.gui.OvenScreenHandler;

public class OvenHandledScreen extends BaseOwoHandledScreen<FlowLayout, OvenScreenHandler> {
    
    private static final Identifier LIT_PROGRESS_TEXTURE = Identifier.ofVanilla("textures/gui/sprites/container/furnace/lit_progress.png");
    private static final Identifier BURN_PROGRESS_TEXTURE = Identifier.ofVanilla("textures/gui/sprites/container/furnace/burn_progress.png");
    
    public OvenHandledScreen(OvenScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }
    
    @Override
    protected @NotNull OwoUIAdapter<FlowLayout> createAdapter() {
        return OwoUIAdapter.create(this, Containers::verticalFlow);
        
    }
    
    @Override
    protected void handledScreenTick() {
        uiAdapter.rootComponent.clearChildren();
        build(uiAdapter.rootComponent);
    }
    
    @Override
    protected void build(FlowLayout root) {
        
        root.surface(Surface.VANILLA_TRANSLUCENT);
        
        root.child(
                Containers.verticalFlow(Sizing.fixed(177), Sizing.fixed(167))
                        .child(
                                Components.texture(Identifier.of("rpmechanics","textures/gui/container/oven.png"),
                                        0,
                                        0,
                                        176,
                                        166,
                                        256,
                                        256)
                        )
                        .child(
                                Components.label(Text.translatable("container.inventory"))
                                        .color(Color.ofFormatting(Formatting.DARK_GRAY))
                                        .positioning(Positioning.absolute(7, 72))
                        )
                        .child(
                                Components.label(Text.translatable(QuesBlocks.OVEN.getTranslationKey()))
                                        .color(Color.ofFormatting(Formatting.DARK_GRAY))
                                        .positioning(Positioning.relative(50, 3))
                        )
                        .positioning(Positioning.relative(50, 50))
                        .id("oven")
        );
        
        if (handler.isBurning()) {
            int fuelProgress = MathHelper.ceil(handler.getFuelProgress() * 13.0F) + 1;
            root.childById(FlowLayout.class, "oven").child(Components.texture(LIT_PROGRESS_TEXTURE, 0, 14-fuelProgress, 14, fuelProgress, 14, 14)
                    .positioning(Positioning.absolute(35, 25 + (14 - fuelProgress))));
        }
        
        int cookProgress = MathHelper.ceil(handler.getCookProgress() * 24.0F);
        root.childById(FlowLayout.class, "oven").child(Components.texture(BURN_PROGRESS_TEXTURE, 0, 0, cookProgress, 16, 24, 16)
                .positioning(Positioning.absolute(56, 32)));
    }
    
}
