package com.nukkitx.fakeinventories.inventory;

import cn.nukkit.Player;
import cn.nukkit.math.BlockVector3;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class FakeInventories {
    private final Set<FakeInventory> inventories = Collections.newSetFromMap(new ConcurrentHashMap<>());

    public List<BlockVector3> getFakeInventoryPositions(Player player) {
        FakeInventory inventory = FakeInventory.open.get(player);
        if (inventory == null) {
            return null;
        }
        return inventory.getPosition(player);
    }

    public Optional<FakeInventory> getFakeInventory(Player player) {
        return Optional.ofNullable(FakeInventory.open.get(player));
    }

    public ChestFakeInventory createChestInventory() {
        ChestFakeInventory inventory = new ChestFakeInventory();
        inventories.add(inventory);
        return inventory;
    }

    public DoubleChestFakeInventory createDoubleChestInventory() {
        DoubleChestFakeInventory inventory = new DoubleChestFakeInventory();
        inventories.add(inventory);
        return inventory;
    }

    public void removeFakeInventory(FakeInventory inventory) {
        inventories.remove(inventory);
        inventory.close();
    }
}
