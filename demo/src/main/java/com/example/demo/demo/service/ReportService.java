package com.example.demo.demo.service;

import com.example.expense.entity.Expense;
import com.example.expense.repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReportService {
    @Autowired
    private ExpenseRepository expenseRepository;

    public String generateBalanceSheet() {
        List<Expense> expenses = expenseRepository.findAll();

        StringBuilder report = new StringBuilder();
        report.append("Date,Total Amount,Split Type,User ID,Amount,Percentage\n");

        for (Expense expense : expenses) {
            expense.getExpenseDetails().forEach(detail -> report.append(String.format(
                    "%s,%.2f,%s,%d,%.2f,%.2f\n",
                    expense.getDate(),
                    expense.getTotalAmount(),
                    expense.getSplitType(),
                    detail.getUserId(),
                    detail.getAmount(),
                    detail.getPercentage()
            )));
        }

        return report.toString();
    }

    public byte[] downloadBalanceSheet() {
        String report = generateBalanceSheet();
        return report.getBytes(StandardCharsets.UTF_8);
    }
}