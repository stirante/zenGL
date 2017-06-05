package com.stirante.opengl.util;

public class AxisAlignedBoundingBox {

    private final Vector3f min;
    private final Vector3f max;

    public AxisAlignedBoundingBox(double x, double y, double z, double width, double height, double length) {
        min = new Vector3f(x, y, z);
        max = new Vector3f(x + width, y + height, z + length);
    }

//    public AxisAlignedBoundingBox(Vector3f v1, Vector3f v2) {
//        x = Math.min(v1.getX(), v2.getX());
//        width = Math.max(v1.getX(), v2.getX()) - x;
//        y = Math.min(v1.getY(), v2.getY());
//        height = Math.max(v1.getY(), v2.getY()) - y;
//        z = Math.min(v1.getZ(), v2.getZ());
//        length = Math.max(v1.getZ(), v2.getZ()) - z;
//    }

    public AxisAlignedBoundingBox(Vector3f min, Vector3f max) {
        this.min = min;
        this.max = max;
    }

    public Vector3f getMin() {
        return min;
    }

    public Vector3f getMax() {
        return max;
    }
}
