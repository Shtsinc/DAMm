package daam.client;

import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "daam", bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class RegionCreatorHandler {

    public static boolean showGrid = false;
    public static BlockPos LEFT;
    public static BlockPos RIGHT;

    // TODO: Port region creation functionality from 1.12.2 to 1.20.1
    // This includes render events, ray tracing, and block selection
}