package com.schoolerp.service;

import com.schoolerp.dto.request.ExamRequest;
import com.schoolerp.dto.request.MarksRequest;
import com.schoolerp.dto.response.ExamResponse;
import com.schoolerp.dto.response.MarksResponse;
import com.schoolerp.entity.ClassRoom;
import com.schoolerp.entity.Exam;
import com.schoolerp.entity.Marks;
import com.schoolerp.entity.Student;
import com.schoolerp.entity.Subject;
import com.schoolerp.exception.DuplicateResourceException;
import com.schoolerp.exception.ResourceNotFoundException;
import com.schoolerp.repository.ClassRoomRepository;
import com.schoolerp.repository.ExamRepository;
import com.schoolerp.repository.MarksRepository;
import com.schoolerp.repository.StudentRepository;
import com.schoolerp.repository.SubjectRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ExamService {

    private final ExamRepository examRepository;
    private final MarksRepository marksRepository;
    private final SubjectRepository subjectRepository;
    private final ClassRoomRepository classRoomRepository;
    private final StudentRepository studentRepository;

    public ExamResponse createExam(ExamRequest request) {
        Subject subject = subjectRepository.findById(request.getSubjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Subject", "id", request.getSubjectId()));
        ClassRoom classRoom = classRoomRepository.findById(request.getClassRoomId())
                .orElseThrow(() -> new ResourceNotFoundException("ClassRoom", "id", request.getClassRoomId()));

        Exam exam = Exam.builder()
                .examName(request.getExamName())
                .examType(request.getExamType())
                .subject(subject)
                .classRoom(classRoom)
                .examDate(request.getExamDate())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .totalMarks(request.getTotalMarks())
                .passingMarks(request.getPassingMarks())
                .academicYear(request.getAcademicYear())
                .description(request.getDescription())
                .build();

        return mapExamToResponse(examRepository.save(exam));
    }

    @Transactional(readOnly = true)
    public Page<ExamResponse> getExams(Pageable pageable) {
        return examRepository.findByDeletedFalse(pageable).map(this::mapExamToResponse);
    }

    public List<MarksResponse> enterMarks(MarksRequest request) {
        Exam exam = examRepository.findById(request.getExamId())
                .orElseThrow(() -> new ResourceNotFoundException("Exam", "id", request.getExamId()));

        return request.getRecords().stream().map(record -> {
            if (marksRepository.existsByStudentIdAndExamId(record.getStudentId(), exam.getId())) {
                throw new DuplicateResourceException("Marks already entered for student: " + record.getStudentId());
            }
            Student student = studentRepository.findById(record.getStudentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Student", "id", record.getStudentId()));

            Marks marks = Marks.builder()
                    .student(student)
                    .exam(exam)
                    .marksObtained(record.isAbsent() ? 0.0 : record.getMarksObtained())
                    .grade(record.getGrade())
                    .remarks(record.getRemarks())
                    .absent(record.isAbsent())
                    .build();

            return mapMarksToResponse(marksRepository.save(marks));
        }).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<MarksResponse> getStudentMarks(Long studentId, Pageable pageable) {
        return marksRepository.findByStudentId(studentId, pageable).map(this::mapMarksToResponse);
    }

    private ExamResponse mapExamToResponse(Exam e) {
        return ExamResponse.builder()
                .id(e.getId())
                .examName(e.getExamName())
                .examType(e.getExamType())
                .subjectId(e.getSubject().getId())
                .subjectName(e.getSubject().getSubjectName())
                .classRoomId(e.getClassRoom().getId())
                .className(e.getClassRoom().getClassName())
                .examDate(e.getExamDate())
                .startTime(e.getStartTime())
                .endTime(e.getEndTime())
                .totalMarks(e.getTotalMarks())
                .passingMarks(e.getPassingMarks())
                .academicYear(e.getAcademicYear())
                .description(e.getDescription())
                .build();
    }

    private MarksResponse mapMarksToResponse(Marks m) {
        double percentage = m.getExam().getTotalMarks() > 0
                ? (m.getMarksObtained() / m.getExam().getTotalMarks()) * 100 : 0;
        return MarksResponse.builder()
                .id(m.getId())
                .studentId(m.getStudent().getId())
                .studentName(m.getStudent().getUser().getFullName())
                .rollNumber(m.getStudent().getRollNumber())
                .examId(m.getExam().getId())
                .examName(m.getExam().getExamName())
                .subjectName(m.getExam().getSubject().getSubjectName())
                .marksObtained(m.getMarksObtained())
                .totalMarks(m.getExam().getTotalMarks())
                .percentage(Math.round(percentage * 100.0) / 100.0)
                .grade(m.getGrade())
                .remarks(m.getRemarks())
                .absent(m.isAbsent())
                .build();
    }
}
