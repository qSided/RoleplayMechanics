package qsided.rpmechanics.gui;

import io.wispforest.owo.client.screens.OwoScreenHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipePropertySet;
import net.minecraft.screen.*;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import qsided.rpmechanics.blocks.QuesBlocks;
import qsided.rpmechanics.recipes.QuesRecipePropertySets;


public class OvenScreenHandler extends ScreenHandler implements OwoScreenHandler {
    final Inventory inventory;
    private final PropertyDelegate propertyDelegate;
    protected final World world;
    private final RecipePropertySet recipePropertySet;
    
    public OvenScreenHandler(
            int syncId,
            PlayerInventory playerInventory,
            Inventory inventory,
            PropertyDelegate propertyDelegate
    ) {
        super(QuesBlocks.OVEN_SCREEN_HANDLER, syncId);
        checkSize(inventory, 3);
        checkDataCount(propertyDelegate, 4);
        this.inventory = inventory;
        this.propertyDelegate = propertyDelegate;
        this.world = playerInventory.player.getWorld();
        this.recipePropertySet = this.world.getRecipeManager().getPropertySet(QuesRecipePropertySets.OVEN_INPUT);
        this.addSlot(new Slot(inventory, 0, 56, 17));
        this.addSlot(new OvenFuelSlot(this, inventory, 1, 56, 53));
        this.addSlot(new OvenOutputSlot(playerInventory.player, inventory, 2, 116, 35));
        this.addPlayerSlots(playerInventory, 8, 84);
        this.addProperties(propertyDelegate);
    }
    
    public OvenScreenHandler(
            int syncId,
            PlayerInventory playerInventory
    ) {
        this(syncId, playerInventory, new SimpleInventory(3), new ArrayPropertyDelegate(4));
    }
    
    @Override
    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }
    
    @Override
    public ItemStack quickMove(PlayerEntity player, int slot) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot2 = this.slots.get(slot);
        if (slot2 != null && slot2.hasStack()) {
            ItemStack itemStack2 = slot2.getStack();
            itemStack = itemStack2.copy();
            if (slot == 2) {
                if (!this.insertItem(itemStack2, 3, 39, true)) {
                    return ItemStack.EMPTY;
                }
                
                slot2.onQuickTransfer(itemStack2, itemStack);
            } else if (slot != 1 && slot != 0) {
                if (this.isSmeltable(itemStack2)) {
                    if (!this.insertItem(itemStack2, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (this.isFuel(itemStack2)) {
                    if (!this.insertItem(itemStack2, 1, 2, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (slot >= 3 && slot < 30) {
                    if (!this.insertItem(itemStack2, 30, 39, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (slot >= 30 && slot < 39 && !this.insertItem(itemStack2, 3, 30, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.insertItem(itemStack2, 3, 39, false)) {
                return ItemStack.EMPTY;
            }
            
            if (itemStack2.isEmpty()) {
                slot2.setStack(ItemStack.EMPTY);
            } else {
                slot2.markDirty();
            }
            
            if (itemStack2.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }
            
            slot2.onTakeItem(player, itemStack2);
        }
        
        return itemStack;
    }
    
    protected boolean isSmeltable(ItemStack itemStack) {
        return this.recipePropertySet.canUse(itemStack);
    }
    
    protected boolean isFuel(ItemStack item) {
        return this.world.getFuelRegistry().isFuel(item);
    }
    
    public float getCookProgress() {
        int i = this.propertyDelegate.get(2);
        int j = this.propertyDelegate.get(3);
        return j != 0 && i != 0 ? MathHelper.clamp((float)i / (float)j, 0.0F, 1.0F) : 0.0F;
    }
    
    public float getFuelProgress() {
        int i = this.propertyDelegate.get(1);
        if (i == 0) {
            i = 200;
        }
        
        return MathHelper.clamp((float)this.propertyDelegate.get(0) / (float)i, 0.0F, 1.0F);
    }
    
    public boolean isBurning() {
        return this.propertyDelegate.get(0) > 0;
    }
}
