package daam.common.blocks;

import daam.DAAM;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class BlockRegister {
    
    public static final DeferredRegister<Block> BLOCKS = 
        DeferredRegister.create(ForgeRegistries.BLOCKS, DAAM.MODID);
    
    public static final DeferredRegister<Item> BLOCK_ITEMS = 
        DeferredRegister.create(ForgeRegistries.ITEMS, DAAM.MODID);

    // Light blocks for each level (0-15)
    public static final RegistryObject<LightBlock> LIGHT_BLOCK_0 = registerLightBlock(0);
    public static final RegistryObject<LightBlock> LIGHT_BLOCK_1 = registerLightBlock(1);
    public static final RegistryObject<LightBlock> LIGHT_BLOCK_2 = registerLightBlock(2);
    public static final RegistryObject<LightBlock> LIGHT_BLOCK_3 = registerLightBlock(3);
    public static final RegistryObject<LightBlock> LIGHT_BLOCK_4 = registerLightBlock(4);
    public static final RegistryObject<LightBlock> LIGHT_BLOCK_5 = registerLightBlock(5);
    public static final RegistryObject<LightBlock> LIGHT_BLOCK_6 = registerLightBlock(6);
    public static final RegistryObject<LightBlock> LIGHT_BLOCK_7 = registerLightBlock(7);
    public static final RegistryObject<LightBlock> LIGHT_BLOCK_8 = registerLightBlock(8);
    public static final RegistryObject<LightBlock> LIGHT_BLOCK_9 = registerLightBlock(9);
    public static final RegistryObject<LightBlock> LIGHT_BLOCK_10 = registerLightBlock(10);
    public static final RegistryObject<LightBlock> LIGHT_BLOCK_11 = registerLightBlock(11);
    public static final RegistryObject<LightBlock> LIGHT_BLOCK_12 = registerLightBlock(12);
    public static final RegistryObject<LightBlock> LIGHT_BLOCK_13 = registerLightBlock(13);
    public static final RegistryObject<LightBlock> LIGHT_BLOCK_14 = registerLightBlock(14);
    public static final RegistryObject<LightBlock> LIGHT_BLOCK_15 = registerLightBlock(15);

    // Sound block
    public static final RegistryObject<SoundBlock> SOUND_BLOCK = registerBlock("sound_block", 
        () -> new SoundBlock());

    private static RegistryObject<LightBlock> registerLightBlock(int level) {
        return registerBlock("light_lv" + level, () -> new LightBlock(level));
    }

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, RegistryObject<T> block) {
        BLOCK_ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }
}