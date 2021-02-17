package be4rjp.blockstudio.nms.packet;

import be4rjp.blockstudio.BlockStudio;
import be4rjp.blockstudio.nms.NMSUtil;
import io.netty.channel.*;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;

public class PacketHandler extends ChannelDuplexHandler{
    
    private final Player player;
    
    public PacketHandler(Player player){
        this.player = player;
    }
    
    @Override
    public void channelRead(ChannelHandlerContext channelHandlerContext, Object packet) throws Exception {
        
        try{
            Class<?> PacketPlayInUseEntity = NMSUtil.getNMSClass("PacketPlayInUseEntity");
            
            if(packet.getClass().getSimpleName().equalsIgnoreCase("PacketPlayInUseEntity")){
                Field a = PacketPlayInUseEntity.getDeclaredField("a");
                a.setAccessible(true);
                int entityID = (Integer) a.get(packet);
                ObjectClickPacketManager.checkAllObject(entityID, player);
            }
        }catch (Exception e){
            if(BlockStudio.getPlugin().getLogLevel() >= 2){
                e.printStackTrace();
            }
        }
        
        super.channelRead(channelHandlerContext, packet);
    }
    
    @Override
    public void write(ChannelHandlerContext channelHandlerContext, Object packet, ChannelPromise channelPromise) throws Exception {
    
        super.write(channelHandlerContext, packet, channelPromise);
    }
}