package be4rjp.blockstudio;

import be4rjp.blockstudio.api.BSCustomBlock;
import be4rjp.blockstudio.api.BlockStudioAPI;
import be4rjp.blockstudio.command.bsCommandExecutor;
import be4rjp.blockstudio.data.DataStore;
import be4rjp.blockstudio.data.PlayerData;
import be4rjp.blockstudio.file.Config;
import be4rjp.blockstudio.listener.EventListener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public final class BlockStudio extends JavaPlugin {
    
    private static BlockStudio plugin;
    
    private BlockStudioAPI api;
    
    private DataStore dataStore;
    
    public static Config config;
    
    private int LOG_LEVEL;
    private boolean IS_ENABLE_OBJECT_CLICK_EVENT;
    private boolean IS_ENABLE_CUSTOM_BLOCK;
    
    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;
        
        
        //Load config
        loadConfig();
        
        
        //起動時にインスタンス作成
        double objectViewDistance = config.getConfig().getDouble("default-view-distance");
        double blockViewDistance = config.getConfig().getDouble("custom-block-view-distance");
        this.api = new BlockStudioAPI(this, objectViewDistance, blockViewDistance);
        
        
        //Load all object data
        this.api.loadAllObjectData();
        
        
        //Load and spawn custom blocks
        if(IS_ENABLE_CUSTOM_BLOCK)
            this.api.loadAndSpawnAllCustomBlocks();
        
        
        //Register listeners
        getLogger().info("Registering listeners...");
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new EventListener(), this);
    
    
        //Register command executors
        getLogger().info("Registering command executors...");
        getCommand("bs").setExecutor(new bsCommandExecutor());
        getCommand("bs").setTabCompleter(new bsCommandExecutor());
        getCommand("blockstudio").setExecutor(new bsCommandExecutor());
        getCommand("blockstudio").setTabCompleter(new bsCommandExecutor());
        
        
        //Create DataStore instance.
        dataStore = new DataStore();
        
        
        //For restart
        getServer().getOnlinePlayers().stream().forEach(player -> {
            PlayerData playerData = new PlayerData(player);
            getDataStore().setPlayerData(player, playerData);
        });
        
        
        //Spawn objects
        if(config.getConfig().getBoolean("spawn-objects"))
            this.api.spawnAllObjects();
    }
    
    
    @Override
    public void onDisable() {
        // Plugin shutdown logic
        
        //Save config
        api.getCustomBlockConfig().saveConfig();
        api.getCustomBlockDataConfig().saveConfig();
        
        //Remove objects
        api.getObjectList().forEach(bsObject -> bsObject.remove(false));
        api.getObjectList().clear();
        api.getObjectMap().clear();
    
        //Remove custom blocks
        List<BSCustomBlock> blockList = new ArrayList<>();
        api.getBSCustomBlockChunkMap().values().forEach(bsCustomBlockChunk -> {
            bsCustomBlockChunk.getBlockList().forEach(bsCustomBlock -> blockList.add(bsCustomBlock));
        });
        blockList.forEach(bsCustomBlock -> api.breakCustomBlock(bsCustomBlock.getBlock()));
    }
    
    
    public void loadConfig(){
        getLogger().info("Loading config files...");
        config = new Config(this, "config.yml");
        config.saveDefaultConfig();
        config.getConfig();
    
        LOG_LEVEL = config.getConfig().getInt("log-level");
        IS_ENABLE_OBJECT_CLICK_EVENT = config.getConfig().getBoolean("object-click-event");
        IS_ENABLE_CUSTOM_BLOCK = config.getConfig().getBoolean("enable-custom-block");
    }
    
    
    public void errorMessage(String message){
        getLogger().warning("!!! AN ERROR HAS OCCURRED !!!");
        getLogger().warning(message);
    }
    
    
    public int getLogLevel(){return LOG_LEVEL;}
    
    
    public boolean isEnableObjectClickEvent(){return IS_ENABLE_OBJECT_CLICK_EVENT;}
    
    
    public boolean isEnableCustomBlock() {return IS_ENABLE_CUSTOM_BLOCK;}
    
    
    public DataStore getDataStore() {return dataStore;}
    
    
    public static BlockStudio getPlugin(){return plugin;}
    
    
    public static BlockStudioAPI getBlockStudioAPI() {return plugin.api;}
}
