package daam.client.screens;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class GuiSoundEditor extends Screen {
    
    private final Object soundTile; // TODO: Replace with proper SoundBlockTileEntity when ported

    public GuiSoundEditor() {
        this(null);
    }
    
    public GuiSoundEditor(Object soundTile) {
        super(Component.literal("Sound Block Editor"));
        this.soundTile = soundTile;
    }

    @Override
    protected void init() {
        super.init();
        // TODO: Port GUI from 1.12.2 to 1.20.1
    }
}
