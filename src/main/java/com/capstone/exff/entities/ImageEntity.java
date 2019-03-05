package com.capstone.exff.entities;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "image", schema = "exff")
public class ImageEntity {
    private int id;
    private String url;
    private Integer itemId;
    private Integer donationPostId;

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
    @Column(name = "url")
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Basic
    @Column(name = "item_id")
    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    @Basic
    @Column(name = "donation_post_id")
    public Integer getDonationPostId() {
        return donationPostId;
    }

    public void setDonationPostId(Integer donationPostId) {
        this.donationPostId = donationPostId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImageEntity that = (ImageEntity) o;
        return id == that.id &&
                Objects.equals(url, that.url) &&
                Objects.equals(itemId, that.itemId) &&
                Objects.equals(donationPostId, that.donationPostId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, url, itemId, donationPostId);
    }
}
