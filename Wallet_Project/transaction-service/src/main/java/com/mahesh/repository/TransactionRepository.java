package com.mahesh.repository;


import com.mahesh.models.Transaction;
import com.mahesh.models.TransactionStatus;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction,Integer> {
    @Transactional
    @Modifying
    @Query("update Transaction t set t.transactionStatus = ?2 where t.externalId = ?1")
    void updateTransaction(String externalTxnId, TransactionStatus transactionStatus);
}

