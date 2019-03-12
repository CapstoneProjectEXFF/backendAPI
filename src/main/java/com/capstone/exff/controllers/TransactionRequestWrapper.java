package com.capstone.exff.controllers;

import com.capstone.exff.entities.TransactionDetailEntity;
import com.capstone.exff.entities.TransactionEntity;

import java.util.List;

public class TransactionRequestWrapper {

    private TransactionEntity transaction;
    private List<TransactionDetailEntity> details;

    public List<TransactionDetailEntity> getDetails() {
        return details;
    }

    public void setDetails(List<TransactionDetailEntity> details) {
        this.details = details;
    }

    public TransactionEntity getTransaction() {
        return transaction;
    }

    public void setTransaction(TransactionEntity transaction) {
        this.transaction = transaction;
    }
}
