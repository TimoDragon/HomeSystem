package eu.dragonarmy.homesystem.listener;

import eu.dragonarmy.homesystem.Home;
import eu.dragonarmy.homesystem.HomeLocation;
import eu.dragonarmy.homesystem.HomeSystem;
import eu.dragonarmy.homesystem.commands.HomeCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class GuiListener implements Listener {
    public static HashMap<UUID, Integer> teleports = new HashMap<>();

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (event.getView().getTitle().startsWith(ChatColor.GREEN + "Homes")) {
            event.setCancelled(true);

            Player player = (Player) event.getWhoClicked();;

            if (event.getRawSlot() > 10 && event.getRawSlot() < 16) {
                openInv(player, event.getRawSlot() - 10);
            }
        }
        else if (event.getView().getTitle().startsWith(ChatColor.GREEN + "Home | ")) {
            event.setCancelled(true);
            int homeNumber = Integer.parseInt(event.getView().getTitle().substring(9));
            Player player = (Player) event.getWhoClicked();

            switch (event.getRawSlot()) {
                case 11:
                    player.closeInventory();
                    setPos(player, homeNumber);
                    break;
                case 13:
                    player.closeInventory();
                    teleport(player, homeNumber);
                    break;
                case 15:
                    player.closeInventory();
                    delete(player, homeNumber);
                    break;
                case 18:
                    player.closeInventory();
                    HomeCommand.openHomeGui(player);
                    break;
            }
        }
    }

    private void openInv(Player player, int homeID) {
        Inventory inv = Bukkit.createInventory(null, 27, ChatColor.GREEN + "Home | " + homeID);

        ItemStack grayGlass = fastItemStack(Material.GRAY_STAINED_GLASS_PANE, " ", null);
        ItemStack enderPearl = fastItemStack(Material.ENDER_PEARL, ChatColor.AQUA + "Teleportieren", null);
        ItemStack barrier = fastItemStack(Material.BARRIER, ChatColor.DARK_RED + "Home Löschen", null);
        ItemStack book = fastItemStack(Material.WRITABLE_BOOK, ChatColor.YELLOW + "Standort setzen", null);
        ItemStack redWool = fastItemStack(Material.RED_WOOL, ChatColor.RED + "Zurück", null);

        for (int i = 0; i < inv.getSize(); i++) {
            inv.setItem(i, grayGlass);
        }

        inv.setItem(11, book);
        inv.setItem(13, enderPearl);
        inv.setItem(15, barrier);
        inv.setItem(18, redWool);

        player.openInventory(inv);
        player.updateInventory();
    }

    private ItemStack fastItemStack(Material mat, String name, List<String> lore) {
        ItemStack home = new ItemStack(mat);
        ItemMeta homeMeta = home.getItemMeta();
        homeMeta.setDisplayName(name);
        if (lore != null) {
            homeMeta.setLore(lore);
        }

        home.setItemMeta(homeMeta);
        return home;
    }

    private void setPos(Player player, int homeNumber) {
        Bukkit.getScheduler().runTaskAsynchronously(HomeSystem.getInstance(), new Runnable() {
            public void run() {
                Location pLoc = player.getLocation();
                HomeLocation loc = new HomeLocation(player.getWorld().getName(), pLoc.getX(), pLoc.getY(), pLoc.getZ(), pLoc.getPitch(), pLoc.getYaw());
                Home home = new Home(player.getUniqueId(), homeNumber);
                home.setLocation(loc);
                player.sendMessage(ChatColor.GREEN + "Die Position wurde erfolgreich geändert");
            }
        });
    }

    private void teleport(Player player, int homeNumber) {

        Bukkit.getScheduler().runTaskAsynchronously(HomeSystem.getInstance(), new Runnable() {
            public void run() {
                Home home = new Home(player.getUniqueId(), homeNumber);

                if (home.hasHome()) {

                    int tid = Bukkit.getScheduler().scheduleAsyncRepeatingTask(HomeSystem.getInstance(), new Runnable() {
                        int countdown = 3;
                        public void run() {
                            if (countdown <= 0) {
                                Bukkit.getScheduler().cancelTask(teleports.get(player.getUniqueId()));

                                home.teleport();
                                player.sendMessage(ChatColor.GREEN + "Du wurdest zu deinem Home teleportiert");
                                HomeSystem.getInstance().getLogger().info(player.getName() + " was teleportet to their home");
                                teleports.remove(player.getUniqueId());

                                return;
                            }

                            player.sendMessage(ChatColor.GREEN + "Du wirst in " + countdown + " Sekunden teleportiert");
                            countdown--;
                        }
                    }, 0, 20);

                    teleports.put(player.getUniqueId(), tid);
                }
                else {
                    player.sendMessage(ChatColor.RED + "Es wurde noch keine Position für das Home festgelegt");
                }
            }
        });
    }

    private void delete(Player player, int homeNumber) {
        Bukkit.getScheduler().runTaskAsynchronously(HomeSystem.getInstance(), new Runnable() {
            public void run() {
                Home home = new Home(player.getUniqueId(), homeNumber);
                home.deleteHome();
                player.sendMessage(ChatColor.GREEN + "Du hast das Home gelöscht");
            }
        });
    }
}