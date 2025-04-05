package qsided.rpmechanics.networking;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

public record SendSkillsLevelsTwoPayload(Integer farming) implements CustomPayload {
    
    public static final Id<SendSkillsLevelsTwoPayload> ID = new Id<>(QuesNetworkingConstants.SEND_SKILLS_LEVELS_TWO);
    public static final PacketCodec<RegistryByteBuf, SendSkillsLevelsTwoPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.INTEGER, SendSkillsLevelsTwoPayload::farming,
            SendSkillsLevelsTwoPayload::new);
    
    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
