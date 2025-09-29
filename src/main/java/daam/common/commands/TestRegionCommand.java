package daam.common.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import daam.client.RegionHandler;
import daam.common.world.Region;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class TestRegionCommand {
    
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("daam")
            .then(Commands.literal("test")
                .executes(TestRegionCommand::testRegion))
            .then(Commands.literal("stop")
                .executes(TestRegionCommand::stopSounds)));
    }
    
    private static int testRegion(CommandContext<CommandSourceStack> context) {
        if (context.getSource().getLevel().isClientSide) {
            testRegionClient(context);
        }
        return 1;
    }
    
    @OnlyIn(Dist.CLIENT)
    private static void testRegionClient(CommandContext<CommandSourceStack> context) {
        try {
            // Create test region at player position
            BlockPos playerPos = context.getSource().getPlayerOrException().blockPosition();
            
            Region testRegion = new Region();
            testRegion.setUUID("test-region-123");
            testRegion.setNAME("Test Region");
            testRegion.setMUSIC_PATH_DAY("example_day_music");
            testRegion.setMUSIC_PATH_NIGHT("example_night_music");
            testRegion.setAMBIENT_PATH_DAY("example_day_ambient");
            testRegion.setAMBIENT_PATH_NIGHT("example_night_ambient");
            testRegion.setTIME_FACTOR(true);
            
            // Create AABB around player (10x10x10 area)
            AABB testArea = new AABB(
                playerPos.getX() - 5, playerPos.getY() - 5, playerPos.getZ() - 5,
                playerPos.getX() + 5, playerPos.getY() + 5, playerPos.getZ() + 5
            );
            testRegion.setAABB(testArea);
        
            // Set as current region for testing
            RegionHandler.setCurrentRegion(testRegion);
            
            context.getSource().sendSuccess(() -> Component.literal("Test region created and activated!"), false);
        } catch (Exception e) {
            context.getSource().sendFailure(Component.literal("Failed to create test region: " + e.getMessage()));
        }
    }
    
    private static int stopSounds(CommandContext<CommandSourceStack> context) {
        if (context.getSource().getLevel().isClientSide) {
            stopSoundsClient(context);
        }
        return 1;
    }
    
    @OnlyIn(Dist.CLIENT)
    private static void stopSoundsClient(CommandContext<CommandSourceStack> context) {
        RegionHandler.setCurrentRegion(null);
        context.getSource().sendSuccess(() -> Component.literal("All region sounds stopped!"), false);
    }
}