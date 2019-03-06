package com.capstone.exff.services;

import com.capstone.exff.entities.TransactionDetailEntity;
import com.capstone.exff.repositories.TransactionDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionDetailServicesImpl implements TransactionDetailServices {

    private TransactionDetailRepository transactionDetailRepository;

    @Autowired
    public TransactionDetailServicesImpl(TransactionDetailRepository transactionDetailRepository) {
        this.transactionDetailRepository = transactionDetailRepository;
    }

    @Override
    public TransactionDetailEntity createDetailTrans(int transactionId, int itemId) {
        TransactionDetailEntity detailEntity = new TransactionDetailEntity();
        detailEntity.setItemId(itemId);
        detailEntity.setTransactionId(transactionId);
        return transactionDetailRepository.save(detailEntity);
    }

    @Override
    public TransactionDetailEntity updateTransactionDetail(TransactionDetailEntity transactionDetailEntity) {
        return transactionDetailRepository.save(transactionDetailEntity);
    }

    @Override
    public void deleteTransactionDetail(TransactionDetailEntity transactionDetailEntity) {
        transactionDetailRepository.delete(transactionDetailEntity);
    }


}
