package apisystem.posttraining.controller;

import apisystem.posttraining.DTO.Graduations.GraInternshipPreviewRequest;
import apisystem.posttraining.DTO.Graduations.GraAddRequest;
import apisystem.posttraining.DTO.Graduations.GraThesisPreviewRequest;
import apisystem.posttraining.service.IGraduationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/graduations")
@RequiredArgsConstructor
public class GraduationController {

    private final IGraduationService graduationService;

    @PostMapping("thesis/preview")
    public ResponseEntity<?> previewGraduationsThesis(@RequestBody GraThesisPreviewRequest preview){
        return graduationService.previewGraduationsThesis(preview);
    }

    @PostMapping("thesis/{id}")
    public ResponseEntity<?> addGraduationThesis(@PathVariable("id") Long facultyId,@RequestBody List<GraAddRequest> request){
        return graduationService.addGraduationThesis(facultyId,request);
    }

    @GetMapping("thesis")
    public ResponseEntity<?> getGraThesis(@RequestParam(name = "facultyId") Long facultyId,
                                          @RequestParam(name = "makeYear") String makeYear){
        return graduationService.getGraThesis(facultyId,makeYear);
    }

    @PostMapping("internship/preview")
    public ResponseEntity<?> previewGraduationsInternship(@RequestBody GraInternshipPreviewRequest preview){
        return graduationService.previewGraduationsInternship(preview);
    }

    @PostMapping("internship/{id}")
    public ResponseEntity<?> addGraduationInternship(@PathVariable("id") Long facultyId,@RequestBody List<GraAddRequest> request){
        return graduationService.addGraduationInternship(facultyId,request);
    }

    @GetMapping("internship")
    public ResponseEntity<?> getGraInternship(@RequestParam(name = "facultyId") Long facultyId,
                                          @RequestParam(name = "makeYear") String makeYear){
        return graduationService.getGraInternship(facultyId,makeYear);
    }
}
