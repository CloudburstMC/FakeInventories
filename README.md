# Fake Inventories

Easily create fake inventories that players can interact with.

##### [Download](https://github.com/NukkitX/FakeInventories/releases)

## Usage

```java
    public void onEnable() {
        RegisteredServiceProvider<FakeInventories> provider = getServer().getServiceManager().getProvider(FakeInventories.class);
        
        if (provider == null || provider.getProvider() == null) {
            this.getServer().getPluginManager().disablePlugin(this);
        }
    }
``` 

## Maven Dependency

```xml
    <repositories>
        <repository>
            <id>NukkitX-repo</id>
            <url>https://repo.opencollab.dev/</url>
        </repository>
    </repositories>

    <dependencies>
        <dependency>
            <groupId>com.nukkitx</groupId>
            <artifactId>fakeinventories</artifactId>
            <version>1.0.4</version>
            <scope>provided</scope>
        </dependency>
    </dependencies>
```

## Todo

- Add Hopper inventory
