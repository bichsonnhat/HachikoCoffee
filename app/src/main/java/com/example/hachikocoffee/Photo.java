package com.example.hachikocoffee;

public class Photo {

    private int resourceId;

    public Photo(int resourceId){
        this.resourceId = resourceId;
    }

    public int  getResourceId(){
        return this.resourceId;
    }

    public void setResourceId(int resourceId){
        this.resourceId = resourceId;
    }
}
