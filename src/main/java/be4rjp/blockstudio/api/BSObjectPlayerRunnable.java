package be4rjp.blockstudio.api;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class BSObjectPlayerRunnable extends BukkitRunnable {
    private final BSObject bsObject;
    
    
    private final List<String> hideList = new ArrayList<>();
    
    /**
     * Add a player to hide the object.
     * オブジェクトを非表示にするプレイヤーを追加します。
     * @param name Player's name, not UUID.
     */
    public void addHidePlayer(String name){
        hideList.add(name);
    }
    
    /**
     * Remove a player to hide the object.
     * オブジェクトを非表示にするプレイヤーを削除します。
     * @param name Player's name, not UUID.
     */
    public void removeHidePlayer(String name){
        hideList.remove(name);
    }
    
    public List<String> getHidePlayerList(){return this.hideList;}
    
    
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
                
                if (player.getLocation().distance(bsObject.getBaseLocation()) < bsObject.getViewDistance() && !hideList.contains(player.getName())) {
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
