package com.gulteking.pdfencryptor.repository;

import com.gulteking.pdfencryptor.entity.TradeConfoUsers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TradeConfoUserRepository extends JpaRepository<TradeConfoUsers, Long> {}
