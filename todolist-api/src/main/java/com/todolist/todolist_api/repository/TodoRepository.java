package com.todolist.todolist_api.repository;

import com.todolist.todolist_api.entity.Todo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {

  @Query("""
      SELECT t FROM Todo t
      WHERE (:status IS NULL OR t.status = :status)
        AND (:keyword IS NULL OR LOWER(t.title) LIKE LOWER(CONCAT('%', CAST(:keyword AS string), '%'))
                             OR LOWER(t.description) LIKE LOWER(CONCAT('%', CAST(:keyword AS string), '%')))
      """)
  Page<Todo> findAllWithFilters(
      @Param("status") String status,
      @Param("keyword") String keyword,
      Pageable pageable);

  @Query("""
      SELECT COUNT(t) FROM Todo t WHERE t.status = :status
      """)
  long countByStatus(@Param("status") String status);

  @Query("""
      SELECT t FROM Todo t
      WHERE t.status NOT IN (:status)
        AND t.dueDate IS NOT NULL
        AND t.dueDate BETWEEN :now AND :deadline
      ORDER BY t.dueDate ASC
      """)
  List<Todo> findOverdueSoon(@Param("now") LocalDateTime now,
      @Param("deadline") LocalDateTime deadline,
      @Param("status") List<String> status);
}
