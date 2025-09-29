package daam.client;

import daam.DAAM;
import daam.common.world.DAAMWorldSavedData;
import daam.common.world.Region;
import daam.common.world.RegionChunks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

public class RegionHandler {

    public static ArrayList<String> loadedSounds = new ArrayList<>();
    public static boolean hidden = true;
    public static Region currentRegion;
    
    public static RegionSoundHandler soundHandler = new RegionSoundHandler();
    
    private static int tickCounter = 0;

    public static void loadSounds() {
        loadedSounds.clear();
        File soundsDir = DAAM.SOUNDS_DIR;
        if (soundsDir != null && soundsDir.exists()) {
            File[] files = soundsDir.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.getName().endsWith(".ogg")) {
                        loadedSounds.add(file.getName().replace(".ogg", ""));
                    }
                }
            }
        }
        DAAM.LOGGER.info("Loaded " + loadedSounds.size() + " sound files: " + loadedSounds);
    }
    
    /**
     * Called every client tick to check if player is in a region
     */
    public static void clientTick() {
        tickCounter++;
        
        // Only check every 20 ticks (1 second) to avoid performance issues
        if (tickCounter % 20 != 0) {
            return;
        }
        
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null || mc.player == null) {
            return;
        }
        
        Player player = mc.player;
        BlockPos playerPos = player.blockPosition();
        
        // Check if player is in any region
        Region foundRegion = findRegionAtPosition(playerPos, mc.level);
        
        if (foundRegion != currentRegion) {
            if (currentRegion != null && foundRegion == null) {
                // Left region - stop all sounds
                DAAM.LOGGER.info("Player left region: " + currentRegion.getNAME());
                soundHandler.stopAll();
                currentRegion = null;
            } else if (foundRegion != null) {
                // Entered new region
                DAAM.LOGGER.info("Player entered region: " + foundRegion.getNAME());
                currentRegion = foundRegion;
                soundHandler.tick(currentRegion);
            }
        } else if (currentRegion != null) {
            // Still in same region - continue playing sounds
            soundHandler.tick(currentRegion);
        }
    }
    
    /**
     * Find region at given position
     */
    private static Region findRegionAtPosition(BlockPos pos, ClientLevel level) {
        // TODO: We need to get regions from server
        // For now, we'll use a simple approach - check if we have any local regions
        
        // This is a placeholder - in a full implementation, we would either:
        // 1. Request region data from server
        // 2. Sync regions to client
        // 3. Use a packet system to get current region
        
        return null; // Temporary - will be implemented when server-client sync is added
    }
    
    /**
     * Set current region (called from packets or other sources)
     */
    public static void setCurrentRegion(Region region) {
        if (region != currentRegion) {
            if (currentRegion != null) {
                DAAM.LOGGER.info("Switching from region '" + currentRegion.getNAME() + "' to '" + 
                    (region != null ? region.getNAME() : "none") + "'");
            } else {
                DAAM.LOGGER.info("Entering region: " + (region != null ? region.getNAME() : "none"));
            }
            
            currentRegion = region;
            
            if (region != null) {
                soundHandler.tick(region);
            } else {
                soundHandler.stopAll();
            }
        }
    }
}