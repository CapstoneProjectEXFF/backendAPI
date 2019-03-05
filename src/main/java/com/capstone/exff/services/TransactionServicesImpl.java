package com.capstone.exff.services;

import com.capstone.exff.entities.TransactionEntity;
import com.capstone.exff.repositories.TransactionRepository;
import com.capstone.exff.utilities.ExffMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class TransactionServicesImpl implements TransactionServices {

    private TransactionRepository transactionRepository;

    @Autowired
    public TransactionServicesImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public TransactionEntity createTransaction(int senderId, int receiverId, int donationId,
                                               String status, Timestamp createTime, Timestamp modifiedTime) {
        TransactionEntity transaction = new TransactionEntity();
        transaction.setSenderId(senderId);
        transaction.setReceiverId(receiverId);
        transaction.setDonationPostId(donationId);
        transaction.setStatus(status);
        transaction.setCreateTime(createTime);
        transaction.setModifyTime(modifiedTime);
        return transactionRepository.save(transaction);
    }

    @Override
    public ResponseEntity confirmTransaction(int id) {
        TransactionEntity transaction = transactionRepository.getOne(id);
        TransactionEntity updatedTransaction;
        if (transaction == null){
            return ResponseEntity.notFound().build();
        }
        transaction.setStatus("0");
        try {
            updatedTransaction = transactionRepository.save(transaction);
        } catch (Exception e) {
            return new ResponseEntity(new ExffMessage(e.getMessage()), HttpStatus.CONFLICT);
        }
        return new ResponseEntity(updatedTransaction, HttpStatus.OK);
    }
}
