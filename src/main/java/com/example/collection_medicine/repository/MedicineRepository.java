package com.example.collection_medicine.repository;

import com.example.collection_medicine.domain.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MedicineRepository extends JpaRepository<Medicine, Long> {
    Optional<Medicine> findByProductName(String productName);
}
