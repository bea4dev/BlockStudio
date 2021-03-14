package be4rjp.blockstudio.api;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

public class BSCustomBlock {
    
    private final BSCustomBlockChunk bsCustomBlockChunk;
    private final Block block;
    private final ItemStack itemStack;
    private final BSArmorStand armorStand;
    
    public BSCustomBlock(BSCustomBlockChunk bsCustomBlockChunk, Block block, ItemStack itemStack){
        this.bsCustomBlockChunk = bsCustomBlockChunk;
        this.block = block;
        this.itemStack = itemStack;
        
        armorStand = new BSArmorStand(this, block.getLocation().clone().add(0.5, 0, 0.5), false);
        armorStand.setHelmet(itemStack);
        
        block.setType(Material.BARRIER);
    }
    
    public BSCustomBlockChunk getBSCustomBlockChunk(){return this.bsCustomBlockChunk;}
    
    public Block getBlock(){return this.block;}
    
    public ItemStack getItemStack(){return this.itemStack;}
    
    public BSArmorStand getArmorStand(){return this.armorStand;}
    
    public void breakBlock(){
        //block.breakNaturally(itemStack);
        armorStand.remove();
        block.setType(Material.AIR);
    }
}
