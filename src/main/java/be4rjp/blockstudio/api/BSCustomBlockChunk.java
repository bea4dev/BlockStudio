package be4rjp.blockstudio.api;

import be4rjp.blockstudio.BlockStudio;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class BSCustomBlockChunk{
    
    private Set<BSCustomBlock> blockList;
    private Set<Player> playerList;
    
    private final BlockStudioAPI api;
    private final Chunk chunk;
    private final BSCustomBlockPlayerRunnable playerRunnable;
    
    public BSCustomBlockChunk(BlockStudioAPI api, Chunk chunk){
        this.blockList = ConcurrentHashMap.newKeySet();
        this.playerList = ConcurrentHashMap.newKeySet();
        
        this.api = api;
        this.chunk = chunk;
        this.playerRunnable = new BSCustomBlockPlayerRunnable(this);
    }
    
    public Chunk getChunk(){return this.chunk;}
    
    public BlockStudioAPI getAPI(){return this.api;}
    
    public Set<Player> getPlayers(){return this.playerList;}
    
    public Set<BSCustomBlock> getBlockList(){return this.blockList;}
    
    public void startTaskAsync(int period){this.playerRunnable.runTaskTimerAsynchronously(BlockStudio.getPlugin(), 0, period);}
}
