package com.capstone.exff.repositories;

import com.capstone.exff.entities.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<TransactionEntity, Integer> {

    @Override
    Optional<TransactionEntity> findById(Integer integer);

    @Override
    <S extends TransactionEntity> S save(S s);

    @Query("select t " +
            "from TransactionEntity t " +
            "where (t.receiverId = :userId and t.status = :receiveStatus) " +
            "or (t.senderId = :userId and t.status = :sendStatus) " +
            "order by t.modifyTime desc ")
    List<TransactionEntity> findReceiveTransaction(
            @Param("userId") int userId,
            @Param("receiveStatus") String receiveStatus,
            @Param("sendStatus") String sendStatus
    );
    @Query("select t " +
            "from TransactionEntity t " +
            "where (t.receiverId = :userId or t.senderId = :userId) " +
            "and t.status = :sendStatus " +
            "order by t.modifyTime desc ")
    List<TransactionEntity> findSendTransactionByUserId(
            @Param("userId") int userId,
            @Param("sendStatus") String sendStatus
    );

    @Override
    void delete(TransactionEntity transactionEntity);

    @Query("select t from TransactionEntity t where t.donationPostId = :donationPostId")
    List<TransactionEntity> getTransactionByDonationPostId(int donationPostId);

    List<TransactionEntity> findTop10BySenderIdAndStatusOrderByCreateTimeAsc(int receiverId, String status);

    List<TransactionEntity> findBySenderIdOrReceiverIdOrderByCreateTimeAsc(int senderId, int receiverId);
    List<TransactionEntity> findBySenderIdOrReceiverIdOrderByCreateTimeDesc(int senderId, int receiverId);

    int countBySenderIdOrReceiverIdOrderByCreateTimeAsc(int senderId, int receiverId);

    List<TransactionEntity> findByReceiverIdAndDonationPostIdNotNullOrderByCreateTimeDesc(int receiverId);
}
