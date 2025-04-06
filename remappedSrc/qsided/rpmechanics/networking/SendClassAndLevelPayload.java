package qsided.rpmechanics.networking;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

public record SendClassAndLevelPayload(String rpClassId, Integer level, Float experience) implements CustomPayload {
    
    public static final Id<SendClassAndLevelPayload> ID = new Id<>(QuesNetworkingConstants.SEND_CLASS_SELECTED);
    public static final PacketCodec<RegistryByteBuf, SendClassAndLevelPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.STRING, SendClassAndLevelPayload::rpClassId,
            PacketCodecs.INTEGER, SendClassAndLevelPayload::level,
            PacketCodecs.FLOAT, SendClassAndLevelPayload::experience,
            SendClassAndLevelPayload::new);
    
    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
