package daam.common.items;

import daam.client.screens.GuiRegionCreator;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class RegionWand extends Item {
    
    private BlockPos firstPos = null;
    private BlockPos secondPos = null;

    public RegionWand() {
        super(new Item.Properties().stacksTo(1));
    }
    
    public void resetPositions() {
        firstPos = null;
        secondPos = null;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if (context.getLevel().isClientSide) {
            Player player = context.getPlayer();
            BlockPos pos = context.getClickedPos();
            
            if (player != null && player.isShiftKeyDown()) {
                // Second position (shift + right click)
                secondPos = pos;
                player.sendSystemMessage(Component.literal("Second position set: " + pos.getX() + ", " + pos.getY() + ", " + pos.getZ()).withStyle(ChatFormatting.GREEN));
                
                if (firstPos != null) {
                    player.sendSystemMessage(Component.literal("Region selection complete! Use regular right-click to create region.").withStyle(ChatFormatting.GOLD));
                }
            } else {
                // First position (regular right click)
                firstPos = pos;
                player.sendSystemMessage(Component.literal("First position set: " + pos.getX() + ", " + pos.getY() + ", " + pos.getZ()).withStyle(ChatFormatting.GREEN));
            }
        }
        
        return InteractionResult.SUCCESS;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (level.isClientSide && firstPos != null && secondPos != null) {
            // Create AABB from selected positions
            int minX = Math.min(firstPos.getX(), secondPos.getX());
            int minY = Math.min(firstPos.getY(), secondPos.getY());
            int minZ = Math.min(firstPos.getZ(), secondPos.getZ());
            int maxX = Math.max(firstPos.getX(), secondPos.getX()) + 1;
            int maxY = Math.max(firstPos.getY(), secondPos.getY()) + 1;
            int maxZ = Math.max(firstPos.getZ(), secondPos.getZ()) + 1;
            
            AABB regionBounds = new AABB(minX, minY, minZ, maxX, maxY, maxZ);
            
            // Open region creator GUI
            GuiRegionCreator gui = new GuiRegionCreator(regionBounds, this);
            Minecraft.getInstance().setScreen(gui);
        } else if (level.isClientSide) {
            player.sendSystemMessage(Component.literal("You need to select two positions first! Right-click and Shift+Right-click on blocks.").withStyle(ChatFormatting.RED));
        }
        
        return super.use(level, player, hand);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.literal("").withStyle(ChatFormatting.GREEN, ChatFormatting.BOLD).append("RIGHT CLICK"));
        tooltip.add(Component.literal("").withStyle(ChatFormatting.DARK_GREEN, ChatFormatting.BOLD).append("SELECT FIRST POSITION"));
        tooltip.add(Component.literal("").withStyle(ChatFormatting.BLACK, ChatFormatting.BOLD).append("--------------"));
        tooltip.add(Component.literal("").withStyle(ChatFormatting.GREEN, ChatFormatting.BOLD).append("SHIFT + RIGHT CLICK"));
        tooltip.add(Component.literal("").withStyle(ChatFormatting.DARK_GREEN, ChatFormatting.BOLD).append("SELECT SECOND POSITION"));
        tooltip.add(Component.literal("").withStyle(ChatFormatting.BLACK, ChatFormatting.BOLD).append("--------------"));
        tooltip.add(Component.literal("").withStyle(ChatFormatting.YELLOW, ChatFormatting.BOLD).append("RIGHT CLICK IN AIR"));
        tooltip.add(Component.literal("").withStyle(ChatFormatting.GOLD, ChatFormatting.BOLD).append("CREATE REGION"));
    }
}