package com.mahesh.repository;

import com.mahesh.model.Wallet;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletRepository extends JpaRepository<Wallet,Integer> {

    Wallet findByWalletId(String walletId);

    @Transactional
    @Modifying
    @Query("update Wallet w set w.balance = w.balance + :amount where w.walletId = :walletId")
    void updateWallet(String walletId, Long amount);

//    @Query("update Wallet w set w.balance = w.balance + :amount where w.walletId = :walletId")
//    void incrementWallet(String walletId, Long amount);
//
//    @Query("update Wallet w set w.balance = w.balance - :amount where w.walletId = :walletId")
//    void decrementWallet(String walletId, Long amount);

}
