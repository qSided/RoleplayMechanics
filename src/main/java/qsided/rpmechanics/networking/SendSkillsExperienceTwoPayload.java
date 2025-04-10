package qsided.rpmechanics.networking;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

public record SendSkillsExperienceTwoPayload(Float farming, Float axes, Float bows) implements CustomPayload {
    
    public static final Id<SendSkillsExperienceTwoPayload> ID = new Id<>(QuesNetworkingConstants.SEND_SKILLS_EXPERIENCE_TWO);
    public static final PacketCodec<RegistryByteBuf, SendSkillsExperienceTwoPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.FLOAT, SendSkillsExperienceTwoPayload::farming,
            PacketCodecs.FLOAT, SendSkillsExperienceTwoPayload::axes,
            PacketCodecs.FLOAT, SendSkillsExperienceTwoPayload::bows,
            SendSkillsExperienceTwoPayload::new);
    
    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
