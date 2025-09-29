package daam.common.network;

import daam.DAAM;
import daam.common.network.packets.client.*;
import daam.common.network.packets.server.ResponseRegionFromChunkPacket;
import daam.common.network.packets.SyncCurrentRegionPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class NetworkHandler {
    
    private static final String PROTOCOL_VERSION = "1";
    
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
        new ResourceLocation(DAAM.MODID, "main"),
        () -> PROTOCOL_VERSION,
        PROTOCOL_VERSION::equals,
        PROTOCOL_VERSION::equals
    );
    
    private static int packetId = 0;
    
    public static void registry() {
        // Client to Server packets
        INSTANCE.messageBuilder(CreateRegionPacket.class, packetId++, NetworkDirection.PLAY_TO_SERVER)
            .decoder(CreateRegionPacket::decode)
            .encoder((packet, buf) -> CreateRegionPacket.encode(packet, buf))
            .consumerMainThread((packet, ctx) -> CreateRegionPacket.handle(packet, ctx))
            .add();
            
        INSTANCE.messageBuilder(RemoveRegionPacket.class, packetId++, NetworkDirection.PLAY_TO_SERVER)
            .decoder(RemoveRegionPacket::decode)
            .encoder((packet, buf) -> RemoveRegionPacket.encode(packet, buf))
            .consumerMainThread((packet, ctx) -> RemoveRegionPacket.handle(packet, ctx))
            .add();
            
        INSTANCE.messageBuilder(RequestRegionFromChunkPacket.class, packetId++, NetworkDirection.PLAY_TO_SERVER)
            .decoder(RequestRegionFromChunkPacket::decode)
            .encoder((packet, buf) -> RequestRegionFromChunkPacket.encode(packet, buf))
            .consumerMainThread((packet, ctx) -> RequestRegionFromChunkPacket.handle(packet, ctx))
            .add();
            
        INSTANCE.messageBuilder(SyncRegionPacket.class, packetId++, NetworkDirection.PLAY_TO_SERVER)
            .decoder(SyncRegionPacket::decode)
            .encoder((packet, buf) -> SyncRegionPacket.encode(packet, buf))
            .consumerMainThread((packet, ctx) -> SyncRegionPacket.handle(packet, ctx))
            .add();
            
        INSTANCE.messageBuilder(UpdateRegionPacket.class, packetId++, NetworkDirection.PLAY_TO_SERVER)
            .decoder(UpdateRegionPacket::decode)
            .encoder((packet, buf) -> UpdateRegionPacket.encode(packet, buf))
            .consumerMainThread((packet, ctx) -> UpdateRegionPacket.handle(packet, ctx))
            .add();
            
        INSTANCE.messageBuilder(UpdateSoundBlockPacket.class, packetId++, NetworkDirection.PLAY_TO_SERVER)
            .decoder(UpdateSoundBlockPacket::decode)
            .encoder((packet, buf) -> UpdateSoundBlockPacket.encode(packet, buf))
            .consumerMainThread((packet, ctx) -> UpdateSoundBlockPacket.handle(packet, ctx))
            .add();
            
        // Server to Client packets
        INSTANCE.messageBuilder(ResponseRegionFromChunkPacket.class, packetId++, NetworkDirection.PLAY_TO_CLIENT)
            .decoder(ResponseRegionFromChunkPacket::decode)
            .encoder((packet, buf) -> ResponseRegionFromChunkPacket.encode(packet, buf))
            .consumerMainThread((packet, ctx) -> ResponseRegionFromChunkPacket.handle(packet, ctx))
            .add();
            
        INSTANCE.messageBuilder(SyncCurrentRegionPacket.class, packetId++, NetworkDirection.PLAY_TO_CLIENT)
            .decoder(SyncCurrentRegionPacket::decode)
            .encoder((packet, buf) -> packet.encode(buf))
            .consumerMainThread((packet, ctx) -> packet.handle(ctx))
            .add();
    }
}