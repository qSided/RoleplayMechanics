package qsided.rpmechanics.gui;

import io.wispforest.owo.ui.container.ScrollContainer;
import io.wispforest.owo.ui.core.Component;
import io.wispforest.owo.ui.core.Sizing;

public class EnchantScrollContainer extends ScrollContainer<Component> {
    public double amountScrolled;
    
    public EnchantScrollContainer(ScrollDirection direction, Sizing horizontalSizing, Sizing verticalSizing, Component child) {
        super(direction, horizontalSizing, verticalSizing, child);
    }
    
    @Override
    public boolean onMouseScroll(double mouseX, double mouseY, double amount) {
        amountScrolled += -amount * 0.33;
        return super.onMouseScroll(mouseX, mouseY, amount);
    }
}
