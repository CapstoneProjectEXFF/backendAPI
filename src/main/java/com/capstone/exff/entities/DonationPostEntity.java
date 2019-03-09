package com.capstone.exff.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "donation_post", schema = "exff")
public class DonationPostEntity {
    private int id;
    private String content;
    private String status;
    private Timestamp createTime;
    private Timestamp modifyTime;
    private Integer userId;
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
    @Column(name = "content")
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    @OneToMany
    @JoinColumn(name="donation_post_id", insertable = false, updatable = false)
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
        DonationPostEntity that = (DonationPostEntity) o;
        return id == that.id &&
                Objects.equals(content, that.content) &&
                Objects.equals(status, that.status) &&
                Objects.equals(createTime, that.createTime) &&
                Objects.equals(modifyTime, that.modifyTime) &&
                Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, content, status, createTime, modifyTime, userId);
    }
}
