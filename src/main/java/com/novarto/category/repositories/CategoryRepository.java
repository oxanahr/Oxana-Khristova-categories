package com.novarto.category.repositories;

import com.novarto.category.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Transactional(readOnly = false)
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findById(Long id);
    @Transactional
    @Modifying
    @Query(value = "update Category c set c.name = :name where c.categoryId = :id")
    void editCategory(@Param("name") String name, @Param("id") Long id);
}
