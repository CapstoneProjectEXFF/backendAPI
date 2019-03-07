package com.capstone.exff.services;

import com.capstone.exff.entities.TransactionDetailEntity;

public interface TransactionDetailServices {
    TransactionDetailEntity createDetailTrans(int transactionId, int itemId);
    TransactionDetailEntity updateTransactionDetail(TransactionDetailEntity transactionDetailEntity);
    void deleteTransactionDetail(TransactionDetailEntity transactionDetailEntity);
    void deleteTransactionDetailByTransactionId(int transactionId);
}
