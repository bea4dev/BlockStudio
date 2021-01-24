package be4rjp.blockstudio.angle;

import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

public class AngleUtil {
    public static EulerAngle getEulerAngleByXYZVector(Vector XVector, Vector YVector, Vector ZVector){
        
        final double thresh = 0.00001;
        
        if(Math.abs(XVector.getZ() - 1) > thresh && Math.abs(XVector.getZ() + 1) > thresh){
            double theta2_0 = Math.asin(XVector.getZ());
            double theta1_0 = Math.atan2(-YVector.getZ() / Math.cos(theta2_0), ZVector.getZ() / Math.cos(theta2_0));
            double theta3_0 = Math.atan2(-XVector.getY() / Math.cos(theta2_0), XVector.getX() / Math.cos(theta2_0));
            
            return new EulerAngle(-theta1_0, theta2_0, theta3_0);
        }else{
            double theta3 = 0;
            if(Math.abs(XVector.getZ() - 1) < thresh){
                double theta2 = Math.PI / 2;
                double theta1 = -theta3 + Math.atan2(ZVector.getY(), YVector.getY());
                return new EulerAngle(-theta1, theta2, theta3);
            }else{
                double theta2 = -Math.PI / 2;
                double theta1 = theta3 + Math.atan2(ZVector.getY(), YVector.getY());
                return new EulerAngle(-theta1, theta2, theta3);
            }
        }
    }
}
