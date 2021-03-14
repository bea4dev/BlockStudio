package be4rjp.blockstudio.api;

import be4rjp.blockstudio.BlockStudio;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class BSCustomBlockPlayerRunnable extends BukkitRunnable {
    
    private final BSCustomBlockChunk bsCustomBlockChunk;
    
    public BSCustomBlockPlayerRunnable(BSCustomBlockChunk bsCustomBlockChunk) {
        this.bsCustomBlockChunk = bsCustomBlockChunk;
    }
    
    @Override
    public void run() {
        try {
            List<Player> playerList = bsCustomBlockChunk.getPlayers();
        
            playerList.removeIf(player -> !player.isOnline());
        
            for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                if (!player.isOnline()) continue;
                if (player.getWorld() != bsCustomBlockChunk.getChunk().getWorld()) continue;
            
                int x = player.getLocation().getBlockX();
                int z = player.getLocation().getBlockZ();
                
                int cx = bsCustomBlockChunk.getChunk().getX();
                int cz = bsCustomBlockChunk.getChunk().getZ();
                
                if (Math.sqrt( Math.pow(x - cx, 2) + Math.pow(z -cz, 2) ) < bsCustomBlockChunk.getAPI().getCustomBlockViewDistance()) {
                    if (!playerList.contains(player)) {
                        bsCustomBlockChunk.getBlockList().forEach(bsCustomBlock -> bsCustomBlock.getArmorStand().sendSpawnPacket(player));
                        playerList.add(player);
                    }
                } else {
                    if (playerList.contains(player)) {
                        bsCustomBlockChunk.getBlockList().forEach(bsCustomBlock -> bsCustomBlock.getArmorStand().sendDestroyPacket(player));
                        playerList.remove(player);
                    }
                }
            }
        }catch (Exception e){}
    }
}
