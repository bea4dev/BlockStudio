package be4rjp.blockstudio.api;

import be4rjp.blockstudio.BlockStudio;
import be4rjp.blockstudio.file.*;
import be4rjp.blockstudio.region.RegionBlocks;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.*;

public class BlockStudioAPI {
    
    private final JavaPlugin plugin;
    private final Map<String, BSObject> objectMap;
    private final List<BSObject> objectList;
    private final Map<String, ObjectData> objectDataMap;
    private final List<ObjectData> objectDataList;
    private final Map<Chunk, BSCustomBlockChunk> bsCustomBlockChunkMap;
    
    private double defaultViewDistance;
    private double customBlockViewDistance;
    
    private List<ItemStack> blockItemList;
    private Map<String, ItemStack> blockItemNameMap;
    private Map<ItemStack, String> nameBlockItemMap;
    
    private Config customBlockDataConfig;
    private Config customBlockConfig;
    
    
    
    /**
     * Create API instance.
     * @param plugin JavaPlugin that tasks will be allocated on.
     */
    public BlockStudioAPI(JavaPlugin plugin, double defaultViewDistance, double customBlockViewDistance){
        this.plugin = plugin;
        this.defaultViewDistance = defaultViewDistance;
        this.customBlockViewDistance = customBlockViewDistance;
        
        this.objectMap = new HashMap<>();
        this.objectList = new ArrayList<>();
        this.objectDataMap = new HashMap<>();
        this.objectDataList = new ArrayList<>();
        this.bsCustomBlockChunkMap = new HashMap<>();
    
        this.blockItemList = new ArrayList<>();
        this.blockItemNameMap = new HashMap<>();
        this.nameBlockItemMap = new HashMap<>();
    
        this.customBlockDataConfig = new Config(plugin, "CustomBlock/custom-block-data.yml");
        this.customBlockDataConfig.saveDefaultConfig();
    
        this.customBlockConfig = new Config(plugin, "CustomBlock/custom-block.yml");
        this.customBlockConfig.saveDefaultConfig();
    }
    
    
    public JavaPlugin getPlugin() {return plugin;}
    
    public double getDefaultViewDistance() {return defaultViewDistance;}
    
    public double getCustomBlockViewDistance(){return customBlockViewDistance;}
    
    public List<BSObject> getObjectList() {return objectList;}
    
    public List<ObjectData> getObjectDataList() {return objectDataList;}
    
    public Map<String, BSObject> getObjectMap() {return objectMap;}
    
    public Map<String, ObjectData> getObjectDataMap() {return objectDataMap;}
    
    public void setDefaultViewDistance(double defaultViewDistance) {this.defaultViewDistance = defaultViewDistance;}
    
    public Map<Chunk, BSCustomBlockChunk> getBSCustomBlockChunkMap(){return bsCustomBlockChunkMap;}
    
    public Config getCustomBlockConfig(){return customBlockConfig;}
    
    public Config getCustomBlockDataConfig(){return customBlockDataConfig;}
    
    public List<ItemStack> getBlockItemList(){return blockItemList;}
    
