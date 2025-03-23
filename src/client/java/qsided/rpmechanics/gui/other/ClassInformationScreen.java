package qsided.rpmechanics.gui.other;

import io.wispforest.owo.ui.base.BaseUIModelScreen;
import io.wispforest.owo.ui.container.FlowLayout;
import net.minecraft.util.Identifier;
import qsided.rpmechanics.RoleplayMechanicsCommon;

public class ClassInformationScreen  extends BaseUIModelScreen<FlowLayout> {
    
    protected ClassInformationScreen(Class<FlowLayout> rootComponentClass, DataSource source) {
        super(FlowLayout.class, DataSource.asset(Identifier.of(RoleplayMechanicsCommon.MOD_ID, "agility")));
    }
    
    @Override
    protected void build(FlowLayout rootComponent) {
    
    }
}
