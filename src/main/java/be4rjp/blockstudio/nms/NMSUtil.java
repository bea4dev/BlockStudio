package be4rjp.blockstudio.nms;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class NMSUtil {
    public static Class<?> getNMSClass(String nmsClassString) throws ClassNotFoundException {
        String version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3] + ".";
        String name = "net.minecraft.server." + version + nmsClassString;
        Class<?> nmsClass = Class.forName(name);
        return nmsClass;
    }
    
    public static Object getConnection(Player player) throws SecurityException, NoSuchMethodException,
            NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        
        Method getHandle = player.getClass().getMethod("getHandle");
        Object nmsPlayer = getHandle.invoke(player);
        Field conField = nmsPlayer.getClass().getField("playerConnection");
        Object con = conField.get(nmsPlayer);
        return con;
        
    }
    
    public static Object getNMSWorld(World world) throws SecurityException, NoSuchMethodException,
            IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        
        Method getHandle = world.getClass().getMethod("getHandle");
        Object nmsWorld = getHandle.invoke(world);
        return nmsWorld;
        
    }
    
    public static Object createEntityArmorStand(World world)
            throws ClassNotFoundException, SecurityException, NoSuchMethodException, NoSuchFieldException,
            IllegalArgumentException, IllegalAccessException, InvocationTargetException, InstantiationException {
        
        Class<?> EntityArmorStand = getNMSClass("EntityArmorStand");
        Class<?> NMSWorld = getNMSClass("World");
        Object entityArmorStand = EntityArmorStand.getConstructor(NMSWorld).newInstance(getNMSWorld(world));
        
        Method setInvisible = EntityArmorStand.getMethod("setInvisible", boolean.class);
        setInvisible.invoke(entityArmorStand, true);
        
        Method setMarker = EntityArmorStand.getMethod("setMarker", boolean.class);
        setMarker.invoke(entityArmorStand, true);
        
        Method setNoGravity = EntityArmorStand.getMethod("setNoGravity", boolean.class);
        setNoGravity.invoke(entityArmorStand, true);
        
        return entityArmorStand;
        
    }
    
    public static void setEntityPositionRotation(Object entity, double x, double y, double z, float yaw, float pitch)
            throws ClassNotFoundException, SecurityException, NoSuchMethodException, NoSuchFieldException,
            IllegalArgumentException, IllegalAccessException, InvocationTargetException, InstantiationException {
        
        Class<?> Entity = getNMSClass("Entity");
        Method setPositionRotation = Entity.getMethod("setPositionRotation", double.class, double.class, double.class, float.class, float.class);
        setPositionRotation.invoke(entity, x, y, z, yaw, pitch);
    }
    
    public static void setArmorStandHeadRotation(Object entity, float x, float y, float z)
            throws ClassNotFoundException, SecurityException, NoSuchMethodException, NoSuchFieldException,
            IllegalArgumentException, IllegalAccessException, InvocationTargetException, InstantiationException {
        
        Class<?> Vector3f = getNMSClass("Vector3f");
        Object vector3f = Vector3f.getConstructor(float.class, float.class, float.class).newInstance(x, y, z);
        
        Class<?> EntityArmorStand = getNMSClass("EntityArmorStand");
        Method setHeadPose = EntityArmorStand.getMethod("setHeadPose", Vector3f);
        setHeadPose.invoke(entity, vector3f);
    }
    
    public static void sendEntityTeleportPacket(Player player, Object entity)
            throws ClassNotFoundException, SecurityException, NoSuchMethodException, NoSuchFieldException,
            IllegalArgumentException, IllegalAccessException, InvocationTargetException, InstantiationException {
        
        Class<?> packetClass = getNMSClass("PacketPlayOutEntityTeleport");
        Class<?> Entity = getNMSClass("Entity");
        Constructor<?> packetConstructor = packetClass.getConstructor(Entity);
        Object packet = packetConstructor.newInstance(entity);
        Method sendPacket = getNMSClass("PlayerConnection").getMethod("sendPacket", getNMSClass("Packet"));
        sendPacket.invoke(getConnection(player), packet);
    }
    
    public static void sendEntityDestroyPacket(Player player, Object entity)
            throws ClassNotFoundException, SecurityException, NoSuchMethodException, NoSuchFieldException,
            IllegalArgumentException, IllegalAccessException, InvocationTargetException, InstantiationException {
        
        Class<?> packetClass = getNMSClass("PacketPlayOutEntityDestroy");
        Class<?> Entity = getNMSClass("Entity");
        
        Method getBukkitEntity = Entity.getMethod("getBukkitEntity");
        Object bukkitEntity = getBukkitEntity.invoke(entity);
        
        Constructor<?> packetConstructor = packetClass.getConstructor(int[].class);
        int[] ints = {((org.bukkit.entity.Entity)bukkitEntity).getEntityId()};
        Object packet = packetConstructor.newInstance(ints);
        Method sendPacket = getNMSClass("PlayerConnection").getMethod("sendPacket", getNMSClass("Packet"));
        sendPacket.invoke(getConnection(player), packet);
    }
    
    public static void sendSpawnEntityLivingPacket(Player player, Object entity)
            throws ClassNotFoundException, SecurityException, NoSuchMethodException, NoSuchFieldException,
            IllegalArgumentException, IllegalAccessException, InvocationTargetException, InstantiationException {
        
        Class<?> packetClass = getNMSClass("PacketPlayOutSpawnEntityLiving");
        Class<?> LivingEntity = getNMSClass("EntityLiving");
        Constructor<?> packetConstructor = packetClass.getConstructor(LivingEntity);
        Object packet = packetConstructor.newInstance(entity);
        Method sendPacket = getNMSClass("PlayerConnection").getMethod("sendPacket", getNMSClass("Packet"));
        sendPacket.invoke(getConnection(player), packet);
    }
    
    public static void sendEntityMetadataPacket(Player player, Object entity)
            throws ClassNotFoundException, SecurityException, NoSuchMethodException, NoSuchFieldException,
            IllegalArgumentException, IllegalAccessException, InvocationTargetException, InstantiationException {
        
        Class<?> packetClass = getNMSClass("PacketPlayOutEntityMetadata");
        Class<?> Entity = getNMSClass("Entity");
        Class<?> DataWatcher = getNMSClass("DataWatcher");
        
        Method getDataWatcher = Entity.getMethod("getDataWatcher");
        Object dataWatcher = getDataWatcher.invoke(entity);
        
        Method getBukkitEntity = Entity.getMethod("getBukkitEntity");
        Object bukkitEntity = getBukkitEntity.invoke(entity);
        
        Constructor<?> packetConstructor = packetClass.getConstructor(int.class, DataWatcher, boolean.class);
        Object packet = packetConstructor.newInstance(((org.bukkit.entity.Entity)bukkitEntity).getEntityId(), dataWatcher, true);
        Method sendPacket = getNMSClass("PlayerConnection").getMethod("sendPacket", getNMSClass("Packet"));
        sendPacket.invoke(getConnection(player), packet);
    }
    
    public static void sendEntityEquipmentPacket(Player player, Object entity, ItemStack itemStack)
            throws ClassNotFoundException, SecurityException, NoSuchMethodException, NoSuchFieldException,
            IllegalArgumentException, IllegalAccessException, InvocationTargetException, InstantiationException {
        
        Class<?> packetClass = getNMSClass("PacketPlayOutEntityEquipment");
        Class<?> Entity = getNMSClass("Entity");
        String version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3] + ".";
        Class<?> CraftItemStack = Class.forName("org.bukkit.craftbukkit." + version + "inventory.CraftItemStack");
        Class<?> ItemStack = getNMSClass("ItemStack");
        Class<?> EnumItemSlot = getNMSClass("EnumItemSlot");
        
        Object slot = null;
        for (Object o: EnumItemSlot.getEnumConstants()) {
            if(o.toString().equals("HEAD"))
                slot = o;
        }
        
        Method asNMSCopy = CraftItemStack.getMethod("asNMSCopy", org.bukkit.inventory.ItemStack.class);
        Object nmsItemStack = asNMSCopy.invoke(null, itemStack);
        
        Method getBukkitEntity = Entity.getMethod("getBukkitEntity");
        Object bukkitEntity = getBukkitEntity.invoke(entity);
        
        Constructor<?> packetConstructor = packetClass.getConstructor(int.class, EnumItemSlot, ItemStack);
        Object packet = packetConstructor.newInstance(((org.bukkit.entity.Entity)bukkitEntity).getEntityId(), slot, nmsItemStack);
        Method sendPacket = getNMSClass("PlayerConnection").getMethod("sendPacket", getNMSClass("Packet"));
        sendPacket.invoke(getConnection(player), packet);
    }
}
