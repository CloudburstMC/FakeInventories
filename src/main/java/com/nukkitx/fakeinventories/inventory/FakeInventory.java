package com.nukkitx.fakeinventories.inventory;

import cn.nukkit.Player;
import cn.nukkit.inventory.*;
import cn.nukkit.inventory.transaction.action.SlotChangeAction;
import cn.nukkit.level.GlobalBlockPalette;
import cn.nukkit.math.BlockVector3;

import cn.nukkit.network.protocol.ContainerOpenPacket;
import cn.nukkit.network.protocol.UpdateBlockPacket;
import com.google.common.base.Preconditions;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class FakeInventory extends ContainerInventory {
    private static final BlockVector3 ZERO = new BlockVector3(0, 0, 0);

    protected final Map<Player, List<BlockVector3>> blockPositions = new HashMap<>();
    private final List<FakeInventoryListener> listeners = new CopyOnWriteArrayList<>();
    private boolean closed = false;

    FakeInventory(InventoryType type) {
        super(null, type);
    }

    @Override
    public void onOpen(Player who) {
        checkForClosed();
        this.viewers.add(who);

        List<BlockVector3> blocks = onOpenBlock(who);
        blockPositions.put(who, blocks);

        onFakeOpen(who, blocks);
    }

    protected void onFakeOpen(Player who, List<BlockVector3> blocks) {
        BlockVector3 blockPosition = blocks.isEmpty() ? ZERO : blocks.get(0);

        ContainerOpenPacket containerOpen = new ContainerOpenPacket();
        containerOpen.windowId = who.getWindowId(this);
        containerOpen.type = this.getType().getNetworkType();
        containerOpen.x = blockPosition.x;
        containerOpen.y = blockPosition.y;
        containerOpen.z = blockPosition.z;

        who.dataPacket(containerOpen);

        this.sendContents(who);
    }

    protected abstract List<BlockVector3> onOpenBlock(Player who);

    @Override
    public void onClose(Player who) {
        super.onClose(who);
        List<BlockVector3> blocks = blockPositions.get(who);

        for (BlockVector3 blockPosition : blocks) {
            UpdateBlockPacket updateBlock = new UpdateBlockPacket();
            updateBlock.blockRuntimeId = GlobalBlockPalette.getOrCreateRuntimeId(who.getLevel().getFullBlock(blockPosition.x, blockPosition.y, blockPosition.z));
            updateBlock.flags = UpdateBlockPacket.FLAG_ALL_PRIORITY;
            updateBlock.x = blockPosition.x;
            updateBlock.y = blockPosition.y;
            updateBlock.z = blockPosition.z;

            who.dataPacket(updateBlock);
        }
    }

    public List<BlockVector3> getPosition(Player player) {
        checkForClosed();
        return blockPositions.getOrDefault(player, Collections.emptyList());
    }

    public void addListener(FakeInventoryListener listener) {
        Preconditions.checkNotNull(listener);
        checkForClosed();
        listeners.add(listener);
    }

    public void removeListener(FakeInventoryListener listener) {
        checkForClosed();
        listeners.remove(listener);
    }

    public boolean onSlotChange(Player source, SlotChangeAction action) {
        if (!listeners.isEmpty()) {
            FakeSlotChangeEvent event = new FakeSlotChangeEvent(source, this, action);
            for (FakeInventoryListener listener : listeners) {
                listener.onSlotChange(event);
            }
            return event.isCancelled();
        }
        return false;
    }

    private void checkForClosed() {
        Preconditions.checkState(!closed, "Already closed");
    }

    void close() {
        Preconditions.checkState(!closed, "Already closed");
        closed = true;
    }
}
