package qsided.rpmechanics.networking;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

public record SendClassSelectedPayload(Integer rpClassId) implements CustomPayload {
    
    public static final Id<SendClassSelectedPayload> ID = new Id<>(QuesNetworkingConstants.SEND_CLASS_SELECTED);
    public static final PacketCodec<RegistryByteBuf, SendClassSelectedPayload> CODEC = PacketCodec.tuple(PacketCodecs.INTEGER, SendClassSelectedPayload::rpClassId, SendClassSelectedPayload::new);
    
    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
