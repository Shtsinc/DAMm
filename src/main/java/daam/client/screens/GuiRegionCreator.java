package daam.client.screens;

import daam.DAAM;
import daam.common.network.NetworkHandler;
import daam.common.network.packets.client.CreateRegionPacket;
import daam.common.world.Region;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.AABB;

import java.util.UUID;

public class GuiRegionCreator extends Screen {
    
    private final AABB regionBounds;
    private final daam.common.items.RegionWand regionWand;
    private EditBox nameField;
    private EditBox musicDayField;
    private EditBox musicNightField;
    private EditBox ambientDayField;
    private EditBox ambientNightField;
    private Button createButton;
    private Button cancelButton;
    private int startY = 30;
    private int spacing = 25;

    public GuiRegionCreator(AABB regionBounds, daam.common.items.RegionWand regionWand) {
        super(Component.literal("Create New Region"));
        this.regionBounds = regionBounds;
        this.regionWand = regionWand;
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
        nameField.setValue("New Region");
        this.addRenderableWidget(nameField);
        
        musicDayField = new EditBox(this.font, centerX - fieldWidth/2, startY + spacing, fieldWidth, fieldHeight, Component.literal("Day Music"));
        musicDayField.setValue("");
        this.addRenderableWidget(musicDayField);
        
        musicNightField = new EditBox(this.font, centerX - fieldWidth/2, startY + spacing * 2, fieldWidth, fieldHeight, Component.literal("Night Music"));
        musicNightField.setValue("");
        this.addRenderableWidget(musicNightField);
        
        ambientDayField = new EditBox(this.font, centerX - fieldWidth/2, startY + spacing * 3, fieldWidth, fieldHeight, Component.literal("Day Ambient"));
        ambientDayField.setValue("");
        this.addRenderableWidget(ambientDayField);
        
        ambientNightField = new EditBox(this.font, centerX - fieldWidth/2, startY + spacing * 4, fieldWidth, fieldHeight, Component.literal("Night Ambient"));
        ambientNightField.setValue("");
        this.addRenderableWidget(ambientNightField);
        
        // Add sound selector buttons
        addSoundSelectorButton("🎵", musicDayField, centerX + fieldWidth/2 + 5, startY + spacing);
        addSoundSelectorButton("🎵", musicNightField, centerX + fieldWidth/2 + 5, startY + spacing * 2);
        addSoundSelectorButton("🔊", ambientDayField, centerX + fieldWidth/2 + 5, startY + spacing * 3);
        addSoundSelectorButton("🔊", ambientNightField, centerX + fieldWidth/2 + 5, startY + spacing * 4);
        
        // Create buttons
        createButton = Button.builder(Component.literal("Create Region"), button -> {
            createRegion();
            this.onClose();
        }).bounds(centerX - 105, startY + spacing * 6, 100, 20).build();
        this.addRenderableWidget(createButton);
        
        cancelButton = Button.builder(Component.literal("Cancel"), button -> {
            this.onClose();
        }).bounds(centerX + 5, startY + spacing * 6, 100, 20).build();
        this.addRenderableWidget(cancelButton);
    }
    
    private void addSoundSelectorButton(String label, EditBox targetField, int x, int y) {
        String fieldType = label.equals("🎵") ? "Music" : "Ambient";
        Button selectorButton = Button.builder(Component.literal("♪"), button -> {
            GuiSoundSelector soundSelector = new GuiSoundSelector(
                "Select " + fieldType + " Sound", 
                (selectedSound) -> targetField.setValue(selectedSound)
            );
            this.minecraft.setScreen(soundSelector);
        }).bounds(x, y, 20, 20).build();
        this.addRenderableWidget(selectorButton);
    }
    
    private void createRegion() {
        // Create new region
        Region region = new Region();
        region.setUUID(UUID.randomUUID().toString());
        region.setNAME(nameField.getValue() != null ? nameField.getValue() : "New Region");
        region.setMUSIC_PATH_DAY(musicDayField.getValue() != null ? musicDayField.getValue() : "");
        region.setMUSIC_PATH_NIGHT(musicNightField.getValue() != null ? musicNightField.getValue() : "");
        region.setAMBIENT_PATH_DAY(ambientDayField.getValue() != null ? ambientDayField.getValue() : "");
        region.setAMBIENT_PATH_NIGHT(ambientNightField.getValue() != null ? ambientNightField.getValue() : "");
        region.setAABB(regionBounds);
        region.setTIME_FACTOR(true); // Set default time factor
        
        DAAM.LOGGER.info("Creating region: " + region.getNAME() + " with bounds: " + regionBounds);
        
        try {
            // Send to server
            NetworkHandler.INSTANCE.sendToServer(new CreateRegionPacket(region));
            DAAM.LOGGER.info("Region packet sent successfully!");
            
            // Reset positions in RegionWand after successful creation
            if (regionWand != null) {
                regionWand.resetPositions();
                DAAM.LOGGER.info("RegionWand positions reset after successful region creation");
            }
        } catch (Exception e) {
            DAAM.LOGGER.error("Failed to send region packet: " + e.getMessage(), e);
        }
    }

    @Override
    public void render(net.minecraft.client.gui.GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        this.renderBackground(graphics);
        super.render(graphics, mouseX, mouseY, delta);
        
        // Draw title
        graphics.drawCenteredString(this.font, "Create New Region", this.width / 2, 10, 0xFFFFFF);
        
        // Draw labels
        graphics.drawString(this.font, "Region Name:", this.width / 2 - 100, 20, 0xFFFFFF);
        graphics.drawString(this.font, "Day Music Path:", this.width / 2 - 100, 45, 0xFFFFFF);
        graphics.drawString(this.font, "Night Music Path:", this.width / 2 - 100, 70, 0xFFFFFF);
        graphics.drawString(this.font, "Day Ambient Path:", this.width / 2 - 100, 95, 0xFFFFFF);
        graphics.drawString(this.font, "Night Ambient Path:", this.width / 2 - 100, 120, 0xFFFFFF);
        
        // Draw region bounds info
        String boundsInfo = String.format("Region: %.1f,%.1f,%.1f to %.1f,%.1f,%.1f", 
            regionBounds.minX, regionBounds.minY, regionBounds.minZ,
            regionBounds.maxX, regionBounds.maxY, regionBounds.maxZ);
        graphics.drawCenteredString(this.font, boundsInfo, this.width / 2, startY + spacing * 5, 0xAAAAAA);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
