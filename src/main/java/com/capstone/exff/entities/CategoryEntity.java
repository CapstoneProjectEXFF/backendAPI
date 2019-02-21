package com.capstone.exff.entities;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "category", schema = "exff")
public class CategoryEntity {
    private int id;
    private String name;
    private Integer suppercategoryId;

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
    public Integer getSuppercategoryId() {
        return suppercategoryId;
    }

    public void setSuppercategoryId(Integer suppercategoryId) {
        this.suppercategoryId = suppercategoryId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CategoryEntity that = (CategoryEntity) o;
        return id == that.id &&
                Objects.equals(name, that.name) &&
                Objects.equals(suppercategoryId, that.suppercategoryId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, suppercategoryId);
    }

}
