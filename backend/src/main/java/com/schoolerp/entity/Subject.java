package com.schoolerp.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "subjects")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Subject extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "subject_name", nullable = false)
    private String subjectName;

    @Column(name = "subject_code", unique = true)
    private String subjectCode;

    @Column
    private String description;

    @Column(name = "credit_hours")
    private Integer creditHours;

    @ManyToMany
    @JoinTable(
        name = "subject_teachers",
        joinColumns = @JoinColumn(name = "subject_id"),
        inverseJoinColumns = @JoinColumn(name = "teacher_id")
    )
    @Builder.Default
    private List<Teacher> teachers = new ArrayList<>();

    @ManyToMany
    @JoinTable(
        name = "subject_classrooms",
        joinColumns = @JoinColumn(name = "subject_id"),
        inverseJoinColumns = @JoinColumn(name = "classroom_id")
    )
    @Builder.Default
    private List<ClassRoom> classRooms = new ArrayList<>();

    @OneToMany(mappedBy = "subject", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Exam> exams = new ArrayList<>();

    @OneToMany(mappedBy = "subject", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Timetable> timetables = new ArrayList<>();
}
