package apisystem.posttraining.service.Impl;

import apisystem.posttraining.DTO.Account.RequestLogin;
import apisystem.posttraining.DTO.Account.ResponseLogin;
import apisystem.posttraining.DTO.ClassDTO.ClassDTO;
import apisystem.posttraining.DTO.LecturerDTO.LecturerView;
import apisystem.posttraining.DTO.SemesterDTO.SemesterDTO;
import apisystem.posttraining.DTO.StudentDTO.StudentDTO;
import apisystem.posttraining.entity.*;
import apisystem.posttraining.entity.enumreration.ERole;
import apisystem.posttraining.exception.ResourceNotFoundException;
import apisystem.posttraining.repository.*;
import apisystem.posttraining.security.jwt.JwtService;
import apisystem.posttraining.service.ICommonService;
import apisystem.posttraining.service.IContextHolder;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommonServiceImpl implements ICommonService {
    private final SemesterRepository semesterRepository;
    private final ModelMapper modelMapper;
    private final BSheetRepository bSheetRepository;
    private final ClassRepository classRepository;
    private final BSPatternRepository bsPatternRepository;
    private final StudentRepository studentRepository;
    private final AuthenticationManager authenticationManager;
    private final AccountRepository accountRepository;
    private final JwtService jwtService;
    private final IContextHolder contextHolder;
    private final LecturerRepository lecturerRepository;
    private final FacultyRepository facultyRepository;
    private final ShiftSystemRepository shiftSystemRepository;
    private final ExamPlanRepository examPlanRepository;
    private final ExamRepository examRepository;


    @Override
    public ResponseEntity<?> getAllSemester() {
        return ResponseEntity.ok(semesterRepository.findAll().stream()
                .map(s -> modelMapper.map(s, SemesterDTO.class))
                .collect(Collectors.toList())
        );
    }

    record viewMoreInfoInPattern(
            @JsonFormat(pattern = "dd/MM/yyyy", timezone = "Asia/Ho_Chi_Minh") LocalDate dateOpen,
            @JsonFormat(pattern = "dd/MM/yyyy", timezone = "Asia/Ho_Chi_Minh") LocalDate dateClose,
            boolean isPermission
    ) {
    }

    @Override
    public ResponseEntity<?> checkTimeUpdateSheet(Long semesterId) {
        Semester semester = semesterRepository.findById(semesterId)
                .orElseThrow(() -> new ResourceNotFoundException("Empty"));
        Optional<BSPattern> bsPattern = bsPatternRepository.findByStatusTrue();
        if (bsPattern.isEmpty()) {
            return ResponseEntity.ok(
                    new viewMoreInfoInPattern(null,
                            null,
                            false)
            );
        } else if (semester.getDateEnd().isBefore(bsPattern.get().getDateOpen())) {
            return ResponseEntity.ok(
                    new viewMoreInfoInPattern(null,
                            null,
                            false)
            );
        }
        return ResponseEntity.ok(
                new viewMoreInfoInPattern(bsPattern.get().getDateOpen(), null, true)
        );
    }

    @Override
    public ResponseEntity<?> getAllClass() {
        if (contextHolder.getRoleFromContext().stream().anyMatch(role -> role.equals(ERole.AcademicAdvisor.name()))) {
            Lecturer lecturer = lecturerRepository.findByAccountId(
                    accountRepository.findByUsername(contextHolder.getUsernameFromContext()).get().getAccountId()
            );
            return ResponseEntity.ok(lecturer.getClasses().stream()
                    .map(c -> modelMapper.map(c, ClassDTO.class))
                    .collect(Collectors.toList())
            );
        }else if (contextHolder.getRoleFromContext().stream().anyMatch(role -> role.equals(ERole.StudentAffairs.name()))){
            return ResponseEntity.ok(classRepository.findAll().stream()
                    .map(c -> modelMapper.map(c, ClassDTO.class))
                    .collect(Collectors.toList())
            );
        }else return ResponseEntity.ok(new ArrayList<>());
    }

    @Override
    public Integer getTotalStudentInClass(Long classId) {
        return studentRepository.countAllByAClass_ClassId(classId);
    }

    @Override
    public ResponseEntity<?> login(RequestLogin requestLogin) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(requestLogin.getUsername(), requestLogin.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (AuthenticationException e) {
            return ResponseEntity.badRequest().body("Invalid credentials.");
        }
        Account account = accountRepository.findByUsername(requestLogin.getUsername()).get();

        return ResponseEntity.ok(new ResponseLogin(account.getUsername(),
                jwtService.generateJwtToken(requestLogin.getUsername(), new Date(new Date().getTime() + JwtService.jwtExpirationMs)),
                account.getRoles().stream().map(Role::getName).collect(Collectors.toList())));
    }

    @Override
    public ResponseEntity<?> getCurrentSemester() {
        return ResponseEntity.ok(modelMapper.map(semesterRepository.findCurrentSemester(LocalDate.now()), SemesterDTO.class));
    }

    @Override
    public ResponseEntity<?> getAllFaculties() {
        return ResponseEntity.ok(facultyRepository.findAll());
    }

    @Override
    public ResponseEntity<?> getAllShiftSystem() {
        return ResponseEntity.ok(shiftSystemRepository.findAllByShiftType(true));
    }

    @Override
    public ResponseEntity<?> getAllLecturer(Long examPlanId) {
        ExamPlan examPlan = examPlanRepository.findById(examPlanId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy kế hoạch thi"));
        if(contextHolder.getRoleFromContext().stream().anyMatch(r -> r.equals(ERole.TQA.name()))){
            List<Lecturer> uniqueLecturers = examPlan.getClassCredits().stream()
                    .flatMap(c -> c.getExams().stream())
                    .flatMap(e -> e.getLecturers().stream())
                    .distinct()
                    .collect(Collectors.toList());
            return ResponseEntity.ok(uniqueLecturers.stream()
                    .map(l -> modelMapper.map(l, LecturerView.class))
                    .collect(Collectors.toList()));
        }else {
            Lecturer lecturer = lecturerRepository.findByAccountId(contextHolder.getAccount().getAccountId());
            return ResponseEntity.ok(Arrays.asList(modelMapper.map(lecturer, LecturerView.class)));
        }
    }

    @Override
    public ResponseEntity<?> getAllStudents() {
        return ResponseEntity.ok(studentRepository.findAll().stream()
                .map(s -> modelMapper.map(s, StudentDTO.class))
                .collect(Collectors.toList())
        );
    }

    @Override
    public ResponseEntity<?> getAllClassInExam() {
        return ResponseEntity.ok(classRepository.findAll().stream()
                .map(c -> modelMapper.map(c, ClassDTO.class))
                .collect(Collectors.toList())
        );
    }

    @Override
    public ResponseEntity<?> getAllStudentsInClass(Long classId) {
        return ResponseEntity.ok(studentRepository.findAllByClassId(classId).stream()
                .map(s -> modelMapper.map(s,StudentDTO.class))
                .collect(Collectors.toList())
        );
    }

    @Override
    public boolean checkExpiredToEnterPoint(Long semesterId) {
        Semester semester = semesterRepository.findById(semesterId)
                .orElseThrow(() -> new ResourceNotFoundException("Not found semester!"));
        Semester currentSemester = semesterRepository.findCurrentSemester(LocalDate.now());
        if (semester.getSemesterId().equals(currentSemester.getSemesterId()))
            return false;
        if (Math.abs(currentSemester.getYear() - semester.getYear()) <= 1) {
            if (Math.abs(currentSemester.getYear() - semester.getYear()) == 1) {
                if (currentSemester.getNum() == 1 && (semester.getNum() == 2 | semester.getNum() == 3))
                    return false;
                else return true;
            } else {
                if (currentSemester.getNum() == 3 && semester.getNum() == 1)
                    return true;
                else return false;
            }
        }
        return true;
    }

    @Override
    public ResponseEntity<?> getSemesterToPermission() {
        Semester currentSemester = semesterRepository.findCurrentSemester(LocalDate.now());
        List<Semester> semesters = semesterRepository.findAll();
        List<Semester> semesterPermission = new ArrayList<>();
        for (Semester semester : semesters) {
            if (!semester.getSemesterId().equals(currentSemester.getSemesterId())) {
                if (Math.abs(currentSemester.getYear() - semester.getYear()) <= 1) {
                    if (Math.abs(currentSemester.getYear() - semester.getYear()) == 1) {
                        if (currentSemester.getNum() == 1 && (semester.getNum() == 2 || semester.getNum() == 3)) {
                            semesterPermission.add(semester);
                        } else {
                            continue;
                        }
                    } else {
                        if (currentSemester.getNum() == 3 && semester.getNum() == 1) {
                            continue;
                        } else {
                            semesterPermission.add(semester);
                        }
                    }
                }
            }else semesterPermission.add(semester);
        }
        return ResponseEntity.ok(semesterPermission.stream()
                .map(s -> modelMapper.map(s,SemesterDTO.class))
                .collect(Collectors.toList())
        );
    }

    @Override
    public ResponseEntity<?> getFreeLecInExam(Long examId) {
        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new ResourceNotFoundException("Not found!"));
        List<Lecturer> lecturers = lecturerRepository.findFreeLecturersByTimetableAndExam(exam.getExamDate(), exam.getShiftSystem().getTimeStart(), exam.getShiftSystem().getTimeClose());
        return ResponseEntity.ok(lecturers.stream().map(l -> modelMapper.map(l,LecturerView.class)).collect(Collectors.toList()));
    }
}
