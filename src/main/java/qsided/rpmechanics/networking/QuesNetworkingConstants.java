package qsided.rpmechanics.networking;

import net.minecraft.util.Identifier;
import qsided.rpmechanics.RoleplayMechanicsCommon;

public class QuesNetworkingConstants {
    public static final Identifier LEVEL_UP = Identifier.of(RoleplayMechanicsCommon.MOD_ID, "level_up");
    public static final Identifier REQUEST_SKILLS = Identifier.of(RoleplayMechanicsCommon.MOD_ID, "request_skills");
    public static final Identifier SEND_SKILLS_LEVELS = Identifier.of(RoleplayMechanicsCommon.MOD_ID, "send_skills_levels");
    public static final Identifier SEND_SKILLS_EXPERIENCE = Identifier.of(RoleplayMechanicsCommon.MOD_ID, "send_skills_experience");
    public static final Identifier SEND_PLAYER_FALL = Identifier.of(RoleplayMechanicsCommon.MOD_ID, "send_player_fall");
    public static final Identifier SEND_PLAYER_JUMP = Identifier.of(RoleplayMechanicsCommon.MOD_ID, "send_player_jump");
    public static final Identifier SEND_CLASS_SELECTED = Identifier.of(RoleplayMechanicsCommon.MOD_ID, "send_class_selected");
    public static final Identifier SEND_CLASS_AND_LEVEL = Identifier.of(RoleplayMechanicsCommon.MOD_ID, "send_class_and_level");
    public static final Identifier SEND_PLAYER_FIRST_JOIN = Identifier.of(RoleplayMechanicsCommon.MOD_ID, "send_player_first_join");
}
