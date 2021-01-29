package be4rjp.blockstudio.data;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class DataStore {
    
    private Map<String, PlayerData> playerDataMap = new HashMap<>();
    
    
    public PlayerData getPlayerData(String uuid){return this.playerDataMap.get(uuid);}
    
    public PlayerData getPlayerData(Player player){
        String uuid = player.getUniqueId().toString();
        return this.playerDataMap.get(uuid);
    }
    
    
    public void setPlayerData(String uuid, PlayerData playerData){this.playerDataMap.put(uuid, playerData);}
    
    public void setPlayerData(Player player, PlayerData playerData){
        String uuid = player.getUniqueId().toString();
        this.playerDataMap.put(uuid, playerData);
    }
}
