package be4rjp.blockstudio.api;

import be4rjp.blockstudio.angle.AngleUtil;
import be4rjp.blockstudio.angle.VectorRotation;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

public class BSCube {
    private final BSObject bsObject;
    private final Vector location;
    private final EulerAngle baseEulerAngle;
    private BSArmorStand armorStand;
    private World world;
    private ItemStack headItemStack;
    
    public BSCube(BSObject bsObject, World world, Vector location, Material blockMaterial, EulerAngle baseEulerAngle){
        this.bsObject = bsObject;
        this.location = location;
        this.world = world;
        this.baseEulerAngle = baseEulerAngle;
        
        this.armorStand = new BSArmorStand(this, getLocation(), bsObject.isUseBukkitArmorStand());
        this.headItemStack = new ItemStack(blockMaterial);
        armorStand.setHelmet(headItemStack);
    }
    
    public BSCube(BSObject bsObject, World world, Vector location, ItemStack headItemStack, EulerAngle baseEulerAngle){
        this.bsObject = bsObject;
        this.location = location;
        this.world = world;
        this.baseEulerAngle = baseEulerAngle;
        
        this.armorStand = new BSArmorStand(this, getLocation(), bsObject.isUseBukkitArmorStand());
        this.headItemStack = headItemStack;
        armorStand.setHelmet(headItemStack);
    }
    
    public Location getLocation(){
        
        if(location.getX() == 0.0 && location.getY() == 0.0 && location.getZ() == 0.0)
            return bsObject.getBaseLocation().clone();
        
        Vector baseLocation = bsObject.getBaseLocation().toVector();
        Vector xLocation = baseLocation.clone().add(bsObject.getXVector().clone().multiply((location.getX() * bsObject.getBlockSize())));
        Vector yLocation = xLocation.clone().add(bsObject.getYVector().clone().multiply((location.getY() * bsObject.getBlockSize())));
        Vector zLocation = yLocation.clone().add(bsObject.getZVector().clone().multiply((location.getZ() * bsObject.getBlockSize())));
        
        return zLocation.toLocation(world);
    }
    
    public BSArmorStand getBSArmorStand(){return this.armorStand;}
    
    public void move(){
        EulerAngle eulerAngle = bsObject.getEulerAngle();
        
        Vector y = bsObject.getYVector().clone();
        
        if(baseEulerAngle.getX() != 0 || baseEulerAngle.getY() != 0 || baseEulerAngle.getZ() != 0){
            Vector x = bsObject.getXVector().clone();
            Vector z = bsObject.getZVector().clone();
    
            VectorRotation.rotateAroundAxis(x, z, baseEulerAngle.getZ());
            VectorRotation.rotateAroundAxis(y, z, baseEulerAngle.getZ());
    
            VectorRotation.rotateAroundAxis(x, y, baseEulerAngle.getY());
            VectorRotation.rotateAroundAxis(z, y, baseEulerAngle.getY());
    
            VectorRotation.rotateAroundAxis(y, x, baseEulerAngle.getX());
            VectorRotation.rotateAroundAxis(z, x, baseEulerAngle.getX());
            
            eulerAngle = AngleUtil.getEulerAngleByXYZVector(x, y, z);
        }
        
        y = y.multiply(bsObject.getHalfBlockSize() - (1.0/16.0));
        Location loc = getLocation();
        loc.add(-y.getX(), -(y.getY() + (22.0/16.0)), -y.getZ());
        
        this.armorStand.teleport(loc);
        
        this.armorStand.setHeadPose(eulerAngle);
    }
    
    public Vector getRelativeLocation(){return location;}
    
    public BSObject getBsObject() {return bsObject;}
    
    public ItemStack getHeadItemStack() {return headItemStack;}
    
    public EulerAngle getBaseEulerAngle() {return baseEulerAngle;}
    
    public World getWorld() {return world;}

    /**
     * アイテムを変更する
     * @param itemStack
     */
    public void setItem(ItemStack itemStack){
        this.headItemStack = itemStack;
        this.armorStand.setHelmet(itemStack);
    }
}
