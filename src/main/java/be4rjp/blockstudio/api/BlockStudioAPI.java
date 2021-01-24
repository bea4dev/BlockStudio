package be4rjp.blockstudio.api;

import be4rjp.blockstudio.file.CubeDataManager;
import be4rjp.blockstudio.file.ObjectData;
import be4rjp.blockstudio.region.RegionBlocks;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.*;

public class BlockStudioAPI {
    
    private final JavaPlugin plugin;
    private final Map<String, BSObject> objectMap;
    private final List<BSObject> objectList;
    private final Map<String, ObjectData> objectDataMap;
    private final List<ObjectData> objectDataList;
    
    private double defaultViewDistance;
    
    
    /**
     * Create API instance.
     * @param plugin JavaPlugin that tasks will be allocated on.
     */
    public BlockStudioAPI(JavaPlugin plugin, double defaultViewDistance){
        this.plugin = plugin;
        this.defaultViewDistance = defaultViewDistance;
        
        this.objectMap = new HashMap<>();
        this.objectList = new ArrayList<>();
        this.objectDataMap = new HashMap<>();
        this.objectDataList = new ArrayList<>();
    }
    
    
    public JavaPlugin getPlugin() {return plugin;}
    
    public double getDefaultViewDistance() {return defaultViewDistance;}
    
    public List<BSObject> getObjectList() {return objectList;}
    
    public List<ObjectData> getObjectDataList() {return objectDataList;}
    
    public Map<String, BSObject> getObjectMap() {return objectMap;}
    
    public Map<String, ObjectData> getObjectDataMap() {return objectDataMap;}
    
