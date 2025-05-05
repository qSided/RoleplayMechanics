package qsided.rpmechanics.networking;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

public record RequestSkillsPayload(String uuid) implements CustomPayload {
    
    public static final Id<RequestSkillsPayload> ID = new Id<>(QuesNetworkingConstants.REQUEST_SKILLS);
    public static final PacketCodec<RegistryByteBuf, RequestSkillsPayload> CODEC = PacketCodec.tuple(PacketCodecs.STRING, RequestSkillsPayload::uuid, RequestSkillsPayload::new);
    
    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
