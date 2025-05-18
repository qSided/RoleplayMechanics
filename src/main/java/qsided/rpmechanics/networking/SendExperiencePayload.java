package qsided.rpmechanics.networking;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

public record SendExperiencePayload(NbtCompound experience) implements CustomPayload {
    
    public static final Id<SendExperiencePayload> ID = new Id<>(QuesNetworkingConstants.SEND_SKILLS_EXPERIENCE_NBT);
    public static final PacketCodec<RegistryByteBuf, SendExperiencePayload> CODEC = PacketCodec.tuple(
            PacketCodecs.NBT_COMPOUND, SendExperiencePayload::experience,
            SendExperiencePayload::new);
    
    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
