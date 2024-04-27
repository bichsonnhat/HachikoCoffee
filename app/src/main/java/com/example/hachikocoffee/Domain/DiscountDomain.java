package com.example.hachikocoffee.Domain;

public class DiscountDomain {
    private int resourceId;
    private String content;
    private String expired;

    public DiscountDomain(int resourceId, String content, String expired) {
        this.resourceId = resourceId;
        this.content = content;
        this.expired = expired;
    }

    public int getResourceId() {
        return resourceId;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getExpired() {
        return expired;
    }

    public void setExpired(String expired) {
        this.expired = expired;
    }
}
