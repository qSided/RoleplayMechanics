package qsided.rpmechanics.gui;

import io.wispforest.owo.client.screens.OwoScreenHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.RecipePropertySet;
import net.minecraft.screen.*;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import qsided.rpmechanics.blocks.QuesBlocks;
import qsided.rpmechanics.recipes.QuesRecipePropertySets;

import java.util.ArrayList;
import java.util.List;


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
        checkSize(inventory, 7);
        checkDataCount(propertyDelegate, 4);
        this.inventory = inventory;
        this.propertyDelegate = propertyDelegate;
        this.world = playerInventory.player.getWorld();
        this.recipePropertySet = this.world.getRecipeManager().getPropertySet(QuesRecipePropertySets.OVEN_INPUT);
        this.addSlot(new OvenFuelSlot(this, inventory, 0, 34, 42));
        this.addSlot(new OvenInputSlot(playerInventory.player, inventory, 1, 85, 23));
        this.addSlot(new OvenInputSlot(playerInventory.player, inventory, 2, 85, 42));
        this.addSlot(new OvenInputSlot(playerInventory.player, inventory, 3, 104, 23));
        this.addSlot(new OvenInputSlot(playerInventory.player, inventory, 4, 104, 42));
        this.addSlot(new OvenInputSlot(playerInventory.player, inventory, 5, 123, 23));
        this.addSlot(new OvenInputSlot(playerInventory.player, inventory, 6, 123, 42));
        this.addPlayerSlots(playerInventory, 8, 84);
        this.addProperties(propertyDelegate);
    }
    
    public OvenScreenHandler(
            int syncId,
            PlayerInventory playerInventory
    ) {
        this(syncId, playerInventory, new SimpleInventory(7), new ArrayPropertyDelegate(4));
    }
    
    @Override
    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }
    
    @Override
    public ItemStack quickMove(PlayerEntity player, int slotId) {
        ItemStack stackToBe = ItemStack.EMPTY;
        Slot slot = this.slots.get(slotId);
        if (slot != null && slot.hasStack()) {
            ItemStack itemInSlot = slot.getStack();
            stackToBe = itemInSlot.copy();
            if (slotId == 7) {
                if (!this.insertItem(itemInSlot, 9, 43, true)) {
                    return ItemStack.EMPTY;
                }
                
                slot.onQuickTransfer(itemInSlot, stackToBe);
            } else if (slotId != 1 && slotId != 0
                    && slotId != 2 && slotId != 3 && slotId != 4 && slotId != 5 && slotId != 6) {
                if (this.isSmeltable(itemInSlot)) {
                    if (!this.insertItem(itemInSlot, 1, 7, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (this.isFuel(itemInSlot)) {
                    if (!this.insertItem(itemInSlot, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (slotId >= 9 && slotId < 35) {
                    if (!this.insertItem(itemInSlot, 35, 43, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (slotId >= 35 && slotId < 43 && !this.insertItem(itemInSlot, 9, 35, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.insertItem(itemInSlot, 9, 43, false)) {
                return ItemStack.EMPTY;
            }
            
            if (itemInSlot.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }
            
            if (itemInSlot.getCount() == stackToBe.getCount()) {
                return ItemStack.EMPTY;
            }
            
            slot.onTakeItem(player, itemInSlot);
        }
        
        return stackToBe;
    }
    
    protected boolean isSmeltable(ItemStack itemStack) {
        List<ItemStack> validIngredients = new ArrayList<>();
        validIngredients.add(Items.BEEF.getDefaultStack());
        validIngredients.add(Items.CHICKEN.getDefaultStack());
        validIngredients.add(Items.PORKCHOP.getDefaultStack());
        validIngredients.add(Items.RABBIT.getDefaultStack());
        validIngredients.add(Items.MUTTON.getDefaultStack());
        validIngredients.add(Items.POTATO.getDefaultStack());
        
        return validIngredients.stream().anyMatch(itemStack::equals);
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
