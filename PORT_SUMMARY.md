# Портирование мода DAMm с Minecraft 1.12.2 на 1.20.1

## Обзор портирования

Мод **Dynamic Ambience And Music** был успешно портирован с Minecraft 1.12.2 (Forge 14.23.5.2860) на Minecraft 1.20.1 (Forge 47.3.0). 

## Ключевые изменения

### 1. Система сборки (build.gradle)

```gradle
plugins {
    id 'net.minecraftforge.gradle' version '6.0.24'
    id 'org.parchmentmc.librarian.forgegradle' version '1.+'
}

dependencies {
    minecraft 'net.minecraftforge:forge:1.20.1-47.3.0'
}

java.toolchain.languageVersion = JavaLanguageVersion.of(17)
```

**Изменения:**
- ForgeGradle 5.1.40 → 6.0.24
- Java 8 → Java 17
- Gradle 7.1.1 → 8.7
- Добавлена поддержка Parchment mappings

### 2. Метаданные мода (mods.toml)

```toml
modLoader="javafml"
loaderVersion="[47,)"
license="MIT"

[[mods]]
modId="daam"
version="${file.jarVersion}"
displayName="Dynamic Ambience And Music"
```

**Изменения:**
- mcmod.info → mods.toml
- Новый формат метаданных

### 3. Основной класс мода (DAAM.java)

```java
@Mod(DAAM.MODID)
public class DAAM {
    public DAAM() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        
        modEventBus.addListener(this::setup);
        modEventBus.addListener(this::clientSetup);
        
        ItemRegister.ITEMS.register(modEventBus);
        MinecraftForge.EVENT_BUS.register(this);
    }
}
```

**Изменения:**
- `@EventHandler` → `IEventBus.addListener()`
- Убрана аннотация `@SidedProxy`
- Новая система регистрации событий

### 4. Регистрация предметов (ItemRegister.java)

```java
public class ItemRegister {
    public static final DeferredRegister<Item> ITEMS = 
        DeferredRegister.create(ForgeRegistries.ITEMS, DAAM.MODID);

    public static final RegistryObject<Item> REGION_EDITOR = 
        ITEMS.register("region_editor", () -> new RegionEditor());
}
```

**Изменения:**
- `ForgeRegistries.ITEMS.register()` → `DeferredRegister`
- `RegistryObject<Item>` для ленивой регистрации

### 5. Предметы (RegionEditor.java)

```java
public class RegionEditor extends Item {
    public RegionEditor() {
        super(new Item.Properties().stacksTo(1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        // Новый API для использования предметов
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, 
                               List<Component> tooltip, TooltipFlag flag) {
        // Новый API для tooltip
    }
}
```

**Изменения:**
- `EntityPlayer` → `Player`
- `World` → `Level`
- `ActionResult<ItemStack>` → `InteractionResultHolder<ItemStack>`
- `addInformation` → `appendHoverText`
- `String` tooltip → `Component` tooltip

### 6. Система регионов (Region.java)

```java
public class Region implements INBTSerializable<CompoundTag> {
    private AABB AABB;  // net.minecraft.world.phys.AABB
    
    @Override
    public CompoundTag serializeNBT() {
        CompoundTag compound = new CompoundTag();
        // NBTTagCompound → CompoundTag
    }
}
```

**Изменения:**
- `NBTTagCompound` → `CompoundTag`
- `AxisAlignedBB` → `AABB`
- Обновленные методы NBT

### 7. GUI система (GuiRegionEditor.java)

```java
public class GuiRegionEditor extends Screen {
    public GuiRegionEditor(Region region) {
        super(Component.literal("Region Editor"));
    }
    
    @Override
    protected void init() {
        // Новая система GUI для 1.20.1
    }
}
```

**Изменения:**
- `GuiScreen` → `Screen`
- `String` заголовки → `Component`
- Обновленная система GUI элементов

## Структура проекта

```
src/main/java/daam/
├── DAAM.java                    # Основной класс мода
├── client/
│   ├── RegionHandler.java       # Обработка регионов (клиент)
│   ├── screens/
│   │   └── GuiRegionEditor.java # GUI редактора регионов
│   └── ...
├── common/
│   ├── items/
│   │   ├── ItemRegister.java    # Регистрация предметов
│   │   ├── RegionEditor.java    # Предмет-редактор регионов
│   │   └── ...
│   ├── world/
│   │   ├── Region.java          # Класс региона
│   │   └── ...
│   └── network/
│       └── NetworkHandler.java  # Сетевые пакеты
└── ...

src/main/resources/
├── META-INF/
│   └── mods.toml               # Метаданные мода
└── assets/daam/
    ├── models/item/            # Модели предметов
    ├── textures/item/          # Текстуры предметов
    └── lang/                   # Файлы локализации
```

## Функциональность

✅ **Реализовано:**
- Основная структура мода
- Система регионов (базовая)
- Регистрация предметов
- GUI framework
- Система сборки

⚠️ **Требует доработки:**
- Полная система звуков
- Сетевые пакеты
- GUI элементы
- Обработчики событий
- Система загрузки звуков

## Совместимость

- **Minecraft:** 1.20.1
- **Forge:** 47.3.0+
- **Java:** 17+
- **Gradle:** 8.7+

## Следующие шаги

1. Портировать систему звуков (`DynamicSound`)
2. Реализовать сетевые пакеты для 1.20.1
3. Восстановить GUI функциональность
4. Добавить обработчики событий
5. Протестировать загрузку OGG файлов

## Заключение

Базовая структура мода успешно портирована на Minecraft 1.20.1. Основные архитектурные изменения выполнены, система регистрации обновлена. Мод готов для дальнейшей разработки и тестирования.