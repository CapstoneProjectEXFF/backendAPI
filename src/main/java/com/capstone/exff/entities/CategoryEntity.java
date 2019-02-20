package com.capstone.exff.entities;

import javax.persistence.*;

@Entity
@Table(name = "category", schema = "exff")
public class CategoryEntity {
    private int id;
    private String name;
    private int suppercategoryId;

    @Id
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "suppercategory_id")
    public int getSuppercategoryId() {
        return suppercategoryId;
    }

    public void setSuppercategoryId(int suppercategoryId) {
        this.suppercategoryId = suppercategoryId;
    }


}
