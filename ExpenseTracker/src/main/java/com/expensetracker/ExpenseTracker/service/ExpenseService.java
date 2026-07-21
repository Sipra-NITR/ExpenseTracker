package com.expensetracker.ExpenseTracker.service;
import java.util.*;
import com.expensetracker.ExpenseTracker.dto.CategorySummaryDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.expensetracker.ExpenseTracker.dto.ExpenseDTO;
import com.expensetracker.ExpenseTracker.model.Expense;
import com.expensetracker.ExpenseTracker.model.User;
import com.expensetracker.ExpenseTracker.repository.ExpenseRepository;
import com.expensetracker.ExpenseTracker.repository.UserRepository;
@Service
public class ExpenseService {

    @Autowired
    private ExpenseRepository expenseRepository;
    @Autowired
    private UserRepository userRepository;

    // Add Expense
    public Expense addExpense(ExpenseDTO dto, Long userId) {

    User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));

    Expense expense = new Expense();

    expense.setTitle(dto.getTitle());
    expense.setAmount(dto.getAmount());
    expense.setCategory(dto.getCategory());
    expense.setDate(dto.getDate());
    expense.setType(dto.getType());
    expense.setUser(user);

    return expenseRepository.save(expense);
}

    // Get All Expenses
    public List<Expense> getAllExpenses(Long userId) {

    User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));

    return expenseRepository.findByUser(user);
}

    // Update Expense
    public Expense updateExpense(Long id, ExpenseDTO dto) {

        Optional<Expense> optionalExpense = expenseRepository.findById(id);

        if (optionalExpense.isEmpty()) {
            return null;
        }

        Expense expense = optionalExpense.get();

        expense.setTitle(dto.getTitle());
        expense.setAmount(dto.getAmount());
        expense.setCategory(dto.getCategory());
        expense.setDate(dto.getDate());
        expense.setType(dto.getType());

        return expenseRepository.save(expense);
    }

    // Delete Expense
    public String deleteExpense(Long id) {

        if (!expenseRepository.existsById(id)) {
            return "Expense not found!";
        }

        expenseRepository.deleteById(id);

        return "Expense deleted successfully!";
    }
    // Calculate Total Income
public Double getTotalIncome(Long userId) {

    User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));

    return expenseRepository.findByUser(user)
            .stream()
            .filter(expense -> "INCOME".equalsIgnoreCase(expense.getType()))
            .mapToDouble(Expense::getAmount)
            .sum();
}

// Calculate Total Expense
public Double getTotalExpense(Long userId) {

    User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));

    return expenseRepository.findByUser(user)
            .stream()
            .filter(expense -> "EXPENSE".equalsIgnoreCase(expense.getType()))
            .mapToDouble(Expense::getAmount)
            .sum();
}

// Calculate Balance
public Double getBalance(Long userId) {

    return getTotalIncome(userId) - getTotalExpense(userId);
}
public List<CategorySummaryDTO> getCategorySummary(Long userId) {

    User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));

    List<Expense> expenses = expenseRepository.findByUser(user);

    Map<String, Double> map = new HashMap<>();

    for (Expense expense : expenses) {

        if (!expense.getType().equalsIgnoreCase("EXPENSE")) {
            continue;
        }

        map.put(
                expense.getCategory(),
                map.getOrDefault(expense.getCategory(), 0.0)
                        + expense.getAmount()
        );
    }

    List<CategorySummaryDTO> result = new ArrayList<>();

    for (String category : map.keySet()) {

        result.add(
                new CategorySummaryDTO(
                        category,
                        map.get(category)
                )
        );

    }

    return result;

}

}