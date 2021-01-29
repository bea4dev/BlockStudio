package be4rjp.blockstudio.command;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CommandUtil {
    public static void sendHowToMessage(Player player){
        player.sendMessage("§a-----------§b How to create object's data §a-----------");
        player.sendMessage("");
        player.sendMessage("1, Click on the block at the first-position of the range you want to convert to data while holding '§6Region selector§r'.");
        player.sendMessage("2, While holding '§6Region selector§r', right-click on the block at second-position.");
        player.sendMessage("3, While holding the '§bBase selector§r', click on the block you want to set as the center.");
        player.sendMessage("");
        player.sendMessage("§a-----------------------------------------------");
    }
    
    public static void addItem(Player player){
        ItemStack region = new ItemStack(Material.STICK);
        ItemMeta regionMeta = region.getItemMeta();
        regionMeta.setDisplayName("§6Region selector");
        region.setItemMeta(regionMeta);
        
        ItemStack base = new ItemStack(Material.BLAZE_ROD);
        ItemMeta baseMeta = base.getItemMeta();
        baseMeta.setDisplayName("§bBase selector");
        base.setItemMeta(baseMeta);
        
        player.getInventory().addItem(region);
        player.getInventory().addItem(base);
    }
}
