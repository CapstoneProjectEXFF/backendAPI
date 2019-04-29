package com.capstone.exff.entities;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "item", schema = "exff")
public class ItemEntity {
    private int id;
    private String name;
    private Integer userId;
    private String description;
    private String preferItems;
    private String address;
    private String status;
    private String privacy;
    private Timestamp createTime;
    private Timestamp modifyTime;
    private Integer categoryId;
    private CategoryEntity categoryEntity;
    private UserEntity userEntity;
    private List<ImageEntity> imageEntities;

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
    @Column(name = "user_id")
    @JsonIgnore
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    @JsonProperty("user")
    public UserEntity getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    @Basic
    @Column(name = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Basic
    @Column(name = "prefer_items")
    public String getPreferItems() {
        return preferItems;
    }

    public void setPreferItems(String preferItems) {
        this.preferItems = preferItems;
    }

    @Basic
    @Column(name = "address")
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Basic
    @Column(name = "status")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Basic
    @Column(name = "privacy")
    public String getPrivacy() {
        return privacy;
    }

    public void setPrivacy(String privacy) {
        this.privacy = privacy;
    }

    @Basic
    @Column(name = "create_time")
    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    @Basic
    @Column(name = "modify_time")
    public Timestamp getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Timestamp modifyTime) {
        this.modifyTime = modifyTime;
    }

    @Basic
    @Column(name = "category_id")
    @JsonIgnore
    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    @ManyToOne
    @JoinColumn(name="category_id", insertable = false, updatable = false)
    @JsonProperty("category")
    public CategoryEntity getCategoryEntity() {
        return categoryEntity;
    }

    public void setCategoryEntity(CategoryEntity categoryEntity) {
        this.categoryEntity = categoryEntity;
    }

    @OneToMany
    @JoinColumn(name="item_id", insertable = false, updatable = false)
    @JsonProperty("images")
    public List<ImageEntity> getImageEntities() {
        return imageEntities;
    }

    public void setImageEntities(List<ImageEntity> imageEntities) {
        this.imageEntities = imageEntities;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemEntity that = (ItemEntity) o;
        return id == that.id &&
                Objects.equals(name, that.name) &&
                Objects.equals(userId, that.userId) &&
                Objects.equals(description, that.description) &&
                Objects.equals(address, that.address) &&
                Objects.equals(status, that.status) &&
                Objects.equals(privacy, that.privacy) &&
                Objects.equals(createTime, that.createTime) &&
                Objects.equals(modifyTime, that.modifyTime) &&
                Objects.equals(categoryId, that.categoryId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, userId, description, address, status, privacy, createTime, modifyTime, categoryId);
    }
}