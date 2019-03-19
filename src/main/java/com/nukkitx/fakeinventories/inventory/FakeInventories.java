package com.nukkitx.fakeinventories.inventory;

import cn.nukkit.Player;
import cn.nukkit.math.BlockVector3;

import java.util.List;
import java.util.Optional;

public class FakeInventories {
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

    /**
     * @return Chest inventory
     * @deprecated Use {@link ChestFakeInventory} constructor
     */
    @Deprecated
    public ChestFakeInventory createChestInventory() {
        return new ChestFakeInventory();
    }

    /**
     * @return Double chest inventory
     * @deprecated Use {@link DoubleChestFakeInventory} constructor
     */
    @Deprecated
    public DoubleChestFakeInventory createDoubleChestInventory() {
        return new DoubleChestFakeInventory();
    }

    public void removeFakeInventory(FakeInventory inventory) {
        inventory.close();
    }
}
