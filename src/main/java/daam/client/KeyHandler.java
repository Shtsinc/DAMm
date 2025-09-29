package daam.client;

import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(modid = "daam", bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class KeyHandler {

    private static final String CATEGORY = "key.categories.daam";

    public static final KeyMapping musicBinding = new KeyMapping(
        "key.daam.music_mute", 
        GLFW.GLFW_KEY_LEFT_BRACKET, 
        CATEGORY
    );
    
    public static final KeyMapping ambientBinding = new KeyMapping(
        "key.daam.ambient_mute", 
        GLFW.GLFW_KEY_RIGHT_BRACKET, 
        CATEGORY
    );

    @SubscribeEvent
    public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(musicBinding);
        event.register(ambientBinding);
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        
        // Check key presses
        while (musicBinding.consumeClick()) {
            RegionSoundHandler.musicMute = !RegionSoundHandler.musicMute;
            RegionSoundHandler handler = RegionHandler.soundHandler;
            if (handler != null && handler.currentRegion != null) {
                handler.switchRegion(handler.currentRegion);
            }
        }
        
        while (ambientBinding.consumeClick()) {
            RegionSoundHandler.ambientMute = !RegionSoundHandler.ambientMute;
            RegionSoundHandler handler = RegionHandler.soundHandler;
            if (handler != null && handler.currentRegion != null) {
                handler.switchRegion(handler.currentRegion);
            }
        }
    }
}