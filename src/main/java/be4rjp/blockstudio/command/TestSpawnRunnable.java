package be4rjp.blockstudio.command;

import be4rjp.blockstudio.api.BSObject;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;

public class TestSpawnRunnable extends BukkitRunnable {
    
    private final BSObject bsObject;
    
    private int i = 0;
    
    public TestSpawnRunnable(BSObject bsObject){
        this.bsObject = bsObject;
    }
    
    @Override
    public void run() {
        bsObject.setOriginByEulerAngle(new EulerAngle(Math.toRadians((double)i/2.0), Math.toRadians((double)i/2.0), Math.toRadians((double)i/2.0)));
        bsObject.move();
        
        if(i == 200){
            bsObject.remove();
            cancel();
        }
        
        i++;
    }
}
