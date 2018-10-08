package com.nukkitx.fakeinventories.inventory;

import cn.nukkit.Player;
import cn.nukkit.event.Cancellable;
import cn.nukkit.inventory.transaction.action.SlotChangeAction;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FakeSlotChangeEvent implements Cancellable {
    private final Player player;
    private final FakeInventory inventory;
    private final SlotChangeAction action;
    private boolean cancelled = false;

    FakeSlotChangeEvent(Player player, FakeInventory inventory, SlotChangeAction action) {
        this.player = player;
        this.inventory = inventory;
        this.action = action;
    }

    @Override
    public void setCancelled() {
        this.cancelled = true;
    }
}
