package com.capstone.exff.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "donation_post_target", schema = "exff")
public class DonationPostTargetEntity {
    private int id;
    private Integer donationPostId;
    private Integer categoryId;
    private Integer target;
//    private Integer numOfItem;
    private CategoryEntity categoryEntity;

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
    @Column(name = "donation_post_id", updatable = false)
    public Integer getDonationPostId() {
        return donationPostId;
    }

    public void setDonationPostId(Integer donationPostId) {
        this.donationPostId = donationPostId;
    }

    @Basic
    @Column(name = "category_id", updatable = false)
    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    @Basic
    @Column(name = "target")
    public Integer getTarget() {
        return target;
    }

    public void setTarget(Integer target) {
        this.target = target;
    }

//    @Basic
//    @Column(name = "num_of_item")
//    public Integer getNumOfItem() {
//        return numOfItem;
//    }
//
//    public void setNumOfItem(Integer numOfItem) {
//        this.numOfItem = numOfItem;
//    }

    @ManyToOne
    @JoinColumn(name="category_id", insertable = false, updatable = false)
    @JsonProperty("category")
    public CategoryEntity getCategoryEntity() {
        return categoryEntity;
    }

    public void setCategoryEntity(CategoryEntity categoryEntity) {
        this.categoryEntity = categoryEntity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DonationPostTargetEntity that = (DonationPostTargetEntity) o;
        return id == that.id &&
                (Objects.equals(donationPostId, that.donationPostId) &&
                Objects.equals(categoryId, that.categoryId)) &&
                Objects.equals(target, that.target);
    }


    @Override
    public int hashCode() {
        return Objects.hash(id, donationPostId, categoryId, target);
    }
}
