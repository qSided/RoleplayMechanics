package qsided.rpmechanics.gui.other;

import io.wispforest.owo.ui.base.BaseOwoScreen;
import io.wispforest.owo.ui.component.Components;
import io.wispforest.owo.ui.component.TextBoxComponent;
import io.wispforest.owo.ui.container.Containers;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.core.*;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

public class RoleplayNameSelectionScreen extends BaseOwoScreen<FlowLayout> {
    @Override
    protected @NotNull OwoUIAdapter<FlowLayout> createAdapter() {
        return OwoUIAdapter.create(this, Containers::verticalFlow);
    }
    
    @Override
    protected void build(FlowLayout rootComponent) {
        rootComponent.surface(Surface.VANILLA_TRANSLUCENT);
        
        //Information text
        rootComponent.child(
                Components.label(Text.translatable("character.rpmechanics.name_selection"))
                        .maxWidth(400)
                        .verticalTextAlignment(VerticalAlignment.CENTER)
                        .positioning(Positioning.relative(50, 30))
        );
        
        rootComponent.child(
                Components.textBox(Sizing.fill(20))
                        .positioning(Positioning.relative(50, 50))
                        .id("text")
        );
        
        rootComponent.child(
                Components.button(Text.translatable("character.rpmechanics.accept"), onPress -> {
                    //ClientPlayNetworking.send(new SendPlayerDisplayNameC2SPayload(this.component(TextBoxComponent.class, "text").getText()));
                }).positioning(Positioning.relative(50, 60))
        );
        
        //Next page button
        rootComponent.child(
                Components.button(Text.of("→"), onPress -> {
                    client.setScreen(new ClassSelectionScreen());
                }).positioning(Positioning.relative(96, 96)));
    }
}
