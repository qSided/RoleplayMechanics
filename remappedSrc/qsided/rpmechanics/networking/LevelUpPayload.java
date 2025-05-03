package qsided.rpmechanics.networking;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

public record LevelUpPayload(String skill, Integer level, boolean shouldMessage) implements CustomPayload {
    
    public static final Id<LevelUpPayload> ID = new Id<>(QuesNetworkingConstants.LEVEL_UP);
    public static final PacketCodec<RegistryByteBuf, LevelUpPayload> CODEC = PacketCodec.tuple(PacketCodecs.STRING, LevelUpPayload::skill, PacketCodecs.INTEGER, LevelUpPayload::level, PacketCodecs.BOOLEAN, LevelUpPayload::shouldMessage, LevelUpPayload::new);
    
    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
