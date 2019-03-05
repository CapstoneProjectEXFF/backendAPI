package com.capstone.exff.services;

import com.capstone.exff.entities.TransactionEntity;
import org.springframework.http.ResponseEntity;

import java.sql.Timestamp;

public interface TransactionServices {
    TransactionEntity createTransaction(int senderId, int receiverId, int donationId,
                                 String status, Timestamp createTime,
                                 Timestamp modifiedTime);
    ResponseEntity confirmTransaction(int id);
}
