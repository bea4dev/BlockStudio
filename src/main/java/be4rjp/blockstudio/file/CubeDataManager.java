package be4rjp.blockstudio.file;

import be4rjp.blockstudio.api.BSCube;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class CubeDataManager {
    public static int version = 1;
    
    public static List<String> convertStringList(List<BSCube> bsCubeList){
        List<String> strings = new ArrayList<>();
        
        for(BSCube bsCube : bsCubeList){
    
            String line = "Material{" + bsCube.getHeadItemStack().getType().toString() + "} ";
            
            String itemName = "";
            if(bsCube.getHeadItemStack().hasItemMeta()) {
                if (bsCube.getHeadItemStack().getItemMeta().hasDisplayName()) {
                    itemName = bsCube.getHeadItemStack().getItemMeta().getDisplayName();
                }
            }
            line += "ItemName{" + itemName + "} ";
            
            Vector location = bsCube.getRelativeLocation();
            line += "Relative{" + location.getX() + ", " + location.getY() + ", " + location.getZ() + "} ";
    
            EulerAngle eulerAngle = bsCube.getBaseEulerAngle();
            double x = Math.toDegrees(eulerAngle.getX());
            double y = Math.toDegrees(eulerAngle.getY());
            double z = Math.toDegrees(eulerAngle.getZ());
            
            line += "Angle{" + x + ", " + y + ", " + z + "}";
            
            strings.add(line);
        }
        
        return strings;
    }
}
