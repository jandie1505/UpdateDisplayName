# UpdateDisplayNames
## Overview
UpdateDisplayNames is a Paper plugin which automatically updates the display name and tablist name of the players to a specified format.
## Features
- Supports LuckPerms, Vault and PlaceholderAPI
- Supports both MiniMessage and legacy color codes in placeholders
- Has an API which allows to exclude specific players from the display name updates
## How to install
To install this plugin, download the plugin for the correct minecraft version from the releases page and put it into your plugins directory.
## Commands
| Command | Description | Permission |
|--|--|--|
| `/updatedisplayname reload` | Config reload | `updatedisplayname.command` |
| `/updatedisplayname update` | Updates names of all players | `updatedisplayname.command` |
| `/updatedisplayname enable [true|false]` | Enable/disable the plugin's functionality | `updatedisplayname.command` |
| `/updatedisplayname playerlist` | Shows the exclude list | `updatedisplayname.command` |
## Configuration
The plugin should work out of the box without any configuration needed.  
But if you want to change something, here are the config values:
| Option | Default | Description |
|--|--|--|
| `displayname --> enable` | `true` | If the plugin should update the player's display name. |
| `displayname --> format` | `<luckperms:prefix><player><luckperms:suffix>` | The player display name format. |
| `tablist --> enable` | `true` | If the plugin should update the player's tablist name. |
| `tablist --> format` | `<luckperms:prefix><player><luckperms:suffix>` | The player tablist name format. |
| `update_interval` | `60` | The inverval the names should be updated in seconds. |
