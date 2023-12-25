package apisystem.posttraining.controller;

import apisystem.posttraining.DTO.SlotAndBusyLecturers;
import apisystem.posttraining.DTO.StudentDTO.StudentDTO;
import apisystem.posttraining.entity.ShiftSystem;
import apisystem.posttraining.repository.ClassCreditRepository;
import apisystem.posttraining.repository.ShiftSystemRepository;
import apisystem.posttraining.repository.StudentRepository;
import apisystem.posttraining.service.Impl.ExamServiceImpl;
import apisystem.posttraining.service.Impl.SchedulingServiceImpl;
import apisystem.posttraining.service.Impl.TestServiceImpl;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/test")
@RequiredArgsConstructor
public class TestController {
    private final StudentRepository studentRepository;
    private final ShiftSystemRepository shiftSystemRepository;
    private final ClassCreditRepository classCreditRepository;
    private final ModelMapper modelMapper;
    private final SchedulingServiceImpl schedulingService;
    private final TestServiceImpl testService;

    @GetMapping("")
    public ResponseEntity<?> getAllStudents(){
        return ResponseEntity.ok(studentRepository.findAll());
    }

    @GetMapping("shift_system")
    public ResponseEntity<?> getAllShiftSystem(){
        return ResponseEntity.ok(shiftSystemRepository.findAll());
    }

    @GetMapping("students")
    public ResponseEntity<?> getStudents(){
        return ResponseEntity.ok(classCreditRepository.findById(1L).get().getStudents().stream()
        .map(student -> modelMapper.map(student, StudentDTO.class)).collect(Collectors.toList()));
    }

    @GetMapping("newSemester")
    public ResponseEntity<?> newSemester(){
        return schedulingService.newSemesterAPI();
    }

    @PostMapping("init-behavior")
    public boolean initBehavior(@RequestParam(name = "semesterId") Long semesterId,
                                @RequestParam(name = "bsId") Long bsId){
        return testService.initBehavior(semesterId,bsId);
    }

    @PutMapping("update-status")
    public boolean updateStatus(){
        return testService.updateStatus();
    }


}
