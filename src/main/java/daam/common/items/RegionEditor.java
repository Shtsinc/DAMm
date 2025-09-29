package daam.common.items;

import daam.DAAM;
import daam.client.RegionHandler;
import daam.client.screens.GuiRegionEditor;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class RegionEditor extends Item {

    public RegionEditor() {
        super(new Item.Properties()
            .stacksTo(1)
            .durability(-1));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (level.isClientSide) {
            if (player.isShiftKeyDown()) {
                RegionHandler.hidden = !RegionHandler.hidden;
                player.sendSystemMessage(Component.literal("Region visibility: " + (!RegionHandler.hidden ? "SHOWN" : "HIDDEN")).withStyle(ChatFormatting.GOLD));
            } else {
                if (RegionHandler.currentRegion != null) {
                    Minecraft.getInstance().setScreen(new GuiRegionEditor(RegionHandler.currentRegion));
                } else {
                    // Create a dummy region for testing
                    daam.common.world.Region dummyRegion = new daam.common.world.Region();
                    dummyRegion.setUUID("temp-region");
                    dummyRegion.setNAME("Temporary Region");
                    dummyRegion.setMUSIC_PATH_DAY("example_day_music");
                    dummyRegion.setMUSIC_PATH_NIGHT("example_night_music");
                    dummyRegion.setAMBIENT_PATH_DAY("example_day_ambient");
                    dummyRegion.setAMBIENT_PATH_NIGHT("example_night_ambient");
                    
                    Minecraft.getInstance().setScreen(new GuiRegionEditor(dummyRegion));
                    player.sendSystemMessage(Component.literal("Opened region editor (demo mode - you're not in a region)").withStyle(ChatFormatting.YELLOW));
                }
            }
        }
        return super.use(level, player, hand);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.literal("").withStyle(ChatFormatting.GREEN, ChatFormatting.BOLD).append("SHIFT + RIGHT CLICK"));
        tooltip.add(Component.literal("").withStyle(ChatFormatting.DARK_GREEN, ChatFormatting.BOLD).append("HANDLE REGION VISIBILITY"));
        tooltip.add(Component.literal("").withStyle(ChatFormatting.BLACK, ChatFormatting.BOLD).append("--------------"));
        tooltip.add(Component.literal("").withStyle(ChatFormatting.GREEN, ChatFormatting.BOLD).append("RIGHT CLICK"));
        tooltip.add(Component.literal("").withStyle(ChatFormatting.DARK_GREEN, ChatFormatting.BOLD).append("OPEN REGION EDITOR"));
        tooltip.add(Component.literal("").withStyle(ChatFormatting.DARK_GREEN, ChatFormatting.BOLD).append("IF YOU ARE IN THE REGION"));
    }
}