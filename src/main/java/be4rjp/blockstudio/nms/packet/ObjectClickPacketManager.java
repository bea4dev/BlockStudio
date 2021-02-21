package be4rjp.blockstudio.nms.packet;

import be4rjp.blockstudio.BlockStudio;
import be4rjp.blockstudio.api.BSArmorStand;
import be4rjp.blockstudio.api.BSCube;
import be4rjp.blockstudio.api.BSObject;
import be4rjp.blockstudio.api.BlockStudioAPI;
import be4rjp.blockstudio.event.ObjectClickEvent;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class ObjectClickPacketManager {
    
    public static void checkAllObject(int entityID, Player player){
        
        BlockStudioAPI api = BlockStudio.getBlockStudioAPI();
        
        for(BSObject bsObject : api.getObjectList()){
            for(BSCube bsCube : bsObject.getBSCubeList()){
                BSArmorStand bsArmorStand = bsCube.getBSArmorStand();
                int armorStandID = bsArmorStand.getEntityID();
                if(entityID == armorStandID){
                    BukkitRunnable sync = new BukkitRunnable() {
                        @Override
                        public void run() {
                            ObjectClickEvent event = new ObjectClickEvent(bsObject, player);
                            BlockStudio.getPlugin().getServer().getPluginManager().callEvent(event);
                        }
                    };
                    sync.runTask(BlockStudio.getPlugin());
                }
            }
        }
    }
    
}
