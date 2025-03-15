package qsided.rpmechanics.networking;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

public record PlayerFirstJoinPayload(Integer integer) implements CustomPayload {
    
    public static final Id<PlayerFirstJoinPayload> ID = new Id<>(QuesNetworkingConstants.SEND_PLAYER_FIRST_JOIN);
    public static final PacketCodec<RegistryByteBuf, PlayerFirstJoinPayload> CODEC = PacketCodec.tuple(PacketCodecs.INTEGER, PlayerFirstJoinPayload::integer, PlayerFirstJoinPayload::new);
    
    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
