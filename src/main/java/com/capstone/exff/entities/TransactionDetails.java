package com.capstone.exff.entities;

import java.io.Serializable;
import java.util.List;

public class TransactionDetails implements Serializable {

    private int transactionId;
    private List<TransactionDetailEntity> transactionDetails;

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public List<TransactionDetailEntity> getTransactionDetails() {
        return transactionDetails;
    }

    public List<Integer> getItemIds() {
        List<Integer> result = null;
        if (transactionDetails == null) return null;
        transactionDetails.stream().forEach(t -> result.add(t.getItemId()));
        return result;
    }

    public void setTransactionDetails(List<TransactionDetailEntity> transactionDetails) {
        this.transactionDetails = transactionDetails;
    }
}
