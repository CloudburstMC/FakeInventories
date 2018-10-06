# Fake Inventories

Easily create fake inventories that players can interact with.

##### [Download](https://ci.nukkitx.com/job/NukkitX/job/FakeInventories/job/master/)

## Usage

```java
    public void onEnable() {
        RegisteredServiceProvider<FakeInventories> provider = getServer().getServiceManager().getProvider(FakeInventories.class);
        
        if (provider == null || provider.getProvider() == null) {
            this.getServer().getPluginManager().disablePlugin(this);
        }
        
        ...
    }
``` 