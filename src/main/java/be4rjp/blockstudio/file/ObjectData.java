package be4rjp.blockstudio.file;


import be4rjp.blockstudio.api.BSObject;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ObjectData{
    
    private final String dataName;
    private final String fileName;
    private FileConfiguration config;
    private File file;
    
    public ObjectData(String dataName){
        this.dataName = dataName;
        this.fileName = dataName + ".yml";
        this.file = new File("plugins/BlockStudio/Objects", fileName);
    }
    
    public int getVersion(){
        if(config.contains("version")){
            return config.getInt("version");
        }else{
            return CubeDataManager.version;
        }
    }
    
    public List<String> getCubeDataList(){
        return config.getStringList("cube-data");
    }
    
    public void setCubeDataList(List<String> cubeDataList){
        config.set("cube-data", cubeDataList);
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
        
        if(!config.contains("cube-data")) {
            config.set("version", CubeDataManager.version);
            config.set("cube-data", new ArrayList<String>());
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
