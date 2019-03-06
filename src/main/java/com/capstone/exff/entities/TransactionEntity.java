package com.capstone.exff.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "transaction", schema = "exff")
public class TransactionEntity implements Serializable {
    private int id;
    private Integer senderId;
    private Integer receiverId;
    private Integer donationPostId;
    private String status;
    private Timestamp createTime;
    private Timestamp modifyTime;
    private UserEntity sender;
    private UserEntity receiver;

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
    @Column(name = "sender_id")
    public Integer getSenderId() {
        return senderId;
    }

    public void setSenderId(Integer senderId) {
        this.senderId = senderId;
    }

    @Basic
    @Column(name = "receiver_id")
    public Integer getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Integer receiverId) {
        this.receiverId = receiverId;
    }

    @Basic
    @Column(name = "donation_post_id")
    public Integer getDonationPostId() {
        return donationPostId;
    }

    public void setDonationPostId(Integer donationPostId) {
        this.donationPostId = donationPostId;
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

    @ManyToOne
    @JoinColumn(name = "sender_id", referencedColumnName = "id", insertable = false, updatable = false)
    public UserEntity getSender() {
        return sender;
    }

    public void setSender(UserEntity sender) {
        this.sender = sender;
    }

    @ManyToOne
    @JoinColumn(name = "receiver_id", referencedColumnName = "id", insertable = false, updatable = false)
    public UserEntity getReceiver() {
        return receiver;
    }

    public void setReceiver(UserEntity receiver) {
        this.receiver = receiver;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransactionEntity that = (TransactionEntity) o;
        return id == that.id &&
                Objects.equals(senderId, that.senderId) &&
                Objects.equals(receiverId, that.receiverId) &&
                Objects.equals(donationPostId, that.donationPostId) &&
                Objects.equals(status, that.status) &&
                Objects.equals(createTime, that.createTime) &&
                Objects.equals(modifyTime, that.modifyTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, senderId, receiverId, donationPostId, status, createTime, modifyTime);
    }
}
