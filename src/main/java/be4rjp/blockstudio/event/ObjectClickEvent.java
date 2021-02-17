package be4rjp.blockstudio.event;

import be4rjp.blockstudio.api.BSObject;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ObjectClickEvent extends Event {
    
    private static final HandlerList HANDLERS = new HandlerList();
    
    private final BSObject bsObject;
    
    public ObjectClickEvent(BSObject bsObject){
        this.bsObject = bsObject;
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
}
