package com.schoolerp.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "marks", indexes = {
    @Index(name = "idx_marks_student_exam", columnList = "student_id, exam_id", unique = true)
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Marks extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exam_id", nullable = false)
    private Exam exam;

    @Column(name = "marks_obtained", nullable = false)
    private Double marksObtained;

    @Column
    private String grade;

    @Column
    private String remarks;

    @Column(name = "is_absent")
    @Builder.Default
    private boolean absent = false;
}
