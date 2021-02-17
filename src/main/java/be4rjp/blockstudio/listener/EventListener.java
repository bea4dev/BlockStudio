package be4rjp.blockstudio.listener;

import be4rjp.blockstudio.BlockStudio;
import be4rjp.blockstudio.data.PlayerData;
import be4rjp.blockstudio.nms.NMSUtil;
import be4rjp.blockstudio.nms.packet.PacketHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelPipeline;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class EventListener implements Listener {
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event){
        
        if(!event.hasItem()) return;
        
        if(!event.hasBlock()) return;
        
        if(!event.getItem().hasItemMeta()) return;
        
        if(!event.getItem().getItemMeta().hasDisplayName()) return;
    
        Player player = event.getPlayer();
        
        PlayerData playerData = BlockStudio.getPlugin().getDataStore().getPlayerData(player);
        
        if(playerData == null) return;
        
        switch (event.getItem().getItemMeta().getDisplayName()){
            case "§6Region selector":{
                if(event.getAction() == Action.LEFT_CLICK_BLOCK){
                    playerData.setFirst(event.getClickedBlock().getLocation());
                    player.sendMessage(ChatColor.DARK_PURPLE + "Selected the block at the first-position of the range.");
                }else if(event.getAction() == Action.RIGHT_CLICK_BLOCK){
                    playerData.setSecond(event.getClickedBlock().getLocation());
                    player.sendMessage(ChatColor.DARK_PURPLE + "Selected the block at the second-position of the range.");
                }
                event.setCancelled(true);
                break;
            }
            case "§bBase selector":
                playerData.setBase(event.getClickedBlock().getLocation());
                event.setCancelled(true);
                player.sendMessage(ChatColor.DARK_PURPLE + "Selected the block as the center.");
                break;
        }
    }
    
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        String uuid = player.getUniqueId().toString();
        PlayerData playerData = new PlayerData(uuid);
        BlockStudio.getPlugin().getDataStore().setPlayerData(uuid, playerData);
    }
    
    
    @EventHandler
    public void onjoin(PlayerJoinEvent event){
        //Inject packet handler
        Player player = event.getPlayer();
    
        PacketHandler packetHandler = new PacketHandler();
        
        try {
            ChannelPipeline pipeline = NMSUtil.getChannel(player).pipeline();
            pipeline.addBefore("packet_handler", player.getName(), packetHandler);
        }catch (Exception e){
            if(BlockStudio.getPlugin().getLogLevel() >= 2){
                e.printStackTrace();
            }
        }
    }
    
    
    @EventHandler
    public void onleave(PlayerQuitEvent event){
        Player player = event.getPlayer();
        
        try {
            Channel channel = NMSUtil.getChannel(player);
    
            channel.eventLoop().submit(() -> {
                channel.pipeline().remove(player.getName());
                return null;
            });
        }catch (Exception e){
            if(BlockStudio.getPlugin().getLogLevel() >= 2){
                e.printStackTrace();
            }
        }
    }
}
