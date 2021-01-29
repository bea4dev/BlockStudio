package be4rjp.blockstudio.data;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class PlayerData {
    
    private final String uuid;
    
    private Location first;
    private Location second;
    private Location base;
    
    public PlayerData(String uuid){
        this.uuid = uuid;
    }
    
    public PlayerData(Player player){
        this.uuid = player.getUniqueId().toString();
    }
    
    public Location getBase() {return base;}
    
    public Location getFirst() {return first;}
    
    public Location getSecond() {return second;}
    
    
    public void setBase(Location base) {this.base = base;}
    
    public void setFirst(Location first) {this.first = first;}
    
    public void setSecond(Location second) {this.second = second;}
}
