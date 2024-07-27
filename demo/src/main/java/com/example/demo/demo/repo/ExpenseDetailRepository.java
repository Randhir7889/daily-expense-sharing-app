package com.example.demo.demo.repo;
import com.example.expense.entity.ExpenseDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenseDetailRepository extends JpaRepository<ExpenseDetail, Long> {}