package com.expensetracker.ExpenseTracker.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.expensetracker.ExpenseTracker.dto.ExpenseDTO;
import com.expensetracker.ExpenseTracker.model.Expense;
import com.expensetracker.ExpenseTracker.service.ExpenseService;

@RestController
@RequestMapping("/expense")
@CrossOrigin(origins = "*")
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;

    @PostMapping("/{userId}")
    public ResponseEntity<Expense> addExpense(
            @PathVariable Long userId,
            @RequestBody ExpenseDTO dto) {

        return ResponseEntity.ok(expenseService.addExpense(dto, userId));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<Expense>> getAllExpenses(
            @PathVariable Long userId) {

        return ResponseEntity.ok(expenseService.getAllExpenses(userId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Expense> updateExpense(
            @PathVariable Long id,
            @RequestBody ExpenseDTO dto) {

        Expense expense = expenseService.updateExpense(id, dto);

        if (expense == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(expense);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteExpense(@PathVariable Long id) {

        return ResponseEntity.ok(expenseService.deleteExpense(id));
    }
}