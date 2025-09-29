package daam.common.handlers;

import daam.DAAM;
import daam.common.network.NetworkHandler;
import daam.common.network.packets.SyncCurrentRegionPacket;
import daam.common.world.DAAMWorldSavedData;
import daam.common.world.Region;
import daam.common.world.RegionChunks;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

import java.util.HashMap;
import java.util.Map;

@Mod.EventBusSubscriber(modid = DAAM.MODID)
public class PlayerRegionHandler {
    
    // Track current region for each player
    private static final Map<String, String> playerCurrentRegions = new HashMap<>();
    // Track tick counter per player to avoid issues with multiple players
    private static final Map<String, Integer> playerTickCounters = new HashMap<>();
    
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END || !(event.player instanceof ServerPlayer serverPlayer)) {
            return;
        }
        
        String playerUUID = serverPlayer.getUUID().toString();
        
        // Only check every 20 ticks (1 second) to avoid performance issues
        int tickCounter = playerTickCounters.getOrDefault(playerUUID, 0) + 1;
        playerTickCounters.put(playerUUID, tickCounter);
        if (tickCounter % 20 != 0) {
            return;
        }
        
        ServerLevel level = serverPlayer.serverLevel();
        DAAMWorldSavedData worldData = DAAMWorldSavedData.get(level);
        
        BlockPos playerPos = serverPlayer.blockPosition();
        
        // Find region at player position
        Region foundRegion = findRegionAtPosition(playerPos, worldData);
        String currentRegionUUID = playerCurrentRegions.get(playerUUID);
        String foundRegionUUID = foundRegion != null ? foundRegion.getUUID() : null;
        
        // Check if player changed region
        if (!java.util.Objects.equals(currentRegionUUID, foundRegionUUID)) {
            // Update tracking
            if (foundRegionUUID != null) {
                playerCurrentRegions.put(playerUUID, foundRegionUUID);
                DAAM.LOGGER.info("Player " + serverPlayer.getName().getString() + " entered region: " + foundRegion.getNAME());
            } else {
                playerCurrentRegions.remove(playerUUID);
                if (currentRegionUUID != null) {
                    DAAM.LOGGER.info("Player " + serverPlayer.getName().getString() + " left their region");
                }
            }
            
            // Send sync packet to client
            SyncCurrentRegionPacket packet = new SyncCurrentRegionPacket(foundRegion);
            NetworkHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> serverPlayer), packet);
        }
    }
    
    private static Region findRegionAtPosition(BlockPos pos, DAAMWorldSavedData worldData) {
        for (Map.Entry<RegionChunks, Region> entry : worldData.regions.entrySet()) {
            Region region = entry.getValue();
            AABB aabb = region.getAABB();
            
            // Check if player position is within region bounds
            if (pos.getX() >= aabb.minX && pos.getX() <= aabb.maxX &&
                pos.getY() >= aabb.minY && pos.getY() <= aabb.maxY &&
                pos.getZ() >= aabb.minZ && pos.getZ() <= aabb.maxZ) {
                return region;
            }
        }
        return null;
    }
    
    // Clean up when player disconnects
    public static void onPlayerDisconnect(ServerPlayer player) {
        String playerUUID = player.getUUID().toString();
        playerCurrentRegions.remove(playerUUID);
        playerTickCounters.remove(playerUUID);
    }
}