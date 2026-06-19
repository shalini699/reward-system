package com.rewards.rewards_api.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rewards.rewards_api.entity.Transaction;

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
}