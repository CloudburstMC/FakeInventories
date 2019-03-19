package com.nukkitx.fakeinventories;

import cn.nukkit.plugin.PluginBase;
import cn.nukkit.plugin.service.ServicePriority;
import com.nukkitx.fakeinventories.inventory.FakeInventories;

public class FakeInventoriesPlugin extends PluginBase {
    private final FakeInventories fakeInventories = new FakeInventories();

    @Override
    public void onEnable() {
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
