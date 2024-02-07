package com.example.collection_medicine.repository;

import com.example.collection_medicine.domain.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicineRepository extends JpaRepository<Medicine, Long> {
}
