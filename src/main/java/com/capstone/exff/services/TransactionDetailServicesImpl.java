package com.capstone.exff.services;

import com.capstone.exff.constants.ExffStatus;
import com.capstone.exff.entities.ItemEntity;
import com.capstone.exff.entities.TransactionDetailEntity;
import com.capstone.exff.entities.TransactionDetails;
import com.capstone.exff.repositories.ItemRepository;
import com.capstone.exff.repositories.TransactionDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

@Service
public class TransactionDetailServicesImpl implements TransactionDetailServices {

    private TransactionDetailRepository transactionDetailRepository;
    private ItemRepository itemRepository;

    @Autowired
    public TransactionDetailServicesImpl(TransactionDetailRepository transactionDetailRepository,
                                         ItemRepository itemRepository) {
        this.transactionDetailRepository = transactionDetailRepository;
        this.itemRepository = itemRepository;
    }

    @Override
    public TransactionDetailEntity createDetailTrans(int transactionId, int itemId, int userId) {
        TransactionDetailEntity detailEntity = new TransactionDetailEntity();
        detailEntity.setItemId(itemId);
        detailEntity.setUserId(userId);
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

    @Override
    public void deleteTransactionDetailByTransactionId(int transactionId) {
        transactionDetailRepository.deleteByTransactionId(transactionId);
    }

    @Override
    public List<TransactionDetailEntity> getTransactionDetailsByTransactionId(int transactionId) {
        return transactionDetailRepository.findAllByTransactionId(transactionId);
    }

    @Override
    public void confirmTransactionDetail(List<Integer> itemIdList) {
        itemIdList.stream().forEach(t -> {
            TransactionDetailEntity detailEntity = transactionDetailRepository.getOne(t);
            if (detailEntity != null) {
                ItemEntity item = itemRepository.getItemById(detailEntity.getItemId());
                item.setStatus(ExffStatus.ITEM_TRADED);
                try {
                    itemRepository.save(item);
                } catch (Exception e) {
                }
            }
        });
    }


}
