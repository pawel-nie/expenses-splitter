package com.pawelnie.expensessplitter.dao;

import com.pawelnie.expensessplitter.calc.Expense;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpenseRepo extends CrudRepository<Expense, Long>{
}
