package com.rewards.api.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.rewards.api.entity.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

List<Transaction> findByCustomer_Id(Long customerId);

List<Transaction> findByCustomer_NameIgnoreCase(String name);

List<Transaction> findByCustomer_IdAndTransactionDateBetween(
    Long customerId,
    LocalDate startDate,
    LocalDate endDate);

List<Transaction> findByCustomer_NameIgnoreCaseAndTransactionDateBetween(
    String name,
    LocalDate startDate,
    LocalDate endDate);

@Query("SELECT t FROM Transaction t JOIN FETCH t.customer WHERE t.transactionDate BETWEEN :startDate AND :endDate")
List<Transaction> findAllWithCustomerByTransactionDateBetween(
        @Param("startDate") LocalDate startDate, 
        @Param("endDate") LocalDate endDate
);
}

