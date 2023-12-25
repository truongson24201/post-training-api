package apisystem.posttraining.service.Impl;

import apisystem.posttraining.DTO.ClassCreditDTO.RegisInExamPlan;
import apisystem.posttraining.DTO.ExamPlanDTO.ExamPlanAdd;
import apisystem.posttraining.DTO.ExamPlanDTO.ExamPlanView;
import apisystem.posttraining.DTO.RegisExamDTO.RegisExamPlanAdd;
import apisystem.posttraining.entity.*;
import apisystem.posttraining.entity.enumreration.EExamType;
import apisystem.posttraining.entity.enumreration.ERole;
import apisystem.posttraining.exception.ResourceNotFoundException;
import apisystem.posttraining.repository.*;
import apisystem.posttraining.service.IExamPlanService;
import apisystem.posttraining.service.IContextHolder;
import apisystem.posttraining.utils.CommonService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExamPlanServiceImpl implements IExamPlanService {
    private final ExamPlanRepository examPlanRepository;
    private final SemesterRepository semesterRepository;
    private final ModelMapper modelMapper;
//    private final RegisExamPlanRepository regisExamPlanRepository;
    private final ClassCreditRepository classCreditRepository;
    private final LecturerRepository lecturerRepository;
    private final IContextHolder contextHolder;



    @Override
    public ResponseEntity<?> addExamPlan(ExamPlanAdd examPlanAdd) {
        Semester semester = semesterRepository.findCurrentSemester(LocalDate.now());
        CommonService.trimString(examPlanAdd.getTitle());
        ExamPlan examPlan = new ExamPlan(examPlanAdd.getDateStart(),examPlanAdd.getDateEnd(),examPlanAdd.getTitle(),false,semester,false);
        try {
            examPlanRepository.save(examPlan);
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Tạo kế hoạch thi thất bại!");
        }
        return ResponseEntity.ok("tạo kế hoạch thi thành công!");
    }

    @Override
    public ResponseEntity<?> getAllExamPlan() {
//        if (contextHolder.getRoleFromContext().stream().anyMatch(role -> role.equals(ERole.TQA.name()))){
            return ResponseEntity.ok(examPlanRepository.findAll().stream()
                    .map(examPlan -> modelMapper.map(examPlan, ExamPlanView.class))
                    .collect(Collectors.toList()));
//        }
//         giang vien coi thi load ra ky hien tai thoi (current semester)
//        Semester semester = semesterRepository.findCurrentSemester(LocalDate.now());
//        return ResponseEntity.ok(examPlanRepository.findAllBySemesterId(semester.getSemesterId()).stream()
//                .map(examPlan -> modelMapper.map(examPlan, ExamPlanView.class))
//                .collect(Collectors.toList()));
    }

    @Override
    public ResponseEntity<?> getRegisInExamPlan(Long examPlanId) {
        // phong khao thi thi xem tat ca lop dang dy trong ke hoach thi nay

        //Giang vien thi hien thi lop giang vien do dang ky thoi

//        return ResponseEntity.ok(examPlanRepository.findById(examPlanId).get().getClassCredits()
//                .stream().map(cc -> modelMapper.map(cc,ClassCreditView.class)).collect(Collectors.toList()));
        return ResponseEntity.ok(examPlanRepository.findById(examPlanId).get().getClassCredits()
                .stream().map(cc -> RegisInExamPlan.builder()
                        .classCreditId(cc.getClassCreditId())
                        .examType(cc.getExamType().name())
                        .numberExamGroups(cc.getNumberExamGroups())
                        .subjectId(cc.getSubject().getSubjectId())
                        .subject(cc.getSubject().getName())
                        .lecturerId(cc.getLecturer().getLecturerId())
                        .lecturerName(cc.getLecturer().getProfile().getFullname())
                        .facultyId(cc.getFaculty().getFacultyId())
                        .facultyName(cc.getFaculty().getName())
                        .numberStudents(cc.getStudents().size())
                        .build()).collect(Collectors.toList()));
    }

//    public static boolean validateExamPlan(ExamPlan examPlan){
//        LocalDate currentDate = LocalDate.now();
//        return (currentDate.equals(examPlan.getRegisOpening()) || currentDate.isAfter(examPlan.getRegisOpening()))
//                && (currentDate.equals(examPlan.getRegisClosing()) || currentDate.isBefore(examPlan.getRegisClosing()));
//    }

    public boolean validateClassCredit(ClassCredit classCredit){
        // tim theo giang vien dang dang nhap
        if (contextHolder.getRoleFromContext().stream().anyMatch(r -> r.equals(ERole.TQA.name()))) return true;
        Lecturer lecturer = lecturerRepository.findByAccountId(contextHolder.getAccount().getAccountId());
        return (classCredit.getLecturer() == lecturer && classCredit.getSemester() == semesterRepository.findCurrentSemester(LocalDate.now()));
    }

    @Override
    public ResponseEntity<?> regisIntoExamPlan(RegisExamPlanAdd regisExamPlanAdd) {
        ExamPlan examPlan = examPlanRepository.findById(regisExamPlanAdd.getExamPlanId())
                .orElseThrow(() -> new ResourceNotFoundException("Not found exam plan"));
//        if (!validateExamPlan(examPlan)){
//            return ResponseEntity.badRequest().body("Outside of registration time");
//        }
        ClassCredit classCredit = classCreditRepository.findById(regisExamPlanAdd.getClassCreditId()).get();
        if (!validateClassCredit(classCredit))
            return ResponseEntity.badRequest().body("You dont teach this class, or your regis not in current semester -> Please check again!");

        classCredit.setExamPlan(examPlan);
        classCredit.setNumberExamGroups(regisExamPlanAdd.getNumberExamGroup());
        classCredit.setExamType(EExamType.valueOf(regisExamPlanAdd.getExamType()));

        // old
//        RegisExamPlan regisExamPlan = RegisExamPlan.builder()
//                .classCredit(classCredit)
//                .examPlan(examPlan)
//                .numberGroup(regisExamPlanAdd.getNumberGroup())
//                .examType(EExamType.valueOf(regisExamPlanAdd.getExamType()))
//                .build();
        try {
            //old
//            regisExamPlan = regisExamPlanRepository.save(regisExamPlan);
            classCreditRepository.save(classCredit);
            examPlan.setFlag(true);
            examPlanRepository.save(examPlan);
            return ResponseEntity.ok(RegisInExamPlan.builder()
                    .classCreditId(classCredit.getClassCreditId())
                    .examType(classCredit.getExamType().name())
                    .numberExamGroups(classCredit.getNumberExamGroups())
                    .subjectId(classCredit.getSubject().getSubjectId())
                    .subject(classCredit.getSubject().getName())
                    .lecturerId(classCredit.getLecturer().getLecturerId())
                    .lecturerName(classCredit.getLecturer().getProfile().getFullname())
                    .facultyId(classCredit.getFaculty().getFacultyId())
                    .facultyName(classCredit.getFaculty().getName())
                    .numberStudents(classCredit.getStudents().size())
                    .build());
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Regis class credit in to this exam plan failed!");
        }
    }


    @Override
    public ResponseEntity<?> updateExamPlan(Long examPlanId, ExamPlanAdd examPlanAdd) {
        ExamPlan examPlan = examPlanRepository.findById(examPlanId).get();
        if (examPlan.getDateStart().isBefore(LocalDate.now()))
            return ResponseEntity.badRequest().body("Kế hoạch thi đã diễn ra, không thể sửa");

        examPlan.setDateStart(examPlanAdd.getDateStart());
        examPlan.setDateEnd(examPlanAdd.getDateEnd());
        try {
            examPlanRepository.save(examPlan);
            return ResponseEntity.ok("Cập nhật kế hoạch thi thành công");
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Cập nhật kế hoạch thi thất bại");
        }
    }

    @Override
    public ResponseEntity<?> deleteExamPlan(Long examPlanId) {
        ExamPlan examPlan = examPlanRepository.findById(examPlanId).get();
        if (!examPlan.getClassCredits().isEmpty())
            return ResponseEntity.badRequest().body("Đã có lớp tin chỉ trong kế hoạch thi. không thể xóa");
        try {
            examPlanRepository.deleteById(examPlanId);
            return ResponseEntity.ok("Xóa kế hoạch thi thành công!");
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Xóa kế hoạch thi thất bại!");
        }
    }

    @Override
    public ResponseEntity<?> getDetailsExamPlan(Long examPlanId) {

        return ResponseEntity.ok(modelMapper.map(examPlanRepository.findById(examPlanId).get(),
                ExamPlanView.class));
    }

    @Override
    public ResponseEntity<?> updateCCInExamPlan(RegisExamPlanAdd classCreditRequest) {
        ExamPlan examPlan = examPlanRepository.findById(classCreditRequest.getExamPlanId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy kế hoạch thi"));
        if (!examPlan.getFlag()) return ResponseEntity.badRequest().body("Kế hoạch thi không mở -> không thể sửa");
        ClassCredit classCredit = classCreditRepository.findById(classCreditRequest.getClassCreditId()).get();
        classCredit.setNumberExamGroups(classCreditRequest.getNumberExamGroup());
        classCredit.setExamType(EExamType.valueOf(classCreditRequest.getExamType()));
        try {
            classCreditRepository.save(classCredit);
            return ResponseEntity.ok("Cập nhật thành công!");
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Cập nhật thất bại!");
        }
    }

    @Override
    public ResponseEntity<?> deleteCCInExamPlan(Long classCreditId) {
        ClassCredit classCredit = classCreditRepository.findById(classCreditId)
                .orElseThrow(()-> new ResourceNotFoundException("Không tìm thấy Lớp tín chỉ"));
        ExamPlan examPlan = examPlanRepository.findById(classCredit.getExamPlan().getExamPlanId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy kế hoạch thi"));
        if (!examPlan.getFlag()) return ResponseEntity.badRequest().body("Kế hoạch thi không mở -> không thể xóa");
        classCredit.setExamPlan(null);
        try {
            classCreditRepository.save(classCredit);
            return ResponseEntity.ok("Xóa lớp tín chỉ ra khỏi kế hoạch thi thành công!");
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Xóa lớp tín chỉ ra khỏi kế hoạch thi thất bại!");
        }
    }

    @Override
    public ResponseEntity<?> updateFlag(Long examPlanId) {
        ExamPlan examPlan = examPlanRepository.findById(examPlanId).get();
        if(!examPlan.getFlag() && (examPlan.getDateStart().isBefore(LocalDate.now()) | examPlan.getDateStart().equals(LocalDate.now())))
            return ResponseEntity.badRequest().body("Kế hoạch thi đã/đang diễn ra, không thể mở đăng ký");
        examPlan.setFlag(!examPlan.getFlag());
        String mess = "";
        if (examPlan.getFlag()) mess = "Mở đăng ký cho kế hoạch thi thành công";
        else mess = "Đóng đăng ký cho kế hoạch thi thành công";
        try {
            examPlanRepository.save(examPlan);
            return ResponseEntity.ok(mess);
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Lỗi");
        }
    }

    @Override
    public ResponseEntity<?> getAllExamPlanHasBuild() {
        return ResponseEntity.ok(examPlanRepository.findAllHasBuild().stream()
                .map(examPlan -> modelMapper.map(examPlan, ExamPlanView.class))
                .collect(Collectors.toList()));
    }

    @Override
    public ResponseEntity<?> closeAllHasExpired() {
        List<ExamPlan> examPlans = examPlanRepository.findAllHasTurnOn();
        examPlans.forEach(ep ->{
            if (ep.getDateStart().isBefore(LocalDate.now()) || ep.getDateStart().equals(LocalDate.now())){
                ep.setFlag(false);
            }
        });
        try {
            examPlanRepository.saveAll(examPlans);
            return ResponseEntity.ok("Close All Exam Plan has expired successfully!");
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Close All Exam Plan has expired failed!");
        }
    }
}
