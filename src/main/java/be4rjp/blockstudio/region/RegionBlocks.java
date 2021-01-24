package be4rjp.blockstudio.region;

import org.apache.commons.lang.Validate;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class RegionBlocks {
    private final World world;
    
    private final Vector maximum;
    private final Vector minimum;
    
    public RegionBlocks(Location firstPoint, Location secondPoint) {
        world = firstPoint.getWorld();
        Vector firstVector = firstPoint.toVector();
        Vector secondVector = secondPoint.toVector();
        maximum = Vector.getMaximum(firstVector, secondVector);
        minimum = Vector.getMinimum(firstVector, secondVector);
    }
    
    public List<Block> getBlocks() {
        List<Block> blocks = new ArrayList<>();
        for (int y = minimum.getBlockY(); y <= maximum.getBlockY(); y++)
            for (int x = minimum.getBlockX(); x <= maximum.getBlockX(); x++)
                for (int z = minimum.getBlockZ(); z <= maximum.getBlockZ(); z++)
                    blocks.add(world.getBlockAt(x, y, z));
        
        return blocks;
    }
}
