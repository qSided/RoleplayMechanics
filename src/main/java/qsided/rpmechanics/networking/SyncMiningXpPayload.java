package qsided.rpmechanics.networking;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import qsided.rpmechanics.RoleplayMechanicsCommon;

public record SyncMiningXpPayload(byte[] compressedJson) implements CustomPayload {

    public static final CustomPayload.Id<SyncMiningXpPayload> ID =
            new CustomPayload.Id<>(Identifier.of(RoleplayMechanicsCommon.MOD_ID, "sync_mining_xp"));

    public static final PacketCodec<RegistryByteBuf, SyncMiningXpPayload> CODEC =
            PacketCodec.tuple(
                    PacketCodecs.BYTE_ARRAY, SyncMiningXpPayload::compressedJson,
                    SyncMiningXpPayload::new
            );

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }
}
