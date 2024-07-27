package com.example.demo.demo.service;

import com.example.expense.entity.Expense;
import com.example.expense.entity.ExpenseDetail;
import com.example.expense.entity.SplitType;
import com.example.expense.repository.ExpenseRepository;
import com.example.expense.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExpenseService {
    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private UserRepository userRepository;

    public Expense addExpense(Expense expense) {
        // Validate and calculate expense details based on split type
        validateExpense(expense);
        calculateExpenseDetails(expense);
        return expenseRepository.save(expense);
    }

    public List<Expense> getExpensesByUserId(Long userId) {
        return expenseRepository.findAll()
            .stream()
            .filter(expense -> expense.getExpenseDetails().stream()
                    .anyMatch(detail -> detail.getUserId().equals(userId)))
            .collect(Collectors.toList());
    }

    public List<Expense> getAllExpenses() {
        return expenseRepository.findAll();
    }

    private void validateExpense(Expense expense) {
        if (expense.getSplitType() == SplitType.PERCENTAGE) {
            double totalPercentage = expense.getExpenseDetails().stream()
                    .mapToDouble(ExpenseDetail::getPercentage)
                    .sum();
            if (totalPercentage != 100.0) {
                throw new IllegalArgumentException("Percentages must add up to 100%");
            }
        }
    }

    private void calculateExpenseDetails(Expense expense) {
        double totalAmount = expense.getTotalAmount();
        switch (expense.getSplitType()) {
            case EQUAL:
                double equalShare = totalAmount / expense.getExpenseDetails().size();
                expense.getExpenseDetails().forEach(detail -> detail.setAmount(equalShare));
                break;
            case PERCENTAGE:
                expense.getExpenseDetails().forEach(detail -> {
                    double amount = (detail.getPercentage() / 100.0) * totalAmount;
                    detail.setAmount(amount);
                });
                break;
            case EXACT:
                // Ensure amounts match the total
                double totalExact = expense.getExpenseDetails().stream()
                        .mapToDouble(ExpenseDetail::getAmount)
                        .sum();
                if (totalExact != totalAmount) {
                    throw new IllegalArgumentException("Exact amounts must add up to total");
                }
                break;
        }
    }
}