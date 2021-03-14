package be4rjp.blockstudio.api;

import be4rjp.blockstudio.BlockStudio;
import be4rjp.blockstudio.nms.NMSUtil;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.EulerAngle;

public class BSArmorStand {
    private final BSCube bsCube;
    private final BSCustomBlock bsCustomBlock;
    private Location location;
    private ArmorStand armorStand;
    private Object entityArmorStand;
    
    public BSArmorStand(BSCube bsCube, Location location, boolean useBukkitAPI){
        this.bsCube = bsCube;
        this.bsCustomBlock = null;
        this.location = location;
        if(useBukkitAPI){
            this.armorStand = location.getWorld().spawn(location, ArmorStand.class, armorStand -> {
                armorStand.setVisible(false);
                armorStand.setGravity(false);
                armorStand.setMarker(true);
            });
        }else{
            try {
                this.entityArmorStand = NMSUtil.createEntityArmorStand(
                        bsCube.getLocation().getWorld(), bsCube.getLocation().getX(), bsCube.getLocation().getY(), bsCube.getLocation().getZ());
                NMSUtil.setEntityPositionRotation(this.entityArmorStand, location.getX(), location.getY(), location.getZ(), 0F, 0F);
                
                for(Player player : bsCube.getBsObject().getPlayers()) {
                    if (player == null) continue;
                    if(player.getWorld() !=  bsCube.getLocation().getWorld()) continue;
                    NMSUtil.sendSpawnEntityLivingPacket(player, this.entityArmorStand);
                    NMSUtil.sendEntityMetadataPacket(player, this.entityArmorStand);
                }
            } catch (Exception e) {
                if(BlockStudio.getPlugin().getLogLevel() >= 2)
                    e.printStackTrace();
            }
        }
    }
    
    
    public BSArmorStand(BSCustomBlock bsCustomBlock, Location location, boolean useBukkitAPI){
        this.bsCube = null;
        this.bsCustomBlock = bsCustomBlock;
        this.location = location;
        if(useBukkitAPI){
            this.armorStand = location.getWorld().spawn(location, ArmorStand.class, armorStand -> {
                armorStand.setVisible(false);
                armorStand.setGravity(false);
                armorStand.setMarker(true);
            });
        }else{
            try {
                this.entityArmorStand = NMSUtil.createEntityArmorStand(
                        bsCustomBlock.getBlock().getWorld(), bsCustomBlock.getBlock().getX() + 0.5, bsCustomBlock.getBlock().getY(), bsCustomBlock.getBlock().getZ() + 0.5);
                NMSUtil.setEntityPositionRotation(this.entityArmorStand, bsCustomBlock.getBlock().getX() + 0.5, bsCustomBlock.getBlock().getY(), bsCustomBlock.getBlock().getZ() + 0.5, 0F, 0F);
                
                for(Player player : bsCustomBlock.getBSCustomBlockChunk().getPlayers()) {
                    if (player == null) continue;
                    if(player.getWorld() !=  bsCustomBlock.getBlock().getWorld()) continue;
                    NMSUtil.sendSpawnEntityLivingPacket(player, this.entityArmorStand);
                    NMSUtil.sendEntityMetadataPacket(player, this.entityArmorStand);
                }
            } catch (Exception e) {
                if(BlockStudio.getPlugin().getLogLevel() >= 2)
                    e.printStackTrace();
            }
        }
    }
    
    
    public int getEntityID() {
        if (this.armorStand != null) {
            return this.armorStand.getEntityId();
        } else {
            try {
                return NMSUtil.getEntityID(this.entityArmorStand);
            }catch (Exception e){
                if(BlockStudio.getPlugin().getLogLevel() >= 2)
                    e.printStackTrace();
            }
        }
        return 0;
    }
    
    public ArmorStand getBukkitArmorStand() {return armorStand;}
    
    
    public Object getNMSArmorStand() {return entityArmorStand;}
    
    
    public void setHelmet(ItemStack itemStack){
        if(this.armorStand != null){
            this.armorStand.setHelmet(itemStack);
        }else{
            try {
                if(bsCube != null) {
                    for (Player player : bsCube.getBsObject().getPlayers()) {
                        if (player == null) continue;
                        if (player.getWorld() != bsCube.getLocation().getWorld()) continue;
                        sendHelmetEquipmentPacket(player, itemStack);
                    }
                }else{
                    for (Player player : bsCustomBlock.getBSCustomBlockChunk().getPlayers()) {
                        if (player == null) continue;
                        if (player.getWorld() != bsCustomBlock.getBlock().getWorld()) continue;
                        sendHelmetEquipmentPacket(player, itemStack);
                    }
                }
            } catch (Exception e) {
                if(BlockStudio.getPlugin().getLogLevel() >= 2)
                    e.printStackTrace();
            }
        }
    }
    
