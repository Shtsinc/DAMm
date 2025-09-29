package daam.common.network.packets.client;

import daam.common.network.packets.SimplePacket;
import daam.common.world.Region;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SyncRegionPacket extends SimplePacket {

    public SyncRegionPacket() {
        // TODO: Port from 1.12.2 to 1.20.1
    }
    
    public SyncRegionPacket(Region region) {
        // TODO: Implement region synchronization
    }

    public static void encode(SyncRegionPacket packet, FriendlyByteBuf buf) {
        // TODO: Implement encoding
    }

    public static SyncRegionPacket decode(FriendlyByteBuf buf) {
        // TODO: Implement decoding
        return new SyncRegionPacket();
    }

    public static void handle(SyncRegionPacket packet, Supplier<NetworkEvent.Context> contextSupplier) {
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
}
