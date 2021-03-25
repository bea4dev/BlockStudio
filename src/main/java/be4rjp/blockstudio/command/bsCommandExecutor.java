package be4rjp.blockstudio.command;

import be4rjp.blockstudio.BlockStudio;
import be4rjp.blockstudio.api.*;
import be4rjp.blockstudio.data.PlayerData;
import be4rjp.blockstudio.file.Config;
import be4rjp.blockstudio.file.ObjectConfig;
import be4rjp.blockstudio.file.ObjectData;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class bsCommandExecutor implements CommandExecutor, TabExecutor {
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (args == null) return false;
        if (args.length == 0) return false;
        
        
        if(args[0].equals("reload")){
            BlockStudioAPI api = BlockStudio.getBlockStudioAPI();
            //Save config
            api.getCustomBlockConfig().saveConfig();
            api.getCustomBlockDataConfig().saveConfig();
            
            List<BSCustomBlock> blockList = new ArrayList<>();
            api.getBSCustomBlockChunkMap().values().forEach(bsCustomBlockChunk -> {
                bsCustomBlockChunk.getBlockList().forEach(bsCustomBlock -> blockList.add(bsCustomBlock));
            });
            blockList.forEach(bsCustomBlock -> bsCustomBlock.getArmorStand().remove());
            
            //Load config
            BlockStudio.getPlugin().loadConfig();
            
            api.loadAllObjectData();
            
            api.getObjectList().forEach(bsObject -> bsObject.remove(false));
            api.getObjectList().clear();
            api.getObjectMap().clear();
            if(BlockStudio.config.getConfig().getBoolean("spawn-objects"))
                api.spawnAllObjects();
            
            api.loadAndSpawnAllCustomBlocks();
            
            
            sender.sendMessage(ChatColor.GREEN + "Successfully reloaded.");
            return true;
        }
        
        
        if(!(sender instanceof Player)){
            sender.sendMessage(ChatColor.RED + "This command cannot be executed on the console.");
            return true;
        }
        
        switch (args[0]){
            case "createdata":{
                if(args.length != 2) {
                    sender.sendMessage(ChatColor.RED + "Please specify the name of the data.");
                    return true;
                }
    
                PlayerData playerData = BlockStudio.getPlugin().getDataStore().getPlayerData((Player)sender);
                
                if(playerData == null){
                    sender.sendMessage(ChatColor.RED + "Can't find player data!");
                    return true;
                }
                
                if(playerData.getBase() == null || playerData.getFirst() == null || playerData.getSecond() == null){
                    sender.sendMessage(ChatColor.RED + "You have not yet selected the blocks at the first position, second position, and base position.");
                    CommandUtil.sendHowToMessage((Player)sender);
                    CommandUtil.addItem((Player)sender);
                    return true;
                }
    
                BlockStudioAPI api = BlockStudio.getBlockStudioAPI();
                
                api.createObjectDataFromRegion(args[1], playerData.getBase(), playerData.getFirst(), playerData.getSecond());
    
                sender.sendMessage(ChatColor.GREEN + "The data was successfully created.");
                break;
            }
            case "savedata":{
                if(args.length != 2) {
                    sender.sendMessage(ChatColor.RED + "Please specify the name of the data.");
                    return true;
                }
    
                BlockStudioAPI api = BlockStudio.getBlockStudioAPI();
                ObjectData objectData = api.getObjectData(args[1]);
                
                if(objectData == null){
                    sender.sendMessage(ChatColor.RED + "Can't find object data!");
                    return true;
                }
                
                objectData.saveFile();
                sender.sendMessage(ChatColor.GREEN + "The data was successfully saved.");
                break;
            }
            case "testspawn":{
                if(args.length != 2) {
                    sender.sendMessage(ChatColor.RED + "Please specify the name of the data.");
                    return true;
                }
        
                BlockStudioAPI api = BlockStudio.getBlockStudioAPI();
                ObjectData objectData = api.getObjectData(args[1]);
        
                if(objectData == null){
                    sender.sendMessage(ChatColor.RED + "Can't find object data!");
                    return true;
                }
                
                BSObject bsObject = new BSObject(api, "test", ((Player)sender).getLocation(), api.getDefaultViewDistance(), false);
                List<BSCube> bsCubeList = BSUtil.createBSCubeListFromObjectData(((Player)sender).getWorld(), bsObject, objectData);
                bsObject.setBSCubeList(bsCubeList);
                bsObject.startTaskAsync(40);
                bsObject.move();
                
                TestSpawnRunnable runnable = new TestSpawnRunnable(bsObject);
                runnable.runTaskTimer(BlockStudio.getPlugin(), 0, 1);
                
                sender.sendMessage(ChatColor.GREEN + "Object spawned successfully.");
                sender.sendMessage(ChatColor.GREEN + "It will be displayed for 10 seconds.");
                break;
            }
            case "addobject":{
                if(args.length < 2) {
                    sender.sendMessage(ChatColor.RED + "Please specify the name of object to create.");
                    return true;
                }
    
                BlockStudioAPI api = BlockStudio.getBlockStudioAPI();
                if(api.hasNamedObject(args[1])){
                    sender.sendMessage(ChatColor.RED + "An object with the specified name already exists.");
                    return true;
                }
                
                if(args.length < 3){
                    sender.sendMessage(ChatColor.RED + "Please specify the name of an object data.");
                    return true;
                }
    
                ObjectData objectData = api.getObjectData(args[2]);
                if(objectData == null){
                    sender.sendMessage(ChatColor.RED + "Can't find specified object data!");
                    return true;
                }
    
                try {
                    Player player = (Player) sender;
                    ObjectConfig objectConfig = new ObjectConfig(args[1]);
                    objectConfig.loadFile();
                    objectConfig.setObjectDataName(args[2]);
                    objectConfig.setLocation(player.getLocation());
                    objectConfig.setDirection(player.getEyeLocation().getDirection());
    
                    objectConfig.spawnObject();
                    objectConfig.saveFile();
                }catch (Exception e){
                    sender.sendMessage(ChatColor.RED + "The wrong argument was specified.");
                    if(BlockStudio.getPlugin().getLogLevel() >= 2)
                        e.printStackTrace();
                }
                break;
            }
            case "item":{
                CommandUtil.sendHowToMessage((Player)sender);
                CommandUtil.addItem((Player)sender);
                break;
            }
        }
        
        return true;
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
    
        List<String> list = new ArrayList<>();
        
        if (args.length == 1) {
            
            list.add("createdata");
            list.add("savedata");
            list.add("testspawn");
            list.add("addobject");
            list.add("reload");
            list.add("item");
            list.add("help");
            
            return list;
        }else if(args.length == 2){
            if(args[0].equals("savedata") || args[0].equals("testspawn")){
                BlockStudio.getBlockStudioAPI().getObjectDataList().forEach(objectData -> list.add(objectData.getDataName()));
                return list;
            }else if(args[0].equals("createdata")){
                list.add("[data name]");
                return list;
            }else if(args[0].equals("addobject")){
                list.add("[object name]");
                return list;
            }
        }else if(args.length == 3){
            if(args[0].equals("addobject")){
                BlockStudio.getBlockStudioAPI().getObjectDataList().forEach(objectData -> list.add(objectData.getDataName()));
                return list;
            }
        }
        return null;
    }
}
