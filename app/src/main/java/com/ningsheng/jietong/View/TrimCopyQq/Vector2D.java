package com.ningsheng.jietong.View.TrimCopyQq;

/**
 * Created by zhangheng on 2015/12/3.
 */
public class Vector2D {
    private float x;
    private float y;

    public Vector2D() {
    }

    public Vector2D(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vector2D(Vector2D vector2D) {
        this.x = vector2D.x;
        this.y = vector2D.y;
    }

    public Vector2D set(Vector2D vector2D) {
        this.x = vector2D.x;
        this.y = vector2D.y;
        return this;
    }

    public Vector2D set(float x, float y) {
        this.x = x;
        this.y = y;
        return this;
    }

    public Vector2D subtract(Vector2D vector2D) {
        this.x -= vector2D.x;
        this.y -= vector2D.y;
        return this;
    }

    public static Vector2D subtract(Vector2D vector2D1, Vector2D vector2D2) {
        return new Vector2D(vector2D1).subtract(vector2D2);
    }

    public Vector2D add(Vector2D vector2D) {
        this.x += vector2D.x;
        this.y += vector2D.y;
        return this;
    }

    public float distance(Vector2D vector2D) {
        return subtract(vector2D).length();
    }

    public float length() {
        return (float) Math.sqrt(Math.pow(this.x, 2) + Math.pow(this.y, 2));
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {

        this.y = y;
    }

    @Override
    public String toString() {
        return "Vector2D{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
