# 🎵 РУКОВОДСТВО ПО АУДИОСИСТЕМЕ DAMm

## 📁 **КАК РАЗМЕЩАТЬ АУДИОФАЙЛЫ**

### **Автоматическое создание папок:**
При первом запуске мод автоматически создает структуру папок:

```
📁 .minecraft/
└── 📁 DynamicAmbienceAndMusic/
    └── 📁 assets/
        └── 📁 daam/
            ├── 📁 sounds/          ← СЮДА РАЗМЕЩАЙТЕ OGG ФАЙЛЫ
            └── 📄 sounds.json      ← Автоматически создается
```

### **Полный путь к папке звуков:**
- **Windows:** `%appdata%\.minecraft\DynamicAmbienceAndMusic\assets\daam\sounds\`
- **macOS:** `~/Library/Application Support/minecraft/DynamicAmbienceAndMusic/assets/daam/sounds/`
- **Linux:** `~/.minecraft/DynamicAmbienceAndMusic/assets/daam/sounds/`

---

## 🎵 **ПОДДЕРЖИВАЕМЫЕ ФОРМАТЫ АУДИО**

### **✅ Поддерживается:**
- **OGG Vorbis** (рекомендуется) - `.ogg`
- Качество: любое (рекомендуется 128-320 kbps)
- Длительность: любая (для музыки 2-10 минут, для амбиента 30-120 секунд)

### **❌ НЕ поддерживается:**
- MP3 файлы (будут игнорироваться)
- WAV файлы (слишком большие)
- FLAC файлы (не поддерживается Minecraft)

---

## 🎮 **КАК УКАЗЫВАТЬ ПУТИ К ФАЙЛАМ В GUI**

### **Формат путей:**
В полях GUI указывайте **только имя файла БЕЗ расширения**:

**✅ Правильно:**
```
Day Music Path:     forest_day
Night Music Path:   forest_night
Day Ambient Path:   birds_chirping
Night Ambient Path: cricket_sounds
```

**❌ Неправильно:**
```
Day Music Path:     forest_day.ogg          ← НЕ указывайте .ogg
Night Music Path:   /sounds/forest_night    ← НЕ указывайте путь
Day Ambient Path:   daam:birds_chirping     ← НЕ указывайте namespace
```

### **Примеры файлов и путей:**

| Файл в папке sounds/    | Путь в GUI        |
|------------------------|-------------------|
| `forest_day.ogg`       | `forest_day`      |
| `ocean_waves.ogg`      | `ocean_waves`     |
| `mountain_wind.ogg`    | `mountain_wind`   |
| `cave_drips.ogg`       | `cave_drips`      |

---

## 🌅 **СИСТЕМА ДЕНЬ/НОЧЬ**

### **Как работает:**
- **День (6:00-18:00):** Воспроизводится Day Music + Day Ambient
- **Ночь (18:00-6:00):** Воспроизводится Night Music + Night Ambient
- **Плавное переключение:** Звуки меняются автоматически при смене времени
- **Зацикливание:** Все звуки воспроизводятся в бесконечном цикле

### **Рекомендации по контенту:**
**Day Music:** Позитивная, светлая музыка
- Примеры: acoustic_guitar.ogg, peaceful_piano.ogg, upbeat_folk.ogg

**Night Music:** Спокойная, атмосферная музыка  
- Примеры: calm_strings.ogg, mysterious_ambient.ogg, soft_piano.ogg

**Day Ambient:** Дневные природные звуки
- Примеры: birds_singing.ogg, wind_in_trees.ogg, water_flowing.ogg

**Night Ambient:** Ночные атмосферные звуки
- Примеры: cricket_chorus.ogg, owl_hooting.ogg, gentle_breeze.ogg

---

## 🎧 **ТЕСТИРОВАНИЕ ЗВУКОВ**

### **Проверка после размещения файлов:**
1. Поместите OGG файлы в папку `sounds/`
2. Перезапустите Minecraft
3. Создайте регион с указанием имен файлов
4. Войдите в созданный регион
5. Звуки должны начать воспроизводиться автоматически

### **Устранение проблем:**
**Звуки не воспроизводятся:**
- ✅ Проверьте что файлы в формате OGG Vorbis
- ✅ Убедитесь что указали правильное имя без расширения
- ✅ Проверьте что файлы находятся в правильной папке
- ✅ Перезапустите Minecraft после добавления файлов

**Звуки прерываются:**
- ✅ Используйте файлы без тишины в начале/конце
- ✅ Убедитесь что файлы не повреждены
- ✅ Рекомендуется качество 128-192 kbps для стабильности

---

## 📊 **ПРИМЕРЫ ГОТОВЫХ РЕГИОНОВ**

### **🌲 Лесной регион:**
```
Region Name:        Enchanted Forest
Day Music Path:     peaceful_forest
Night Music Path:   mystical_night
Day Ambient Path:   birds_and_wind
Night Ambient Path: owls_and_crickets
```

### **🏔️ Горный регион:**
```
Region Name:        Mountain Peak
Day Music Path:     epic_mountains
Night Music Path:   cold_mountain_night
Day Ambient Path:   mountain_wind
Night Ambient Path: distant_wolves
```

### **🌊 Океанский регион:**
```
Region Name:        Ocean Shore
Day Music Path:     sea_adventure
Night Music Path:   calm_ocean_night
Day Ambient Path:   waves_and_seagulls
Night Ambient Path: gentle_waves
```

---

## 🔧 **ТЕХНИЧЕСКИЕ ДЕТАЛИ**

### **Логирование:**
Проверьте логи для отладки:
```
[DAAM] Loading sounds from: DynamicAmbienceAndMusic/assets/daam/sounds
[DAAM] Found sound file: forest_day.ogg
[DAAM] Playing day music: forest_day
```

### **Производительность:**
- Мод автоматически управляет загрузкой/выгрузкой звуков
- Рекомендуется размер файлов: 2-10 МБ для музыки, 0.5-2 МБ для амбиента
- Одновременно может воспроизводиться до 4 звуков (день/ночь музыка + амбиент)

### **Совместимость:**
- ✅ Работает в одиночной игре
- ✅ Готов для мультиплеера (синхронизация регионов)
- ✅ Совместим с другими аудио модами
- ✅ Поддерживает несколько регионов одновременно

---

## 🎉 **ГОТОВЫЕ ПАКЕТЫ ЗВУКОВ**

### **Где скачать готовые OGG файлы:**
- **Freesound.org** - бесплатные звуки с лицензией CC
- **Zapsplat.com** - профессиональные аудио файлы
- **Minecraft Resource Packs** - извлеките звуки из ресурспаков

### **Конвертация в OGG:**
Если у вас MP3/WAV файлы, конвертируйте через:
- **Audacity** (бесплатно) - File → Export → OGG Vorbis
- **VLC Media Player** - Media → Convert/Save → OGG format
- **Online convertors** - cloudconvert.com, online-audio-converter.com

---

**🎵 Наслаждайтесь атмосферными звуками в ваших мирах Minecraft! 🎵**