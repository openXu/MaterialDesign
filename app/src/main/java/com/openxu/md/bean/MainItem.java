package com.openxu.md.bean;

/**
 * Author: openXu
 * Time: 2019/3/26 13:30
 * class: MainItem
 * Description:
 */
public class MainItem {

    private int id;
    private String name;

    public MainItem(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
