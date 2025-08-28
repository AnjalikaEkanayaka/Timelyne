package com.Timelyne.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "routines")
@Getter
@Setter
@NoArgsConstructor

public class Routine {

     @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false)
    private String userEmail;

    @Column(nullable=false,length=100)
    private String title;

    @Column(length=500)
    private String description;

    // How many minutes this routine consumes EACH day (must be multiple of 15)

    @Column(nullable=false)
    private Integer minutesPerDay;

      // User can pause a routine without deleting it
    @Column(nullable = false)
    private Boolean active=true;

    private LocalDateTime createAt;
    private LocalDateTime updateAt;

    @PrePersist
    protected void onCreate(){
        createAt=LocalDateTime.now();
        updateAt=createAt;
    }
    







}
