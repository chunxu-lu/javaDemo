package com.example.javademo.repository;

import com.example.javademo.model.Bgimgtable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface BgimgtableRepository extends JpaRepository<Bgimgtable, Long> {
    @Modifying
    @Transactional
    @Query("UPDATE Bgimgtable b SET b.flag = 0")
    void resetAllFlags();
}