package com.stirante.opengl.util;

import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.glVertex3f;

public class Vector3f {

    private static final long serialVersionUID = 1L;

    public float x, y, z;

    /**
     * Constructor for Vector3f.
     */
    public Vector3f() {
        super();
    }

    /**
     * Constructor
     */
    public Vector3f(float x, float y, float z) {
        set(x, y, z);
    }

    /* (non-Javadoc)
     * @see org.lwjgl.util.vector.WritableVector2f#set(float, float)
     */
    public void set(float x, float y) {
        this.x = x;
        this.y = y;
    }

    /* (non-Javadoc)
     * @see org.lwjgl.util.vector.WritableVector3f#set(float, float, float)
     */
    public void set(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * @return the length squared of the vector
     */
    public float lengthSquared() {
        return x * x + y * y + z * z;
    }

    /**
     * Translate a vector
     *
     * @param x The translation in x
     * @param y the translation in y
     * @return this
     */
    public Vector3f translate(float x, float y, float z) {
        this.x += x;
        this.y += y;
        this.z += z;
        return this;
    }

    /**
     * Add a vector to another vector and place the result in a destination
     * vector.
     *
     * @param left  The LHS vector
     * @param right The RHS vector
     * @param dest  The destination vector, or null if a new vector is to be created
     * @return the sum of left and right in dest
     */
    public static Vector3f add(Vector3f left, Vector3f right, Vector3f dest) {
        if (dest == null)
            return new Vector3f(left.x + right.x, left.y + right.y, left.z + right.z);
        else {
            dest.set(left.x + right.x, left.y + right.y, left.z + right.z);
            return dest;
        }
    }

    /**
     * Subtract a vector from another vector and place the result in a destination
     * vector.
     *
     * @param left  The LHS vector
     * @param right The RHS vector
     * @param dest  The destination vector, or null if a new vector is to be created
     * @return left minus right in dest
     */
    public static Vector3f sub(Vector3f left, Vector3f right, Vector3f dest) {
        if (dest == null)
            return new Vector3f(left.x - right.x, left.y - right.y, left.z - right.z);
        else {
            dest.set(left.x - right.x, left.y - right.y, left.z - right.z);
            return dest;
        }
    }

    /**
     * The cross product of two vectors.
     *
     * @param left  The LHS vector
     * @param right The RHS vector
     * @param dest  The destination result, or null if a new vector is to be created
     * @return left cross right
     */
    public static Vector3f cross(
            Vector3f left,
            Vector3f right,
            Vector3f dest) {

        if (dest == null)
            dest = new Vector3f();

        dest.set(
                left.y * right.z - left.z * right.y,
                right.x * left.z - right.z * left.x,
                left.x * right.y - left.y * right.x
        );

        return dest;
    }


    /**
     * Negate a vector
     *
     * @return this
     */
    public Vector3f negate() {
        x = -x;
        y = -y;
        z = -z;
        return this;
    }

    /**
     * Negate a vector and place the result in a destination vector.
     *
     * @param dest The destination vector or null if a new vector is to be created
     * @return the negated vector
     */
    public Vector3f negate(Vector3f dest) {
        if (dest == null)
            dest = new Vector3f();
        dest.x = -x;
        dest.y = -y;
        dest.z = -z;
        return dest;
    }


    /**
     * Normalise this vector and place the result in another vector.
     *
     * @return the normalised vector
     */
    public Vector3f normalise() {
        float l = length();

        set(x / l, y / l, z / l);

        return this;
    }

    /**
     * @return the length of the vector
     */
    public final float length() {
        return (float) Math.sqrt(lengthSquared());
    }


    /**
     * The dot product of two vectors is calculated as
     * v1.x * v2.x + v1.y * v2.y + v1.z * v2.z
     *
     * @param left  The LHS vector
     * @param right The RHS vector
     * @return left dot right
     */
    public static float dot(Vector3f left, Vector3f right) {
        return left.x * right.x + left.y * right.y + left.z * right.z;
    }

    /**
     * Calculate the angle between two vectors, in radians
     *
     * @param a A vector
     * @param b The other vector
     * @return the angle between the two vectors, in radians
     */
    public static float angle(Vector3f a, Vector3f b) {
        float dls = dot(a, b) / (a.length() * b.length());
        if (dls < -1f)
            dls = -1f;
        else if (dls > 1.0f)
            dls = 1.0f;
        return (float) Math.acos(dls);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(64);

        sb.append("Vector3f[");
        sb.append(x);
        sb.append(", ");
        sb.append(y);
        sb.append(", ");
        sb.append(z);
        sb.append(']');
        return sb.toString();
    }

    /**
     * @return x
     */
    public final float getX() {
        return x;
    }

    /**
     * @return y
     */
    public final float getY() {
        return y;
    }

    /**
     * Set X
     *
     * @param x
     */
    public final void setX(float x) {
        this.x = x;
    }

    /**
     * Set Y
     *
     * @param y
     */
    public final void setY(float y) {
        this.y = y;
    }

    /**
     * Set Z
     *
     * @param z
     */
    public void setZ(float z) {
        this.z = z;
    }

    /* (Overrides)
     * @see org.lwjgl.vector.ReadableVector3f#getZ()
     */
    public float getZ() {
        return z;
    }

    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Vector3f other = (Vector3f) obj;

        if (x == other.x && y == other.y && z == other.z) return true;

        return false;
    }

    public void gl() {
        glVertex3f(x, y, z);
    }

}