    public void setDefaultViewDistance(double defaultViewDistance) {this.defaultViewDistance = defaultViewDistance;}
    
    
    /**
     * Checks for the existence of an object with the specified name.
     * 指定された名前のオブジェクトが存在するかどうかをチェックします
     * @param objectName
     * @return boolean
     */
    public boolean hasNamedObject(String objectName){
        return objectMap.containsKey(objectName);
    }
    
    
    /**
     * Checks for the existence of an object data with the specified name.
     * 指定された名前のオブジェクトデータが存在するかどうかをチェックします
     * @param objectDataName
     * @return boolean
     */
    public boolean hasNamedObjectData(String objectDataName){
        return objectDataMap.containsKey(objectDataName);
    }
    
    
    /**
     * Get an object with the specified name.
     * 指定された名前のオブジェクトを取得します
     * @param objectName
     * @return BSObject
     */
    public BSObject getBSObject(String objectName){
        return objectMap.get(objectName);
    }
    
    
    /**
     * Get an objectData with the specified name.
     * 指定された名前のオブジェクトデータを取得します
     * @param objectDataName
     * @return BSObject
     */
    public ObjectData getObjectData(String objectDataName){
        return objectDataMap.get(objectDataName);
    }
    
    
    /**
     * Create empty object.
     * 空のオブジェクトを生成します
     * @param objectName Object's name.
     * @param baseLocation Spawn an object at this location.
     * @param viewDistance Object drawing distance.
     * @param useBukkitArmorStand Whether to use BukkitArmorStand.
     *                            If you specify true for this argument,
     *                            you will not be able to move the object asynchronously.
     * @return BSObject Created object.
     */
    public BSObject createEmptyObject(String objectName, Location baseLocation, double viewDistance, boolean useBukkitArmorStand){
        BSObject bsObject = new BSObject(this, objectName, baseLocation, viewDistance, useBukkitArmorStand);
        objectList.add(bsObject);
        objectMap.put(objectName, bsObject);
    
        return bsObject;
    }
    
    
    /**
     * Create empty object.
     * 空のオブジェクトを生成します
     * @param objectName Object's name.
     * @param baseLocation Spawn an object at this location.
     * @param useBukkitArmorStand Whether to use BukkitArmorStand.
     *                            If you specify true for this argument,
     *                            you will not be able to move the object asynchronously.
     * @return BSObject Created object.
     */
    public BSObject createEmptyObject(String objectName, Location baseLocation, boolean useBukkitArmorStand){
        return createEmptyObject(objectName, baseLocation, defaultViewDistance, useBukkitArmorStand);
    }
    
    
    /**
     * Creates an object from a list of specified blocks.
     * 指定されたブロックのリストからオブジェクトを生成します
     * @param objectName Object name.
     * @param baseLocation Spawn a object at this location.
     * @param blocks Block list to convert object.
     * @param viewDistance Object drawing distance.
     * @param useBukkitArmorStand Whether to use BukkitArmorStand.
     *                            If you specify true for this argument,
     *                            you will not be able to move the object asynchronously.
     * @return BSObject Created object.
     */
    public BSObject createObjectFromBlocks(String objectName, Location baseLocation, List<Block> blocks, double viewDistance, boolean useBukkitArmorStand){
        BSObject bsObject = new BSObject(this, objectName, baseLocation, viewDistance, useBukkitArmorStand);
        List<BSCube> bsCubeList = BSUtil.createBSCubeListFromBlocks(baseLocation, bsObject, blocks);
        bsObject.setBSCubeList(bsCubeList);
        
        objectList.add(bsObject);
        objectMap.put(objectName, bsObject);
        
        return bsObject;
    }
    
    
    /**
     * Creates an object from a list of specified blocks.
     * 指定されたブロックのリストからオブジェクトを生成します
     * @param objectName Object name.
     * @param baseLocation Spawn a object at this location.
     * @param blocks Block list to convert object.
     * @param useBukkitArmorStand Whether to use BukkitArmorStand.
     *                            If you specify true for this argument,
     *                            you will not be able to move the object asynchronously.
     * @return BSObject Created object.
     */
    public BSObject createObjectFromBlocks(String objectName, Location baseLocation, List<Block> blocks, boolean useBukkitArmorStand){
        
        return createObjectFromBlocks(objectName, baseLocation, blocks, defaultViewDistance, useBukkitArmorStand);
    }
    
    
    /**
     * Create object from blocks in specified region.
     * 指定された範囲内にあるブロックからオブジェクトを生成します
     * @param objectName Object name.
     * @param baseLocation Spawn a object at this location.
     * @param firstLocation Region's first position.
     * @param secondLocation Region's second position.
     * @param viewDistance Object drawing distance.
     * @param useBukkitArmorStand Whether to use BukkitArmorStand.
     *                            If you specify true for this argument,
     *                            you will not be able to move the object asynchronously.
     * @return BSObject Created object.
     */
    public BSObject createObjectFromRegion(String objectName, Location baseLocation, Location firstLocation,
                                           Location secondLocation, double viewDistance, boolean useBukkitArmorStand){
        BSObject bsObject = new BSObject(this, objectName, baseLocation, viewDistance, useBukkitArmorStand);
        List<Block> blocks = new RegionBlocks(firstLocation, secondLocation).getBlocks();
        List<BSCube> bsCubeList = BSUtil.createBSCubeListFromBlocks(baseLocation, bsObject, blocks);
        bsObject.setBSCubeList(bsCubeList);
        
        objectList.add(bsObject);
        objectMap.put(objectName, bsObject);
        
        return bsObject;
    }
    
    
    /**
     * Create object from blocks in specified region.
     * 指定された範囲内にあるブロックからオブジェクトを生成します
     * @param objectName Object name.
     * @param baseLocation Spawn a object at this location.
     * @param firstLocation Region's first position.
     * @param secondLocation Region's second position.
     * @param useBukkitArmorStand Whether to use BukkitArmorStand.
     *                            If you specify true for this argument,
     *                            you will not be able to move the object asynchronously.
     * @return BSObject Created object.
     */
    public BSObject createObjectFromRegion(String objectName, Location baseLocation, Location firstLocation,
                                           Location secondLocation, boolean useBukkitArmorStand){
        return createObjectFromRegion(objectName, baseLocation, firstLocation, secondLocation, defaultViewDistance, useBukkitArmorStand);
    }
    
    
    /**
     * Create a BSObject from ObjectData.
     * ObjectDataからBSObjectを作成します。
     * @param objectName Object name.
     * @param baseLocation Spawn a object at this location.
     * @param objectData Object data.
     * @param viewDistance Object drawing distance.
     * @param useBukkitArmorStand Whether to use BukkitArmorStand.
     *                            If you specify true for this argument,
     *                            you will not be able to move the object asynchronously.
     * @return BSObject Created object.
     */
    public BSObject createObjectFromObjectData(String objectName, Location baseLocation,
                                               ObjectData objectData, double viewDistance, boolean useBukkitArmorStand){
    
        BSObject bsObject = new BSObject(this, objectName, baseLocation, viewDistance, useBukkitArmorStand);
        List<BSCube> bsCubeList = BSUtil.createBSCubeListFromObjectData(baseLocation, bsObject, objectData);
        bsObject.setBSCubeList(bsCubeList);
    
        objectList.add(bsObject);
        objectMap.put(objectName, bsObject);
    
        return bsObject;
    }
    
    
    /**
     * Create a BSObject from ObjectData.
     * ObjectDataからBSObjectを作成します。
     * @param objectName Object name.
     * @param baseLocation Spawn a object at this location.
     * @param objectData Object data.
     * @param useBukkitArmorStand Whether to use BukkitArmorStand.
     *                            If you specify true for this argument,
     *                            you will not be able to move the object asynchronously.
     * @return BSObject Created object.
     */
    public BSObject createObjectFromObjectData(String objectName, Location baseLocation,
                                               ObjectData objectData, boolean useBukkitArmorStand){
        
        return createObjectFromObjectData(objectName, baseLocation, objectData, defaultViewDistance, useBukkitArmorStand);
    }
    
    
    /**
     * Loads and creates ObjectData from all yml files in plugins/BlockStudio/Objects/.
     * plugins/BlockStudio/Objects/に存在するすべてのymlファイルからObjectDataを読み込み作成します
     */
    public void loadAllObjectData(){
        objectDataList.clear();
        objectDataMap.clear();
        
        File dir = new File("plugins/BlockStudio/Objects");
        
        dir.mkdir();
        File[] files = dir.listFiles();
    
        for(File file : Arrays.asList(files)){
            if(!file.getName().contains(".yml")) continue;
    
            String name = file.getName().replace(".yml", "");
            ObjectData objectData = new ObjectData(name);
            objectData.loadFile();
    
            objectDataList.add(objectData);
            objectDataMap.put(name, objectData);
        }
    }
    
    
    /**
     * Create ObjectData from BSObject.
     * BSObjectからObjectDataを作成します。
     * @param dataName Object data name.
     * @param bsObject BSObject.
     * @return ObjectData Created object data.
     */
    public ObjectData createObjectDataFromBSObject(String dataName, BSObject bsObject){
        ObjectData objectData = new ObjectData(dataName);
        objectData.loadFile();
        objectData.setCubeDataList(CubeDataManager.convertStringList(bsObject.getBSCubeList()));
        
        objectDataList.add(objectData);
        objectDataMap.put(dataName, objectData);
        
        return objectData;
    }
    
    
    /**
     * Creates ObjectData from the specified range.
     * 指定された範囲からObjectDataを作成します。
     * @param dataName Object data name.
     * @param baseLocation Base location
     * @param firstLocation Region's first position.
     * @param secondLocation Region's second position.
     * @return ObjectData Created object data.
     */
    public ObjectData createObjectDataFromRegion(String dataName, Location baseLocation, Location firstLocation,
                                                 Location secondLocation){
        ObjectData objectData = new ObjectData(dataName);
        objectData.loadFile();
    
        List<Block> blocks = new RegionBlocks(firstLocation, secondLocation).getBlocks();
        
        objectData.setCubeDataList(BSUtil.createCubeDataListFromBlocks(baseLocation, blocks));
        
        objectDataList.add(objectData);
        objectDataMap.put(dataName, objectData);
        
        return objectData;
    }
    
    
    /**
     * Creates an ObjectData from a list of specified blocks.
     * 指定されたブロックのリストからオブジェクトデータを生成します
     * @param dataName Object data name.
     * @param baseLocation Base location
     * @param blocks Block list to convert ObjectData.
     * @return ObjectData Created object data.
     */
    public ObjectData createObjectDataFromBlocks(String dataName, Location baseLocation, List<Block> blocks){
        ObjectData objectData = new ObjectData(dataName);
        objectData.loadFile();
        
        objectData.setCubeDataList(BSUtil.createCubeDataListFromBlocks(baseLocation, blocks));
        
        objectDataList.add(objectData);
        objectDataMap.put(dataName, objectData);
        
        return objectData;
    }
}
