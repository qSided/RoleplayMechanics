package qsided.rpmechanics.networking;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import qsided.rpmechanics.RoleplayMechanicsCommon;

public record SyncItemUseReqsPayload(byte[] compressedJson) implements CustomPayload {

    public static final CustomPayload.Id<SyncItemUseReqsPayload> ID =
            new CustomPayload.Id<>(Identifier.of(RoleplayMechanicsCommon.MOD_ID, "sync_item_use_reqs"));

    public static final PacketCodec<RegistryByteBuf, SyncItemUseReqsPayload> CODEC =
            PacketCodec.tuple(
                    PacketCodecs.BYTE_ARRAY, SyncItemUseReqsPayload::compressedJson,
                    SyncItemUseReqsPayload::new
            );

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }
}
