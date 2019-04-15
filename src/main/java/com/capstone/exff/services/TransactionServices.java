package com.capstone.exff.services;

import com.capstone.exff.entities.TransactionEntity;

import java.sql.Timestamp;
import java.util.List;

public interface TransactionServices {
    List<TransactionEntity> getDonationTransactionByUserId(int userId);

    List<TransactionEntity> getTopTransactionByUserId(int receiverId);

    List<TransactionEntity> getAllTransactionByUserID(int userId);

    List<TransactionEntity> getTopTransactionBySenderId(int senderId);

    int createTransaction(int senderId, TransactionEntity transaction);

    TransactionEntity updateTransaction(TransactionEntity transactionEntity);

    void deleteTransaction(TransactionEntity transactionEntity);

    TransactionEntity getTransactionByTransactionId(int transactionId);

    boolean isValidTransaction(int userId, int transactionId);

    void confirmTransaction(int transactionId);
    List<TransactionEntity> getTransactionByDonationPostId(int donationPostId);


    int getCountAllTransactionsByUserID(int userId);
}
