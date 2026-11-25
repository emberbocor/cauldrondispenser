# Cauldron Dispenser

Let vanilla dispensers fill and empty cauldrons with buckets and bottles, the way a player does.

With Cauldron Dispenser, a dispenser pointed at a cauldron pours water, lava and powder snow buckets into it, scoops a full cauldron back into an empty bucket, and fills or drains water one bottle at a time. Supply the dispenser, then send a redstone pulse.

Available for NeoForge, Forge and Fabric.

## Features

- **Fill cauldrons.** Water, lava and powder snow buckets fill the cauldron in front, and water bottles add one level of water.
- **Empty cauldrons.** An empty bucket takes a full cauldron of water, lava or powder snow, and a glass bottle takes one level of water.
- **Nothing is wasted.** A full bucket is only poured when it adds to the cauldron; it never replaces or overflows what is already there.
- **Player rules.** Levels, sounds and the resulting items are the same as when a player uses the item on the cauldron.
- **Vanilla handlers stay.** Without a cauldron in front, buckets and bottles do what they do in a vanilla dispenser.

There is no config file.

## How it works

Point a **vanilla dispenser** at a cauldron and give it a redstone pulse. All six facing directions work. As in vanilla, the dispenser picks one non-empty slot at random, and each pulse uses at most one item.

| Item | Cauldron in front | Result |
|---|---|---|
| Water bucket | Empty, or water below full | Full water cauldron, empty bucket |
| Lava bucket | Empty | Lava cauldron, empty bucket |
| Powder snow bucket | Empty, or powder snow below full | Full powder snow cauldron, empty bucket |
| Empty bucket | Full water, lava, or full powder snow | Empty cauldron, filled bucket |
| Water bottle | Empty, or water below full | One more level of water, glass bottle |
| Glass bottle | Water, any level | One level less, water bottle |

- **The item it gives back.** If the used item was the last one in its slot, the new item takes its place. Otherwise it goes into a free or matching slot of the dispenser, and is thrown out in front if the dispenser is full. This is how vanilla dispensers handle buckets.
- **Feedback.** You hear the bucket or bottle sound at the cauldron and the dispenser plays its usual click and smoke.

What stays vanilla:

- **Droppers** keep their normal transfer and ejection behavior.
- **No cauldron in front, or a cauldron the item cannot be used on**: the dispenser does what vanilla does. Full buckets pour their contents into the world or are thrown out, empty buckets pick up a fluid source, glass bottles fill from water or a full beehive, and water bottles turn dirt into mud. This includes a full bucket aimed at a full cauldron or one holding something else, an empty bucket aimed at a cauldron that is not full, and a water bottle aimed at a full water cauldron.
- **Other items and other cauldron uses**: washing leather armor, banners and shulker boxes, other potions and items from other mods are not handled.
- **Cauldrons from other mods** are not touched.

## Requirements

| | NeoForge | Forge | Fabric |
|---|---|---|---|
| Minecraft | 1.20.1 | 1.20.1 | 1.20.1 |
| Java | 17 | 17 | 17 |
| Loader | NeoForge 1.20.1-47.1.5 or newer | Forge 47.1.0 or newer | Fabric Loader 0.14.21 or newer |
| Jar | `cauldrondispenser-forge-1.20.1-<version>.jar` | `cauldrondispenser-forge-1.20.1-<version>.jar` | `cauldrondispenser-fabric-1.20.1-<version>.jar` |

On Minecraft 1.20.1, Forge and NeoForge use the same Forge jar. Fabric uses its own jar. Pick the jar that matches your game's loader.

The Fabric version does not need Fabric API.

### Client and server

Install the matching jar on the **server**. It adds no items, blocks, entities, sounds, network messages or registries, so clients can join without Cauldron Dispenser:

- **NeoForge:** vanilla clients and NeoForge clients without the mod can join.
- **Forge:** vanilla clients and Forge clients without the mod can join. The mod is marked as server-only, so a Forge client without it still shows the server as compatible in the server list.
- **Fabric:** vanilla clients and Fabric clients without the mod can join.

In singleplayer, install it in your own game; the built-in server runs it.

## Removing the mod

The world opens normally. The mod saves nothing of its own: cauldrons and dispenser contents are ordinary vanilla blocks and items. Without the mod, dispensers handle buckets and bottles as in vanilla again.

## Compatibility

- Targets Minecraft 1.20.1. Use one matching loader jar at a time.
- **Ranching Dispensers:** the cauldron comes first. An empty bucket aimed at a full cauldron fills from the cauldron; without a cauldron, or with one it cannot fill from, Ranching Dispensers can still milk a cow or goat in front. The two mods do not depend on each other.
- **Placing Dispensers:** buckets and bottles have their own vanilla dispenser action, so Placing Dispensers leaves them alone and both mods work side by side.
- Mods that replace the dispenser behavior of buckets or bottles: when the replacement builds on the vanilla default dispenser behavior, a cauldron in front is still filled or emptied first and the replacement handles everything else. A replacement that does not build on it takes over completely.

## Building from source

Use JDK 21 to run Gradle, with JDK 17 available for game compilation, and the included Gradle wrapper:

```
./gradlew build
```

Release jars are written to `forge/build/libs/` and `fabric/build/libs/`. Use Fabric's remapped jar from `fabric/build/libs/`; development jars are not release artifacts.

Development profiles use separate `runs/1.20.1/forge-client`, `forge-server`, `fabric-client` and `fabric-server` directories. These directories are ignored by Git.

## License

MIT, copyright emberbocor. See [LICENSE](LICENSE).

The original NeoForged template notice is retained in [TEMPLATE_LICENSE.txt](TEMPLATE_LICENSE.txt).
