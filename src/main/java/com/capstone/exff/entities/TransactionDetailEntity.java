package com.capstone.exff.entities;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "transaction_detail", schema = "exff")
public class TransactionDetailEntity {
    private int id;
    private Integer transactionId;
    private Integer itemId;

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
    @Column(name = "transaction_id")
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

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransactionDetailEntity that = (TransactionDetailEntity) o;
        return id == that.id &&
                Objects.equals(transactionId, that.transactionId) &&
                Objects.equals(itemId, that.itemId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, transactionId, itemId);
    }
}
