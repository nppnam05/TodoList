package com.todolist.todolist_api.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

import com.todolist.todolist_api.constant.TodoStatus;

@Entity
@Table(name = "todos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Todo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long id;

    @Column(name = "Title", length = 255)
    private String title;

    @Column(name = "Description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "DueDate")
    private LocalDateTime dueDate;

    @Column(name = "Status", length = 50)
    private String status;

    @Column(name = "CreatedOn")
    private LocalDateTime createdOn;

    @Column(name = "ModifiedOn")
    private LocalDateTime modifiedOn;

    @PrePersist
    protected void onCreate() {
        createdOn = LocalDateTime.now();
        modifiedOn = LocalDateTime.now();
        if (status == null) {
            status = TodoStatus.TODO.name();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        modifiedOn = LocalDateTime.now();
    }
}
