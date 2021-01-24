package be4rjp.blockstudio;

import be4rjp.blockstudio.api.BlockStudioAPI;
import be4rjp.blockstudio.file.Config;
import org.bukkit.plugin.java.JavaPlugin;

public final class BlockStudio extends JavaPlugin {
    
    private static BlockStudio plugin;
    
    private BlockStudioAPI api;
    
    private Config config;
    
    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;
        
        //Load config
        getLogger().info("Loading config files...");
        this.config = new Config(this, "config.yml");
        config.saveDefaultConfig();
        config.getConfig();
        
        //起動時にインスタンス作成
        this.api = new BlockStudioAPI(this, config.getConfig().getInt("default-view-distance"));
    }
    
    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
    
    
    
    public static BlockStudio getPlugin(){
        return plugin;
    }
    
    public static BlockStudioAPI getBlockStudioAPI() {return plugin.api;}
}
