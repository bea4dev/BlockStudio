package be4rjp.blockstudio.file;

import be4rjp.blockstudio.BlockStudio;
import be4rjp.blockstudio.api.BSObject;
import be4rjp.blockstudio.api.BlockStudioAPI;
import be4rjp.blockstudio.api.ObjectAnimationRunnable;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import java.io.File;
import java.io.IOException;

public class ObjectConfig {
    
    private static final int VERSION = 1;
    
    private final String name;
    private final String fileName;
    private FileConfiguration config;
    private File file;
    
    public ObjectConfig(String name){
        this.name = name;
        this.fileName = name + ".yml";
        this.file = new File("plugins/BlockStudio/Objects", fileName);
    }
    
    public int getVersion(){
        if(config.contains("version")){
            return config.getInt("version");
        }else{
            return ObjectConfig.VERSION;
        }
    }
    
    public String getObjectName() {return name;}
    
    public boolean isObjectConfig(){return config.contains("object-data");}
    
    public boolean hasDirection(){return config.contains("direction");}
    
    public boolean hasEulerAngle(){return config.contains("euler-angle");}
    
    public boolean hasViewDistance(){return config.contains("view-distance");}
    
    public boolean hasTaskPeriod(){return config.contains("task-period");}
    
    public boolean hasAnimation(){return config.contains("animation");}
    
    public String getObjectDataName(){return config.getString("object-data");}
    
    public Location getLocation(){return ConfigUtil.toLocation(config.getString("location"));}
    
    public Vector getDirection(){return ConfigUtil.toDirection(config.getString("direction"));}
    
    public EulerAngle getEulerAngle(){return ConfigUtil.toEulerAngle(config.getString("euler-angle"));}
    
    public double getViewDistance(){return config.getDouble("view-distance");}
    
    public int getTaskPeriod(){return config.getInt("task-period");}
    
    public String getAnimation(){return config.getString("animation");}
    
    public void setObjectDataName(String name){config.set("object-data", name);}
    
    public void setLocation(Location location){
        config.set("location", location.getWorld().getName() + ", " + location.getX() + ", " + location.getY() + ", " + location.getZ());
    }
    
    public void setDirection(Vector direction){
        config.set("direction", direction.getX() + ", " + direction.getY() + ", " + direction.getZ());
    }
    
    public void setEulerAngle(EulerAngle eulerAngle){
        config.set("euler-angle", eulerAngle.getX() + ", " + eulerAngle.getY() + ", " + eulerAngle.getZ());
    }
    
    public void setViewDistance(double distance){config.set("view-distance", distance);}
    
    public void setTaskPeriod(int period){config.set("task-period", period);}
    
    public void setAnimation(String option){config.set("animation", option);}
    
    
    public void spawnObject(){
        BlockStudioAPI api = BlockStudio.getBlockStudioAPI();
        ObjectData objectData = api.getObjectData(getObjectDataName());
        
        if(objectData == null) {
            BlockStudio.getPlugin().getLogger().warning("Object data (" + getObjectDataName() + ") was not found.");
            return;
        }
        
        BSObject bsObject = api.createObjectFromObjectData(name, getLocation(), objectData,
                hasViewDistance() ? getViewDistance() : api.getDefaultViewDistance(), false);
        
        if(hasDirection()){
            bsObject.setDirection(getDirection());
        }else if(hasEulerAngle()){
            bsObject.setOriginByEulerAngle(getEulerAngle());
        }else{
            bsObject.setDirection(new Vector(0, 0, 1));
        }
        
        bsObject.move();
        
        bsObject.startTaskAsync(
                hasTaskPeriod() ? getTaskPeriod() : BlockStudio.config.getConfig().getInt("default-task-period"));
        
        if(hasAnimation()){
            ObjectAnimationRunnable runnable = new ObjectAnimationRunnable(bsObject, getAnimation());
            runnable.startTask();
        }
    }
    
    
    public void loadFile(){
        file.getParentFile().mkdir();
        if(file.exists()){
            config = YamlConfiguration.loadConfiguration(file);
        }else{
            try {
                file.createNewFile();
                config = YamlConfiguration.loadConfiguration(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
   
    public void saveFile(){
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
