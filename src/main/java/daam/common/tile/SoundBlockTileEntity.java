package daam.common.tile;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class SoundBlockTileEntity extends BlockEntity {

    public SoundBlockTileEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        // TODO: Port tile entity data saving
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        // TODO: Port tile entity data loading
    }

    // TODO: Port ITickable functionality if needed
}