package com.nukkitx.fakeinventories;

import cn.nukkit.plugin.PluginBase;
import cn.nukkit.plugin.service.ServicePriority;
import com.nukkitx.fakeinventories.inventory.FakeInventories;

public class FakeInventoriesPlugin extends PluginBase {
    // Static reference to the plugin instance
    private static FakeInventoriesPlugin instance;

    // FakeInventories instance
    private final FakeInventories fakeInventories = new FakeInventories();

    // Getter to retrieve the plugin instance globally
    public static FakeInventoriesPlugin getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        // Store the instance of this plugin to be accessed globally
        instance = this;

        // register service
        getServer().getServiceManager().register(FakeInventories.class, fakeInventories, this, ServicePriority.HIGHEST);

        // register listener
        getServer().getPluginManager().registerEvents(new FakeInventoriesListener(fakeInventories), this);
    }

    @Override
    public void onDisable() {
        // deregister service
        getServer().getServiceManager().cancel(this);
    }
}
