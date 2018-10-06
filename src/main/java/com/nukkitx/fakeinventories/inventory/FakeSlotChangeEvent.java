package com.nukkitx.fakeinventories.inventory;

import cn.nukkit.event.Cancellable;
import cn.nukkit.inventory.transaction.action.SlotChangeAction;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FakeSlotChangeEvent implements Cancellable {
    private final SlotChangeAction action;
    private boolean cancelled = false;

    FakeSlotChangeEvent(SlotChangeAction action) {
        this.action = action;
    }

    @Override
    public void setCancelled() {
        this.cancelled = true;
    }
}
