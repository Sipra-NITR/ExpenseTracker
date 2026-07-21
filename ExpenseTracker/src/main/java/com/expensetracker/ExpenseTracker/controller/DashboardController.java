package com.expensetracker.ExpenseTracker.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import com.expensetracker.ExpenseTracker.dto.CategorySummaryDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.expensetracker.ExpenseTracker.service.ExpenseService;

@RestController
@RequestMapping("/dashboard")
@CrossOrigin(origins = "*")
public class DashboardController {

    @Autowired
    private ExpenseService expenseService;

    @GetMapping("/{userId}")
    public ResponseEntity<Map<String, Double>> getDashboard(
            @PathVariable Long userId) {

        Map<String, Double> dashboard = new HashMap<>();

        dashboard.put(
                "income",
                expenseService.getTotalIncome(userId));

        dashboard.put(
                "expense",
                expenseService.getTotalExpense(userId));

        dashboard.put(
                "balance",
                expenseService.getBalance(userId));

        return ResponseEntity.ok(dashboard);

    }
    @GetMapping("/category-summary/{userId}")
public ResponseEntity<List<CategorySummaryDTO>> getCategorySummary(
        @PathVariable Long userId) {

    return ResponseEntity.ok(
            expenseService.getCategorySummary(userId)
    );

}

}