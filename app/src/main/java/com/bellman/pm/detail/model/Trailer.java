package com.bellman.pm.detail.model;

/**
 * Created by Potencio on 12/1/2016. @ 3:21 AM
 * For PopularMovies
 */

public class Trailer {
    private String id;
    private String key;
    private String name;
    private String type;
    private String size;

    public Trailer(String id, String key, String name, String type, String size) {
        this.id = id;
        this.key = key;
        this.name = name;
        this.type = type;
        this.size = size;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "Trailer: "+key+" : "+name;
    }
}
