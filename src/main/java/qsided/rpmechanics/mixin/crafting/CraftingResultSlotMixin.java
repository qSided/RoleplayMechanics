package qsided.rpmechanics.mixin.crafting;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.screen.slot.CraftingResultSlot;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import qsided.rpmechanics.PlayerData;
import qsided.rpmechanics.RoleplayMechanicsCommon;
import qsided.rpmechanics.StateManager;
import qsided.rpmechanics.config.requirements.ItemCraftingRequirement;
import qsided.rpmechanics.events.IncreaseSkillExperienceCallback;

import java.util.List;

/**
 * Mixin that intercepts vanilla crafting completion events to award
 * crafting experience through RPMechanics' custom skill system.
 *
 * <p>
 * MixinExtras' {@link WrapMethod}, ensuring that XP is awarded immediately
 * before vanilla's post-crafting logic runs (such as decrementing ingredients
 * or awarding recipe statistics).
 * </p>
 *
 * <p>
 * XP awards depend on:
 * <ul>
 *     <li>The crafted item's registry ID (e.g. {@code minecraft:stone_pickaxe})</li>
 *     <li>Configuration entries defined in
 *         {@link qsided.rpmechanics.config.requirements.ItemCraftingRequirement}</li>
 * </ul>
 * </p>
 *
 * <p>
 * This mixin is server-side only. When executed on the client, no XP is awarded.
 * </p>
 */
@Mixin(CraftingResultSlot.class)
public class CraftingResultSlotMixin {

    /**
     * Reference to the player who owns this crafting result slot.
     * Shadowed from {@link CraftingResultSlot}.
     */
    @Shadow @Final private PlayerEntity player;

    /**
     * Wraps execute custom XP-award logic
     * while still preserving all vanilla crafting behavior.
     *
     * <p>
     * Steps:
     * <ol>
     *     <li>Server checks crafted item registry ID</li>
     *     <li>Matches the ID against RPMechanics crafting configuration</li>
     *     <li>Awards skill experience via event callback</li>
     *     <li>Executes original vanilla crafting logic</li>
     * </ol>
     * </p>
     *
     * @param stack     The crafted result item
     * @param original  The original onCrafted method call
     */
    @WrapMethod(method = "onCrafted(Lnet/minecraft/item/ItemStack;)V")
    private void rpmechanics$onCrafted(ItemStack stack, Operation<Void> original) {
        if (!player.getWorld().isClient) {
            // Resolve the registry ID (actual identifier used in config)
            String itemId = Registries.ITEM.getId(stack.getItem()).toString();

            // Attempt XP award
            awardXp(RoleplayMechanicsCommon.getCraftingReqs(), itemId);
        }

        // Execute original vanilla method behavior
        original.call(stack);
    }

    /**
     * Awards crafting XP based on the crafted item ID and the player's
     * current skill data. Only executes on the server thread.
     *
     * <p>
     * XP amounts are defined in
     * {@link ItemCraftingRequirement#getExpWorth()} inside the mod's configuration.
     * If no entry matches the crafted item, no XP is granted.
     * </p>
     *
     * @param items  List of crafting requirement entries to search
     * @param itemId Registry ID of the item that was crafted
     */
    @Unique
    private void awardXp(List<ItemCraftingRequirement> items, String itemId) {
        // XP only applies to real server players
        if (!(player instanceof ServerPlayerEntity serverPlayer)) {
            return;
        }

        // Resolve player RPG data
        PlayerData state = StateManager.getPlayerState(player);
        if (state == null) {
            return;
        }

        // Filter configuration to matching item ID and award XP accordingly
        items.stream()
                .filter(item -> itemId.equals(item.getItemId()))
                .forEach(item -> {
                    IncreaseSkillExperienceCallback.EVENT.invoker()
                            .increaseExp(serverPlayer, state, "crafting", item.getExpWorth());

                    System.out.println(
                            "[RPMechanics] Crafted " + itemId +
                                    " for " + item.getExpWorth() + " XP"
                    );
                });
    }
}
