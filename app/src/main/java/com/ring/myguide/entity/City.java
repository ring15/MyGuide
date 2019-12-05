package com.ring.myguide.entity;

import java.io.Serializable;
import java.util.Objects;

public class City implements Serializable {

    private static final long serialVersionUID = -591904622697795411L;

    private int id;
    private String name;
    private boolean isHot;

    public City() {
    }

    public City(String name) {
        this.name = name;
        this.id = id;
    }

    public City(int id, String name) {
        this.name = name;
        this.id = id;
    }

    public City(int id, String name, boolean isHot) {
        this.name = name;
        this.id = id;
        this.isHot = isHot;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isHot() {
        return isHot;
    }

    public void setHot(boolean hot) {
        isHot = hot;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        City city = (City) o;
        return Objects.equals(name, city.name);
    }

    @Override
    public int hashCode() {

        return Objects.hash(name);
    }
}
