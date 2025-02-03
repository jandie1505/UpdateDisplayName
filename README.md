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
| `/updatedisplayname enable [true\|false]` | Enable/disable the plugin's functionality | `updatedisplayname.command` |
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
## Using the API
### Import
Using gradle:
```kotlin
repositories {
    maven("https://maven.chaossquad.net/releases")
}

dependencies {
    compileOnly("net.jandie1505:UpdateDisplayName:1.0-RELEASE")
}
```

Using maven:
```xml
<repository>
  <id>ChaosSquad-Repository-releases</id>
  <name>ChaosSquad Repository</name>
  <url>https://maven.chaossquad.net/releases</url>
</repository>
```
```xml
<dependency>
  <groupId>net.jandie1505</groupId>
  <artifactId>UpdateDisplayName</artifactId>
  <version>1.0-RELEASE</version>
  <scope>provided</scope>
</dependency>
```

### Usage
```java
// General features
public void ApiExample() {
    UDNApi api = UpdateDisplayName.getApi();

    Player player = Bukkit.getPlayer("playername")
        
    boolean updatesEnabled = this.updatesEnabled; // Get plugin functionality status
    api.setUpdatesEnabled(false); // Disable plugin functionality
        
    api.updatePlayers(); // Update display names of all players
    api.updatePlayer(player); // Update display name of the specified player
        
    Set<UUID> excludedPlayers = api.getExcludedPlayers(); // Get excluded players
    boolean excluded = api.isPlayerExcluded(player); // Check if a player is excluded
        
    DataStorage config = api.getPluginConfig(); // Get plugin config

    // Get config values
    boolean enableDisplaynameUpdate = config.optBoolean(UpdateDisplayname.CONFIG_ENABLE_DISPLAYNAME, false);
    String displayNameFormat = config.optString(UpdateDisplayName.CONFIG_FORMAT_DISPLAYNAME, null);
    boolean enableTablistName = config.optString(UpdateDisplayName.CONFIG_ENABLE_TABLIST_NAME, false);
    String tablistFormat = config.optString(UpdateDisplayName.CONFIG_TABLIST_FORMAT, null);
    int updateInterval = config.optInt(UpdateDisplayName.CONFIG_UPDATE_INTERVAL, 60);

    // Set config values
    config.set(UpdateDisplayName.CONFIG_ENABLE_DISPLAYNAME, true);
    config.set(UpdateDisplayName.CONFIG_FORMAT_DISPLAYNAME, "<luckperms:prefix><player><luckperms:suffix>");
    config.set(UpdateDisplayName.CONFIG_ENABLE_TABLIST_NAME, true);
    config.set(UpdateDisplayName.CONFIG_TABLIST_FORMAT, "<luckperms:prefix><player><luckperms:suffix>");
    config.set(UpdateDisplayName.CONFIG_UPDATE_INTERVAL, 60);
}

// Event for updated display name
@EventHandler
public void onDisplayNameUpdated(DisplayNameUpdatedEvent event) {

    if (event.getPlayer().getName().equals("forbiddenName")) {
        event.setCancelled(true)
        return;
    }

    if (event.getUpdatedDisplayName() == null) {
        Bukkit.broadcast(Component.text("Display name of " + event.getPlayer().getName() + " has been reset"))
        return;
    ]

    Bukkit.broadcast(Component.empty()
            .append(Component.text("Updated display name of player " + event.getPlayer().getName() + ": "))
            .append(event.getUpdatedDisplayName())
    );

}

// Event for updated tablist name
@EventHandler
public void onDisplayNameUpdated(TablistNameUpdatedEvent event) {

    if (event.getPlayer().getName().equals("PleaseDontGiveMeATablistName")) {
        event.setCancelled(true)
        return;
    }

    if (event.getUpdatedTablistName() == null) {
        Bukkit.broadcast(Component.text("Tablist name of " + event.getPlayer().getName() + " has been reset"))
        return;
    ]

    Bukkit.broadcast(Component.empty()
            .append(Component.text("Updated tablist name of player " + event.getPlayer().getName() + ": "))
            .append(event.getUpdatedTablistName())
    );

}
```

### JavaDocs
Click [here](https://jandie1505.github.io/UpdateDisplayName/) to get to the JavaDocs.
