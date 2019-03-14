package com.capstone.exff.services;

import com.capstone.exff.entities.TransactionDetailEntity;

import java.util.List;

public interface TransactionDetailServices {
    TransactionDetailEntity createDetailTrans(int transactionId, int itemId, int useId);
    TransactionDetailEntity updateTransactionDetail(TransactionDetailEntity transactionDetailEntity);
    void deleteTransactionDetail(TransactionDetailEntity transactionDetailEntity);
    void deleteTransactionDetailByTransactionId(int transactionId);
    List<TransactionDetailEntity> getTransactionDetailsByTransactionId(int transactionId);
    void confirmTransactionDetail(List<Integer> itemIdList);
}
