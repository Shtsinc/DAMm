package daam.client;

import daam.DAAM;
import daam.common.world.Region;

import java.io.File;
import java.util.ArrayList;

public class RegionHandler {

    public static ArrayList<String> loadedSounds = new ArrayList<>();
    public static boolean hidden = true;
    public static Region currentRegion;
    
    public static RegionSoundHandler soundHandler = new RegionSoundHandler();

    public static void loadSounds() {
        loadedSounds.clear();
        File soundsDir = DAAM.SOUNDS_DIR;
        if (soundsDir != null && soundsDir.exists()) {
            File[] files = soundsDir.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.getName().endsWith(".ogg")) {
                        loadedSounds.add(file.getName().replace(".ogg", ""));
                    }
                }
            }
        }
    }
}