package be4rjp.blockstudio.api;

import be4rjp.blockstudio.file.ObjectData;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class BSUtil {
    public static List<BSCube> createBSCubeListFromBlocks(Location baseLocation, BSObject bsObject, List<Block> blocks){
        List<BSCube> bsCubeList = new ArrayList<>();
    
        for(Block block : blocks){
            if(block.getType() == Material.AIR) continue;
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
            if(block.getType() == Material.AIR) continue;
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
    
    
    public static List<BSCube> createBSCubeListFromObjectData(Location baseLocation, BSObject bsObject, ObjectData objectData){
        List<BSCube> bsCubeList = new ArrayList<>();
        
        for(String line : objectData.getCubeDataList()){
            
            String convert = line.replace("Material", "");
            convert = convert.replace("ItemName", "");
            convert = convert.replace("Relative", "");
            convert = convert.replace("Angle", "");
            convert = convert.replace(" ", "");
            convert = convert.replace("{", "");
            
            String[] args = convert.split("}");
            
            
            Material material = Material.getMaterial(args[0]);
            String itemName = args[1];
            
            if(material == Material.AIR) continue;
    
            ItemStack itemStack = new ItemStack(material);
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName(itemName);
            itemStack.setItemMeta(itemMeta);
            
            String[] locations = args[2].split(",");
            
            Vector vector = new Vector(Double.valueOf(locations[0]), Double.valueOf(locations[1]), Double.valueOf(locations[2]));
            
            String[] angles = args[3].split(",");
            
            double x = Double.valueOf(angles[0]);
            double y = Double.valueOf(angles[1]);
            double z = Double.valueOf(angles[2]);
            
            EulerAngle eulerAngle = new EulerAngle(Math.toRadians(x), Math.toRadians(y), Math.toRadians(z));
            
            BSCube bsCube = new BSCube(bsObject, baseLocation.getWorld(), vector, itemStack, eulerAngle);
            bsCubeList.add(bsCube);
        }
        
        return  bsCubeList;
    }
}
