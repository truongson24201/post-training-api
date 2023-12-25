package apisystem.posttraining.controller;

import apisystem.posttraining.DTO.ExamDTO.BuildRequest;
import apisystem.posttraining.DTO.ExamPlanDTO.ExamPlanAdd;
import apisystem.posttraining.DTO.RegisExamDTO.RegisExamPlanAdd;
import apisystem.posttraining.entity.enumreration.EExamType;
import apisystem.posttraining.service.IExamPlanService;
import apisystem.posttraining.service.IExamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/exam-plan")
public class ExamPlanController {
    private final IExamPlanService examPlanService;
    private final IExamService examService;


    // phong khao thi mo ke hoach thi
    @PostMapping("")
    public ResponseEntity<?> addExamPlan(@RequestBody ExamPlanAdd examPlanAdd){
        return examPlanService.addExamPlan(examPlanAdd);
    }

    @PutMapping("has-expired")
    public ResponseEntity<?> closeAllHasExpired(){
        return examPlanService.closeAllHasExpired();
    }

    @PutMapping("{id}")
    public ResponseEntity<?> updateExamPlan(@PathVariable("id") Long examPlanId,
                                            @RequestBody ExamPlanAdd examPlanAdd){
        return examPlanService.updateExamPlan(examPlanId,examPlanAdd);
    }

    @DeleteMapping("")
    public ResponseEntity<?> deleteExamPlan(@RequestParam(name = "id") Long examPlanId){
        return examPlanService.deleteExamPlan(examPlanId);
    }

    @GetMapping("")
    public ResponseEntity<?> getAllExamPlan(){
        return examPlanService.getAllExamPlan();
    }

    @GetMapping("build")
    public ResponseEntity<?> getAllExamPlanHasBuild(){
        return examPlanService.getAllExamPlanHasBuild();
    }

    @GetMapping("{id}/regis")
    public ResponseEntity<?> getRegisInExamPlan(@PathVariable("id") Long examPlanId){
        return examPlanService.getRegisInExamPlan(examPlanId);
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getDetailsInExamPlan(@PathVariable("id") Long examPlanId){
        return examPlanService.getDetailsExamPlan(examPlanId);
    }

    @PostMapping("regis")
    public ResponseEntity<?> regisIntoExamPlan(@RequestBody RegisExamPlanAdd regisExamPlanAdd){
        return examPlanService.regisIntoExamPlan(regisExamPlanAdd);
    }

    @GetMapping("types")
    public ResponseEntity<?> getAllExamType(){
        return ResponseEntity.ok(EExamType.values());
    }

    @PutMapping("cc")
    public ResponseEntity<?> updateCCInExamPlan(@RequestBody RegisExamPlanAdd classCredit){
        return examPlanService.updateCCInExamPlan(classCredit);
    }

    @PutMapping("{id}/turn")
    public ResponseEntity<?> updateFlag(@PathVariable("id") Long examPlanId){
        return examPlanService.updateFlag(examPlanId);
    }

    @DeleteMapping("cc")
    public ResponseEntity<?> deleteCCInExamPlan(@RequestParam(name = "id") Long classCreditId){
        return examPlanService.deleteCCInExamPlan(classCreditId);
    }

    @PostMapping("build")
    public ResponseEntity<?> buildExamScheduling(@RequestBody BuildRequest request){
        return examService.buildExamSchedulingForExamPlan(request.getExamPlanId());
    }
}
