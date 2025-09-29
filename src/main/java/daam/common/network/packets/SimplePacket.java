package daam.common.network.packets;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public abstract class SimplePacket {

    public abstract void encode(FriendlyByteBuf buf);

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            if (context.getDirection().getReceptionSide().isServer()) {
                handleServer(context.getSender());
            } else {
                handleClient();
            }
        });
        context.setPacketHandled(true);
    }

    protected void handleClient() {
        // Override in client packets
    }

    protected void handleServer(ServerPlayer player) {
        // Override in server packets
    }

    @OnlyIn(Dist.CLIENT)
    protected Player getClientPlayer() {
        return Minecraft.getInstance().player;
    }

    @OnlyIn(Dist.CLIENT)
    protected Minecraft getMinecraft() {
        return Minecraft.getInstance();
    }
}