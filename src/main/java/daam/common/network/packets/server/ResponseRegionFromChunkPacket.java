package daam.common.network.packets.server;

import daam.common.network.packets.SimplePacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ResponseRegionFromChunkPacket extends SimplePacket {

    public ResponseRegionFromChunkPacket() {
        // TODO: Port from 1.12.2 to 1.20.1
    }

    public static void encode(ResponseRegionFromChunkPacket packet, FriendlyByteBuf buf) {
        // TODO: Implement encoding
    }

    public static ResponseRegionFromChunkPacket decode(FriendlyByteBuf buf) {
        // TODO: Implement decoding
        return new ResponseRegionFromChunkPacket();
    }

    public static void handle(ResponseRegionFromChunkPacket packet, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            // TODO: Implement packet handling
        });
        context.setPacketHandled(true);
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
        encode(this, buf);
    }

    @Override
    public void decode(FriendlyByteBuf buf) {
        // This method is not used in the new system
    }
}