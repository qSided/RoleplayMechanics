package qsided.rpmechanics;

import net.minecraft.util.ActionResult;
import qsided.rpmechanics.events.IncreaseSkillExperienceCallback;
//import xaero.pac.common.server.api.OpenPACServerAPI;

public class OpenPACCompat {
    
    public static void initialize() {
        
        //IncreaseSkillExperienceCallback.EVENT.register((player, state, skill, value) -> {
        //
        //    if (OpenPACServerAPI.get(player.server).getPartyManager().getPartyByMember(player.getUuid()) != null) {
        //        OpenPACServerAPI.get(player.server).getPartyManager().getPartyByMember(player.getUuid()).getOnlineMemberStream().forEach(member -> {
        //            if (isWithinRange(member.getX(), member.getY(), member.getZ(), player.getX() + 50, player.getY() + 50, member.getZ() + 50, player.getX() - 50, player.getY() - 50, member.getZ() - 50)) {
        //                PlayerData memberState = StateManager.getPlayerState(member);
        //
        //                memberState.skillExperience.put(skill, memberState.skillExperience.getOrDefault(skill, 0F) + (value / 2));
        //            }
        //        });
        //    }
        //
        //    return ActionResult.PASS;
        //});
    }
    
    public static boolean isWithinRange(double x, double y, double z,
                                        double minX, double minY, double minZ,
                                        double maxX, double maxY, double maxZ) {
        return (x >= minX && x <= maxX &&
                y >= minY && y <= maxY &&
                z >= minZ && z <= maxZ);
    }
    
}
