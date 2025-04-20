package qsided.rpmechanics.gui;

import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import qsided.rpmechanics.blockentities.OvenBlockEntity;

public class OvenInputSlot extends Slot {
    private final PlayerEntity player;
    private int amount;
    
    public OvenInputSlot(PlayerEntity player, Inventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
        this.player = player;
    }
    
    @Override
    public int getMaxItemCount() {
        return 1;
    }
    
    @Override
    public void onTakeItem(PlayerEntity player, ItemStack stack) {
        this.onCrafted(stack);
        super.onTakeItem(player, stack);
    }
    
    @Override
    protected void onCrafted(ItemStack stack, int amount) {
        this.amount += amount;
        this.onCrafted(stack);
    }
    
    @Override
    protected void onCrafted(ItemStack stack) {
        stack.onCraftByPlayer(this.player.getWorld(), this.player, this.amount);
        if (this.player instanceof ServerPlayerEntity serverPlayerEntity && this.inventory instanceof OvenBlockEntity ovenBlockEntity) {
            ovenBlockEntity.dropExperienceForRecipesUsed(serverPlayerEntity);
        }
        
        this.amount = 0;
    }
}
