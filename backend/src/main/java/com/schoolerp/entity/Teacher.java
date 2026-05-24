package com.schoolerp.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "teachers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Teacher extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "employee_id", unique = true)
    private String employeeId;

    @Column
    private String qualification;

    @Column
    private String specialization;

    @Column(name = "years_of_experience")
    private Integer yearsOfExperience;

    @Column
    private String address;

    @ManyToMany(mappedBy = "teachers")
    @Builder.Default
    private List<Subject> subjects = new ArrayList<>();

    @OneToMany(mappedBy = "classTeacher")
    @Builder.Default
    private List<ClassRoom> classRooms = new ArrayList<>();

    @OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Attendance> attendanceRecords = new ArrayList<>();
}
