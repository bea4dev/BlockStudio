package be4rjp.blockstudio.api;

import be4rjp.blockstudio.BlockStudio;
import be4rjp.blockstudio.file.ObjectData;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BSUtil {
    public static List<BSCube> createBSCubeListFromBlocks(Location baseLocation, BSObject bsObject, List<Block> blocks){
        List<BSCube> bsCubeList = new ArrayList<>();
    
        for(Block block : blocks){
            if(block.getType() == Material.AIR || block.getType() == Material.STRUCTURE_VOID) continue;
            Location bl = block.getLocation();
            Vector vector = new Vector(bl.getX() - baseLocation.getX(), bl.getY() - baseLocation.getY(), bl.getZ() - baseLocation.getZ());
            EulerAngle eulerAngle = new EulerAngle(0, 0, 0);
            BSCube bsCube = new BSCube(bsObject, baseLocation.getWorld(), vector, block.getType(), eulerAngle);
            bsCubeList.add(bsCube);
        }
        
        return  bsCubeList;
    }
    
    
    public static List<String> createCubeDataListFromBlocks(Location baseLocation, List<Block> blocks){
        List<String> bsCubeList = new ArrayList<>();
        
        for(Block block : blocks){
            if(block.getType() == Material.AIR || block.getType() == Material.STRUCTURE_VOID) continue;
            Location bl = block.getLocation();
            Vector location = new Vector(bl.getX() - baseLocation.getX(), bl.getY() - baseLocation.getY(), bl.getZ() - baseLocation.getZ());
            EulerAngle eulerAngle = new EulerAngle(0, 0, 0);
            
            String line = "Material{" + block.getType().toString() + "} ";
            
            line += "ItemName{} ";
            
            line += "Relative{" + location.getX() + ", " + location.getY() + ", " + location.getZ() + "} ";
            
            double x = Math.toDegrees(eulerAngle.getX());
            double y = Math.toDegrees(eulerAngle.getY());
            double z = Math.toDegrees(eulerAngle.getZ());
    
            line += "Angle{" + x + ", " + y + ", " + z + "}";
    
            bsCubeList.add(line);
        }
        
        return  bsCubeList;
    }
    
    
    public static List<BSCube> createBSCubeListFromObjectData(World world, BSObject bsObject, ObjectData objectData){
        List<BSCube> bsCubeList = new ArrayList<>();
        
        for(String line : objectData.getCubeDataList()){
            
            int mode = line.contains("ItemName") ? 0 : 1; //ItemName=0, CustomItemModel=1
            
            String convert = line.replace("Material", "");
            for (String s : Arrays.asList("ItemName", "CustomItemModel", "Relative", "Angle", " ", "{")) {
                convert = convert.replace(s, "");
            }
    
            String[] args = convert.split("}");
            
            
            Material material = Material.getMaterial(args[0]);
            String itemNameOrModelID = args[1];
            
            if(material == null) continue;
            if(material == Material.AIR) continue;
            
            ItemStack itemStack = new ItemStack(material);
            ItemMeta itemMeta = itemStack.getItemMeta();
            if(itemMeta != null) {
                if(mode == 0) {
                    itemMeta.setDisplayName(itemNameOrModelID);
                }else {
                    try {
                        itemMeta.setCustomModelData(Integer.parseInt(itemNameOrModelID));
                    } catch (NoSuchMethodError e) {
                        BlockStudio.getPlugin().errorMessage("Failed to create an object from object data." +
                                "'CustomItemModel' can only be used with 1.14 or higher.");
                    }
                }
            }
            itemStack.setItemMeta(itemMeta);
            
            String[] locations = args[2].split(",");
            
            Vector vector = new Vector(Double.parseDouble(locations[0]), Double.parseDouble(locations[1]), Double.parseDouble(locations[2]));
            
            String[] angles = args[3].split(",");
            
            double x = Double.parseDouble(angles[0]);
            double y = Double.parseDouble(angles[1]);
            double z = Double.parseDouble(angles[2]);
            
            EulerAngle eulerAngle = new EulerAngle(Math.toRadians(x), Math.toRadians(y), Math.toRadians(z));
            
            BSCube bsCube = new BSCube(bsObject, world, vector, itemStack, eulerAngle);
            bsCubeList.add(bsCube);
        }
        
        return  bsCubeList;
    }
}