    public void sendHelmetEquipmentPacket(Player player, ItemStack itemStack){
        try{
            NMSUtil.sendEntityEquipmentPacket(player, this.entityArmorStand, itemStack);
        } catch (Exception e) {
            if(BlockStudio.getPlugin().getLogLevel() >= 2)
                e.printStackTrace();
        }
    }
    
    public void sendSpawnPacket(Player player){
        if(this.entityArmorStand != null) {
            try {
                NMSUtil.sendSpawnEntityLivingPacket(player, this.entityArmorStand);
                NMSUtil.sendEntityMetadataPacket(player, this.entityArmorStand);
            } catch (Exception e) {
                if(BlockStudio.getPlugin().getLogLevel() >= 2)
                    e.printStackTrace();
            }
            
            if(bsCube != null)
                sendHelmetEquipmentPacket(player, bsCube.getHeadItemStack());
            else
                sendHelmetEquipmentPacket(player, bsCustomBlock.getItemStack());
        }
    }
    
    public void sendDestroyPacket(Player player){
        if(this.entityArmorStand != null) {
            try{
                NMSUtil.sendEntityDestroyPacket(player, this.entityArmorStand);
            } catch (Exception e) {
                if(BlockStudio.getPlugin().getLogLevel() >= 2)
                    e.printStackTrace();
            }
        }
    }
    
    public void teleport(Location location){
        if(this.armorStand != null){
            this.armorStand.teleport(location);
        }else{
            try {
                NMSUtil.setEntityPositionRotation(this.entityArmorStand, location.getX(), location.getY(), location.getZ(), 0F, 0F);
                if(bsCube != null) {
                    for (Player player : bsCube.getBsObject().getPlayers()) {
                        if (player == null) continue;
                        if (player.getWorld() != bsCube.getLocation().getWorld()) continue;
                        NMSUtil.sendEntityTeleportPacket(player, this.entityArmorStand);
                    }
                }else{
                    for (Player player : bsCustomBlock.getBSCustomBlockChunk().getPlayers()) {
                        if (player == null) continue;
                        if (player.getWorld() != bsCustomBlock.getBlock().getWorld()) continue;
                        NMSUtil.sendEntityTeleportPacket(player, this.entityArmorStand);
                    }
                }
            } catch (Exception e) {
                if(BlockStudio.getPlugin().getLogLevel() >= 2)
                    e.printStackTrace();
            }
        }
    }
    
    public void setHeadPose(EulerAngle eulerAngle){
        if(this.armorStand != null){
            this.armorStand.setHeadPose(eulerAngle);
        }else{
            try{
                NMSUtil.setArmorStandHeadRotation(this.entityArmorStand, (float)Math.toDegrees(eulerAngle.getX()), (float)Math.toDegrees(eulerAngle.getY()), (float)Math.toDegrees(eulerAngle.getZ()));
                if(bsCube != null) {
                    for (Player player : bsCube.getBsObject().getPlayers()) {
                        if (player == null) continue;
                        if (player.getWorld() != bsCube.getLocation().getWorld()) continue;
                        NMSUtil.sendEntityMetadataPacket(player, this.entityArmorStand);
                    }
                }else{
                    for (Player player : bsCustomBlock.getBSCustomBlockChunk().getPlayers()) {
                        if (player == null) continue;
                        if (player.getWorld() != bsCustomBlock.getBlock().getWorld()) continue;
                        NMSUtil.sendEntityMetadataPacket(player, this.entityArmorStand);
                    }
                }
            } catch (Exception e) {
                if(BlockStudio.getPlugin().getLogLevel() >= 2)
                    e.printStackTrace();
            }
        }
    }
    
    public void remove(){
        if(this.armorStand != null){
            this.armorStand.remove();
        }else{
            try {
                if(bsCube != null) {
                    for (Player player : bsCube.getBsObject().getPlayers()) {
                        if (player == null) continue;
                        if (player.getWorld() != bsCube.getLocation().getWorld()) continue;
                        sendDestroyPacket(player);
                    }
                }else{
                    for (Player player : bsCustomBlock.getBSCustomBlockChunk().getPlayers()) {
                        if (player == null) continue;
                        if (player.getWorld() != bsCustomBlock.getBlock().getWorld()) continue;
                        sendDestroyPacket(player);
                    }
                }
            } catch (Exception e) {
                if(BlockStudio.getPlugin().getLogLevel() >= 2)
                    e.printStackTrace();
            }
        }
    }
}
