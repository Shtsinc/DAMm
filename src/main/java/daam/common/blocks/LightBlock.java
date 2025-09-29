package daam.common.blocks;

import daam.client.RegionHandler;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class LightBlock extends Block {

    // Remove local hidden field - use RegionHandler.hidden instead
    
    public static final BooleanProperty HIDDEN = BooleanProperty.create("hidden");
    private final int level;
    
    protected static final VoxelShape EMPTY_SHAPE = Shapes.empty();

    public LightBlock(int level) {
        super(BlockBehaviour.Properties.of()
            .mapColor(MapColor.NONE)
            .noCollission()
            .lightLevel((state) -> level)
            .noOcclusion()
            .strength(-1.0F, 3600000.0F) // Unbreakable
            .noLootTable());
        this.level = level;
        this.registerDefaultState(this.stateDefinition.any().setValue(HIDDEN, RegionHandler.hidden));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(HIDDEN);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return isHidden() ? EMPTY_SHAPE : Shapes.block();
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return EMPTY_SHAPE;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return isHidden() ? RenderShape.INVISIBLE : RenderShape.MODEL;
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, BlockGetter reader, BlockPos pos) {
        return true;
    }

    @Override
    public float getShadeBrightness(BlockState state, BlockGetter level, BlockPos pos) {
        return 1.0F;
    }

    @Override
    public boolean useShapeForLightOcclusion(BlockState state) {
        return true;
    }
    
    private boolean isHidden() {
        return RegionHandler.hidden;
    }
}