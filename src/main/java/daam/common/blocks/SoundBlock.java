package daam.common.blocks;

import daam.client.DrawUtils;
import daam.client.screens.GuiSoundEditor;
import daam.common.items.SoundStick;
import daam.common.tile.SoundBlockTileEntity;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

public class SoundBlock extends BaseEntityBlock {

    @Setter
    @Getter
    private static boolean hidden = true;
    
    public static final BooleanProperty HIDDEN = BooleanProperty.create("hidden");
    protected static final VoxelShape EMPTY_SHAPE = Shapes.empty();

    public SoundBlock() {
        super(BlockBehaviour.Properties.of()
            .mapColor(MapColor.NONE)
            .noCollission()
            .noOcclusion()
            .strength(-1.0F, 3600000.0F) // Unbreakable
            .noLootTable());
        this.registerDefaultState(this.stateDefinition.any().setValue(HIDDEN, hidden));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<net.minecraft.world.level.block.Block, BlockState> builder) {
        builder.add(HIDDEN);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, 
                               InteractionHand hand, BlockHitResult hit) {
        if (level.isClientSide) {
            boolean flag = player.getMainHandItem().getItem() instanceof SoundStick;
            if (flag) {
                BlockEntity blockEntity = level.getBlockEntity(pos);
                if (blockEntity instanceof SoundBlockTileEntity soundTile) {
                    DrawUtils.open(new GuiSoundEditor(soundTile));
                }
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new SoundBlockTileEntity(null, pos, state); // TODO: Add BlockEntityType
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
}