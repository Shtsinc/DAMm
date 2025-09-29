package daam.client.screens;

import daam.common.world.Region;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class GuiRegionEditor extends Screen {
    
    private final Region region;

    public GuiRegionEditor(Region region) {
        super(Component.literal("Region Editor"));
        this.region = region;
    }

    @Override
    protected void init() {
        super.init();
        // TODO: Implement GUI elements for region editing
    }
}