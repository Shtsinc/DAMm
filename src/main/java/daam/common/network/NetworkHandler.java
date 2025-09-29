package daam.common.network;

import daam.DAAM;
import daam.common.network.packets.client.*;
import daam.common.network.packets.server.ResponseRegionFromChunkPacket;
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
    
    public void registry() {
        // Client to Server packets
        INSTANCE.messageBuilder(CreateRegionPacket.class, packetId++, NetworkDirection.PLAY_TO_SERVER)
            .decoder(CreateRegionPacket::decode)
            .encoder(CreateRegionPacket::encode)
            .consumerMainThread(CreateRegionPacket::handle)
            .add();
            
        INSTANCE.messageBuilder(RemoveRegionPacket.class, packetId++, NetworkDirection.PLAY_TO_SERVER)
            .decoder(RemoveRegionPacket::decode)
            .encoder(RemoveRegionPacket::encode)
            .consumerMainThread(RemoveRegionPacket::handle)
            .add();
            
        INSTANCE.messageBuilder(RequestRegionFromChunkPacket.class, packetId++, NetworkDirection.PLAY_TO_SERVER)
            .decoder(RequestRegionFromChunkPacket::decode)
            .encoder(RequestRegionFromChunkPacket::encode)
            .consumerMainThread(RequestRegionFromChunkPacket::handle)
            .add();
            
        INSTANCE.messageBuilder(SyncRegionPacket.class, packetId++, NetworkDirection.PLAY_TO_SERVER)
            .decoder(SyncRegionPacket::decode)
            .encoder(SyncRegionPacket::encode)
            .consumerMainThread(SyncRegionPacket::handle)
            .add();
            
        INSTANCE.messageBuilder(UpdateRegionPacket.class, packetId++, NetworkDirection.PLAY_TO_SERVER)
            .decoder(UpdateRegionPacket::decode)
            .encoder(UpdateRegionPacket::encode)
            .consumerMainThread(UpdateRegionPacket::handle)
            .add();
            
        INSTANCE.messageBuilder(UpdateSoundBlockPacket.class, packetId++, NetworkDirection.PLAY_TO_SERVER)
            .decoder(UpdateSoundBlockPacket::decode)
            .encoder(UpdateSoundBlockPacket::encode)
            .consumerMainThread(UpdateSoundBlockPacket::handle)
            .add();
            
        // Server to Client packets
        INSTANCE.messageBuilder(ResponseRegionFromChunkPacket.class, packetId++, NetworkDirection.PLAY_TO_CLIENT)
            .decoder(ResponseRegionFromChunkPacket::decode)
            .encoder(ResponseRegionFromChunkPacket::encode)
            .consumerMainThread(ResponseRegionFromChunkPacket::handle)
            .add();
    }
}