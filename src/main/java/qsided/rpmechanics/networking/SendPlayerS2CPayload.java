package qsided.rpmechanics.networking;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

public record SendPlayerS2CPayload(Integer enchantingLevel) implements CustomPayload {
    
    public static final Id<SendPlayerS2CPayload> ID = new Id<>(QuesNetworkingConstants.SEND_PLAYER_S2C);
    public static final PacketCodec<RegistryByteBuf, SendPlayerS2CPayload> CODEC = PacketCodec.tuple(PacketCodecs.INTEGER, SendPlayerS2CPayload::enchantingLevel, SendPlayerS2CPayload::new);
    
    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
