package com.example.jsonex.repository;

import com.example.jsonex.model.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("SELECT c.name, count(p), AVG(p.price), Sum(p.price) FROM Category c, Product p " +
            "GROUP BY c.name " +
            "ORDER BY count(c)")
    List<Category> findAllByNameOrderByCountOfProducts();
}
