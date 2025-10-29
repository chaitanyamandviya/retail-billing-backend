package com.vandana.repository;

import com.vandana.entity.BillItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BillItemRepository extends JpaRepository<BillItem, Integer> {
    // You can add custom queries here as needed later
}
