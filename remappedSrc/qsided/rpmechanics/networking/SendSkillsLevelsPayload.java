package qsided.rpmechanics.networking;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

public record SendSkillsLevelsPayload(Integer mining, Integer enchanting, Integer combat, Integer woodcutting, Integer endurance, Integer agility) implements CustomPayload {
    
    public static final Id<SendSkillsLevelsPayload> ID = new Id<>(QuesNetworkingConstants.SEND_SKILLS_LEVELS);
    public static final PacketCodec<RegistryByteBuf, SendSkillsLevelsPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.INTEGER, SendSkillsLevelsPayload::mining,
            PacketCodecs.INTEGER, SendSkillsLevelsPayload::enchanting,
            PacketCodecs.INTEGER, SendSkillsLevelsPayload::combat,
            PacketCodecs.INTEGER, SendSkillsLevelsPayload::woodcutting,
            PacketCodecs.INTEGER, SendSkillsLevelsPayload::endurance,
            PacketCodecs.INTEGER, SendSkillsLevelsPayload::agility,
            SendSkillsLevelsPayload::new);
    
    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
