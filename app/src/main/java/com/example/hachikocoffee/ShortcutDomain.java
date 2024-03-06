package com.example.hachikocoffee;

public class ShortcutDomain {
    private String title;
    private String pic;
    public ShortcutDomain(String title, String pic){
        this.title = title;
        this.pic = pic;
    }

    public String getTitle(){
        return title;
    }

    public String getPic(){
        return pic;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public void setPic(String pic){
        this.pic = pic;
    }
}
