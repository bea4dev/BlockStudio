package be4rjp.blockstudio.event;

import be4rjp.blockstudio.api.BSObject;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ObjectClickEvent extends Event {
    
    private static final HandlerList HANDLERS = new HandlerList();
    
    private final BSObject bsObject;
    private final Player player;
    
    public ObjectClickEvent(BSObject bsObject, Player player){
        this.bsObject = bsObject;
        this.player = player;
    }
    
    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }
    
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
    
    /**
     * Get clicked object.
     * @return clicked object
     */
    public BSObject getBSObject() {
        return bsObject;
    }
    
    /**
     * Get player who clicked object.
     * @return Player
     */
    public Player getPlayer() {
        return player;
    }
}
