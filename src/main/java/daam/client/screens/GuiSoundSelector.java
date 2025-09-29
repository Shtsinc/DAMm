package daam.client.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import daam.client.RegionHandler;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.io.File;
import java.util.List;
import java.util.function.Consumer;

public class GuiSoundSelector extends Screen {
    
    private static final ResourceLocation BACKGROUND_TEXTURE = 
        new ResourceLocation("minecraft", "textures/gui/options_background.png");
    
    private final Consumer<String> onSoundSelected;
    private final String title;
    private List<String> availableSounds;
    private int selectedIndex = -1;
    private int scrollOffset = 0;
    private static final int SOUNDS_PER_PAGE = 10;
    
    public GuiSoundSelector(String title, Consumer<String> onSoundSelected) {
        super(Component.literal(title));
        this.title = title;
        this.onSoundSelected = onSoundSelected;
        this.availableSounds = RegionHandler.loadedSounds;
    }
    
    @Override
    protected void init() {
        super.init();
        
        // Refresh sound list
        RegionHandler.loadSounds();
        this.availableSounds = RegionHandler.loadedSounds;
        
        int centerX = this.width / 2;
        int centerY = this.height / 2;
        
        // Cancel button
        this.addRenderableWidget(Button.builder(Component.literal("Cancel"), 
            (button) -> this.onClose())
            .bounds(centerX - 100, centerY + 100, 90, 20)
            .build());
            
        // Select button
        this.addRenderableWidget(Button.builder(Component.literal("Select"), 
            (button) -> this.selectSound())
            .bounds(centerX + 10, centerY + 100, 90, 20)
            .build());
            
        // Scroll buttons
        this.addRenderableWidget(Button.builder(Component.literal("↑"), 
            (button) -> this.scrollUp())
            .bounds(centerX + 120, centerY - 60, 20, 20)
            .build());
            
        this.addRenderableWidget(Button.builder(Component.literal("↓"), 
            (button) -> this.scrollDown())
            .bounds(centerX + 120, centerY + 60, 20, 20)
            .build());
            
        // Refresh button
        this.addRenderableWidget(Button.builder(Component.literal("Refresh"), 
            (button) -> this.refreshSounds())
            .bounds(centerX - 140, centerY + 100, 60, 20)
            .build());
    }
    
    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        // Render background
        this.renderBackground(guiGraphics);
        
        int centerX = this.width / 2;
        int centerY = this.height / 2;
        
        // Title
        guiGraphics.drawCenteredString(this.font, this.title, centerX, centerY - 120, 0xFFFFFF);
        
        // Sound count info
        String countText = "Available sounds: " + availableSounds.size();
        guiGraphics.drawCenteredString(this.font, countText, centerX, centerY - 100, 0xAAAAAA);
        
        // Sound list background
        guiGraphics.fill(centerX - 150, centerY - 80, centerX + 110, centerY + 80, 0x80000000);
        
        // Render sound list
        renderSoundList(guiGraphics, centerX, centerY, mouseX, mouseY);
        
        // Instructions
        String instruction = availableSounds.isEmpty() ? 
            "No OGG files found in sounds folder!" : 
            "Click on a sound to select it";
        guiGraphics.drawCenteredString(this.font, instruction, centerX, centerY + 85, 0xAAAA00);
        
        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }
    
    private void renderSoundList(GuiGraphics guiGraphics, int centerX, int centerY, int mouseX, int mouseY) {
        int startY = centerY - 70;
        int maxDisplayed = Math.min(SOUNDS_PER_PAGE, availableSounds.size() - scrollOffset);
        
        for (int i = 0; i < maxDisplayed; i++) {
            int index = i + scrollOffset;
            if (index >= availableSounds.size()) break;
            
            String soundName = availableSounds.get(index);
            int y = startY + (i * 15);
            
            // Highlight selection
            boolean isHovered = mouseX >= centerX - 145 && mouseX <= centerX + 105 && 
                               mouseY >= y - 2 && mouseY <= y + 12;
            boolean isSelected = index == selectedIndex;
            
            if (isSelected) {
                guiGraphics.fill(centerX - 145, y - 2, centerX + 105, y + 12, 0x80FFFF00);
            } else if (isHovered) {
                guiGraphics.fill(centerX - 145, y - 2, centerX + 105, y + 12, 0x40FFFFFF);
            }
            
            // Truncate long names
            String displayName = soundName;
            if (displayName.length() > 30) {
                displayName = displayName.substring(0, 27) + "...";
            }
            
            // Draw sound name
            int color = isSelected ? 0xFFFF00 : (isHovered ? 0xFFFFFF : 0xCCCCCC);
            guiGraphics.drawString(this.font, displayName, centerX - 140, y, color);
        }
        
        // Scroll indicators
        if (scrollOffset > 0) {
            guiGraphics.drawString(this.font, "...", centerX - 140, startY - 15, 0x888888);
        }
        if (scrollOffset + SOUNDS_PER_PAGE < availableSounds.size()) {
            guiGraphics.drawString(this.font, "...", centerX - 140, startY + (SOUNDS_PER_PAGE * 15), 0x888888);
        }
    }
    
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) { // Left click
            int centerX = this.width / 2;
            int centerY = this.height / 2;
            int startY = centerY - 70;
            
            // Check if clicked on sound list
            if (mouseX >= centerX - 145 && mouseX <= centerX + 105) {
                for (int i = 0; i < Math.min(SOUNDS_PER_PAGE, availableSounds.size() - scrollOffset); i++) {
                    int y = startY + (i * 15);
                    if (mouseY >= y - 2 && mouseY <= y + 12) {
                        selectedIndex = i + scrollOffset;
                        return true;
                    }
                }
            }
        }
        
        return super.mouseClicked(mouseX, mouseY, button);
    }
    
    private void scrollUp() {
        if (scrollOffset > 0) {
            scrollOffset--;
        }
    }
    
    private void scrollDown() {
        if (scrollOffset + SOUNDS_PER_PAGE < availableSounds.size()) {
            scrollOffset++;
        }
    }
    
    private void refreshSounds() {
        RegionHandler.loadSounds();
        this.availableSounds = RegionHandler.loadedSounds;
        selectedIndex = -1;
        scrollOffset = 0;
    }
    
    private void selectSound() {
        if (selectedIndex >= 0 && selectedIndex < availableSounds.size()) {
            String selectedSound = availableSounds.get(selectedIndex);
            onSoundSelected.accept(selectedSound);
            this.onClose();
        }
    }
    
    @Override
    public boolean isPauseScreen() {
        return false;
    }
}