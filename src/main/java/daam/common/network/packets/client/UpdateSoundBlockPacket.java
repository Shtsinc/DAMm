package daam.common.network.packets.client;

import daam.common.network.packets.SimplePacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class UpdateSoundBlockPacket extends SimplePacket {

    public UpdateSoundBlockPacket() {
        // TODO: Port from 1.12.2 to 1.20.1
    }

    public static void encode(UpdateSoundBlockPacket packet, FriendlyByteBuf buf) {
        // TODO: Implement encoding
    }

    public static UpdateSoundBlockPacket decode(FriendlyByteBuf buf) {
        // TODO: Implement decoding
        return new UpdateSoundBlockPacket();
    }

    public static void handle(UpdateSoundBlockPacket packet, Supplier<NetworkEvent.Context> contextSupplier) {
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
