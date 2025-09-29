package daam.client.screens;

import daam.common.world.Region;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class GuiRegionEditor extends Screen {
    
    private final Region region;
    private EditBox nameField;
    private EditBox musicDayField;
    private EditBox musicNightField;
    private EditBox ambientDayField;
    private EditBox ambientNightField;
    private Button saveButton;
    private Button cancelButton;

    public GuiRegionEditor(Region region) {
        super(Component.literal("Region Editor"));
        this.region = region;
    }

    @Override
    protected void init() {
        super.init();
        
        int centerX = this.width / 2;
        int startY = 30;
        int fieldWidth = 200;
        int fieldHeight = 20;
        int spacing = 25;
        
        // Create text fields
        nameField = new EditBox(this.font, centerX - fieldWidth/2, startY, fieldWidth, fieldHeight, Component.literal("Region Name"));
        nameField.setValue(region.getNAME());
        this.addRenderableWidget(nameField);
        
        musicDayField = new EditBox(this.font, centerX - fieldWidth/2, startY + spacing, fieldWidth, fieldHeight, Component.literal("Day Music"));
        musicDayField.setValue(region.getMUSIC_PATH_DAY());
        this.addRenderableWidget(musicDayField);
        
        musicNightField = new EditBox(this.font, centerX - fieldWidth/2, startY + spacing * 2, fieldWidth, fieldHeight, Component.literal("Night Music"));
        musicNightField.setValue(region.getMUSIC_PATH_NIGHT());
        this.addRenderableWidget(musicNightField);
        
        ambientDayField = new EditBox(this.font, centerX - fieldWidth/2, startY + spacing * 3, fieldWidth, fieldHeight, Component.literal("Day Ambient"));
        ambientDayField.setValue(region.getAMBIENT_PATH_DAY());
        this.addRenderableWidget(ambientDayField);
        
        ambientNightField = new EditBox(this.font, centerX - fieldWidth/2, startY + spacing * 4, fieldWidth, fieldHeight, Component.literal("Night Ambient"));
        ambientNightField.setValue(region.getAMBIENT_PATH_NIGHT());
        this.addRenderableWidget(ambientNightField);
        
        // Create buttons
        saveButton = Button.builder(Component.literal("Save"), button -> {
            saveRegion();
            this.onClose();
        }).bounds(centerX - 105, startY + spacing * 6, 100, 20).build();
        this.addRenderableWidget(saveButton);
        
        cancelButton = Button.builder(Component.literal("Cancel"), button -> {
            this.onClose();
        }).bounds(centerX + 5, startY + spacing * 6, 100, 20).build();
        this.addRenderableWidget(cancelButton);
    }
    
    private void saveRegion() {
        region.setNAME(nameField.getValue());
        region.setMUSIC_PATH_DAY(musicDayField.getValue());
        region.setMUSIC_PATH_NIGHT(musicNightField.getValue());
        region.setAMBIENT_PATH_DAY(ambientDayField.getValue());
        region.setAMBIENT_PATH_NIGHT(ambientNightField.getValue());
        // TODO: Send update packet to server
    }

    @Override
    public void render(net.minecraft.client.gui.GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        this.renderBackground(graphics);
        super.render(graphics, mouseX, mouseY, delta);
        
        // Draw labels
        graphics.drawString(this.font, "Region Name:", this.width / 2 - 100, 20, 0xFFFFFF);
        graphics.drawString(this.font, "Day Music Path:", this.width / 2 - 100, 45, 0xFFFFFF);
        graphics.drawString(this.font, "Night Music Path:", this.width / 2 - 100, 70, 0xFFFFFF);
        graphics.drawString(this.font, "Day Ambient Path:", this.width / 2 - 100, 95, 0xFFFFFF);
        graphics.drawString(this.font, "Night Ambient Path:", this.width / 2 - 100, 120, 0xFFFFFF);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}