package be4rjp.blockstudio.file;

import be4rjp.blockstudio.BlockStudio;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

public class ConfigUtil {
    public static Location toLocation(String locString){
        
        locString = locString.replace(" ", "");
        
        String[] args = locString.split(",");
        
        if(args.length != 4) throw new IllegalArgumentException("Failed to load the location data from the configuration." +
                "'location' must be written in the format 'world, x, y, z'.");
        
        World world = Bukkit.getWorld(args[0]);
        
        if(world == null){
            BlockStudio.getPlugin().getLogger().info("Loading world [ " + args[0] + " ]");
            Bukkit.createWorld(new WorldCreator(args[0]));
            world = Bukkit.getWorld(args[0]);
        }
        
        double x = Double.parseDouble(args[1]);
        double y = Double.parseDouble(args[2]);
        double z = Double.parseDouble(args[3]);
        
        return new Location(world, x, y, z, 0, 0);
    }
    
    public static Vector toDirection(String directionData){
        
        directionData = directionData.replace(" ", "");
        
        String[] args = directionData.split(",");
        
        if(args.length != 3) throw new IllegalArgumentException("Failed to load the direction data from the configuration." +
                "'direction' must be written in the format 'x, y, z'.");
        
        double x = Double.parseDouble(args[0]);
        double y = Double.parseDouble(args[1]);
        double z = Double.parseDouble(args[2]);
        
        return new Vector(x, y, z);
    }
    
    public static EulerAngle toEulerAngle(String angleData){
    
        angleData = angleData.replace(" ", "");
        
        String[] args = angleData.split(",");
        
        if(args.length != 3) throw new IllegalArgumentException("Failed to load the EulerAngle data from the configuration." +
                "'euler-angle' must be written in the format 'x, y, z'.");
        
        double x = Double.parseDouble(args[0]);
        double y = Double.parseDouble(args[1]);
        double z = Double.parseDouble(args[2]);
        
        return new EulerAngle(Math.toRadians(x), Math.toRadians(y), Math.toRadians(z));
    }
    
    public static String getLocationLine(String line){
        String temp1 = line.replace(" ", "");
        String[] tempArr = temp1.split(",");
        
        return temp1.replace(tempArr[0] + ",", "");
    }
    
    public static String getCustomModel(String line){
        String temp1 = line.replace(" ", "");
        String[] tempArr = temp1.split(",");
        
        return tempArr[0];
    }
}
