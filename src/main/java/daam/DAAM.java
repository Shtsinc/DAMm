package daam;

import daam.client.RegionHandler;
import daam.common.blocks.BlockRegister;
import daam.common.items.ItemRegister;
import daam.common.network.NetworkHandler;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Mod(DAAM.MODID)
public class DAAM {

    public static final String MODID = "daam";
    public static final String NAME = "Dynamic Ambience and Music";
    public static final String VERSION = "2.0.0";

    public static final Logger LOGGER = LogManager.getLogger(MODID);
    public static final NetworkHandler NETWORK = new NetworkHandler();
    
    // Creative tabs will be handled via BuildCreativeModeTabContentsEvent
    
    public static File DAAM_DIR;
    public static File SOUNDS_DIR;
    public static File SOUND_JSON;

    public DAAM() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        
        // Register the setup method for modloading
        modEventBus.addListener(this::constructMod);
        modEventBus.addListener(this::setup);
        modEventBus.addListener(this::clientSetup);
        
        // Register deferred registers
        ItemRegister.ITEMS.register(modEventBus);
        BlockRegister.BLOCKS.register(modEventBus);
        BlockRegister.BLOCK_ITEMS.register(modEventBus);
        
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void constructMod(FMLConstructModEvent event) {
        // Get game directory
        String gameDir = FMLLoader.getGamePath().toString();
        
        DAAM_DIR = new File(gameDir, "DynamicAmbienceAndMusic");
        SOUNDS_DIR = new File(Paths.get(DAAM_DIR.getPath(), "assets/daam/sounds").toUri());
        SOUND_JSON = new File(Paths.get(DAAM_DIR.getPath(), "assets/daam/sounds.json").toUri());

        if (!DAAM_DIR.exists()) {
            DAAM_DIR.mkdirs();
            try {
                SOUNDS_DIR.mkdirs();
                SOUND_JSON.createNewFile();

                String jsonContent = "{\\n\\n}";
                Files.write(SOUND_JSON.toPath(), jsonContent.getBytes());

                LOGGER.info("The file was successfully created and completed: " + SOUND_JSON);
            } catch (IOException e) {
                LOGGER.error("An error has occurred: " + e.getMessage());
                e.printStackTrace();
            }
            LOGGER.info("Created DynamicAmbienceAndMusic folder, this is necessary to load sounds.");
        }

        // Load sounds on client side only
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            RegionHandler.loadSounds();
        });
    }

    private void setup(final FMLCommonSetupEvent event) {
        LOGGER.info("         ,.  '             ,.-·.          ,. -  .,                              ,.,   '          ,-·-.          ,'´¨;    ");
        LOGGER.info("       /   ';\\\\            /    ;'\\\\'      ,' ,. -  .,  `' ·,                     ;´   '· .,         ';   ';\\\\      ,'´  ,':\\\\'  ");
        LOGGER.info("     ,'   ,'::'\\\\          ;    ;:::\\\\     '; '·~;:::::'`,   ';\\\\                .´  .-,    ';\\\\        ;   ';:\\\\   .'   ,'´::'\\\\' ");
        LOGGER.info("    ,'    ;:::';'        ';    ;::::;'     ;   ,':\\\\::;:´  .·´::\\\\'             /   /:\\\\:';   ;:'\\\\'      '\\\\   ';::;'´  ,'´::::;'  ");
        LOGGER.info("    ';   ,':::;'          ;   ;::::;      ;  ·'-·'´,.-·'´:::::::';          ,'  ,'::::'\\\\';  ;::';        \\\\  '·:'  ,'´:::::;' '  ");
        LOGGER.info("    ;  ,':::;' '         ';  ;'::::;     ;´    ':,´:::::::::::·´'       ,.-·'  '·~^*'´¨,  ';::;         '·,   ,'::::::;'´    ");
        LOGGER.info("   ,'  ,'::;'            ;  ';:::';       ';  ,    `·:;:-·'´            ':,  ,·:²*´¨¯'`;  ;::';          ,'  /::::::;'  '    ");
        LOGGER.info("   ;  ';_:,.-·´';\\\\'     ';  ;::::;'      ; ,':\\\\'`:·.,  ` ·.,           ,'  / \\\\::::::::';  ;::';        ,´  ';\\\\::::;'  '      ");
        LOGGER.info("   ',   _,.-·'´:\\\\:\\\\'     \\\\*´\\\\:::;'      \\\\·-;::\\\\:::::'`:·-.,';        ,' ,'::::\\\\·²*'´¨¯':,'\\\\:;         \\\\`*ª'´\\\\\\\\::/'         ");
        LOGGER.info("    \\\\¨:::::::::::\\\\';      '\\\\::\\\\:;'        \\\\::\\\\:;'` ·:;:::::\\\\::\\\\'      \\\\`¨\\\\:::/          \\\\::\\\\'          '\\\\:::::\\\\';  '        ");
        
        NetworkHandler.registry();
        
        // Register commands
        MinecraftForge.EVENT_BUS.addListener(this::registerCommands);
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        LOGGER.info("######### Dynamic Ambience And Music successfully loaded #########");
        
        // Register client tick event for region checking
        MinecraftForge.EVENT_BUS.addListener(this::onClientTick);
        
        // Load sounds on client setup
        event.enqueueWork(() -> {
            daam.client.RegionHandler.loadSounds();
        });
    }
    
    @SubscribeEvent
    public void onClientTick(net.minecraftforge.event.TickEvent.ClientTickEvent event) {
        if (event.phase == net.minecraftforge.event.TickEvent.Phase.END) {
            daam.client.RegionHandler.clientTick();
        }
    }

    @SubscribeEvent
    public void registerCommands(net.minecraftforge.event.RegisterCommandsEvent event) {
        daam.common.commands.TestRegionCommand.register(event.getDispatcher());
    }

    @SubscribeEvent
    public void buildContents(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey().equals(CreativeModeTabs.TOOLS_AND_UTILITIES)) {
            event.accept(ItemRegister.REGION_EDITOR);
            event.accept(ItemRegister.REGION_CREATOR);
            event.accept(ItemRegister.VOLUME_EDITOR);
        }
    }

    public static ResourceLocation rl(String path) {
        return new ResourceLocation(MODID, path);
    }
}