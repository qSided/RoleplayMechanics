package qsided.rpmechanics.networking;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import qsided.rpmechanics.RoleplayMechanicsCommon;

public record SyncConfigPayload(
        String rpClassesJson,
        String milestonesJson
) implements CustomPayload {

    public static final CustomPayload.Id<SyncConfigPayload> ID =
            new CustomPayload.Id<>(Identifier.of(RoleplayMechanicsCommon.MOD_ID, "sync_config"));

    public static final PacketCodec<RegistryByteBuf, SyncConfigPayload> CODEC =
            PacketCodec.tuple(
                    PacketCodecs.STRING, SyncConfigPayload::rpClassesJson,
                    PacketCodecs.STRING, SyncConfigPayload::milestonesJson,
                    SyncConfigPayload::new
            );

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }
}
