# NTroll

Troll plugin for Minecraft Spigot servers

Tested versions: 1.15 - 1.21

## Commads

- `/ntroll on` Enables troll

- `/ntroll off` Disables troll

- `/ntroll status` Shows troll status

- `/ntroll add [player]` Adds a target player

- `/ntroll remove [player]` Removes a target player

- `/ntroll list` Shows target player list

- `/ntroll break-actions` Shows block break action list with chances

- `/ntroll help` Shows help message

- `/spawnmobbehind [entity] [player] [flags?]` Spawns a mob behind a player with optional flags:

    * `--force` Forces the entity to spawn on player location if no safe location behind
    * `--invisible` Makes the entity invisible
    * `--powered` Sets creepers powered
    * `--autotarget` Mob attacks the player instantly
    * `--scale=[number]` Sets mob size scale (default: 1.0)

- `/spawnarrow [player] [flags?]` Spawns an arrow to a player with optional flags:

    * `--critical` Sets arrow critical
    * `--flame` Sets arrow flame

- `/spawnshulkerbullet [player]` Spawns a shulker bullet to a player

- `/spawnfireball [player] [flags?]` Spawns a fireball to a player with optional flags:

    * `--incendiary` Sets fireball incendiary

## Permissions

- `ntroll.command` Allows /ntroll command

- `ntroll.command.admin` Allows /ntroll admin subcommands for example 'on' and 'off'

- `ntroll.spawnmobbehind` Allows /spawnmobbehind command

- `ntroll.spawnarrow` Allows /spawnarrow command

- `ntroll.spawnshulkerbullet` Allows /spawnshulkerbullet command

- `ntroll.spawnfireball` Allows /spawnfireball command