package daam.common.world;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.AABB;

import java.util.ArrayList;

@SuppressWarnings("unchecked")
public class RegionChunks {

    private static final String PATTERN = "%s,%s";
    private static final Gson GSON = new GsonBuilder().create();

    public String UUID;
    public ArrayList<String> chunks = new ArrayList<>();

    public RegionChunks(String uuid) {
        this.UUID = uuid;
    }

    public RegionChunks(String uuid, ServerLevel world, AABB aabb) {
        this.UUID = uuid;
        if (world != null) {
            ArrayList<LevelChunk> chunks = getChunksFromAABB(world, aabb);
            chunks.forEach(this::addChunk);
        }
    }

    public void addChunk(LevelChunk chunk) {
        chunks.add(String.format(PATTERN, chunk.getPos().x, chunk.getPos().z));
    }

    public boolean equalsWithChunk(LevelChunk chunk) {
        String chunkString = String.format(PATTERN, chunk.getPos().x, chunk.getPos().z);
        return chunks.contains(chunkString);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof RegionChunks other) {
            return other.UUID.equals(this.UUID);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return UUID.hashCode();
    }

    @Override
    public String toString() {
        return GSON.toJson(this);
    }

    public ArrayList<LevelChunk> getChunksFromAABB(ServerLevel world, AABB aabb) {
        ArrayList<LevelChunk> chunks = new ArrayList<>();
        
        if (world == null || aabb == null) {
            return chunks;
        }

        int minChunkX = (int) Math.floor(aabb.minX / 16.0);
        int maxChunkX = (int) Math.floor(aabb.maxX / 16.0);
        int minChunkZ = (int) Math.floor(aabb.minZ / 16.0);
        int maxChunkZ = (int) Math.floor(aabb.maxZ / 16.0);

        for (int x = minChunkX; x <= maxChunkX; x++) {
            for (int z = minChunkZ; z <= maxChunkZ; z++) {
                ChunkPos chunkPos = new ChunkPos(x, z);
                if (world.hasChunk(x, z)) {
                    LevelChunk chunk = world.getChunk(x, z);
                    chunks.add(chunk);
                }
            }
        }
        
        return chunks;
    }

    public boolean allChunksUnload(ServerLevel world) {
        if (world == null) return true;
        
        for (String chunkString : chunks) {
            String[] split = chunkString.split(",");
            int x = Integer.parseInt(split[0]);
            int z = Integer.parseInt(split[1]);
            if (world.hasChunk(x, z)) {
                return false;
            }
        }
        return true;
    }
}