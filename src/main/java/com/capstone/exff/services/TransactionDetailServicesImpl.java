package com.capstone.exff.services;

import com.capstone.exff.entities.TransactionDetailEntity;
import com.capstone.exff.repositories.TransactionDetailRepository;
import org.springframework.stereotype.Service;

@Service
public class TransactionDetailServicesImpl implements TransactionDetailServices {

    private TransactionDetailRepository transactionDetailRepository;

    @Override
    public TransactionDetailEntity createDetailTrans(int transactionId, int itemId) {
        TransactionDetailEntity detailEntity = new TransactionDetailEntity();
        detailEntity.setItemId(itemId);
        return transactionDetailRepository.save(detailEntity);
    }
}
