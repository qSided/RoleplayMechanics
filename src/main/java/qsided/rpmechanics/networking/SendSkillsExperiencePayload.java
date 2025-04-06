package qsided.rpmechanics.networking;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

public record SendSkillsExperiencePayload(Float mining, Float enchanting, Float swords, Float woodcutting, Float endurance, Float agility, Float crafting, Float smithing) implements CustomPayload {
    
    public static final Id<SendSkillsExperiencePayload> ID = new Id<>(QuesNetworkingConstants.SEND_SKILLS_EXPERIENCE);
    public static final PacketCodec<RegistryByteBuf, SendSkillsExperiencePayload> CODEC = PacketCodec.tuple(
            PacketCodecs.FLOAT, SendSkillsExperiencePayload::mining,
            PacketCodecs.FLOAT, SendSkillsExperiencePayload::enchanting,
            PacketCodecs.FLOAT, SendSkillsExperiencePayload::swords,
            PacketCodecs.FLOAT, SendSkillsExperiencePayload::woodcutting,
            PacketCodecs.FLOAT, SendSkillsExperiencePayload::endurance,
            PacketCodecs.FLOAT, SendSkillsExperiencePayload::agility,
            PacketCodecs.FLOAT, SendSkillsExperiencePayload::crafting,
            PacketCodecs.FLOAT, SendSkillsExperiencePayload::smithing,
            SendSkillsExperiencePayload::new);
    
    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
