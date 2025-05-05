package qsided.rpmechanics.networking;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

public record SendPlayerFallPayload(Integer integer) implements CustomPayload {
    
    public static final Id<SendPlayerFallPayload> ID = new Id<>(QuesNetworkingConstants.SEND_PLAYER_FALL);
    public static final PacketCodec<RegistryByteBuf, SendPlayerFallPayload> CODEC = PacketCodec.tuple(PacketCodecs.INTEGER, SendPlayerFallPayload::integer, SendPlayerFallPayload::new);
    
    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
