package com.nukkitx.fakeinventories;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParameter;
import com.nukkitx.fakeinventories.inventory.FakeInventories;

public class FakeInventoriesCommand extends Command {
    private final FakeInventories fakeInventories;

    FakeInventoriesCommand(FakeInventories fakeInventories) {
        super("fakeinventories", "Create a fake inventory", "/fakeinv", new String[]{"fakeinv"});
        this.fakeInventories = fakeInventories;
        commandParameters.put("default", new CommandParameter[]{
                new CommandParameter("inventory", false, new String[]{"double", "single"})
        });
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }

        if (args.length != 1) {
            return false;
        }

        Player player = (Player) sender;

        if (fakeInventories.getFakeInventory(player).isPresent()) {
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "single":
                player.addWindow(fakeInventories.createChestInventory());
                break;
            case "double":
                player.addWindow(fakeInventories.createDoubleChestInventory());
        }
        return true;
    }
}
