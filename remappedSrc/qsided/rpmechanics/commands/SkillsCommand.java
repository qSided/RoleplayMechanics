package qsided.rpmechanics.commands;

import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
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
                    .then(literal("setLevel")
                            .then(argument("skill", StringArgumentType.string())
                                    .then(argument("value", IntegerArgumentType.integer())
                                            .executes(context -> {
                                                final String skill = StringArgumentType.getString(context, "skill");
                                                final int value = IntegerArgumentType.getInteger(context, "value");
                                                final ServerPlayerEntity player = context.getSource().getPlayer();
                                                PlayerData state = StateManager.getPlayerState(player);
                                                IncreaseSkillLevelCallback.EVENT.invoker().increaseLevel(player, state, skill, value - state.skillLevels.getOrDefault(skill, 1), true);
                                                return 1;
                                            })
                                    )))
                    .then(literal("setExperience")
                            .then(argument("skill", StringArgumentType.string())
                                    .then(argument("value", FloatArgumentType.floatArg())
                                            .executes(context -> {
                                                final String skill = StringArgumentType.getString(context, "skill");
                                                final float value = FloatArgumentType.getFloat(context, "value");
                                                final ServerPlayerEntity player = context.getSource().getPlayer();
                                                PlayerData state = StateManager.getPlayerState(player);
                                                IncreaseSkillExperienceCallback.EVENT.invoker().increaseExp(player, state, skill, value);
                                                return 1;
                                            })))));
        });
    }
}
