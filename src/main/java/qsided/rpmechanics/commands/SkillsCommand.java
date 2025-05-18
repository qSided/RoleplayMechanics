package qsided.rpmechanics.commands;

import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.argument.ArgumentTypes;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.network.ServerPlayerEntity;
import qsided.rpmechanics.PlayerData;
import qsided.rpmechanics.StateManager;
import qsided.rpmechanics.events.IncreaseSkillExperienceCallback;
import qsided.rpmechanics.events.IncreaseSkillLevelCallback;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class SkillsCommand {
    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(literal("skills")
                    .requires(source -> source.hasPermissionLevel(2))
                    .then(argument("player", EntityArgumentType.player())
                            .then(literal("setLevel")
                                    .then(argument("skill", StringArgumentType.string())
                                            .suggests(new QuesSuggestionsProvider())
                                            .then(argument("amount", IntegerArgumentType.integer())
                                                    .executes(context -> {
                                                        final String skill = StringArgumentType.getString(context, "skill");
                                                        final int value = IntegerArgumentType.getInteger(context, "amount");
                                                        final ServerPlayerEntity player = EntityArgumentType.getPlayer(context, "player");
                                                        PlayerData state = StateManager.getPlayerState(player);
                                                        IncreaseSkillLevelCallback.EVENT.invoker().increaseLevel(player, state, skill, value - state.skillLevels.getOrDefault(skill, 1), true);
                                                        return 1;
                                                    })
                                            )))
                            .then(literal("setExperience")
                                    .then(argument("skill", StringArgumentType.string())
                                            .suggests(new QuesSuggestionsProvider())
                                            .then(argument("amount", FloatArgumentType.floatArg())
                                                    .executes(context -> {
                                                        final String skill = StringArgumentType.getString(context, "skill");
                                                        final float value = FloatArgumentType.getFloat(context, "amount");
                                                        final ServerPlayerEntity player = EntityArgumentType.getPlayer(context, "player");
                                                        PlayerData state = StateManager.getPlayerState(player);
                                                        IncreaseSkillExperienceCallback.EVENT.invoker().increaseExp(player, state, skill, value);
                                                        return 1;
                                                    })))))
            );
        });
    }
}
