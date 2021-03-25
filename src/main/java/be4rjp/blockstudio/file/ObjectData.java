package be4rjp.blockstudio.file;


import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.util.List;

public class ObjectData{
    
    private final String dataName;
    private final String fileName;
    private FileConfiguration config;
    private File file;
    
    public ObjectData(String dataName){
        this.dataName = dataName;
        this.fileName = dataName + ".yml";
        this.file = new File("plugins/BlockStudio/Data", fileName);
    }
    
    public int getVersion(){
        if(config.contains("version")){
            return config.getInt("version");
        }else{
            return CubeDataManager.VERSION;
        }
    }
    
    public String getDataName() {return dataName;}
    
    public List<String> getCubeDataList(){
        return config.getStringList("cube-data");
    }
    
    public void setCubeDataList(List<String> cubeDataList){
        config.set("version", CubeDataManager.VERSION);
        config.set("cube-data", cubeDataList);
    }
    
    public boolean isObjectData(){return config.contains("cube-data");}
    
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
