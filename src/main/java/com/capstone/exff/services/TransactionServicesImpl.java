package com.capstone.exff.services;

import com.capstone.exff.constants.ExffStatus;
import com.capstone.exff.entities.TransactionEntity;
import com.capstone.exff.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class TransactionServicesImpl implements TransactionServices {

    private TransactionRepository transactionRepository;

    @Autowired
    public TransactionServicesImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public List<TransactionEntity> getTopTransactionByUserId(int receiverId) {
        return transactionRepository.findReceiveTransaction(
                receiverId,
                ExffStatus.TRANSACTION_SEND,
                ExffStatus.TRANSACTION_RESEND
        );
    }

    @Override
    public int createTransaction(int senderId, TransactionEntity transaction) {
        Timestamp createTime = new Timestamp(System.currentTimeMillis());
        Timestamp modifiedTime = createTime;
        transaction.setSenderId(senderId);
        transaction.setCreateTime(createTime);
        transaction.setModifyTime(modifiedTime);
        if (transaction.getDonationPostId() != null){
            transaction.setStatus(ExffStatus.TRANSACTION_DONATE);
        } else {
            transaction.setStatus(ExffStatus.TRANSACTION_SEND);
        }
        TransactionEntity trans = transactionRepository.save(transaction);
        return trans.getId();
    }

    @Override
    public TransactionEntity updateTransaction(TransactionEntity transactionEntity) {
        return transactionRepository.save(transactionEntity);
    }

    @Override
    public void deleteTransaction(TransactionEntity transactionEntity) {
        transactionRepository.delete(transactionEntity);
    }

    @Override
    public TransactionEntity getTransactionByTransactionId(int transactionId) {
        return transactionRepository.findById(transactionId).get();
    }

    @Override
    public boolean isValidTransaction(int userId, int transactionId) {
        TransactionEntity transactionEntity = transactionRepository.getOne(transactionId);
        return transactionEntity.getReceiverId() == userId;
    }

    @Override
    public void confirmTransaction(int transactionId) {
        TransactionEntity transactionEntity = transactionRepository.getOne(transactionId);
        if (transactionEntity != null) {
            transactionEntity.setStatus(ExffStatus.TRANSACTION_DONE);
            try {
                transactionRepository.save(transactionEntity);
            } catch (Exception e) {
            }
        }
    }

    @Override
    public List<TransactionEntity> getTransactionByDonationPostId(int donationPostId) {
        return transactionRepository.getTransactionByDonationPostId(donationPostId);
    }
}
