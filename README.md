<div align="center">

# VexorTroll

---

> **For English-speaking users:**
> This plugin is made for the Russian-speaking community. GUI, messages and config are in Russian.
> English localization may come in the future.

---

Троллинг-плагин для Paper 1.21+

Выбери жертву → Выбери эффект → Готово.

[![Paper](https://img.shields.io/badge/Paper-1.21+-232323?logo=papermc&logoColor=white)](https://papermc.io/)
[![Gradle](https://img.shields.io/badge/Gradle-8.x-02303A?logo=gradle&logoColor=white)](https://gradle.org/)
[![Java](https://img.shields.io/badge/Java-21+-ED8B00?logo=openjdk&logoColor=white)](https://openjdk.org/)
[![Minecraft](https://img.shields.io/badge/Minecraft-1.21+-62B47A?logo=mojangstudios&logoColor=white)]()
[![License](https://img.shields.io/badge/License-MIT-green.svg)](LICENSE)
[![GitHub](https://img.shields.io/badge/GitHub-d2glaidee-181717?logo=github)](https://github.com/d2glaidee)

</div>

## Возможности

- 25 троллинг-эффектов
- GUI-меню выбора жертвы и эффекта
- Настройки и кулдауны под каждую функцию
- Защита через permission bypass
- Горячий reload конфига

## Команды

| Команда | Описание |
|---|---|
| `/troll` | Открыть GUI |
| `/troll reload` | Перезагрузить конфиг |

## Права

| Permission | Описание |
|---|---|
| `vexortroll.*` | Полный доступ |
| `vexortroll.use` | Открыть GUI |
| `vexortroll.menu` | Взаимодействие с GUI |
| `vexortroll.bypass` | Защита от троллинга |
| `vexortroll.reload` | Перезагрузка конфига |

## Установка

1. `.jar` → `plugins/`
2. Перезапусти сервер
3. Настрой `config.yml`
4. `/troll reload` для применения изменений

## Сборка

```bash
git clone https://github.com/d2glaidee/VexorTroll.git
cd VexorTroll
./gradlew clean build
