package com.nukkitx.fakeinventories.inventory;

import cn.nukkit.Player;
import cn.nukkit.math.BlockVector3;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class FakeInventories {
    private final Set<FakeInventory> inventories = Collections.newSetFromMap(new ConcurrentHashMap<>());

    public List<BlockVector3> getFakeInventoryPositions(Player player) {
        for (FakeInventory inventory : inventories) {
            List<BlockVector3> positions = inventory.getPosition(player);
            if (!positions.isEmpty()) {
                return positions;
            }
        }

        return Collections.emptyList();
    }

    public Optional<FakeInventory> getFakeInventory(Player player) {
        for (FakeInventory inventory : inventories) {
            if (inventory.getViewers().contains(player)) {
                return Optional.of(inventory);
            }
        }

        return Optional.empty();
    }

    public FakeInventory createChestInventory() {
        FakeInventory inventory = new ChestFakeInventory();
        inventories.add(inventory);
        return inventory;
    }

    public FakeInventory createDoubleChestInventory() {
        FakeInventory inventory = new DoubleChestFakeInventory();
        inventories.add(inventory);
        return inventory;
    }
}
