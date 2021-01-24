package be4rjp.blockstudio.api;

import be4rjp.blockstudio.angle.AngleUtil;
import be4rjp.blockstudio.angle.VectorRotation;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class BSObject {
    private final String name;
    private final BlockStudioAPI api;
    private final boolean useBukkitArmorStand;
    private double viewDistance;
    private Location baseLocation;
    private List<BSCube> list = new ArrayList<>();
    private List<Player> players = new ArrayList<>();
    private Vector x = new Vector(1, 0, 0);
    private Vector y = new Vector(0, 1, 0);
    private Vector z = new Vector(0, 0, 1);
    private EulerAngle eulerAngle = new EulerAngle(0, 0, 0);
    
    private final BSObjectPlayerRunnable bsObjectPlayerRunnable;
    
    private static final double blockSize = 5.0/8.0;
    private static final double halfBlockSize = (5.0/8.0)/2.0;
    
    /**
     * Create an object.
     * オブジェクトを生成します
     * @param api BlockStudioAPI
     * @param name Object's name.
     * @param baseLocation Spawn at this location.
     * @param useBukkitArmorStand Whether to use BukkitArmorStand.
     *                            If you specify true for this argument,
     *                            you will not be able to move the object asynchronously.
     */
    public BSObject(BlockStudioAPI api, String name, Location baseLocation, double viewDistance, boolean useBukkitArmorStand){
        this.api = api;
        this.name = name;
        this.viewDistance = viewDistance;
        this.useBukkitArmorStand = useBukkitArmorStand;
        baseLocation.setYaw(0);
        baseLocation.setPitch(0);
        this.baseLocation = baseLocation;
        
        this.bsObjectPlayerRunnable = new BSObjectPlayerRunnable(this);
    }
    
    
    /**
     * Specifies the orientation of the object.
     * Calculates and specifies the X and Y axes from the specified Z axis vector.
     * (Moves like the player's head.)
     * オブジェクトの向きを指定します。
     * 指定されたZ軸のベクトルからX軸とY軸を算出して指定します。
     * （プレイヤーの頭に似た動きをします）
     * @param direction
     */
    public void setZAxes(Vector direction) {
        Vector xzVector = new Vector(direction.getX(), 0, direction.getZ());
        float xzAngle = xzVector.angle(new Vector(0, 0, 1)) * (direction.getX() >= 0 ? 1 : -1);
        this.z = direction.clone();
        
        Vector X = new Vector(1, 0, 0);
        VectorRotation.rotateAroundY(X, xzAngle);
        this.x = X;
        
        Vector Y = direction.clone();
        VectorRotation.rotateAroundAxis(Y, x, Math.toRadians(270));
        this.y = Y;
        
        
        float upAngle = y.angle(new Vector(0, 1, 0));
        if(direction.getY() >= 0){
            upAngle = (float)Math.toRadians(360) - upAngle;
        }
        
        eulerAngle = new EulerAngle(upAngle, -xzAngle, 0);
    }
    
    
    /**
     * Specifies the orientation of the object.
     * Calculate and specify the X and Y axes from the specified Z-axis vector and Z-axis rotation.
     * オブジェクトの向きを指定します。
     * 指定されたZ軸のベクトルとZ軸の回転からX軸とY軸を算出して指定します。
     * @param direction
     * @param rotation
     */
    public void setZAxesRotation(Vector direction, double rotation) {
        Vector xzVector = new Vector(direction.getX(), 0, direction.getZ());
        float xzAngle = xzVector.angle(new Vector(0, 0, 1)) * (direction.getX() >= 0 ? 1 : -1);
        this.z = direction.clone();
        
        Vector X = new Vector(1, 0, 0);
        VectorRotation.rotateAroundY(X, xzAngle);
        this.x = X;
        
        Vector Y = direction.clone();
        VectorRotation.rotateAroundAxis(Y, x, Math.toRadians(270));
        this.y = Y;
        
        VectorRotation.rotateAroundAxis(x, z, Math.toRadians(rotation));
        VectorRotation.rotateAroundAxis(y, z, Math.toRadians(rotation));
        
        eulerAngle = AngleUtil.getEulerAngleByXYZVector(x, y, z);
    }
    
    
    /**
     * Specifies the orientation of the object.
     * Calculates and specifies the vectors of X, Y, and Z axes from the specified EulerAngle.
     * オブジェクトの向きを指定します。
     * 指定されたオイラー角からX,Y,Z軸のベクトルを算出して指定します。
     * @param eulerAngle
     */
    public void setOriginByEulerAngle(EulerAngle eulerAngle) {
        Vector x = new Vector(1, 0, 0);
        Vector y = new Vector(0, 1, 0);
        Vector z = new Vector(0, 0, 1);
        
        VectorRotation.rotateAroundAxis(x, z, -eulerAngle.getZ());
        VectorRotation.rotateAroundAxis(y, z, -eulerAngle.getZ());
        
        VectorRotation.rotateAroundAxis(x, y, -eulerAngle.getY());
        VectorRotation.rotateAroundAxis(z, y, -eulerAngle.getY());
        
        VectorRotation.rotateAroundAxis(y, x, eulerAngle.getX());
        VectorRotation.rotateAroundAxis(z, x, eulerAngle.getX());
        
        this.x = x;
        this.y = y;
        this.z = z;
        
        this.eulerAngle = eulerAngle;
    }
    
    
    /**
     * If you are not using BukkitArmorStand, start a task to find out which player the packet should be sent.
     * BukkitArmorStandを使用していない場合に、どのプレイヤーにパケットを送るべきかを調べるタスクを開始します。
     * @param period
     */
    @Deprecated
    public void startTask(int period){
        this.bsObjectPlayerRunnable.runTaskTimer(api.getPlugin(), 0, period);
    }
    
    
    /**
     * If you are not using BukkitArmorStand, start a task to find out which player the packet should be sent.
     * BukkitArmorStandを使用していない場合に、どのプレイヤーにパケットを送るべきかを調べるタスクを開始します。
     * @param period
     */
    public void startTaskAsync(int period){
        this.bsObjectPlayerRunnable.runTaskTimerAsynchronously(api.getPlugin(), 0, period);
    }
    
    
    public Vector getXVector(){return this.x;}
    
    public Vector getYVector(){return this.y;}
    
    public Vector getZVector(){return this.z;}
    
    public Location getBaseLocation(){return this.baseLocation;}
    
    
    /**
     * Specifies the location of the object.
     * オブジェクトの座標を指定します。
     * @param baseLocation
     */
    public void setBaseLocation(Location baseLocation){
        this.baseLocation = baseLocation;
        this.baseLocation.setYaw(0);
        this.baseLocation.setPitch(0);
    }
    
    
    public List<BSCube> getBSCubeList(){return this.list;}
    
    public List<Player> getPlayers() {return players;}
    
    public double getBlockSize(){return blockSize;}
    
    public double getHalfBlockSize(){return halfBlockSize;}
    
    public boolean isUseBukkitArmorStand() {return useBukkitArmorStand;}
    
    public EulerAngle getEulerAngle(){return eulerAngle;}
    
    public double getViewDistance() {return viewDistance;}
    
    
    /**
     * Change the view distance.
     * !!The changes may not be synchronized properly!!
     * 描画距離を変更します。
     * !!変更内容が正しく同期されない場合があります!!
     * @param viewDistance
     */
    @Deprecated
    public void setViewDistance(double viewDistance) {
        try {
            this.viewDistance = viewDistance;
        }catch (Exception e){}
    }
    
    /**
     * Move this object.
     * このオブジェクトを動かします。
     */
    public void move(){
        for(BSCube bsCube : list){
            bsCube.move();
        }
    }
    
    
    /**
     * Despawn this object.
     * このオブジェクトをデスポーンさせます。
     */
    public void remove(){
        for(BSCube bsCube : list){
            bsCube.getBSArmorStand().remove();
        }
        
        try{
            bsObjectPlayerRunnable.cancel();
        }catch (Exception e){}
    }
    
    public void setBSCubeList(List<BSCube> list){this.list = list;}
}
