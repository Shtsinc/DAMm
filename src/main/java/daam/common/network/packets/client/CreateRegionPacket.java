package daam.common.network.packets.client;

import daam.common.network.NetworkHandler;
import daam.common.network.packets.SimplePacket;
import daam.common.world.DAAMWorldSavedData;
import daam.common.world.Region;
import daam.common.world.RegionChunks;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.PacketDistributor;

import java.util.function.Supplier;

public class CreateRegionPacket extends SimplePacket {

    private CompoundTag regionData;

    public CreateRegionPacket() {
    }

    public CreateRegionPacket(Region region) {
        this.regionData = region.serializeNBT();
    }

    public static void encode(CreateRegionPacket packet, FriendlyByteBuf buf) {
        buf.writeNbt(packet.regionData);
    }

    public static CreateRegionPacket decode(FriendlyByteBuf buf) {
        CreateRegionPacket packet = new CreateRegionPacket();
        packet.regionData = buf.readNbt();
        return packet;
    }

    public static void handle(CreateRegionPacket packet, Supplier<net.minecraftforge.network.NetworkEvent.Context> contextSupplier) {
        net.minecraftforge.network.NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player != null) {
                DAAMWorldSavedData savedData = DAAMWorldSavedData.get(player.serverLevel());
                Region region = new Region();
                region.deserializeNBT(packet.regionData);

                savedData.regions.put(new RegionChunks(region.getUUID(), player.serverLevel(), region.getAABB()), region);
                savedData.setDirty();

                // Sync to all players
                NetworkHandler.INSTANCE.send(PacketDistributor.ALL.noArg(), new SyncRegionPacket(region));
            }
        });
        context.setPacketHandled(true);
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
        encode(this, buf);
    }

}