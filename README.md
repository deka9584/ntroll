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

- `/spawnarrow [player]` Spawns an arrow to a player

- `/spawnshulkerbullet [player]` Spawns a shulker bullet to a player

## Permissions

- `ntroll.command` Allows /ntroll command

- `ntroll.command.admin` Allows /ntroll admin subcommands for example 'on' and 'off'

- `ntroll.spawnmobbehind` Allows /spawnmobbehind command

- `ntroll.spawnarrow` Allows /spawnarrow command

- `ntroll.spawnshulkerbullet` Allows /spawnshulkerbullet command