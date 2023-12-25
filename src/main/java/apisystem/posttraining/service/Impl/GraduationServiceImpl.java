package apisystem.posttraining.service.Impl;

import apisystem.posttraining.DTO.ComSubjectDTO.ComPointDTO;
import apisystem.posttraining.DTO.Graduations.*;
import apisystem.posttraining.DTO.StudentDTO.StudentDTO;
import apisystem.posttraining.entity.*;
import apisystem.posttraining.entity.enumreration.EClassCredit;
import apisystem.posttraining.entity.enumreration.EGraduation;
import apisystem.posttraining.entity.enumreration.EStudentStatus;
import apisystem.posttraining.exception.ResourceNotFoundException;
import apisystem.posttraining.repository.*;
import apisystem.posttraining.service.IContextHolder;
import apisystem.posttraining.service.IGraduationService;
import apisystem.posttraining.utils.CommonService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GraduationServiceImpl implements IGraduationService {

    private final ClassCreditRepository classCreditRepository;
    private final CreditsStudentsRepository creditsStudentsRepository;
    private final StudentsScoresRepository studentsScoresRepository;
    private final FacultyRepository facultyRepository;
    private final ModelMapper modelMapper;
    private final IContextHolder contextHolder;
    private final StudentRepository studentRepository;
    private final SemesterRepository semesterRepository;
//    private final GraduationThesisRepository graduationThesisRepository;
    private final GraduationRepository graduationRepository;


    @Override
    public ResponseEntity<?> previewGraduationsThesis(GraThesisPreviewRequest preview) {
        Faculty faculty = facultyRepository.findById(preview.getFacultyId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy ngành!"));
        Semester semester = semesterRepository.findCurrentSemester(LocalDate.now());
        List<Graduation> graduations = graduationRepository.findAllTrueGraduationInternship(faculty.getFacultyId());
        if (graduations.isEmpty()) return ResponseEntity.badRequest().body("Chưa có sinh viên nào hoàn thành thực tập tốt nghiệp trong năm "+ LocalDate.now().getYear() +" này!");

//            List<Graduation> filteredGraduations = graduations.stream()
//                .filter(graduation -> graduation.getStudent().getClassCredits().stream()
//                        .noneMatch(cc -> cc.getClassCredit().getSubject().getName().equals("Thay thế")))
//                .collect(Collectors.toList());
//        if (filteredGraduations.isEmpty()) return ResponseEntity.ok("Chưa có sinh viên nào hoàn thành thực tập tốt nghiệp trong năm "+LocalDate.now().getYear()+" này!");

//        List<Student> students = graduations.stream()
//                .map(Graduation::getCreditsStudents)
//                .collect(Collectors.toList());

//        List<ClassCreditsStudents> allStudents = graduations.stream()
//                .flatMap(graduation -> graduation.getStudent().getClassCredits().stream())
//                .collect(Collectors.toList());

//        List<ClassCreditsStudents> allStudents = graduations.stream()
//                .map(Graduation::getCreditsStudents).collect(Collectors.toList());

        List<Student> studentsHasPassIntern = graduations.stream()
                .map(Graduation::getCreditsStudents)
                .filter(Objects::nonNull)
                .map(ClassCreditsStudents::getStudent)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

//        List<ClassCreditsStudents> allStudents = students.stream()
//                .flatMap(student -> student.getClassCredits().stream()) // Lấy danh sách ClassCreditsStudents từ mỗi Student
//                .collect(Collectors.toList());

//        List<Long> regisIds = allStudents.stream()
//                .map(ClassCreditsStudents::getId).collect(Collectors.toList());

        List<ClassCreditsStudents> allStudents = studentsHasPassIntern.stream()
                .flatMap(student -> student.getClassCredits().stream())
                .collect(Collectors.toList());
        List<Long> regisIds = allStudents.stream()
                .map(ClassCreditsStudents::getId).collect(Collectors.toList());

        Map<String, Map<Long, List<StudentsScores>>> studentScoresMap = new HashMap<>();

        List<StudentsScores> studentsScores = studentsScoresRepository.findAllByRegisIDs(regisIds);

        // regidId <-> list points (ck,cuoiky,giuaky,...)
        Map<Long, List<StudentsScores>> pointOneSubject = new HashMap<>();
        studentsScores.forEach(sS -> {
            pointOneSubject.computeIfAbsent(sS.getRegisClass().getId(), k -> new ArrayList<>())
                    .add(sS);
        });

        // student <-> list regis <-> point list in one regis
        for (StudentsScores studentScore : studentsScores) {
            String studentId = studentScore.getRegisClass().getStudent().getStudentId();
            Long regisId = studentScore.getRegisClass().getId();
            // Kiểm tra xem Map chính có chứa StudentId này hay không
            if (!studentScoresMap.containsKey(studentId)) {
                studentScoresMap.put(studentId, new HashMap<>());
            }
            // Lấy Map con của StudentId này
            Map<Long, List<StudentsScores>> innerMap = studentScoresMap.get(studentId);
            // Kiểm tra xem Map con có chứa regisId này hay không
            if (!innerMap.containsKey(regisId)) {
                innerMap.put(regisId, new ArrayList<>());
            }
            // Thêm điểm số vào danh sách tương ứng trong Map con
            innerMap.get(regisId).add(studentScore);
        }

        Map<String, GraduationGPA> studentGraduations = new HashMap<>();


        for (Map.Entry<String, Map<Long, List<StudentsScores>>> entry : studentScoresMap.entrySet()) {
            String studentId = entry.getKey();
            Map<Long, List<StudentsScores>> regisScoresMap = entry.getValue();
            int totalCredit = 0;
            double totalFound = 0.0;
            boolean hasDoGra = false;
            int i=0;
            for (List<StudentsScores> scores : regisScoresMap.values()) {
                double calTenPointOneSubject1 = CommonService.calTenPointOneSubject1(scores);
                double calFoundPoint = CommonService.convertTenToFour(calTenPointOneSubject1);
                int calTotalCredit = scores.get(0).getComponentSubject().getSubject().getCreditNum();
                if (calFoundPoint == 0.0) {
                    calTotalCredit = 0;
                }

                totalFound += calFoundPoint;
                totalCredit += calTotalCredit;
                i++;
            }
            double averageFound = Math.round((totalFound / i)* 100.0) / 100.0;
            GraduationGPA graduationGPA;
            if (averageFound >= preview.getGpaFoundMin() && totalCredit >= preview.getCreditMin()){
                graduationGPA = new GraduationGPA(averageFound, totalCredit, true);
            }else {
                graduationGPA = new GraduationGPA(averageFound, totalCredit, false);
            }
            studentGraduations.put(studentId, graduationGPA);
        }

        Map<String, GraduationGPA> sortedMap = studentGraduations.entrySet()
                .stream()
                .sorted((e1, e2) -> {
                    int compareGPA = Double.compare(e2.getValue().getGpaFound(), e1.getValue().getGpaFound());
                    if (compareGPA != 0) {
                        return compareGPA;
                    }
                    return Integer.compare(e2.getValue().getCreditNum(), e1.getValue().getCreditNum());
                })
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new));

//        List<GraduationThesis> graduationTheses = graduationThesisRepository.findAllByResult(false);
//
//        // sinh vien da rot do an
//        List<GraThesisPreviewResponse> graduationInDB = graduationTheses.stream()
//                .map(g -> {
//                    StudentDTO studentDTO = modelMapper.map(g.getStudent(), StudentDTO.class);
//                    GraduationGPA graduationGPA = new GraduationGPA(g.getGpaFound(),g.getCreditNum(),g.getPointDNum(),g.getResult());
//                    return new GraThesisPreviewResponse(studentDTO,graduationGPA);
//                })
//                .collect(Collectors.toList());

        List<GraThesisPreviewResponse> graduationPreviewList = sortedMap.entrySet().stream()
                .map(entry -> new GraThesisPreviewResponse(
                        modelMapper.map(studentRepository.findById(entry.getKey()).get(), StudentDTO.class),
                        entry.getValue()
                ))
                .collect(Collectors.toList());

//        graduationPreviewList.addAll(graduationInDB);

        return ResponseEntity.ok(graduationPreviewList);
    }

    @Transactional
    @Override
    public ResponseEntity<?> addGraduationThesis(Long facultyId,List<GraAddRequest> request) {
        Faculty faculty = facultyRepository.findById(facultyId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy ngành!"));
        Semester semester = semesterRepository.findCurrentSemester(LocalDate.now());
        Optional<ClassCredit> classCredit = classCreditRepository.findCCInSeAndFacAndSub(facultyId,semester.getSemesterId(),"Tốt nghiệp");
        if (classCredit.isEmpty()) return ResponseEntity.badRequest().body("Không có lớp Tốt nghiệp nào được mở trong học kỳ: "+semester.getNum()+",Năm "+semester.getYear()+" này!");
        if (classCredit.get().getStatus().equals(EClassCredit.Open)) return ResponseEntity.badRequest().body("Lớp tín chỉ đã chốt danh sách sinh viên. không thể cập nhật!");
        if (classCredit.get().getStatus().equals(EClassCredit.registration)){
            try {
                List<ClassCreditsStudents> studentsTrue = request.stream().filter(r -> r.isResult())
                        .map(s-> new ClassCreditsStudents(classCredit.get(),studentRepository.findById(s.getStudentId()).get(),false,false))
                        .collect(Collectors.toList());

                if (!classCredit.get().getStudents().isEmpty()) {
                    List<Long> regisId = classCredit.get().getStudents().stream().map(ClassCreditsStudents::getId).collect(Collectors.toList());
//                    graduationRepository.deleteAll(graduationRepository.findAllClassCredit(regisId));
                    graduationRepository.deleteAllClassCredit(regisId);
//                    creditsStudentsRepository.deleteAllById(regisId);
                    creditsStudentsRepository.deleteAllRegisId(regisId);
                }
                List<ClassCreditsStudents> s = creditsStudentsRepository.saveAll(studentsTrue);
                List<Graduation> graduations = s.stream().
                        map(s1 -> new Graduation(s1,semester.getYear().toString(),false,EGraduation.GraduationThesis))
                        .collect(Collectors.toList());
                graduationRepository.saveAll(graduations);
                return ResponseEntity.ok("Tạo danh sách thực tập thành công!");
            } catch (Exception e){
                e.printStackTrace();
                return ResponseEntity.badRequest().body("Tạo danh sách thất bại!");
            }
        }else return ResponseEntity.badRequest().body("Lớp thực tập chưa mở đăng ký hoặc lớp không phù hợp!");
    }

    @Override
    public ResponseEntity<?> getGraThesis(Long facultyId, String makeYear) {
        List<Graduation> graduations = graduationRepository.findAllByMakeYearAndGradutation(makeYear,facultyId, EGraduation.GraduationThesis);
        return ResponseEntity.ok(graduations.stream()
                .map(g->{
                    GraduationView graduationView = modelMapper.map(g, GraduationView.class);
                    graduationView.setStudent(modelMapper.map(g.getCreditsStudents().getStudent(),StudentDTO.class));
                    return graduationView;
                })
                .collect(Collectors.toList())
        );
    }

    @Override
    public ResponseEntity<?> previewGraduationsInternship(GraInternshipPreviewRequest preview) {
        Faculty faculty = facultyRepository.findById(preview.getFacultyId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy ngành!"));
        Semester semester = semesterRepository.findCurrentSemester(LocalDate.now());
        List<Student> students = studentRepository.findAllStudentStatus(faculty.getFacultyId(),EStudentStatus.dang_hoc);
        List<Graduation> graduations = graduationRepository.findAllTrueGraduationInternship(faculty.getFacultyId());

        List<Student> studentsHasPassIntern = graduations.stream()
                .map(Graduation::getCreditsStudents)
                .filter(Objects::nonNull)
                .map(ClassCreditsStudents::getStudent)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        students.removeAll(studentsHasPassIntern);
        List<ClassCreditsStudents> allStudents = students.stream()
                .flatMap(student -> student.getClassCredits().stream())
                .collect(Collectors.toList());
        List<Long> regisIds = allStudents.stream()
                .map(ClassCreditsStudents::getId).collect(Collectors.toList());

        Map<String, Map<Long, List<StudentsScores>>> studentScoresMap = new HashMap<>();

        List<StudentsScores> studentsScores = studentsScoresRepository.findAllByRegisIDs(regisIds);

        // regidId <-> list points (ck,cuoiky,giuaky,...)
        Map<Long, List<StudentsScores>> pointOneSubject = new HashMap<>();
        studentsScores.forEach(sS -> {
            pointOneSubject.computeIfAbsent(sS.getRegisClass().getId(), k -> new ArrayList<>())
                    .add(sS);
        });

        // student <-> list regis <-> point list in one regis
        for (StudentsScores studentScore : studentsScores) {
            String studentId = studentScore.getRegisClass().getStudent().getStudentId();
            Long regisId = studentScore.getRegisClass().getId();
            // Kiểm tra xem Map chính có chứa StudentId này hay không
            if (!studentScoresMap.containsKey(studentId)) {
                studentScoresMap.put(studentId, new HashMap<>());
            }
            // Lấy Map con của StudentId này
            Map<Long, List<StudentsScores>> innerMap = studentScoresMap.get(studentId);
            // Kiểm tra xem Map con có chứa regisId này hay không
            if (!innerMap.containsKey(regisId)) {
                innerMap.put(regisId, new ArrayList<>());
            }
            // Thêm điểm số vào danh sách tương ứng trong Map con
            innerMap.get(regisId).add(studentScore);
        }

        Map<String, GraduationGPA> studentGraduations = new HashMap<>();


        for (Map.Entry<String, Map<Long, List<StudentsScores>>> entry : studentScoresMap.entrySet()) {
            String studentId = entry.getKey();
            Map<Long, List<StudentsScores>> regisScoresMap = entry.getValue();
            int totalCredit = 0;
            boolean hasDoGra = false;
            for (List<StudentsScores> scores : regisScoresMap.values()) {
                double calTenPointOneSubject1 = CommonService.calTenPointOneSubject1(scores);
                double calFoundPoint = CommonService.convertTenToFour(calTenPointOneSubject1);
                int calTotalCredit = scores.get(0).getComponentSubject().getSubject().getCreditNum();
                if (calFoundPoint == 0.0) {
                    calTotalCredit = 0;
                }
                totalCredit += calTotalCredit;
            }
            if (totalCredit < preview.getCreditMin()) continue;
            GraduationGPA graduationGPA = new GraduationGPA(totalCredit);
            studentGraduations.put(studentId, graduationGPA);
        }

        Map<String, GraduationGPA> sortedMap = studentGraduations.entrySet()
                .stream()
                .sorted((e1, e2) -> Integer.compare(e2.getValue().getCreditNum(), e1.getValue().getCreditNum()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (e1, e2) -> e1, LinkedHashMap::new));

        List<GraThesisPreviewResponse> graduationPreviewList = sortedMap.entrySet().stream()
                .map(entry -> new GraThesisPreviewResponse(
                        modelMapper.map(studentRepository.findById(entry.getKey()).get(), StudentDTO.class),
                        entry.getValue()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(graduationPreviewList);
    }

    @Transactional
    @Override
    public ResponseEntity<?> addGraduationInternship(Long facultyId, List<GraAddRequest> request) {
        Faculty faculty = facultyRepository.findById(facultyId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy ngành!"));
        Semester semester = semesterRepository.findCurrentSemester(LocalDate.now());
        Optional<ClassCredit> classCredit = classCreditRepository.findCCInSeAndFacAndSub(facultyId,semester.getSemesterId(),"Thực tập");
        if (classCredit.isEmpty()) return ResponseEntity.badRequest().body("Không có lớp Thực tập nào được mở trong học kỳ: "+semester.getNum()+",Năm "+semester.getYear()+" này!");
        if (classCredit.get().getStatus().equals(EClassCredit.Open)) return ResponseEntity.badRequest().body("Lớp tín chỉ đã chốt danh sách sinh viên. không thể cập nhật!");
        if (classCredit.get().getStatus().equals(EClassCredit.registration)){
            try {
                List<ClassCreditsStudents> students = request.stream()
                        .map(s-> new ClassCreditsStudents(classCredit.get(),studentRepository.findById(s.getStudentId()).get(),false,false))
                        .collect(Collectors.toList());

                if (!classCredit.get().getStudents().isEmpty()) {
                    List<Long> regisId = classCredit.get().getStudents().stream().map(ClassCreditsStudents::getId).collect(Collectors.toList());
//                    graduationRepository.deleteAll(graduationRepository.findAllClassCredit(regisId));
                    graduationRepository.deleteAllClassCredit(regisId);
//                    creditsStudentsRepository.deleteAllById(regisId);
                    creditsStudentsRepository.deleteAllRegisId(regisId);
                }
                List<ClassCreditsStudents> s = creditsStudentsRepository.saveAll(students);
                List<Graduation> graduations = s.stream().
                        map(s1 -> new Graduation(s1,semester.getYear().toString(),false,EGraduation.GraduationInternship))
                        .collect(Collectors.toList());
                graduationRepository.saveAll(graduations);
                return ResponseEntity.ok("Tạo danh sách thực tập thành công!");
            } catch (Exception e){
                e.printStackTrace();
                return ResponseEntity.badRequest().body("Tạo danh sách thất bại!");
            }
        }else return ResponseEntity.badRequest().body("Lớp thực tập chưa mở đăng ký hoặc lớp không phù hợp!");
    }

    @Override
    public ResponseEntity<?> getGraInternship(Long facultyId, String makeYear) {
        List<Graduation> graduations = graduationRepository.findAllByMakeYearAndGradutation(makeYear,facultyId, EGraduation.GraduationInternship);
        return ResponseEntity.ok(graduations.stream()
                .map(g->{
                    GraduationView graduationView = modelMapper.map(g, GraduationView.class);
                    graduationView.setStudent(modelMapper.map(g.getCreditsStudents().getStudent(),StudentDTO.class));
                    return graduationView;
                })
                .collect(Collectors.toList())
        );
    }
}
