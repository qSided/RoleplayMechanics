package qsided.rpmechanics.networking;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

public record SendPlayerJumpPayload(Integer integer) implements CustomPayload {
    
    public static final Id<SendPlayerJumpPayload> ID = new Id<>(QuesNetworkingConstants.SEND_PLAYER_JUMP);
    public static final PacketCodec<RegistryByteBuf, SendPlayerJumpPayload> CODEC = PacketCodec.tuple(PacketCodecs.INTEGER, SendPlayerJumpPayload::integer, SendPlayerJumpPayload::new);
    
    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