    public Map<ItemStack, String> getNameBlockItemMap(){return nameBlockItemMap;}
    
    
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
        List<BSCube> bsCubeList = BSUtil.createBSCubeListFromObjectData(baseLocation.getWorld(), bsObject, objectData);
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
     * Loads and creates ObjectData from all yml files in plugins/BlockStudio/Data/.
     * plugins/BlockStudio/Data/に存在するすべてのymlファイルからObjectDataを読み込み作成します
     */
    public void loadAllObjectData(){
        objectDataList.clear();
        objectDataMap.clear();
        
        File dir = new File("plugins/BlockStudio/Data");
        
        dir.mkdir();
        File[] files = dir.listFiles();
    
        if(files.length == 0){
            plugin.saveResource("Data/example-data.yml", false);
            files = dir.listFiles();
        }
        
        if(files != null) {
            for (File file : Arrays.asList(files)) {
                if (!file.getName().contains(".yml")) continue;
        
                String name = file.getName().replace(".yml", "");
                ObjectData objectData = new ObjectData(name);
                objectData.loadFile();
                
                if(!objectData.isObjectData()) continue;
        
                objectDataList.add(objectData);
                objectDataMap.put(name, objectData);
            }
        }
    }
    
    
    /**
     * Loads and spawns object from all yml files in plugins/BlockStudio/Objects/.
     * plugins/BlockStudio/Objects/に存在するすべてのymlファイルからオブジェクトを読み込み作成します
     */
    public void spawnAllObjects(){
        File dir = new File("plugins/BlockStudio/Objects");
        
        dir.mkdir();
        File[] files = dir.listFiles();
        
        if(files.length == 0){
            plugin.saveResource("Objects/example-object.yml", false);
            files = dir.listFiles();
        }
        
        if(files != null) {
            for (File file : Arrays.asList(files)) {
                if (!file.getName().contains(".yml")) continue;
                
                String name = file.getName().replace(".yml", "");
                ObjectConfig objectConfig = new ObjectConfig(name);
                objectConfig.loadFile();
                
                if(!objectConfig.isObjectConfig()) continue;
                
                objectConfig.spawnObject();
            }
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
    
    
    /**
     *
     * @param block
     * @param blockItem
     * @return
     */
    public BSCustomBlock setCustomBlock(Block block, ItemStack blockItem){
        BSCustomBlockChunk bsCustomBlockChunk = bsCustomBlockChunkMap.get(block.getChunk());
        
        if(bsCustomBlockChunk == null){
            bsCustomBlockChunk = new BSCustomBlockChunk(this, block.getChunk());
            bsCustomBlockChunk.startTaskAsync(100);
            bsCustomBlockChunkMap.put(block.getChunk(), bsCustomBlockChunk);
        }
        
        BSCustomBlock bsCustomBlock = new BSCustomBlock(bsCustomBlockChunk, block, blockItem);
        bsCustomBlockChunk.getBlockList().add(bsCustomBlock);
        
        return bsCustomBlock;
    }
    
    
    public BSCustomBlock addCustomBlock(Block block, ItemStack blockItem, String modelName){
        List<String> list = customBlockConfig.getConfig().getStringList("custom-blocks");
        list.add(modelName + ", " + block.getWorld().getName() + ", " + block.getX() + ", " + block.getY() + ", " + block.getZ());
        customBlockConfig.getConfig().set("custom-blocks", list);
        
        return setCustomBlock(block, blockItem);
    }
    
    
    public void breakCustomBlock(Block block){
        BSCustomBlockChunk bsCustomBlockChunk = bsCustomBlockChunkMap.get(block.getChunk());
    
        if(bsCustomBlockChunk == null){
            return;
        }
        bsCustomBlockChunk.getBlockList().forEach(bsCustomBlock -> {
            if(bsCustomBlock.getBlock().getX() == block.getX() && bsCustomBlock.getBlock().getY() == block.getY() && bsCustomBlock.getBlock().getZ() == block.getZ()){
                bsCustomBlock.breakBlock();
            }
        });
    
        bsCustomBlockChunk.getBlockList().removeIf(bsCustomBlock -> bsCustomBlock.getBlock() == block);
        
        List<String> blockList = customBlockConfig.getConfig().getStringList("custom-blocks");
        blockList.removeIf(line -> {
            Location location = ConfigUtil.toLocation(ConfigUtil.getLocationLine(line));
            return location.getBlockX() == block.getX() && location.getBlockY() == block.getY() && location.getBlockZ() == block.getZ();
        });
        customBlockConfig.getConfig().set("custom-blocks", blockList);
    }
    
    
    public void loadAndSpawnAllCustomBlocks(){
        customBlockDataConfig.getConfig();
        customBlockConfig.getConfig();
        
        blockItemList.clear();
        blockItemNameMap.clear();
    
        for (String item : customBlockDataConfig.getConfig().getConfigurationSection("custom-model-data").getKeys(false)){
            String material = customBlockDataConfig.getConfig().getString("custom-model-data." + item + ".material");
            String name = customBlockDataConfig.getConfig().getString("custom-model-data." + item + ".display-name");
            int id = customBlockDataConfig.getConfig().getInt("custom-model-data." + item + ".model-id");
            
            ItemStack itemStack = new ItemStack(Material.getMaterial(material));
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName(name);
            itemMeta.setCustomModelData(id);
            itemStack.setItemMeta(itemMeta);
            
            blockItemList.add(itemStack);
            blockItemNameMap.put(item, itemStack);
            nameBlockItemMap.put(itemStack, item);
        }
    
        customBlockConfig.getConfig().getStringList("custom-blocks").forEach(line -> {
            Location location = ConfigUtil.toLocation(ConfigUtil.getLocationLine(line));
            String modelName = ConfigUtil.getCustomModel(line);
            
            ItemStack itemStack = blockItemNameMap.get(modelName);
            
            if(itemStack == null){
                BlockStudio.getPlugin().errorMessage("Model data : " + modelName + " is not found!");
                return;
            }
            
            setCustomBlock(location.getBlock(), itemStack);
        });
    }
}
