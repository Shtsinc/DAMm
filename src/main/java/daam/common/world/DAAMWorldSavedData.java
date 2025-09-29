package daam.common.world;

import daam.DAAM;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Mod.EventBusSubscriber(modid = DAAM.MODID)
public class DAAMWorldSavedData extends SavedData {

    private static final String DATA_NAME = DAAM.MODID + "_data";
    
    public Map<RegionChunks, Region> regions = new ConcurrentHashMap<>();

    public DAAMWorldSavedData() {
        super();
    }

    public DAAMWorldSavedData(CompoundTag tag) {
        super();
        this.regions = readNBT(tag);
    }

    public static DAAMWorldSavedData create() {
        return new DAAMWorldSavedData();
    }

    public static DAAMWorldSavedData load(CompoundTag tag) {
        return new DAAMWorldSavedData(tag);
    }

    public static DAAMWorldSavedData get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(
            DAAMWorldSavedData::load,
            DAAMWorldSavedData::create,
            DATA_NAME
        );
    }

    @SubscribeEvent
    public static void onWorldLoadEvent(LevelEvent.Load event) {
        if (event.getLevel() instanceof ServerLevel serverLevel) {
            DAAMWorldSavedData data = get(serverLevel);
            DAAM.LOGGER.info("Loaded DAAM world data for " + serverLevel.dimension().location());
        }
    }

    @SubscribeEvent
    public void worldSave(LevelEvent.Save event) {
        if (event.getLevel() instanceof ServerLevel) {
            this.setDirty();
        }
    }

    @Override
    public CompoundTag save(CompoundTag compound) {
        return writeNBT(new HashMap<>(regions));
    }

    public static HashMap<RegionChunks, Region> readNBT(CompoundTag compound) {
        HashMap<RegionChunks, Region> regions = new HashMap<>();
        
        if (!compound.contains("regions")) {
            return regions;
        }
        
        CompoundTag regionsTag = compound.getCompound("regions");
        for (String key : regionsTag.getAllKeys()) {
            CompoundTag regionCompound = regionsTag.getCompound(key);
            Region region = new Region();
            region.deserializeNBT(regionCompound.getCompound("region"));
            
            RegionChunks regionChunks = new RegionChunks(region.getUUID(), null, region.getAABB());
            regions.put(regionChunks, region);
        }
        
        return regions;
    }

    public static CompoundTag writeNBT(HashMap<RegionChunks, Region> regions) {
        CompoundTag compound = new CompoundTag();
        CompoundTag regionsTag = new CompoundTag();
        
        int index = 0;
        for (Map.Entry<RegionChunks, Region> entry : regions.entrySet()) {
            CompoundTag regionEntry = new CompoundTag();
            regionEntry.put("region", entry.getValue().serializeNBT());
            regionsTag.put("region_" + index, regionEntry);
            index++;
        }
        
        compound.put("regions", regionsTag);
        return compound;
    }
}