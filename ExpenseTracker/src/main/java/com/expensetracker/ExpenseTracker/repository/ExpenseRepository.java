package com.expensetracker.ExpenseTracker.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.expensetracker.ExpenseTracker.model.Expense;
import com.expensetracker.ExpenseTracker.model.User;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    // Get all expenses of a particular user
    List<Expense> findByUser(User user);

    // Get expenses by category
    List<Expense> findByCategory(String category);

    // Get expenses of a user by category
    List<Expense> findByUserAndCategory(User user, String category);

}