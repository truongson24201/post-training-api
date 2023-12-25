package apisystem.posttraining.service.Impl;

import apisystem.posttraining.DTO.ClassCreditDTO.RegisInExamPlan;
import apisystem.posttraining.DTO.ComSubjectDTO.ComPointDTO;
import apisystem.posttraining.entity.*;
import apisystem.posttraining.entity.enumreration.ERole;
import apisystem.posttraining.exception.ResourceNotFoundException;
import apisystem.posttraining.repository.*;
import apisystem.posttraining.service.IClassCreditService;
import apisystem.posttraining.service.IContextHolder;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClassCreditServiceImpl implements IClassCreditService {
    private final ClassCreditRepository classCreditRepository;
    private final SemesterRepository semesterRepository;
    private final LecturerRepository lecturerRepository;
    private final IContextHolder contextHolder;
    private final FacultyRepository facultyRepository;
    private final CreditsStudentsRepository creditsStudentsRepository;
    private final StudentsScoresRepository studentsScoresRepository;
    private final ModelMapper modelMapper;
    private final ComponentPointRepository componentPointRepository;



    @Override
    public ResponseEntity<?> getAllCCToRegis(Long semesterId) {
        Semester semester;
        if (semesterId == null)
            semester = semesterRepository.findCurrentSemester(LocalDate.now());
        else semester = semesterRepository.findById(semesterId)
                .orElseThrow(() -> new ResourceNotFoundException("Not found semester"));
        // giang vien dang nhap
        if (contextHolder.getRoleFromContext().stream().anyMatch(r ->r.equals(ERole.lecturer.name()))){
            Lecturer lecturer = lecturerRepository.findByAccountId(contextHolder.getAccount().getAccountId());
            return ResponseEntity.ok(classCreditRepository.findAllByCurSemester(semester.getSemesterId(),lecturer.getLecturerId())
                    .stream().map(cc -> RegisInExamPlan.builder()
                            .classCreditId(cc.getClassCreditId())
                            .subject(cc.getSubject().getName())
                            .numberStudents(cc.getStudents().size())
                            .facultyId(cc.getFaculty().getFacultyId())
                            .facultyName(cc.getFaculty().getName())
                            .isCompleted(cc.getIsCompleted())
                            .build()).collect(Collectors.toList()));
        }else // phong giao vu thi
            return ResponseEntity.ok(classCreditRepository.findAllByCurSemester(semester.getSemesterId())
                    .stream().map(cc -> RegisInExamPlan.builder()
                            .classCreditId(cc.getClassCreditId())
                            .subject(cc.getSubject().getName())
                            .lecturerName(cc.getLecturer().getProfile().getFullname())
                            .numberStudents(cc.getStudents().size())
                            .facultyId(cc.getFaculty().getFacultyId())
                            .facultyName(cc.getFaculty().getName())
                            .isCompleted(cc.getIsCompleted())
                            .build()).collect(Collectors.toList()));
    }

    @Override
    public ResponseEntity<?> getAllCCToEnterPoint(Long semesterId) {
        Semester semester;
        if (semesterId == null)
            semester = semesterRepository.findCurrentSemester(LocalDate.now());
        else semester = semesterRepository.findById(semesterId)
                .orElseThrow(() -> new ResourceNotFoundException("Not found semester"));
        // giang vien dang nhap
        if (contextHolder.getRoleFromContext().stream().anyMatch(r ->r.equals(ERole.lecturer.name()))){
            Lecturer lecturer = lecturerRepository.findByAccountId(contextHolder.getAccount().getAccountId());
            return ResponseEntity.ok(classCreditRepository.findAllToEnterPoint(semester.getSemesterId(),lecturer.getLecturerId())
                    .stream().map(cc -> RegisInExamPlan.builder()
                            .classCreditId(cc.getClassCreditId())
                            .subject(cc.getSubject().getName())
                            .numberStudents(cc.getStudents().size())
                            .facultyId(cc.getFaculty().getFacultyId())
                            .facultyName(cc.getFaculty().getName())
                            .isCompleted(cc.getIsCompleted())
                            .build()).collect(Collectors.toList()));
        }else // phong giao vu thi
            return ResponseEntity.ok(classCreditRepository.findAllToEnterPoint(semester.getSemesterId())
                    .stream().map(cc -> RegisInExamPlan.builder()
                            .classCreditId(cc.getClassCreditId())
                            .subject(cc.getSubject().getName())
//                            .lecturerName(cc.getLecturer().getProfile().getFullname())
                            .numberStudents(cc.getStudents().size())
                            .facultyId(cc.getFaculty().getFacultyId())
                            .facultyName(cc.getFaculty().getName())
                            .isCompleted(cc.getIsCompleted())
                            .build()).collect(Collectors.toList()));
    }

    @Override
    public ResponseEntity<?> initScores() {
        List<ClassCredit> classCredits = classCreditRepository.findAllByOpen();
        if (classCredits.isEmpty()) return ResponseEntity.badRequest().body("Không có lớp tín chỉ nào được mở");
        List<StudentsScores> studentsScores = classCredits.stream()
                .flatMap(classCredit -> {
                    List<ComponentSubject> componentSubjects = classCredit.getSubject().getComponentSubjects();
                    List<ClassCreditsStudents> creditsStudents = creditsStudentsRepository.findAllByClassCreditId(classCredit.getClassCreditId());
                    if (!studentsScoresRepository.findAllByRegisID(creditsStudents.get(0).getId()).isEmpty()) return null;
                    else return componentSubjects.stream()
                            .flatMap(comSub -> creditsStudents.stream()
                                    .map(classCreditsStudents -> new StudentsScores(classCreditsStudents, comSub))
                            );
                })
                .collect(Collectors.toList());

        if (!studentsScores.isEmpty()) {
            try {
                studentsScoresRepository.saveAll(studentsScores);
            }catch (Exception e){
                return ResponseEntity.badRequest().body("Khởi tạo bảng điểm LỖI, vui lòng xem lại!");
            }
        }
        return ResponseEntity.ok("Khởi tạo bảng điểm thành công!");
    }


    @Override
    public ResponseEntity<?> getComponentsScore(Long classCreditId) {
        ClassCredit classCredit = classCreditRepository.findById(classCreditId).get();
        if (contextHolder.getRoleFromContext().stream().anyMatch(r -> r.equals(ERole.TQA.name()))){
            return ResponseEntity.ok(classCredit.getSubject().getComponentSubjects().stream()
                    .map(com -> modelMapper.map(com.getComponent(), ComPointDTO.class))
                    .sorted(Comparator.comparing(ComPointDTO::getComponentId))
                    .collect(Collectors.toList()));
        }else {
            return ResponseEntity.ok(classCredit.getSubject().getComponentSubjects().stream()
                    .filter(com -> !com.getComponent().getName().equals("Cuối kỳ"))
                    .map(com -> modelMapper.map(com.getComponent(), ComPointDTO.class))
                    .sorted(Comparator.comparing(ComPointDTO::getComponentId))
                    .collect(Collectors.toList()));
        }
    }

    @Override
    public ResponseEntity<?> getComponentFinal() {
        return ResponseEntity.ok(
                new ArrayList<>(List.of(
                        modelMapper.map(componentPointRepository.findByName("Cuối kỳ"),ComPointDTO.class)
                ))
        );
    }

    @Override
    public ResponseEntity<?> updateIsComplete(Long classCreditId) {
        ClassCredit classCredit = classCreditRepository.findById(classCreditId).get();
        try {
            classCredit.setIsCompleted(true);
            classCreditRepository.save(classCredit);
            return ResponseEntity.ok("Lớp tín chỉ đã được hoàn thành thành công");
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Cập nhật thất bại");
        }
    }

    @Override
    public boolean checkClassOfLec(Long classCreditId) {
        if (contextHolder.getRoleFromContext().stream().anyMatch(r -> r.equals(ERole.TQA.name())))
            return true;
        else {
            Lecturer lecturer = lecturerRepository.findByAccountId(contextHolder.getAccount().getAccountId());
            Optional<ClassCredit> classCredit = classCreditRepository.findByLecturer(classCreditId,lecturer.getLecturerId());
            if (classCredit.isEmpty()) return false;
            else return true;
        }
    }

    @Override
    public boolean hasEnterCK(Long classCreditId) {
        ClassCredit classCredit = classCreditRepository.findById(classCreditId)
                .orElseThrow(() -> new ResourceNotFoundException("Not found!"));
        if (classCredit.getSubject().getName().equals("Thực tập") || classCredit.getSubject().getName().equals("Tốt nghiệp"))
            return true;
        List<Exam> exams = classCredit.getExams();
        if (exams.isEmpty()) return false;
        Optional<Exam> latestExamOptional = exams.stream()
                .max(Comparator.comparing(Exam::getExamDate));
        if (latestExamOptional.get().getExamDate().isAfter(LocalDate.now()) || latestExamOptional.get().getExamDate().equals(LocalDate.now()))
            return false;
        return true;
    }
}
