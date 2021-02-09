package be4rjp.blockstudio.api;

import be4rjp.blockstudio.BlockStudio;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Arrays;

public class ObjectAnimationRunnable extends BukkitRunnable {
    
    private Vector tickMove = new Vector(0, 0, 0);
    private double maxMove = 0;
    private int loopOption = 0;
    
    private final Location originalLocation;
    private final BSObject bsObject;
    
    private double rotation = 0;
    private double angle = 0;
    
    //-move[0.0, 0.0, 0.0],[0.0],[0] -rotation[0.0]
    public ObjectAnimationRunnable(BSObject bsObject, String optionLine){
        this.bsObject = bsObject;
        this.originalLocation = bsObject.getBaseLocation().clone();
        
        optionLine = optionLine.replace(" ", "");
        optionLine = optionLine.replace("[", "");
        optionLine = optionLine.replace("]", "");
        String[] options = optionLine.split("/");
        
        for(String option : Arrays.asList(options)){
            if(option.contains("move")){
                String data = option.replace("move", "");
                String[] dataArr = data.split(",");
                
                tickMove = new Vector(Double.parseDouble(dataArr[0]), Double.parseDouble(dataArr[1]), Double.parseDouble(dataArr[2]));
                maxMove = Double.parseDouble(dataArr[3]);
                
                if(dataArr.length == 5)
                    loopOption = Integer.parseInt(dataArr[4]);
            }
            
            if(option.contains("rotation")){
                String data = option.replace("rotation", "");
                rotation = Double.parseDouble(data);
            }
        }
    }
    
    @Override
    public void run() {
        if(originalLocation.distance(bsObject.getBaseLocation()) < maxMove){
            bsObject.getBaseLocation().add(tickMove);
        }else{
            switch (loopOption){
                case 0:
                    bsObject.setBaseLocation(originalLocation.clone());
                    break;
                default:
                    break;
            }
        }
        
        angle+=rotation;
        if(rotation >= 0){
            if(angle >= 360.0)
                angle-=360.0;
        }else{
            if(angle <= -360.0)
                angle+=360.0;
        }
        
        bsObject.setDirectionRotation(bsObject.getZVector().clone(), angle);
        
        bsObject.move();
    }
    
    public void startTask(){
        this.runTaskTimerAsynchronously(BlockStudio.getPlugin(), 0, 1);
    }
}
