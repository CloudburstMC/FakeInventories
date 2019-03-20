package com.nukkitx.fakeinventories.inventory;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.inventory.ContainerInventory;
import cn.nukkit.inventory.InventoryHolder;
import cn.nukkit.inventory.InventoryType;
import cn.nukkit.inventory.transaction.action.SlotChangeAction;
import cn.nukkit.level.GlobalBlockPalette;
import cn.nukkit.math.BlockVector3;
import cn.nukkit.math.Vector3;
import cn.nukkit.network.protocol.ContainerOpenPacket;
import cn.nukkit.network.protocol.UpdateBlockPacket;
import com.google.common.base.Preconditions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class FakeInventory extends ContainerInventory {
    private static final BlockVector3 ZERO = new BlockVector3(0, 0, 0);

    static final Map<Player, FakeInventory> open = new ConcurrentHashMap<>();

    protected final Map<Player, List<BlockVector3>> blockPositions = new HashMap<>();
    private final List<FakeInventoryListener> listeners = new CopyOnWriteArrayList<>();
    private boolean closed = false;
    private String title;

    public FakeInventory(InventoryType type) {
        this(type, null);
    }

    public FakeInventory(InventoryType type, InventoryHolder holder) {
        this(type, holder, null);
    }

    public FakeInventory(InventoryType type, InventoryHolder holder, String title) {
        super(holder, type);
        this.title = title == null ? type.getDefaultTitle() : title;
    }

    @Override
    public void onOpen(Player who) {
        checkForClosed();
        this.viewers.add(who);
        if (open.putIfAbsent(who, this) != null) {
            throw new IllegalStateException("Inventory was already open");
        }

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
        open.remove(who, this);

        List<BlockVector3> blocks = blockPositions.get(who);

        for (int i = 0, size = blocks.size(); i < size; i++) {
            final int index = i;
            Server.getInstance().getScheduler().scheduleDelayedTask(() -> {
                Vector3 blockPosition = blocks.get(index).asVector3();
                UpdateBlockPacket updateBlock = new UpdateBlockPacket();
                updateBlock.blockRuntimeId = GlobalBlockPalette.getOrCreateRuntimeId(who.getLevel().getBlock(blockPosition).getFullId());
                updateBlock.flags = UpdateBlockPacket.FLAG_ALL_PRIORITY;
                updateBlock.x = blockPosition.getFloorX();
                updateBlock.y = blockPosition.getFloorY();
                updateBlock.z = blockPosition.getFloorZ();

                who.dataPacket(updateBlock);
            }, 2 + i, false);
        }
    }

    public List<BlockVector3> getPosition(Player player) {
        checkForClosed();
        return blockPositions.getOrDefault(player, null);
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
        getViewers().forEach(player -> player.removeWindow(this));
        closed = true;
    }

    @Override
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        if (title == null) {
            this.title = type.getDefaultTitle();
        } else {
            this.title = title;
        }
    }
}
