package daam.common.items;

import daam.DAAM;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemRegister {
    
    public static final DeferredRegister<Item> ITEMS = 
        DeferredRegister.create(ForgeRegistries.ITEMS, DAAM.MODID);

    public static final RegistryObject<Item> REGION_EDITOR = ITEMS.register("region_editor", 
        () -> new RegionEditor());
    
    public static final RegistryObject<Item> REGION_CREATOR = ITEMS.register("region_creator", 
        () -> new RegionWand());
    
    public static final RegistryObject<Item> LIGHT_STICK = ITEMS.register("light_stick", 
        () -> new LightStick());
    
    public static final RegistryObject<Item> SOUND_STICK = ITEMS.register("sound_stick", 
        () -> new SoundStick());
    
    public static final RegistryObject<Item> VOLUME_EDITOR = ITEMS.register("volume_editor", 
        () -> new VolumeEditor());
}