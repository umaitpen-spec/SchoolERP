package com.schoolerp.service;

import com.schoolerp.dto.request.TimetableRequest;
import com.schoolerp.dto.response.TimetableResponse;
import com.schoolerp.entity.ClassRoom;
import com.schoolerp.entity.Subject;
import com.schoolerp.entity.Teacher;
import com.schoolerp.entity.Timetable;
import com.schoolerp.exception.ResourceNotFoundException;
import com.schoolerp.repository.ClassRoomRepository;
import com.schoolerp.repository.SubjectRepository;
import com.schoolerp.repository.TeacherRepository;
import com.schoolerp.repository.TimetableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class TimetableService {

    private final TimetableRepository timetableRepository;
    private final ClassRoomRepository classRoomRepository;
    private final SubjectRepository subjectRepository;
    private final TeacherRepository teacherRepository;

    public TimetableResponse createEntry(TimetableRequest request) {
        ClassRoom classRoom = classRoomRepository.findById(request.getClassRoomId())
                .orElseThrow(() -> new ResourceNotFoundException("ClassRoom", "id", request.getClassRoomId()));
        Subject subject = subjectRepository.findById(request.getSubjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Subject", "id", request.getSubjectId()));
        Teacher teacher = teacherRepository.findById(request.getTeacherId())
                .orElseThrow(() -> new ResourceNotFoundException("Teacher", "id", request.getTeacherId()));

        Timetable timetable = Timetable.builder()
                .classRoom(classRoom)
                .subject(subject)
                .teacher(teacher)
                .dayOfWeek(request.getDayOfWeek())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .academicYear(request.getAcademicYear())
                .roomNumber(request.getRoomNumber())
                .build();

        return mapToResponse(timetableRepository.save(timetable));
    }

    @Transactional(readOnly = true)
    public List<TimetableResponse> getClassTimetable(Long classRoomId, String academicYear) {
        return timetableRepository.findByClassRoomIdAndAcademicYear(classRoomId, academicYear)
                .stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TimetableResponse> getTeacherTimetable(Long teacherId, String academicYear) {
        return timetableRepository.findByTeacherIdAndAcademicYear(teacherId, academicYear)
                .stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    public void deleteEntry(Long id) {
        if (!timetableRepository.existsById(id)) {
            throw new ResourceNotFoundException("Timetable", "id", id);
        }
        timetableRepository.deleteById(id);
    }

    private TimetableResponse mapToResponse(Timetable t) {
        return TimetableResponse.builder()
                .id(t.getId())
                .classRoomId(t.getClassRoom().getId())
                .className(t.getClassRoom().getClassName())
                .section(t.getClassRoom().getSection())
                .subjectId(t.getSubject().getId())
                .subjectName(t.getSubject().getSubjectName())
                .teacherId(t.getTeacher().getId())
                .teacherName(t.getTeacher().getUser().getFullName())
                .dayOfWeek(t.getDayOfWeek())
                .startTime(t.getStartTime())
                .endTime(t.getEndTime())
                .academicYear(t.getAcademicYear())
                .roomNumber(t.getRoomNumber())
                .build();
    }
}
