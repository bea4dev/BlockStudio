package be4rjp.blockstudio.api;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class BSObjectPlayerRunnable extends BukkitRunnable {
    private final BSObject bsObject;
    
    public BSObjectPlayerRunnable(BSObject bsObject){
        this.bsObject = bsObject;
    }
    
    @Override
    public void run() {
        try {
            List<Player> playerList = bsObject.getPlayers();
            
            playerList.removeIf(player -> !player.isOnline());
            
            for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                if (!player.isOnline()) continue;
                if (player.getWorld() != bsObject.getBaseLocation().getWorld()) continue;
                
                if (player.getLocation().distance(bsObject.getBaseLocation()) < bsObject.getViewDistance()) {
                    if (!playerList.contains(player)) {
                        for (BSCube bsCube : bsObject.getBSCubeList()) {
                            bsCube.getBSArmorStand().sendSpawnPacket(player);
                        }
                        bsObject.getPlayers().add(player);
                    }
                } else {
                    if (playerList.contains(player)) {
                        for (BSCube bsCube : bsObject.getBSCubeList()) {
                            bsCube.getBSArmorStand().sendDestroyPacket(player);
                        }
                        playerList.remove(player);
                    }
                }
            }
        }catch (Exception e){}
    }
}
