package com.capstone.exff.entities;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "category", schema = "exff")
public class CategoryEntity {
    private int id;
    private String name;
    private Integer supercategoryId;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
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
    @Column(name = "supercategory_id")
    public Integer getSupercategoryId() {
        return supercategoryId;
    }

    public void setSupercategoryId(Integer suppercategoryId) {
        this.supercategoryId = suppercategoryId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CategoryEntity that = (CategoryEntity) o;
        return id == that.id &&
                Objects.equals(name, that.name) &&
                Objects.equals(supercategoryId, that.supercategoryId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, supercategoryId);
    }

}
