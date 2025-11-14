package qsided.rpmechanics.mixin.crafting;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.ScreenHandlerSlotUpdateS2CPacket;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.registry.Registries;
import net.minecraft.screen.CraftingScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;
import qsided.rpmechanics.PlayerData;
import qsided.rpmechanics.RoleplayMechanicsCommon;
import qsided.rpmechanics.StateManager;
import qsided.rpmechanics.config.requirements.ItemCraftingRequirement;

import java.util.List;
import java.util.Optional;

/**
 * Mixin that overwrites vanilla crafting result logic to enforce
 * custom skill-based crafting restrictions for the RPMechanics mod.
 * <p>
 * This class controls:
 * <ul>
 *     <li>Whether a crafting recipe should successfully output an item</li>
 *     <li>Whether the player's crafting skill level meets configuration requirements</li>
 *     <li>Displaying an error message when a player attempts to craft an item they
 *         do not have the required crafting level for</li>
 *     <li>Synchronizing the modified crafting result back to the client</li>
 * </ul>
 * </p>
 *
 *
 * @author qSided
 * @reason Custom crafting logic based off RPG skill levels
 */
@Mixin(CraftingScreenHandler.class)
public class CraftingScreenHandlerMixin {

    /**
     * Overwrites the vanilla method responsible for determining the result of a crafting recipe.
     *
     * <p>
     * This implementation applies the RPMechanics crafting rules:
     * <ul>
     *     <li>Fetch the crafted result item</li>
     *     <li>Check whether the item has a crafting-level requirement configured</li>
     *     <li>If the player's crafting level is high enough, output the crafted item</li>
     *     <li>If not, prevent crafting and send the player a warning message</li>
     *     <li>If the item has no requirement, allow crafting normally</li>
     * </ul>
     * </p>
     *
     * <p>
     * After determining the final result, the slot is updated and synchronized to the client.
     * </p>
     *
     * @param handler            The crafting screen handler instance
     * @param world              The world the player is in
     * @param player             The player interacting with the table
     * @param craftingInventory  The 3×3 crafting grid
     * @param resultInventory    The output slot inventory
     * @param recipe             The optional recipe detected by the crafting grid
     * @author qSided
     */
    @Overwrite
    public static void updateResult(
            ScreenHandler handler,
            World world,
            PlayerEntity player,
            RecipeInputInventory craftingInventory,
            CraftingResultInventory resultInventory,
            @Nullable RecipeEntry<CraftingRecipe> recipe
    ) {
        // Default: nothing is craftable until proven otherwise
        ItemStack output = ItemStack.EMPTY;

        if (!world.isClient) {
            CraftingRecipeInput craftingRecipeInput = craftingInventory.createRecipeInput();
            ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;

            // Try to match a crafting recipe using vanilla recipe manager
            Optional<RecipeEntry<CraftingRecipe>> optional =
                    world.getServer().getRecipeManager().getFirstMatch(
                            RecipeType.CRAFTING,
                            craftingRecipeInput,
                            world,
                            recipe
                    );

            if (optional.isPresent()) {
                RecipeEntry<CraftingRecipe> recipeEntry = optional.get();
                CraftingRecipe craftingRecipe = recipeEntry.value();

                // Ensure vanilla checks pass before applying custom logic
                if (resultInventory.shouldCraftRecipe(world, serverPlayer, recipeEntry)) {
                    ItemStack craftedItem = craftingRecipe.craft(
                            craftingRecipeInput,
                            world.getRegistryManager()
                    );

                    // Avoid disabled items when datapacks remove features
                    if (craftedItem.isItemEnabled(world.getEnabledFeatures())) {

                        // Load player's crafting level
                        PlayerData state = StateManager.getPlayerState(serverPlayer);
                        int craftingLevel = state != null
                                ? state.skillLevels.getOrDefault("crafting", 1)
                                : 1;

                        // Registry ID of the crafted item (e.g. "minecraft:oak_planks")
                        String itemId = Registries.ITEM.getId(craftedItem.getItem()).toString();
                        List<ItemCraftingRequirement> reqs = RoleplayMechanicsCommon.getCraftingReqs();

                        // Item IS part of the config requirements → enforce level checks
                        if (containsItem(reqs, itemId)) {
                            for (ItemCraftingRequirement requirement : reqs) {
                                if (itemId.equals(requirement.getItemId())) {

                                    if (craftingLevel >= requirement.getLevelReq()) {
                                        // Player meets requirement → allow crafting
                                        output = craftedItem;

                                    } else {
                                        // Player does NOT meet level requirement → deny crafting
                                        serverPlayer.sendMessage(
                                                Text.literal("Your crafting level is too low!"),
                                                true
                                        );
                                    }
                                    break;
                                }
                            }

                        } else {
                            // Item is NOT in requirements list → always allowed
                            output = craftedItem;
                        }
                    }
                }
            }

            // Write final output to result slot
            resultInventory.setStack(0, output);
            handler.setPreviousTrackedSlot(0, output);

            // Sync the result slot to the client
            serverPlayer.networkHandler.sendPacket(
                    new ScreenHandlerSlotUpdateS2CPacket(
                            handler.syncId,
                            handler.nextRevision(),
                            0,
                            output
                    )
            );
        }
    }

    /**
     * Checks whether the given registry item ID exists inside the configured
     * list of crafting requirements.
     *
     * @param items   List of configured crafting requirements
     * @param itemId  Registry item ID (e.g. "minecraft:stone")
     * @return true if a requirement exists for the item, false otherwise
     */
    @Unique
    private static boolean containsItem(List<ItemCraftingRequirement> items, String itemId) {
        return items.stream()
                .map(ItemCraftingRequirement::getItemId)
                .anyMatch(itemId::equals);
    }
}
