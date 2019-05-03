package com.capstone.exff.services;

import com.capstone.exff.constants.ExffStatus;
import com.capstone.exff.entities.TransactionDetailEntity;
import com.capstone.exff.entities.TransactionEntity;
import com.capstone.exff.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class TransactionServicesImpl implements TransactionServices {

    private TransactionRepository transactionRepository;
    private TransactionDetailServices transactionDetailServices;

    @Autowired
    public TransactionServicesImpl(TransactionRepository transactionRepository, TransactionDetailServices transactionDetailServices) {
        this.transactionRepository = transactionRepository;
        this.transactionDetailServices = transactionDetailServices;
    }

    @Override
    public List<TransactionEntity> getDonationTransactionByUserId(int userId) {
        return transactionRepository.findByReceiverIdAndDonationPostIdNotNullOrderByCreateTimeDesc(userId);
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
    public List<TransactionEntity> getAllTransactionByUserID(int userId) {
        return transactionRepository.findBySenderIdOrReceiverIdOrderByCreateTimeDesc(userId, userId);
    }

    @Override
    public List<TransactionEntity> getTopTransactionBySenderId(int senderId) {
        return transactionRepository.findTop10BySenderIdAndStatusOrderByCreateTimeAsc(senderId, ExffStatus.TRANSACTION_DONE);
    }

    @Override
    public int createTransaction(int senderId, TransactionEntity transaction) {
        Timestamp createTime = new Timestamp(System.currentTimeMillis());
        Timestamp modifiedTime = createTime;
        transaction.setSenderId(senderId);
        transaction.setCreateTime(createTime);
        transaction.setModifyTime(modifiedTime);
        if (transaction.getDonationPostId() != null) {
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
        return (transactionEntity.getReceiverId() == userId) || (transactionEntity.getSenderId() == userId);
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

    @Override
    public TransactionEntity uploadReceiptImage(int transactionId, int userId, String imageUrl) {
        TransactionEntity transactionEntity = getTransactionByTransactionId(transactionId);
        if (transactionEntity.getStatus().equals(ExffStatus.TRANSACTION_DONE)) {
            return null;
        }
        if (transactionEntity.getSenderId() == userId) {
            if (transactionEntity.getSenderReceipt() == null || !transactionEntity.getStatus().equals(ExffStatus.TRANSACTION_SENDER_RECEIPT_CONFRIMED)) {
                transactionEntity.setSenderReceipt(imageUrl);
                return transactionRepository.save(transactionEntity);
            }
        } else if (transactionEntity.getReceiverId() == userId) {
            if (transactionEntity.getReceiverReceipt() == null || !transactionEntity.getStatus().equals(ExffStatus.TRANSACTION_RECEIVER_RECEIPT_CONFRIMED)) {
                transactionEntity.setReceiverReceipt(imageUrl);
                return transactionRepository.save(transactionEntity);
            }
        }
        return null;
    }

    @Override
    public TransactionEntity confirmReceiptImage(int transactionId, int userId) {
        TransactionEntity transactionEntity = getTransactionByTransactionId(transactionId);
        String status = transactionEntity.getStatus();
        if (status.equals(ExffStatus.TRANSACTION_DONATE) && transactionEntity.getReceiverId() == userId){
            transactionEntity.setStatus(ExffStatus.TRANSACTION_DONE);
            return transactionRepository.save(transactionEntity);
        }
        if (transactionEntity.getSenderId() == userId) { // check sender is confirming
            if (transactionEntity.getReceiverReceipt() != null) { // check receipt exist
                if (status.equals(ExffStatus.TRANSACTION_SENDER_RECEIPT_CONFRIMED)) { // check sender's receipt is confirmed
                    transactionEntity.setStatus(ExffStatus.TRANSACTION_DONE); // if 2 confirm
                    return transactionRepository.save(transactionEntity);
                } else if (status.equals(ExffStatus.TRANSACTION_SEND)) {
                    if (checkGiftAway(transactionId, transactionEntity.getReceiverId())) {
                        transactionEntity.setStatus(ExffStatus.TRANSACTION_DONE); // if 2 confirm
                        return transactionRepository.save(transactionEntity);
                    } else {
                        transactionEntity.setStatus(ExffStatus.TRANSACTION_RECEIVER_RECEIPT_CONFRIMED); // confirm to receiver
                        return transactionRepository.save(transactionEntity);
                    }
                }
            }
        } else if (transactionEntity.getReceiverId() == userId) { // check receiver is confirming
            if (transactionEntity.getSenderReceipt() != null) { // check receipt exist
                if (status.equals(ExffStatus.TRANSACTION_RECEIVER_RECEIPT_CONFRIMED)) {
                    transactionEntity.setStatus(ExffStatus.TRANSACTION_DONE);
                    return transactionRepository.save(transactionEntity);
                } else if (status.equals(ExffStatus.TRANSACTION_SEND)) {
                    if (checkGiftAway(transactionId, transactionEntity.getSenderId())) {
                        transactionEntity.setStatus(ExffStatus.TRANSACTION_DONE); // if 2 confirm
                        return transactionRepository.save(transactionEntity);
                    } else {
                        transactionEntity.setStatus(ExffStatus.TRANSACTION_SENDER_RECEIPT_CONFRIMED);
                        return transactionRepository.save(transactionEntity);
                    }
                }
            }
        }
        return null;
    }

    @Override
    public int getCountAllTransactionsByUserID(int userId) {
        return transactionRepository.countBySenderIdOrReceiverIdOrderByCreateTimeAsc(userId, userId);
    }

    private boolean checkGiftAway(int transactionId, int userId) {
        List<TransactionDetailEntity> details = transactionDetailServices.getTransactionDetailsByTransactionId(transactionId);
        int tmp = 0;
        for (TransactionDetailEntity detailEntity :
                details) {
            if (detailEntity.getItem().getUserId() == userId) {
                tmp++;
            } else {
                break;
            }
        }
        return details.size() == tmp;
    }
}
