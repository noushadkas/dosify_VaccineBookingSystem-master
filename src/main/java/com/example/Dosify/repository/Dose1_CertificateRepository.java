package com.example.Dosify.repository;

import com.example.Dosify.model.Dose1_Certificate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Dose1_CertificateRepository extends JpaRepository<Dose1_Certificate, Integer> {
    Dose1_Certificate findByUserId(int id);
}