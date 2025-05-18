package qsided.rpmechanics.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import io.wispforest.owo.ui.component.ButtonComponent;
import io.wispforest.owo.ui.event.MouseEnter;
import io.wispforest.owo.ui.event.MouseLeave;
import io.wispforest.owo.ui.util.NinePatchTexture;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import qsided.rpmechanics.RoleplayMechanicsCommon;

import java.util.function.Consumer;

public class EnchantButtonComponent extends ButtonComponent {
    
    private static final Identifier ENCHANTMENT_SLOT_DISABLED_TEXTURE = Identifier.of(RoleplayMechanicsCommon.MOD_ID, "textures/gui/container/enchanting_table/disabled.png");
    private static final Identifier ENCHANTMENT_SLOT_HIGHLIGHTED_TEXTURE = Identifier.of(RoleplayMechanicsCommon.MOD_ID, "textures/gui/container/enchanting_table/highlighted.png");
    private static final Identifier ENCHANTMENT_SLOT_TEXTURE = Identifier.of(RoleplayMechanicsCommon.MOD_ID, "textures/gui/container/enchanting_table/base.png");
    
    
    public EnchantButtonComponent(Text message, Consumer<ButtonComponent> onPress) {
        super(message, onPress);
        
        //var texture = this.active ? this.isHovered() ? ENCHANTMENT_SLOT_HIGHLIGHTED_TEXTURE : ENCHANTMENT_SLOT_TEXTURE : ENCHANTMENT_SLOT_DISABLED_TEXTURE;
        
        if (!active) {
            renderer(Renderer.texture(ENCHANTMENT_SLOT_DISABLED_TEXTURE, 0, 0, 108, 19));
        } else {
            renderer(Renderer.texture(ENCHANTMENT_SLOT_TEXTURE, 0, 0, 108, 19));
        }
        
        
        this.mouseEnter().subscribe(new MouseEnter() {
            @Override
            public void onMouseEnter() {
                if (active) {
                    renderer(Renderer.texture(ENCHANTMENT_SLOT_HIGHLIGHTED_TEXTURE, 0, 0, 108, 19));
                }
            }
        });
        
        this.mouseLeave().subscribe(new MouseLeave() {
            @Override
            public void onMouseLeave() {
                if (active) {
                    renderer(Renderer.texture(ENCHANTMENT_SLOT_TEXTURE, 0, 0, 108, 19));
                }
            }
        });
    }
}
