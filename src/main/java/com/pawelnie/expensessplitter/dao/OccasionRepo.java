package com.pawelnie.expensessplitter.dao;

import com.pawelnie.expensessplitter.calc.Expense;
import com.pawelnie.expensessplitter.calc.Occasion;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OccasionRepo extends CrudRepository<Occasion, Long>{
}
