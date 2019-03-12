package com.capstone.exff.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "transaction_detail", schema = "exff")
public class TransactionDetailEntity implements Serializable {
    private int id;
    private Integer transactionId;
    private Integer itemId;
    private Integer userId;
    private ItemEntity item;

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
    @Column(name = "transaction_id", updatable = false)
    public Integer getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Integer transactionId) {
        this.transactionId = transactionId;
    }

    @Basic
    @Column(name = "item_id")
    public Integer getItemId() {
        return itemId;
    }

    @Basic
    @Column(name = "user_id")
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @ManyToOne
    @JoinColumn(name = "item_id", insertable = false, updatable = false)
    @JsonProperty("item")
    public ItemEntity getItem() {
        return item;
    }

    public void setItem(ItemEntity item) {
        this.item = item;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        TransactionDetailEntity that = (TransactionDetailEntity) o;
//        return id == that.id &&
//                Objects.equals(transactionId, that.transactionId) &&
//                Objects.equals(itemId, that.itemId);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(id, transactionId, itemId);
//    }
}
