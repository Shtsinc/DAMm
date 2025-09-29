package daam.common.world;

import lombok.Data;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.util.INBTSerializable;

@Data
public class Region implements INBTSerializable<CompoundTag> {

    private String UUID;
    private String NAME;
    private AABB AABB;
    private String MUSIC_PATH_DAY;
    private String AMBIENT_PATH_DAY;
    private String MUSIC_PATH_NIGHT;
    private String AMBIENT_PATH_NIGHT;
    private boolean TIME_FACTOR;

    public Region() {
        this.setUUID(String.valueOf(java.util.UUID.randomUUID()));
        this.setNAME("New Region");
        this.setAABB(new AABB(0, 0, 0, 0, 0, 0));
        this.setMUSIC_PATH_DAY("");
        this.setMUSIC_PATH_NIGHT("");
        this.setAMBIENT_PATH_DAY("");
        this.setAMBIENT_PATH_NIGHT("");
        this.setTIME_FACTOR(true);
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag compound = new CompoundTag();
        compound.putString("uuid", getUUID());
        compound.putString("name", getNAME());
        
        AABB aabb = getAABB();
        compound.putDouble("miX", aabb.minX);
        compound.putDouble("miY", aabb.minY);
        compound.putDouble("miZ", aabb.minZ);
        compound.putDouble("maX", aabb.maxX);
        compound.putDouble("maY", aabb.maxY);
        compound.putDouble("maZ", aabb.maxZ);
        
        compound.putString("mPath", getMUSIC_PATH_DAY());
        compound.putString("mPath2", getMUSIC_PATH_NIGHT());
        compound.putString("aPath", getAMBIENT_PATH_DAY());
        compound.putString("aPath2", getAMBIENT_PATH_NIGHT());
        compound.putBoolean("timeF", isTIME_FACTOR());
        
        return compound;
    }

    @Override
    public void deserializeNBT(CompoundTag compound) {
        setUUID(compound.getString("uuid"));
        setNAME(compound.getString("name"));
        
        double minX = compound.getDouble("miX");
        double minY = compound.getDouble("miY");
        double minZ = compound.getDouble("miZ");
        double maxX = compound.getDouble("maX");
        double maxY = compound.getDouble("maY");
        double maxZ = compound.getDouble("maZ");
        setAABB(new AABB(minX, minY, minZ, maxX, maxY, maxZ));
        
        setMUSIC_PATH_DAY(compound.getString("mPath"));
        setMUSIC_PATH_NIGHT(compound.getString("mPath2"));
        setAMBIENT_PATH_DAY(compound.getString("aPath"));
        setAMBIENT_PATH_NIGHT(compound.getString("aPath2"));
        setTIME_FACTOR(compound.getBoolean("timeF"));
    }

    public boolean equals(Region another) {
        return this.getUUID().equals(another.getUUID());
    }
}