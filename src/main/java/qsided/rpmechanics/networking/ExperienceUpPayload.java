package qsided.rpmechanics.networking;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

public record ExperienceUpPayload(String skill, Float amount) implements CustomPayload {
    
    public static final Id<ExperienceUpPayload> ID = new Id<>(QuesNetworkingConstants.EXPERIENCE_UP);
    public static final PacketCodec<RegistryByteBuf, ExperienceUpPayload> CODEC = PacketCodec.tuple(PacketCodecs.STRING, ExperienceUpPayload::skill, PacketCodecs.FLOAT, ExperienceUpPayload::amount, ExperienceUpPayload::new);
    
    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
