package daam.common.sounds;

import daam.client.RegionSoundHandler;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;

public class DynamicSound extends AbstractTickableSoundInstance {

    public final boolean flag;

    private boolean stop = false;

    private boolean finallyStop = false;

    public DynamicSound(String soundIn, boolean flag) {
        super(createSoundEvent(soundIn), SoundSource.MASTER, SoundInstance.createUnseededRandom());
        this.volume = 0.01f;
        this.flag = flag;
        this.looping = true;
    }
    
    private static SoundEvent createSoundEvent(String soundPath) {
        if (soundPath == null || soundPath.isEmpty()) {
            // Return a default silent sound event if no path provided
            return SoundEvent.createFixedRangeEvent(new ResourceLocation("minecraft", "intentionally_empty"), 16.0f);
        }
        
        // Create proper DAAM sound resource location
        ResourceLocation soundLocation = new ResourceLocation("daam", soundPath);
        return SoundEvent.createFixedRangeEvent(soundLocation, 16.0f);
    }

    @Override
    public boolean canPlaySound() {
        return !this.finallyStop;
    }

    @Override
    public boolean canStartSilent() {
        return true;
    }

    public void setVolume(float volume) {
        this.volume = volume;
    }

    @Override
    public void tick() {
        Minecraft mc = Minecraft.getInstance();

        if (mc.player != null) {
            this.x = mc.player.getX();
            this.y = mc.player.getY();
            this.z = mc.player.getZ();
        }

        float limit = 1F;
        if (flag) {
            limit = RegionSoundHandler.musicVolume;
        } else {
            limit = RegionSoundHandler.ambientVolume;
        }

        if (stop) {
            if (volume - 0.05f <= 0) {
                this.finallyStop = true;
            }
            this.volume -= 0.05f;
        } else {
            if (volume + 0.05f >= limit) {
                this.volume = limit;
            }
            this.volume += 0.05f;
        }
    }
    
    public void setStop(boolean stop) {
        this.stop = stop;
    }
}