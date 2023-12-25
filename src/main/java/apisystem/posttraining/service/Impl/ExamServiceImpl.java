package apisystem.posttraining.service.Impl;

import apisystem.posttraining.DTO.ClassCreditDTO.RegisInExamPlan;
import apisystem.posttraining.DTO.ExamDTO.ChangeLecRequest;
import apisystem.posttraining.DTO.ExamDTO.ExamView;
import apisystem.posttraining.DTO.ExamDTO.ShiftSystemView;
import apisystem.posttraining.DTO.LecturerDTO.LecturerView;
import apisystem.posttraining.DTO.SlotAndBusyLecturers;
import apisystem.posttraining.DTO.StudentDTO.StudentDTO;
import apisystem.posttraining.entity.*;
import apisystem.posttraining.entity.enumreration.EExamType;
import apisystem.posttraining.entity.enumreration.ERole;
import apisystem.posttraining.exception.ResourceNotFoundException;
import apisystem.posttraining.repository.*;
import apisystem.posttraining.service.IContextHolder;
import apisystem.posttraining.service.IExamService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExamServiceImpl implements IExamService {
    private final ExamRepository examRepository;
    private final ExamPlanRepository examPlanRepository;
    //    private final RegisExamPlanRepository regisExamPlanRepository;
    private final TimeTableRepository timeTableRepository;
    private final ClassCreditRepository classCreditRepository;
    private final CreditsStudentsRepository creditsStudentsRepository;
    private final SemesterRepository semesterRepository;
    private final ClassRoomRepository classRoomRepository;
    private final ShiftSystemRepository shiftSystemRepository;
    private final StudentRepository studentRepository;
    private final ModelMapper modelMapper;
    private final IContextHolder contextHolder;
    private final LecturerRepository lecturerRepository;


    private List<Classroom> getAvailableClassrooms1(String[] arrRoomtype, LocalDate date, LocalTime shiftSystem) {
        // Trả về danh sách tất cả các phòng ban đầu cho mỗi ca thi
        // Bạn cần triển khai logic của mình để xác định danh sách phòng trống
        // Dựa trên các điều kiện như không có lịch học và không có bài thi khác trong cùng một thời gian
//        return classRoomRepository.findAllAvailableClassroom(arrRoomtype, date, shiftSystem);

        return classRoomRepository.findAllAvailableClassroom(arrRoomtype, date, shiftSystem);

    }

    private List<Classroom> getAvailableClassrooms(String[] arrRoomtype, LocalDate date, LocalTime shiftSystem) {
        // Trả về danh sách tất cả các phòng ban đầu cho mỗi ca thi
        // Bạn cần triển khai logic của mình để xác định danh sách phòng trống
        // Dựa trên các điều kiện như không có lịch học và không có bài thi khác trong cùng một thời gian
//        return classRoomRepository.findAllAvailableClassroom(arrRoomtype, date, shiftSystem);

        return classRoomRepository.findAllAvailableClassroomWithExams(arrRoomtype, date, shiftSystem);

    }

//    private Map<LocalDate, Map<ShiftSystem, List<Classroom>>> initializeAvailableSlots(List<LocalDate> dates,
//                                                                                       List<ShiftSystem> shiftSystems) {
//        String[] arrRoomType = {"Study", "Practice"};
//
//        return dates.stream()
//                .filter(date -> shiftSystems.stream()
//                        .map(shiftSystem -> Map.entry(shiftSystem, getAvailableClassrooms(arrRoomType, date, shiftSystem.getTimeStart())))
//                        .anyMatch(entry -> !entry.getValue().isEmpty()))
//                .collect(Collectors.toMap(date -> date,
//                        date -> shiftSystems.stream()
//                                .map(shiftSystem -> Map.entry(shiftSystem, getAvailableClassrooms(arrRoomType, date, shiftSystem.getTimeStart())))
//                                .filter(entry -> !entry.getValue().isEmpty())
//                                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))));
//    }

    // old (version 1)
    private SlotAndBusyLecturers initializeAvailableSlots1(List<LocalDate> dates,
                                                          List<ShiftSystem> shiftSystems) {
        // Khởi tạo một bảng chứa thông tin về các ô trống (ngày, ca, phòng)
        // Giả sử ban đầu tất cả các ô đều trống
        String[] arrRoomtype = {"Study", "Practice"};
        Map<LocalDate, Map<ShiftSystem, List<Classroom>>> freeSlotsTimetable = new HashMap<>();
        Map<LocalDate, Map<ShiftSystem, List<Lecturer>>> freeLecturersTimetable = new HashMap<>();
        Map<LocalDate, Map<ShiftSystem, List<Classroom>>> busySlotsExam = new HashMap<>();
        Map<LocalDate, Map<ShiftSystem, List<Lecturer>>> busyLecturersExam = new HashMap<>();

        for (LocalDate date : dates) {
            Map<ShiftSystem, List<Classroom>> freeRoomTimetable = new HashMap<>();
            Map<ShiftSystem, List<Lecturer>> freeLecturerTimetable = new HashMap<>();
            Map<ShiftSystem, List<Classroom>> busyRoomExam = new HashMap<>();
            Map<ShiftSystem, List<Lecturer>> busyLecturerExam = new HashMap<>();
            for (ShiftSystem shiftSystem : shiftSystems) {
                List<Classroom> classrooms = new ArrayList<>(getAvailableClassrooms(arrRoomtype, date, shiftSystem.getTimeStart()));
                List<Lecturer> freeLecturersFromTimetable = lecturerRepository.findFreeLecturersByTimetable(date, shiftSystem.getTimeStart(), shiftSystem.getTimeClose());
                List<Lecturer> busyLecturersFromExam = lecturerRepository.findBusyLecturersByExam(date, shiftSystem.getTimeStart(), shiftSystem.getTimeClose());
                List<Classroom> busyRoomForExam = classRoomRepository.findAllBusyClassroomExam(arrRoomtype, date, shiftSystem.getTimeStart());
//                List<Lecturer> combinedFreeLecturers = Stream.concat(busyLecturersFromTimetable.stream(), busyLecturersFromExam.stream())
//                        .distinct()
//                        .collect(Collectors.toList());

                // Cách dùng HashSet
//                Set<Lecturer> combinedBusyLecturersSet = new HashSet<>();
//                combinedBusyLecturersSet.addAll(busyLecturersFromTimetable);
//                combinedBusyLecturersSet.addAll(busyLecturersFromExam);
//                List<Lecturer> combinedBusyLecturers = new ArrayList<>(combinedBusyLecturersSet);

                if (!classrooms.isEmpty())
                    freeRoomTimetable.put(shiftSystem, classrooms);
                if (!freeLecturersFromTimetable.isEmpty())
                    freeLecturerTimetable.put(shiftSystem, freeLecturersFromTimetable);
                if (!busyLecturersFromExam.isEmpty())
                    busyLecturerExam.put(shiftSystem, busyLecturersFromExam);
                if (!busyRoomForExam.isEmpty())
                    busyRoomExam.put(shiftSystem, busyRoomForExam);
            }
            if (!freeRoomTimetable.isEmpty())
                freeSlotsTimetable.put(date, freeRoomTimetable);
            if (!freeLecturerTimetable.isEmpty())
                freeLecturersTimetable.put(date, freeLecturerTimetable);
            if (!busyLecturerExam.isEmpty())
                busyLecturersExam.put(date, busyLecturerExam);
            if (!busyRoomExam.isEmpty())
                busySlotsExam.put(date, busyRoomExam);
        }
        return new SlotAndBusyLecturers(freeSlotsTimetable, freeLecturersTimetable, busySlotsExam, busyLecturersExam);
    }

    private SlotAndBusyLecturers initializeAvailableSlots(List<LocalDate> dates,
                                                          List<ShiftSystem> shiftSystems) {
        // Khởi tạo một bảng chứa thông tin về các ô trống (ngày, ca, phòng)
        // Giả sử ban đầu tất cả các ô đều trống
        String[] arrRoomtype = {"Study", "Practice"};

        Map<LocalDate, Map<ShiftSystem, List<Classroom>>> freeClassroom = new HashMap<>();
        Map<LocalDate, Map<ShiftSystem, List<Lecturer>>> freeLecturer = new HashMap<>();

        for (LocalDate date : dates) {
            Map<ShiftSystem, List<Classroom>> freeClassroomOneDate = new HashMap<>();
            Map<ShiftSystem, List<Lecturer>> freeLecturerOneDate = new HashMap<>();

            for (ShiftSystem shiftSystem : shiftSystems) {

                List<Classroom> classroomsOneShift = new ArrayList<>(getAvailableClassrooms(arrRoomtype, date, shiftSystem.getTimeStart()));
                List<Lecturer> lecturersOneShift = lecturerRepository.findFreeLecturersByTimetableAndExam(date, shiftSystem.getTimeStart(), shiftSystem.getTimeClose());

                if (!classroomsOneShift.isEmpty())
                    freeClassroomOneDate.put(shiftSystem, classroomsOneShift);
                if (!lecturersOneShift.isEmpty())
                    freeLecturerOneDate.put(shiftSystem, lecturersOneShift);
            }

            if (!freeClassroomOneDate.isEmpty())
                freeClassroom.put(date, freeClassroomOneDate);
            if (!freeLecturerOneDate.isEmpty())
                freeLecturer.put(date, freeLecturerOneDate);
        }
        return new SlotAndBusyLecturers(freeClassroom,freeLecturer);
    }

    // old
//    private Map<LocalDate, Map<ShiftSystem, List<Classroom>>> initializeAvailableSlots(List<LocalDate> dates,
//                                                                                       List<ShiftSystem> shiftSystems) {
//        // Khởi tạo một bảng chứa thông tin về các ô trống (ngày, ca, phòng)
//        // Giả sử ban đầu tất cả các ô đều trống
//        String[] arrRoomtype = {"Study", "Practice"};
//        Map<LocalDate, Map<ShiftSystem, List<Classroom>>> slots = new HashMap<>();
//        for (LocalDate date : dates) {
//            Map<ShiftSystem, List<Classroom>> shiftSystemMap = new HashMap<>();
//            for (ShiftSystem shiftSystem : shiftSystems) {
//                List<Classroom> classrooms = new ArrayList<>(getAvailableClassrooms(arrRoomtype, date, shiftSystem.getTimeStart()));
//                if (!classrooms.isEmpty())
//                    shiftSystemMap.put(shiftSystem, classrooms);
//            }
//            if (!shiftSystemMap.isEmpty())
//                slots.put(date, shiftSystemMap);
//        }
//        return slots;
//    }

    private List<Student> takeListStudentInExamGroup(List<Student> students, int examGroupNumber, int studentsPerGroup) {
        int startIndex = examGroupNumber * studentsPerGroup;
        int endIndex = Math.min((examGroupNumber + 1) * studentsPerGroup, students.size());
        return students.subList(startIndex, endIndex);
    }

//    private boolean hasSuitableClassroom(Map<ShiftSystem, List<Classroom>> shiftSystemMap, String roomType, Classroom selectedClassroom) {
//        return shiftSystemMap.values().stream()
//                .flatMap(List::stream)
//                .anyMatch(classroom -> classroom.equals(selectedClassroom) && classroom.getRoomType().equals(roomType));
//    }

    private boolean chooseClassroomAndShiftSystem(Map<LocalDate, Map<ShiftSystem, List<Classroom>>> availableSlots, ClassCredit cc, int examGroupNumber, List<Student> studentGroup, String roomType, Exam exam) {
        for (LocalDate date : availableSlots.keySet()) {
            Map<ShiftSystem, List<Classroom>> shiftSystemMap = availableSlots.get(date);

            for (ShiftSystem shiftSystem : shiftSystemMap.keySet()) {
                Optional<Classroom> selectedClassroom = shiftSystemMap.get(shiftSystem).stream()
                        .filter(classroom -> classroom.getRoomType().equals(roomType))
                        .filter(classroom -> classroom.getCapacity() >= 2 * studentGroup.size())
                        .findFirst();

                if (selectedClassroom.isPresent()) {
                    exam.setExamDate(date);
                    exam.setGroupNumber(examGroupNumber + 1);
                    exam.setClassCredit(cc);
                    exam.setClassroom(selectedClassroom.get());
                    exam.setShiftSystem(shiftSystem);
                    exam.setStudents(studentGroup);
                    return true;
                }
            }
        }
        return false;
    }

    private boolean buildExamForClassCredit(Map<LocalDate, Map<ShiftSystem, List<Classroom>>> availableSlots, ClassCredit cc, List<Exam> exams) {
        int numberExamGroups = cc.getNumberExamGroups();
        String roomType = cc.getExamType().equals(EExamType.lab) ? "Practice" : "Study";
        List<Student> students = cc.getStudents().stream()
                .filter(ClassCreditsStudents::getExamStatus)
                .map(ClassCreditsStudents::getStudent)
                .collect(Collectors.toList());

        int studentsPerGroup = students.size() / numberExamGroups;

        for (int examGroupNumber = 0; examGroupNumber < numberExamGroups; examGroupNumber++) {
            Exam exam = new Exam();

            if (examGroupNumber == numberExamGroups - 1) studentsPerGroup = students.size();
            List<Student> studentGroup = takeListStudentInExamGroup(students, examGroupNumber, studentsPerGroup);

            if (chooseClassroomAndShiftSystem(availableSlots, cc, examGroupNumber, studentGroup, roomType, exam)) {
                exams.add(exam);
            } else {
                return false;
            }
        }
        return true;
    }

    @Transactional
//    @Override
    public ResponseEntity<?> buildExamSchedulingForExamPlan2(Long examPlanId) {
        ExamPlan examPlan = examPlanRepository.findById(examPlanId)
                .orElseThrow(() -> new ResourceNotFoundException("Not found exam plan"));
        if (examPlan.getDateStart().isBefore(LocalDate.now()) | examPlan.getDateStart().equals(LocalDate.now()))
            return ResponseEntity.badRequest().body("Have NO class credit regis into this exam plan OR ExamPlan has been implemented");
        // danh sach lop tin chi can xep lich thi cho exam plan nay
//        if (examPlan.getRegisClosing().isAfter(LocalDate.now())) return ResponseEntity.badRequest().body("Out of time");
        List<ClassCredit> classCredits = examPlan.getClassCredits();
        if (classCredits.isEmpty()) {
            return ResponseEntity.badRequest().body("ERROR,Exam Plan have been set exam scheduling");
        }
        Collections.sort(classCredits, Comparator
                .comparingInt((ClassCredit classCredit) -> classCredit.getStudents().size())
                .thenComparing(ClassCredit::getNumberExamGroups)
                .reversed());

        List<ShiftSystem> shiftSystems = shiftSystemRepository.findAllByShiftType(true);
        List<LocalDate> dates = examPlan.getDateStart().datesUntil(examPlan.getDateEnd().plusDays(1))
                .filter(date -> date.getDayOfWeek() != DayOfWeek.SUNDAY)
                .collect(Collectors.toList());

        SlotAndBusyLecturers availableSlots = initializeAvailableSlots(dates, shiftSystems);

        List<Exam> exams = new ArrayList<>();

        for (ClassCredit cc : classCredits) {
            int numberExamGroups = cc.getNumberExamGroups();
            String roomType = cc.getExamType().equals(EExamType.lab) ? "Practice" : "Study";

            List<Student> students = creditsStudentsRepository.findAllByClassCreditId(cc.getClassCreditId()).stream()
                    .filter(ClassCreditsStudents::getExamStatus)
                    .map(ClassCreditsStudents::getStudent)
                    .collect(Collectors.toList());
            int studentsPerGroup = students.size() / numberExamGroups;
//            boolean typeProject = false;
//            List<Exam> examsTypeProject = new ArrayList<>();
//            if (cc.getExamType().equals(EExamType.project) || cc.getExamType().equals(EExamType.project)) {
//                typeProject = true;
//            }

            for (int examGroupNumber = 0; examGroupNumber < numberExamGroups; examGroupNumber++) {
                boolean flag = false;
                boolean planTwo = false;
                boolean conflictLecturer = false;
//                if (examGroupNumber == numberExamGroups - 1) studentsPerGroup = students.size();
                List<Student> studentGroup = takeListStudentInExamGroup(students, examGroupNumber, studentsPerGroup);

                List<LocalDate> lecturersForChoose = new ArrayList<>(availableSlots.getFreeLecturersTimetable().keySet());

                // dates
                for (int i = 0; i < lecturersForChoose.size(); i++) {

                    LocalDate date = lecturersForChoose.get(i);

                    Map<ShiftSystem, List<Lecturer>> freeLecInTimetable = availableSlots.getFreeLecturersTimetable().get(date);
                    Map<ShiftSystem, List<Classroom>> shiftRoomMap = availableSlots.getFreeSlotsTimetable().get(date);
                    Map<ShiftSystem, List<Classroom>> shiftRoomBusyMap = availableSlots.getBusySlotsExam().get(date);

                    if (!planTwo) {
                        if (studentHasExamInDate(date, studentGroup, exams)) {
                            continue;
                        }
                    }

                    for (ShiftSystem shiftSystem : freeLecInTimetable.keySet()) {

                        boolean lecturerAvailable = !freeLecInTimetable.get(shiftSystem).contains(cc.getLecturer()) &&
                                !availableSlots.getBusyLecturersExam().get(date).get(shiftSystem).contains(cc.getLecturer());

                        if (lecturerAvailable) {
                            if (studentHasExamInShiftSystem(date, shiftSystem, studentGroup, exams)) {
                                continue;
                            }

                            Optional<Lecturer> selectedSecondLecturer = availableSlots.getFreeLecturersTimetable().get(date).get(shiftSystem)
                                    .stream()
                                    .filter(lecturer ->
                                            !lecturer.equals(cc.getLecturer()) &&
                                                    !freeLecInTimetable.get(shiftSystem).contains(lecturer) &&
                                                    !availableSlots.getBusyLecturersExam().get(date).get(shiftSystem).contains(lecturer)
                                    )
                                    .findFirst();

                            Optional<Classroom> selectedClassroom = shiftRoomMap.get(shiftSystem).parallelStream()
                                    .filter(classroom ->
                                            classroom.getRoomType().equals(roomType) &&
                                                    classroom.getCapacity() >= 2 * studentGroup.size() &&
                                                    !shiftRoomBusyMap.get(shiftSystem).contains(classroom)
                                    )
                                    .findFirst();

//                            if (!examsTypeProject.isEmpty() && examsTypeProject.stream().anyMatch(e ->
//                                    e.getExamDate().equals(date) && e.getShiftSystem().equals(shiftSystem))) {
//                                continue;
//                            }

                            List<Lecturer> lecChoose = Arrays.asList(cc.getLecturer(),selectedSecondLecturer.orElse(null));

                            if (selectedClassroom.isPresent()) {
                                Exam exam = new Exam();
                                exam.setExamDate(date);
                                exam.setGroupNumber(examGroupNumber + 1);
                                exam.setClassCredit(cc);
                                exam.setClassroom(selectedClassroom.get());
                                exam.setShiftSystem(shiftSystem);
                                exam.setStudents(studentGroup);
                                exam.setLecturers(lecChoose);
//                                if (typeProject) {
//                                    examsTypeProject.add(exam);
//                                } else {
                                    exams.add(exam);
//                                }

                                flag = true;
                                availableSlots.getFreeSlotsTimetable().get(date).get(shiftSystem).remove(selectedClassroom.get());
                                availableSlots.getFreeLecturersTimetable().get(date).get(shiftSystem).removeAll(lecChoose);
                                availableSlots.getBusySlotsExam().get(date).get(shiftSystem).add(selectedClassroom.get());
                                availableSlots.getBusyLecturersExam().get(date).get(shiftSystem).addAll(lecChoose);
                                break;
                            }
                        }
                    }

                    if (!flag) {
                        return ResponseEntity.badRequest().body("Không xếp được lịch thi, hãy thử mở rộng phạm vi thời gian thi hoặc xem lại danh sách giảng viên coi thi");
                    }
//                    if (!examsTypeProject.isEmpty()) {
//                        exams.addAll(examsTypeProject);
//                    }
                    if (flag | (!flag && planTwo)) break;
                    else {
                        i = 0;
                        planTwo = true;
                    }
                }
                if (!flag)
                    return ResponseEntity.badRequest().body("Không xếp được lịch thi, hãy thử mở rộng phạm vi thời gian thi hoặc xem lại danh sách giảng viên coi thi!");
            }
//            if (!examsTypeProject.isEmpty()) exams.addAll(examsTypeProject);
        }

        try {
            examRepository.saveAll(exams);
            examPlan.setStatus(true);
            examPlanRepository.save(examPlan);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Save list exam for this exam plan failed!");
        }
        return ResponseEntity.ok("Building exam scheduling successfully!");
    }


    @Transactional
//    @Override
    public ResponseEntity<?> buildExamSchedulingForExamPlan1(Long examPlanId) {
        ExamPlan examPlan = examPlanRepository.findById(examPlanId)
                .orElseThrow(() -> new ResourceNotFoundException("Not found exam plan"));
        if (examPlan.getDateStart().isBefore(LocalDate.now()) | examPlan.getDateStart().equals(LocalDate.now()))
            return ResponseEntity.badRequest().body("Have NO class credit regis into this exam plan OR ExamPlan has been implemented");
        // danh sach lop tin chi can xep lich thi cho exam plan nay
//        if (examPlan.getRegisClosing().isAfter(LocalDate.now())) return ResponseEntity.badRequest().body("Out of time");
        List<ClassCredit> classCredits = examPlan.getClassCredits();
        if (classCredits.isEmpty()) {
            return ResponseEntity.badRequest().body("ERROR,Exam Plan have been set exam scheduling");
        }
        Collections.sort(classCredits, Comparator
                .comparing(ClassCredit::getNumberExamGroups)
                .thenComparingInt(classCredit -> classCredit.getStudents().size())
                .reversed()
        );

        List<ShiftSystem> shiftSystems = shiftSystemRepository.findAllByShiftType(true);
        List<LocalDate> dates = examPlan.getDateStart().datesUntil(examPlan.getDateEnd().plusDays(1))
                .filter(date -> date.getDayOfWeek() != DayOfWeek.SUNDAY)
                .collect(Collectors.toList());

        Map<LocalDate, Map<ShiftSystem, List<Classroom>>> availableSlots = initializeAvailableSlots(dates, shiftSystems).getFreeSlotsTimetable();

        List<Exam> exams = new ArrayList<>();

        for (ClassCredit cc : classCredits) {
            int numberExamGroups = cc.getNumberExamGroups();
            String roomType = cc.getExamType().equals(EExamType.lab) ? "Practice" : "Study";

            List<Student> students = creditsStudentsRepository.findAllByClassCreditId(cc.getClassCreditId()).stream()
                    .filter(ClassCreditsStudents::getExamStatus)
                    .map(ClassCreditsStudents::getStudent)
                    .collect(Collectors.toList());
            int studentsPerGroup = students.size() / numberExamGroups;
            boolean typeProject = false;
            List<Exam> examsTypeProject = new ArrayList<>();
            if (cc.getExamType().equals(EExamType.project)) {
                typeProject = true;
            }

            for (int examGroupNumber = 0; examGroupNumber < numberExamGroups; examGroupNumber++) {
                Exam exam = new Exam();
                boolean flag = false;
                boolean planTwo = false;
//                if (examGroupNumber == numberExamGroups - 1) studentsPerGroup = students.size();
                List<Student> studentGroup = takeListStudentInExamGroup(students, examGroupNumber, studentsPerGroup);
                for (int i = 0; i < dates.size(); i++) {
                    LocalDate date = dates.get(i);
                    Map<ShiftSystem, List<Classroom>> shiftSystemMap = availableSlots.get(date);
                    if (!planTwo) {
                        if (studentHasExamInDate(date, studentGroup, exams)) {
                            continue;
                        }
                    }

                    for (ShiftSystem shiftSystem : shiftSystemMap.keySet()) {
                        if (studentHasExamInShiftSystem(date, shiftSystem, studentGroup, exams)) {
                            continue;
                        }
                        Optional<Classroom> selectedClassroom = shiftSystemMap.get(shiftSystem).parallelStream()
                                .filter(classroom -> classroom.getRoomType().equals(roomType))
                                .filter(classroom -> classroom.getCapacity() >= 2 * studentGroup.size())
                                .findFirst();
                        if (!examsTypeProject.isEmpty()) {
                            boolean same = examsTypeProject.parallelStream()
                                    .anyMatch(e ->
                                            e.getExamDate().equals(date) &&
                                                    e.getShiftSystem().equals(shiftSystem)
                                    );
                            if (same) {
                                continue;
                            }
                        }

                        if (selectedClassroom.isPresent()) {
                            exam.setExamDate(date);
                            exam.setGroupNumber(examGroupNumber + 1);
                            exam.setClassCredit(cc);
                            exam.setClassroom(selectedClassroom.get());
                            exam.setShiftSystem(shiftSystem);
                            exam.setStudents(studentGroup);
                            if (typeProject) {
                                examsTypeProject.add(exam);
                            } else {
                                exams.add(exam);
                            }
                            flag = true;
                            availableSlots.get(date).get(shiftSystem).remove(selectedClassroom.get());
                            break;
                        }
                    }
                    if (flag | (!flag && planTwo)) break;
                    else {
                        i = 0;
                        planTwo = true;
                    }
                }
                if (!flag)
                    return ResponseEntity.badRequest().body("If a conflict Exams occurs or some constraint is not satisfied, try extending the exam plan time and try again.!");
            }
            if (!examsTypeProject.isEmpty()) exams.addAll(examsTypeProject);
        }

//        try {
//            examRepository.saveAll(exams);
//            examPlan.setFlag(false);
//            examPlanRepository.save(examPlan);
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body("Save list exam for this exam plan failed!");
//        }
        return ResponseEntity.ok("Building exam scheduling successfully!");

    }

    private boolean studentHasExamInShiftSystem(LocalDate date, ShiftSystem shiftSystem, List<Student> studentGroup, List<Exam> exams) {
        for (Student student : studentGroup) {
            // Kiểm tra xem sinh viên đã có kỳ thi trong cùng ngày và cùng ca thi (ShiftSystem) chưa
            boolean hasExam = exams.parallelStream()
                    .anyMatch(exam ->
                            exam.getExamDate().equals(date) &&
                                    exam.getShiftSystem().equals(shiftSystem) &&
                                    exam.getStudents().contains(student) // Giả sử ClassCredit chứa danh sách sinh viên
                    );

            if (hasExam) {
                return true; // Nếu tìm thấy kỳ thi của sinh viên trong cùng ngày và ca thi, trả về true
            }
        }
        return false;
    }

    private boolean studentHasExamInDate(LocalDate date, List<Student> studentGroup, List<Exam> exams) {
        for (Student student : studentGroup) {
            // Kiểm tra xem sinh viên đã có kỳ thi trong cùng ngày và cùng ca thi (ShiftSystem) chưa
            boolean hasExam = exams.parallelStream()
                    .anyMatch(exam ->
                            exam.getExamDate().equals(date) &&
                                    exam.getStudents().contains(student) // Giả sử ClassCredit chứa danh sách sinh viên
                    );

            if (hasExam) {
                return true; // Nếu tìm thấy kỳ thi của sinh viên trong cùng ngày và ca thi, trả về true
            }
        }
        return false;
    }

    @Override
    public ResponseEntity<?> getExamScheduling(Long examPlanId) {
        ExamPlan examPlan = examPlanRepository.findById(examPlanId).get();
        if (contextHolder.getRoleFromContext().stream().anyMatch(r -> r.equals(ERole.student.name()))) {
            Student student = studentRepository.findById(contextHolder.getUsernameFromContext().toUpperCase(Locale.ROOT))
                    .orElseThrow(() -> new ResourceNotFoundException("Not found student"));
            List<Exam> examsInSemester = student.getExams().stream()
                    .filter(exam -> exam.getClassCredit() != null && exam.getClassCredit().getExamPlan().getExamPlanId().equals(examPlan.getExamPlanId()))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(
                    examsInSemester.stream()
                            .map(exam -> {
                                ExamView examView = modelMapper.map(exam, ExamView.class);
                                ClassCredit cc = exam.getClassCredit();
                                RegisInExamPlan regisInExamPlan = RegisInExamPlan.builder()
                                        .subject(cc.getSubject().getName())
                                        .build();
                                examView.setClassCredit(regisInExamPlan);
                                examView.setClassroomId(exam.getClassroom().getClassroomId());
                                examView.setClassroomName(exam.getClassroom().getName());
                                return examView;
                            })
                            .collect(Collectors.toList())
            );
        } else if (contextHolder.getRoleFromContext().stream().anyMatch(r -> r.equals(ERole.TQA.name()))) {
            return ResponseEntity.ok(
                    examRepository.findAllByExamPlanId(examPlanId).stream()
                            .map(exam -> {
                                ClassCredit cc = exam.getClassCredit();
                                RegisInExamPlan regisInExamPlan = RegisInExamPlan.builder()
                                        .classCreditId(cc.getClassCreditId())
                                        .subject(cc.getSubject().getName())
                                        .lecturerId(cc.getLecturer().getLecturerId())
                                        .lecturerName(cc.getLecturer().getProfile().getFullname())
                                        .facultyId(cc.getFaculty().getFacultyId())
                                        .facultyName(cc.getFaculty().getName())
                                        .examType(cc.getExamType().name())
                                        .build();

                                return ExamView.builder()
                                        .examId(exam.getExamId())
                                        .examDate(exam.getExamDate())
                                        .groupNumber(exam.getGroupNumber())
                                        .classCredit(regisInExamPlan)
                                        .classroomId(exam.getClassroom().getClassroomId())
                                        .classroomName(exam.getClassroom().getName())
                                        .shiftSystem(modelMapper.map(exam.getShiftSystem(), ShiftSystemView.class))
                                        .studentSize(exam.getStudents().size())
                                        .lecturers(exam.getLecturers().stream().map(l -> modelMapper.map(l, LecturerView.class)).collect(Collectors.toList()))
                                        .build();
                            })
                            .collect(Collectors.toList())
            );
        } else {
            return ResponseEntity.ok(
                    examRepository.findAllByLecturer(examPlanId, contextHolder.getAccount().getAccountId()).stream()
                            .map(exam -> {
                                ClassCredit cc = exam.getClassCredit();
                                RegisInExamPlan regisInExamPlan = RegisInExamPlan.builder()
                                        .classCreditId(cc.getClassCreditId())
                                        .subject(cc.getSubject().getName())
                                        .lecturerId(cc.getLecturer().getLecturerId())
                                        .lecturerName(cc.getLecturer().getProfile().getFullname())
                                        .facultyId(cc.getFaculty().getFacultyId())
                                        .facultyName(cc.getFaculty().getName())
                                        .examType(cc.getExamType().name())
                                        .build();

                                return ExamView.builder()
                                        .examId(exam.getExamId())
                                        .examDate(exam.getExamDate())
                                        .groupNumber(exam.getGroupNumber())
                                        .classCredit(regisInExamPlan)
                                        .classroomId(exam.getClassroom().getClassroomId())
                                        .classroomName(exam.getClassroom().getName())
                                        .shiftSystem(modelMapper.map(exam.getShiftSystem(), ShiftSystemView.class))
                                        .studentSize(exam.getStudents().size())
                                        .lecturers(exam.getLecturers().stream().map(l -> modelMapper.map(l, LecturerView.class)).collect(Collectors.toList()))
                                        .build();
                            })
                            .collect(Collectors.toList())
            );
        }
    }

    // Cua giang vien va QTA
    @Override
    public ResponseEntity<?> getExamDetails(Long examId) {
        Exam exam = examRepository.findById(examId).get();
        ClassCredit cc = exam.getClassCredit();
        RegisInExamPlan regisInExamPlan = RegisInExamPlan.builder()
                .classCreditId(cc.getClassCreditId())
                .subject(cc.getSubject().getName())
                .lecturerId(cc.getLecturer().getLecturerId())
                .lecturerName(cc.getLecturer().getProfile().getFullname())
                .facultyId(cc.getFaculty().getFacultyId())
                .facultyName(cc.getFaculty().getName())
                .examType(cc.getExamType().name())
                .build();
//        List<StudentDTO> studentDTOS = exam.getStudents().stream().map(s -> modelMapper.map(s,StudentDTO.class)).collect(Collectors.toList());
        List<LecturerView> lecturerViews = Optional.ofNullable(exam.getLecturers()).orElse(Collections.emptyList()).stream().map(l -> modelMapper.map(l, LecturerView.class)).collect(Collectors.toList());
        ExamView examView = ExamView.builder()
                .examId(exam.getExamId())
                .examDate(exam.getExamDate())
                .groupNumber(exam.getGroupNumber())
                .classCredit(regisInExamPlan)
                .classroomId(exam.getClassroom().getClassroomId())
                .classroomName(exam.getClassroom().getName())
                .shiftSystem(modelMapper.map(exam.getShiftSystem(), ShiftSystemView.class))
//                .students(studentDTOS)
                .lecturers(lecturerViews)
                .build();
        return ResponseEntity.ok(examView);

    }

    @Override
    public ResponseEntity<?> getStudentsInExam(Long examId) {
        Exam exam = examRepository.findById(examId).get();
        return ResponseEntity.ok(exam.getStudents().stream().map(s -> modelMapper.map(s, StudentDTO.class)).collect(Collectors.toList()));
    }

    @Override
    public ResponseEntity<?> getProctoringOfLecurrer() {
        Lecturer lecturer = lecturerRepository.findByAccountId(contextHolder.getAccount().getAccountId());
        return ResponseEntity.ok(Optional.ofNullable(lecturer.getExams()).orElse(Collections.emptyList()).stream()
                .map(exam -> {
                    ExamView examView = modelMapper.map(exam, ExamView.class);
                    ClassCredit cc = exam.getClassCredit();
                    RegisInExamPlan regisInExamPlan = RegisInExamPlan.builder()
                            .subject(cc.getSubject().getName())
                            .build();
                    examView.setClassCredit(regisInExamPlan);
                    examView.setClassroomId(exam.getClassroom().getClassroomId());
                    examView.setClassroomName(exam.getClassroom().getName());
                    return examView;
                })
                .collect(Collectors.toList()));
    }

    @Override
    @Transactional
    public ResponseEntity<?> buildExamSchedulingForExamPlan(Long examPlanId) {
        ExamPlan examPlan = examPlanRepository.findById(examPlanId)
                .orElseThrow(() -> new ResourceNotFoundException("Not found exam plan"));
//        if (examPlan.getDateStart().isBefore(LocalDate.now()) | examPlan.getDateStart().equals(LocalDate.now()))
//            return ResponseEntity.badRequest().body("Have NO class credit regis into this exam plan OR ExamPlan has been implemented");
        List<ClassCredit> classCredits = examPlan.getClassCredits().stream()
                .filter(cc -> cc.getExams().isEmpty())
                .collect(Collectors.toList());

        if (classCredits.isEmpty()) {
            return ResponseEntity.badRequest().body("Không có lớp tín chỉ nào hoặc đã kế hoạch đã được lập lịch thi rồi!");
        }

        Collections.sort(classCredits, Comparator
                .comparing(ClassCredit::getNumberExamGroups)
                .thenComparing((classCredit1, classCredit2) ->
                        Integer.compare(classCredit2.getStudents().size(), classCredit1.getStudents().size())));

        List<ShiftSystem> shiftSystems = shiftSystemRepository.findAllByShiftType(true);
        List<LocalDate> dates = examPlan.getDateStart().datesUntil(examPlan.getDateEnd().plusDays(1))
                .filter(date -> date.getDayOfWeek() != DayOfWeek.SUNDAY)
                .collect(Collectors.toList());

        SlotAndBusyLecturers availableSlots = initializeAvailableSlots(dates, shiftSystems);

        List<Exam> exams = new ArrayList<>();

        for (ClassCredit cc : classCredits) {
            int numberExamGroups = cc.getNumberExamGroups();
            String roomType = cc.getExamType().equals(EExamType.lab) ? "Practice" : "Study";

            List<Student> students = creditsStudentsRepository.findAllByClassCreditId(cc.getClassCreditId()).stream()
                    .filter(ClassCreditsStudents::getExamStatus)
                    .map(ClassCreditsStudents::getStudent)
                    .collect(Collectors.toList());
            int studentsPerGroup = students.size() / numberExamGroups;

            for (int examGroupNumber = 0; examGroupNumber < numberExamGroups; examGroupNumber++) {
                boolean flag = false;
                boolean planTwo = false;
                boolean conflictLecturer = false;
//                if (examGroupNumber == numberExamGroups - 1) studentsPerGroup = students.size();
                List<Student> studentGroup = takeListStudentInExamGroup(students, examGroupNumber, studentsPerGroup);

                Map<LocalDate, Map<ShiftSystem,List<Lecturer>>> freeLecturers = availableSlots.getFreeLecturer();
                List<LocalDate> lecturerFreeDates = new ArrayList<>(availableSlots.getFreeLecturer().keySet());
                lecturerFreeDates.sort(Comparator.naturalOrder());

                List<Lecturer> allLecturers = freeLecturers.values().stream()
                        .flatMap(shiftSystemMap -> shiftSystemMap.values().stream())
                        .flatMap(List::stream)
                        .collect(Collectors.toList());

                // dates
                for (int i = 0; i < dates.size(); i++) {

                    LocalDate date = dates.get(i);

                    Map<ShiftSystem, List<Classroom>> freeClassroomsOneDate = availableSlots.getFreeClassroom().get(date);
                    Map<ShiftSystem, List<Lecturer>> freeLecturersOneDate = freeLecturers.get(date);


                    if (!planTwo) {
                        if (studentHasExamInDate(date, studentGroup, exams)) {
                            continue;
                        }
                    }
                    List<ShiftSystem> shiftSystemList = new ArrayList<>(freeLecturersOneDate.keySet());
                    Collections.shuffle(shiftSystemList);
                    for (ShiftSystem shiftSystem : shiftSystemList) {

                        boolean lecturerAvailable = freeLecturersOneDate.get(shiftSystem).contains(cc.getLecturer());

                        if (lecturerAvailable) {
                            if (studentHasExamInShiftSystem(date, shiftSystem, studentGroup, exams)) {
                                continue;
                            }

                            Optional<Lecturer> anotherLecturer = allLecturers.stream()
                                    .filter(lecturer -> !lecturer.equals(cc.getLecturer()))
                                    .findAny();

                            Optional<Classroom> selectedClassroom = freeClassroomsOneDate.get(shiftSystem).parallelStream()
                                    .filter(classroom ->
                                            classroom.getRoomType().equals(roomType) &&
                                                    classroom.getCapacity() >= 2 * studentGroup.size()
                                    )
                                    .findFirst();

                            anotherLecturer.ifPresent(another -> {
                                outerLoop:
                                for (Map<ShiftSystem, List<Lecturer>> shiftSystemListMap : availableSlots.getFreeLecturer().values()) {
                                    for (List<Lecturer> lecturers : shiftSystemListMap.values()) {
                                        if (lecturers.removeIf(lecturer -> lecturer.equals(another))) {
                                            break outerLoop; // Kết thúc ngay khi đã xóa Lecturer
                                        }
                                    }
                                }
                            });

                            List<Lecturer> twoLecturers = Arrays.asList(cc.getLecturer(),anotherLecturer.orElse(null));

                            if (selectedClassroom.isPresent()) {
                                Exam exam = new Exam();
                                exam.setExamDate(date);
                                exam.setGroupNumber(examGroupNumber + 1);
                                exam.setClassCredit(cc);
                                exam.setClassroom(selectedClassroom.get());
                                exam.setShiftSystem(shiftSystem);
                                exam.setStudents(studentGroup);
                                exam.setLecturers(twoLecturers);
                                exams.add(exam);

                                flag = true;
                                availableSlots.getFreeLecturer().get(date).get(shiftSystem).remove(cc.getLecturer());
                                availableSlots.getFreeClassroom().get(date).get(shiftSystem).remove(selectedClassroom.get());
                                break;
                            }
                        }
                    }

                    if (!flag) {
                        return ResponseEntity.badRequest().body("Không xếp được lịch thi, hãy thử mở rộng phạm vi thời gian thi hoặc xem lại danh sách giảng viên coi thi");
                    }
                    if (flag | (!flag && planTwo)) break;
                    else {
                        i = 0;
                        planTwo = true;
                    }
                }
                if (!flag)
                    return ResponseEntity.badRequest().body("Không xếp được lịch thi, hãy thử mở rộng phạm vi thời gian thi hoặc xem lại danh sách giảng viên coi thi!");
            }
        }
        try {
            examRepository.saveAll(exams);
            examPlan.setStatus(true);
            examPlanRepository.save(examPlan);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Save list exam for this exam plan failed!");
        }
        return ResponseEntity.ok("Tạo lịch thi cho kế hoạch thi này thành công!");
    }

    @Override
    public ResponseEntity<?> getAllExamToStudent(Long examPlanId,String studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Not Found!"));
        ExamPlan examPlan = examPlanRepository.findById(examPlanId)
                .orElseThrow(() -> new ResourceNotFoundException("Not found exam plan!"));
        LocalDate dateStart = examPlan.getDateStart();
        LocalDate dateEnd = examPlan.getDateEnd();
        List<Exam> exams = student.getExams().stream()
                .filter(e ->
                        (e.getExamDate().isAfter(dateStart) || e.getExamDate().equals(dateStart)) &&
                        (e.getExamDate().isBefore(dateEnd) || e.getExamDate().equals(dateEnd))
                ).collect(Collectors.toList());
        return ResponseEntity.ok(exams.stream()
                .map(exam -> {
                    ClassCredit cc = exam.getClassCredit();
                    RegisInExamPlan regisInExamPlan = RegisInExamPlan.builder()
                            .classCreditId(cc.getClassCreditId())
                            .subject(cc.getSubject().getName())
                            .lecturerId(cc.getLecturer().getLecturerId())
                            .lecturerName(cc.getLecturer().getProfile().getFullname())
                            .facultyId(cc.getFaculty().getFacultyId())
                            .facultyName(cc.getFaculty().getName())
                            .examType(cc.getExamType().name())
                            .build();

                    return ExamView.builder()
                            .examId(exam.getExamId())
                            .examDate(exam.getExamDate())
                            .groupNumber(exam.getGroupNumber())
                            .classCredit(regisInExamPlan)
                            .classroomId(exam.getClassroom().getClassroomId())
                            .classroomName(exam.getClassroom().getName())
                            .shiftSystem(modelMapper.map(exam.getShiftSystem(), ShiftSystemView.class))
                            .studentSize(exam.getStudents().size())
                            .lecturers(exam.getLecturers().stream().map(l -> modelMapper.map(l, LecturerView.class)).collect(Collectors.toList()))
                            .build();
                })
                .collect(Collectors.toList())
        );
    }

    @Override
    public ResponseEntity<?> changeLec(ChangeLecRequest request) {
        Exam exam = examRepository.findById(request.getExamId())
                .orElseThrow(()-> new ResourceNotFoundException("not found"));
        exam.getLecturers().removeIf(lecturer -> lecturer.getLecturerId().equals(request.getLecOldId()));

// Thêm giáo viên mới vào đối tượng exam
        Lecturer newLecturer = lecturerRepository.findById(request.getLecNewId())
                .orElseThrow(() -> new ResourceNotFoundException("New Lecturer not found"));
        exam.getLecturers().add(newLecturer);
        try {
            examRepository.save(exam);
            return ResponseEntity.ok("Đổi người coi thi thành công!");
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Đổi người coi thi thất bại!");
        }
    }
}
