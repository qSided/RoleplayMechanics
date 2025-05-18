package qsided.rpmechanics.networking;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

public record SendLevelsPayload(NbtCompound levels) implements CustomPayload {
    
    public static final Id<SendLevelsPayload> ID = new Id<>(QuesNetworkingConstants.SEND_SKILLS_LEVELS_NBT);
    public static final PacketCodec<RegistryByteBuf, SendLevelsPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.NBT_COMPOUND, SendLevelsPayload::levels,
            SendLevelsPayload::new);
    
    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
