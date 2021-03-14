package be4rjp.blockstudio.api;

import be4rjp.blockstudio.BlockStudio;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class BSCustomBlockChunk{
    
    private List<BSCustomBlock> blockList;
    private List<Player> playerList;
    
    private final BlockStudioAPI api;
    private final Chunk chunk;
    private final BSCustomBlockPlayerRunnable playerRunnable;
    
    public BSCustomBlockChunk(BlockStudioAPI api, Chunk chunk){
        this.blockList = new ArrayList<>();
        this.playerList = new ArrayList<>();
        
        this.api = api;
        this.chunk = chunk;
        this.playerRunnable = new BSCustomBlockPlayerRunnable(this);
    }
    
    public Chunk getChunk(){return this.chunk;}
    
    public BlockStudioAPI getAPI(){return this.api;}
    
    public List<Player> getPlayers(){return this.playerList;}
    
    public List<BSCustomBlock> getBlockList(){return this.blockList;}
    
    public void startTaskAsync(int period){this.playerRunnable.runTaskTimerAsynchronously(BlockStudio.getPlugin(), 0, period);}
}
