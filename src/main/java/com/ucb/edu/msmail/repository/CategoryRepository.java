package com.ucb.edu.msmail.repository;

import com.ucb.edu.msmail.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Integer> {
    CategoryEntity  findByCategoryId(Integer categoryId);
}
