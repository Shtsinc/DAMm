package daam.common.network.packets;

import daam.client.RegionHandler;
import daam.common.world.Region;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SyncCurrentRegionPacket extends SimplePacket {
    
    private Region region;
    private boolean hasRegion;
    
    public SyncCurrentRegionPacket() {
        this.hasRegion = false;
        this.region = null;
    }
    
    public SyncCurrentRegionPacket(Region region) {
        this.region = region;
        this.hasRegion = region != null;
    }
    
    @Override
    public void encode(FriendlyByteBuf buffer) {
        buffer.writeBoolean(hasRegion);
        if (hasRegion && region != null) {
            // Encode region data
            buffer.writeUtf(region.getUUID());
            buffer.writeUtf(region.getNAME());
            buffer.writeUtf(region.getMUSIC_PATH_DAY());
            buffer.writeUtf(region.getMUSIC_PATH_NIGHT());
            buffer.writeUtf(region.getAMBIENT_PATH_DAY());
            buffer.writeUtf(region.getAMBIENT_PATH_NIGHT());
            buffer.writeBoolean(region.isTIME_FACTOR());
            
            // Encode AABB
            var aabb = region.getAABB();
            buffer.writeDouble(aabb.minX);
            buffer.writeDouble(aabb.minY);
            buffer.writeDouble(aabb.minZ);
            buffer.writeDouble(aabb.maxX);
            buffer.writeDouble(aabb.maxY);
            buffer.writeDouble(aabb.maxZ);
        }
    }
    
    public static SyncCurrentRegionPacket decode(FriendlyByteBuf buffer) {
        boolean hasRegion = buffer.readBoolean();
        
        if (!hasRegion) {
            return new SyncCurrentRegionPacket();
        }
        
        Region region = new Region();
        region.setUUID(buffer.readUtf());
        region.setNAME(buffer.readUtf());
        region.setMUSIC_PATH_DAY(buffer.readUtf());
        region.setMUSIC_PATH_NIGHT(buffer.readUtf());
        region.setAMBIENT_PATH_DAY(buffer.readUtf());
        region.setAMBIENT_PATH_NIGHT(buffer.readUtf());
        region.setTIME_FACTOR(buffer.readBoolean());
        
        // Decode AABB
        double minX = buffer.readDouble();
        double minY = buffer.readDouble();
        double minZ = buffer.readDouble();
        double maxX = buffer.readDouble();
        double maxY = buffer.readDouble();
        double maxZ = buffer.readDouble();
        region.setAABB(new net.minecraft.world.phys.AABB(minX, minY, minZ, maxX, maxY, maxZ));
        
        return new SyncCurrentRegionPacket(region);
    }
    
    @Override
    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            // Handle on client side only
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
                if (hasRegion && region != null) {
                    RegionHandler.setCurrentRegion(region);
                } else {
                    RegionHandler.setCurrentRegion(null);
                }
            });
        });
        ctx.get().setPacketHandled(true);
    }
}