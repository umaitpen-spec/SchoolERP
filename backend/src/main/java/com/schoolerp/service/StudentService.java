package com.schoolerp.service;

import com.schoolerp.dto.request.StudentRequest;
import com.schoolerp.dto.response.StudentResponse;
import com.schoolerp.entity.ClassRoom;
import com.schoolerp.entity.Parent;
import com.schoolerp.entity.Student;
import com.schoolerp.entity.User;
import com.schoolerp.exception.DuplicateResourceException;
import com.schoolerp.exception.ResourceNotFoundException;
import com.schoolerp.repository.ClassRoomRepository;
import com.schoolerp.repository.ParentRepository;
import com.schoolerp.repository.StudentRepository;
import com.schoolerp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class StudentService {

    private final StudentRepository studentRepository;
    private final UserRepository userRepository;
    private final ClassRoomRepository classRoomRepository;
    private final ParentRepository parentRepository;

    public StudentResponse createStudent(StudentRequest request) {
        if (studentRepository.findByRollNumber(request.getRollNumber()).isPresent()) {
            throw new DuplicateResourceException("Roll number already exists: " + request.getRollNumber());
        }
        if (studentRepository.findByAdmissionNumber(request.getAdmissionNumber()).isPresent()) {
            throw new DuplicateResourceException("Admission number already exists: " + request.getAdmissionNumber());
        }

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", request.getUserId()));

        Student student = Student.builder()
                .user(user)
                .rollNumber(request.getRollNumber())
                .admissionNumber(request.getAdmissionNumber())
                .dateOfBirth(request.getDateOfBirth())
                .address(request.getAddress())
                .bloodGroup(request.getBloodGroup())
                .admissionDate(request.getAdmissionDate())
                .build();

        if (request.getClassRoomId() != null) {
            ClassRoom classRoom = classRoomRepository.findById(request.getClassRoomId())
                    .orElseThrow(() -> new ResourceNotFoundException("ClassRoom", "id", request.getClassRoomId()));
            student.setClassRoom(classRoom);
        }
        if (request.getParentId() != null) {
            Parent parent = parentRepository.findById(request.getParentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Parent", "id", request.getParentId()));
            student.setParent(parent);
        }

        return mapToResponse(studentRepository.save(student));
    }

    @Transactional(readOnly = true)
    public StudentResponse getStudentById(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", id));
        return mapToResponse(student);
    }

    @Transactional(readOnly = true)
    public Page<StudentResponse> getAllStudents(Pageable pageable) {
        return studentRepository.findByDeletedFalse(pageable).map(this::mapToResponse);
    }

    @Transactional(readOnly = true)
    public Page<StudentResponse> getStudentsByClass(Long classRoomId, Pageable pageable) {
        return studentRepository.findByDeletedFalseAndClassRoomId(classRoomId, pageable).map(this::mapToResponse);
    }

    @Transactional(readOnly = true)
    public Page<StudentResponse> searchStudents(String query, Pageable pageable) {
        return studentRepository.searchStudents(query, pageable).map(this::mapToResponse);
    }

    public StudentResponse updateStudent(Long id, StudentRequest request) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", id));

        student.setDateOfBirth(request.getDateOfBirth());
        student.setAddress(request.getAddress());
        student.setBloodGroup(request.getBloodGroup());

        if (request.getClassRoomId() != null) {
            ClassRoom classRoom = classRoomRepository.findById(request.getClassRoomId())
                    .orElseThrow(() -> new ResourceNotFoundException("ClassRoom", "id", request.getClassRoomId()));
            student.setClassRoom(classRoom);
        }
        if (request.getParentId() != null) {
            Parent parent = parentRepository.findById(request.getParentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Parent", "id", request.getParentId()));
            student.setParent(parent);
        }

        return mapToResponse(studentRepository.save(student));
    }

    public void deleteStudent(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", id));
        student.setDeleted(true);
        studentRepository.save(student);
    }

    public StudentResponse mapToResponse(Student s) {
        return StudentResponse.builder()
                .id(s.getId())
                .userId(s.getUser().getId())
                .fullName(s.getUser().getFullName())
                .email(s.getUser().getEmail())
                .profileImageUrl(s.getUser().getProfileImageUrl())
                .rollNumber(s.getRollNumber())
                .admissionNumber(s.getAdmissionNumber())
                .dateOfBirth(s.getDateOfBirth())
                .address(s.getAddress())
                .bloodGroup(s.getBloodGroup())
                .admissionDate(s.getAdmissionDate())
                .phoneNumber(s.getUser().getPhoneNumber())
                .gender(s.getUser().getGender() != null ? s.getUser().getGender().name() : null)
                .classRoomId(s.getClassRoom() != null ? s.getClassRoom().getId() : null)
                .className(s.getClassRoom() != null ? s.getClassRoom().getClassName() : null)
                .section(s.getClassRoom() != null ? s.getClassRoom().getSection() : null)
                .parentId(s.getParent() != null ? s.getParent().getId() : null)
                .parentName(s.getParent() != null ? s.getParent().getUser().getFullName() : null)
                .createdAt(s.getCreatedAt())
                .build();
    }
}
