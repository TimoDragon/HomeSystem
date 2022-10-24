package eu.dragonarmy.homesystem;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class HomeLocation {
    private String world;
    private double x;
    private double y;
    private double z;
    private float p;
    private float ya;

    public HomeLocation(String world, double x, double y, double z, float p, float ya) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.p = p;
        this.ya = ya;
    }

    // getter
    public String getWorld() {
        return this.world;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double getZ() {
        return this.z;
    }

    public float getP() {
        return this.p;
    }

    public float getYa() {
        return this.getYa();
    }

    public Location getLocation() {
        return new Location(Bukkit.getWorld(world), x, y, z, ya, p);
    }

    // setter
    public void setWorld(String world) {
        this.world = world;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public void setP(float p) {
        this.p = p;
    }

    public void setYa(float ya) {
        this.ya = ya;
    }
}