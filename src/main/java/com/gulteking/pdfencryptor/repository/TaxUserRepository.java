package com.gulteking.pdfencryptor.repository;

import com.gulteking.pdfencryptor.entity.TaxUsers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaxUserRepository extends JpaRepository<TaxUsers, Long> {}
