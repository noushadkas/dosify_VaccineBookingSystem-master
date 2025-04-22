package com.example.Dosify.repository;

import com.example.Dosify.model.Dose2_Certificate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Dose2_CertificateRepository extends JpaRepository<Dose2_Certificate, Integer> {
    Dose2_Certificate findByUserId(int id);
}