package com.gulteking.pdfencryptor.repository;

import com.gulteking.pdfencryptor.entity.TradeLoanUsers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TradeLoanUserRepository extends JpaRepository<TradeLoanUsers, Long> {}
