package daam.common.items;

import daam.common.blocks.BlockRegister;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.List;

public class LightStick extends Item {
    
    private int currentLightLevel = 0;
    
    public LightStick() {
        super(new Item.Properties().stacksTo(1));
    }
    
    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Player player = context.getPlayer();
        
        if (!level.isClientSide && player != null) {
            if (player.isShiftKeyDown()) {
                // Cycle through light levels
                currentLightLevel = (currentLightLevel + 1) % 16;
                player.sendSystemMessage(Component.literal("Light level set to: " + currentLightLevel).withStyle(ChatFormatting.YELLOW));
            } else {
                // Place light block at clicked position
                BlockPos placePos = pos.above();
                if (level.isEmptyBlock(placePos)) {
                    Block lightBlock = getLightBlockForLevel(currentLightLevel);
                    if (lightBlock != null) {
                        BlockState lightState = lightBlock.defaultBlockState();
                        level.setBlock(placePos, lightState, 3);
                        player.sendSystemMessage(Component.literal("Placed light block (level " + currentLightLevel + ")").withStyle(ChatFormatting.GREEN));
                    }
                } else {
                    player.sendSystemMessage(Component.literal("Cannot place light block here!").withStyle(ChatFormatting.RED));
                }
            }
        }
        
        return InteractionResult.SUCCESS;
    }
    
    private Block getLightBlockForLevel(int level) {
        return switch (level) {
            case 0 -> BlockRegister.LIGHT_BLOCK_0.get();
            case 1 -> BlockRegister.LIGHT_BLOCK_1.get();
            case 2 -> BlockRegister.LIGHT_BLOCK_2.get();
            case 3 -> BlockRegister.LIGHT_BLOCK_3.get();
            case 4 -> BlockRegister.LIGHT_BLOCK_4.get();
            case 5 -> BlockRegister.LIGHT_BLOCK_5.get();
            case 6 -> BlockRegister.LIGHT_BLOCK_6.get();
            case 7 -> BlockRegister.LIGHT_BLOCK_7.get();
            case 8 -> BlockRegister.LIGHT_BLOCK_8.get();
            case 9 -> BlockRegister.LIGHT_BLOCK_9.get();
            case 10 -> BlockRegister.LIGHT_BLOCK_10.get();
            case 11 -> BlockRegister.LIGHT_BLOCK_11.get();
            case 12 -> BlockRegister.LIGHT_BLOCK_12.get();
            case 13 -> BlockRegister.LIGHT_BLOCK_13.get();
            case 14 -> BlockRegister.LIGHT_BLOCK_14.get();
            case 15 -> BlockRegister.LIGHT_BLOCK_15.get();
            default -> null;
        };
    }
    
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.literal("").withStyle(ChatFormatting.GREEN, ChatFormatting.BOLD).append("SHIFT + RIGHT CLICK"));
        tooltip.add(Component.literal("").withStyle(ChatFormatting.DARK_GREEN, ChatFormatting.BOLD).append("CYCLE LIGHT LEVEL"));
        tooltip.add(Component.literal("").withStyle(ChatFormatting.BLACK, ChatFormatting.BOLD).append("--------------"));
        tooltip.add(Component.literal("").withStyle(ChatFormatting.GREEN, ChatFormatting.BOLD).append("RIGHT CLICK"));
        tooltip.add(Component.literal("").withStyle(ChatFormatting.DARK_GREEN, ChatFormatting.BOLD).append("PLACE LIGHT BLOCK"));
        tooltip.add(Component.literal("").withStyle(ChatFormatting.GRAY).append("Current level: " + currentLightLevel));
    }
}