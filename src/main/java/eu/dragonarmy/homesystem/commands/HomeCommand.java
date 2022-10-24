package eu.dragonarmy.homesystem.commands;

import eu.dragonarmy.homesystem.HomeSystem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class HomeCommand implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command cmd, String Label, String[] args) {
        if (!(sender instanceof Player)) {
            HomeSystem.getInstance().getLogger().info("Du musst ein Spieler sein um den Command ausführen zu können");
            return false;
        }

        Player player = (Player) sender;

        openHomeGui(player);

        return true;
    }

    private static ItemStack fastItemStack(Material mat, String name, List<String> lore) {
        ItemStack home = new ItemStack(mat);
        ItemMeta homeMeta = home.getItemMeta();
        homeMeta.setDisplayName(name);
        if (lore != null) {
            homeMeta.setLore(lore);
        }

        home.setItemMeta(homeMeta);
        return home;
    }

    public static void openHomeGui(Player player) {
        Inventory inv = Bukkit.createInventory(null, 27, ChatColor.GREEN + "Homes");

        ItemStack grayGlass = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta grayGlassMeta = grayGlass.getItemMeta();
        grayGlassMeta.setDisplayName(" ");
        grayGlass.setItemMeta(grayGlassMeta);

        for (int i = 0; i < inv.getSize(); i++) {
            inv.setItem(i, grayGlass);
        }

        ItemStack home1 = fastItemStack(Material.RED_BED, ChatColor.GREEN + "Home 1", null);
        ItemStack home2 = fastItemStack(Material.RED_BED, ChatColor.GREEN + "Home 2", null);
        ItemStack home3 = fastItemStack(Material.RED_BED, ChatColor.GREEN + "Home 3", null);
        ItemStack home4 = fastItemStack(Material.RED_BED, ChatColor.GREEN + "Home 4", null);
        ItemStack home5 = fastItemStack(Material.RED_BED, ChatColor.GREEN + "Home 5", null);

        inv.setItem(11, home1);
        inv.setItem(12, home2);
        inv.setItem(13, home3);
        inv.setItem(14, home4);
        inv.setItem(15, home5);

        player.openInventory(inv);
        player.updateInventory();
    }
}