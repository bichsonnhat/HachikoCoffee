package com.example.hachikocoffee.Management;

public class ManagementMinDistance {
    private static ManagementMinDistance instance;

    private double minDistance;

    private ManagementMinDistance() {
        this.minDistance = Double.MAX_VALUE;
    }

    public static synchronized ManagementMinDistance getInstance() {
        if (instance == null) {
            instance = new ManagementMinDistance();
        }
        return instance;
    }

    public double getMinDistance() {
        return minDistance;
    }

    public void setMinDistance(double minDistance) {
        this.minDistance = minDistance;
    }
}
