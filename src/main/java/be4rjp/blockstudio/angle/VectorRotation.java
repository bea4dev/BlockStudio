package be4rjp.blockstudio.angle;

import org.bukkit.util.Vector;

public class VectorRotation {
    public static void rotateAroundY(Vector vector, double angle) {
        double angleCos = Math.cos(angle);
        double angleSin = Math.sin(angle);
        double x = angleCos * vector.getX() + angleSin * vector.getZ();
        double z = -angleSin * vector.getX() + angleCos * vector.getZ();
        vector.setX(x).setZ(z);
    }
    
    
    public static void rotateAroundAxis(Vector vector, Vector axis, double angle) throws IllegalArgumentException {
        rotateAroundNonUnitAxis(vector, isNormalized(vector) ? axis : axis.clone().normalize(), angle);
    }
    
    public static boolean isNormalized(Vector vector) {
        return (Math.abs(vector.lengthSquared() - 1.0D) < vector.getEpsilon());
    }
    
    public static void rotateAroundNonUnitAxis(Vector vector, Vector axis, double angle) throws IllegalArgumentException {
        double x = vector.getX(), y = vector.getY(), z = vector.getZ();
        double x2 = axis.getX(), y2 = axis.getY(), z2 = axis.getZ();
        
        double cosTheta = Math.cos(angle);
        double sinTheta = Math.sin(angle);
        double dotProduct = vector.dot(axis);
        
        double xPrime = x2 * dotProduct * (1.0D - cosTheta) + x * cosTheta + (
                -z2 * y + y2 * z) * sinTheta;
        double yPrime = y2 * dotProduct * (1.0D - cosTheta) +
                y * cosTheta + (z2 * x - x2 * z) * sinTheta;
        double zPrime = z2 * dotProduct * (1.0D - cosTheta) +
                z * cosTheta + (-y2 * x + x2 * y) * sinTheta;
        vector.setX(xPrime).setY(yPrime).setZ(zPrime);
    }
}
