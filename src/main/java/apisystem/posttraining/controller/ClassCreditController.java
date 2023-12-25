package apisystem.posttraining.controller;

import apisystem.posttraining.service.IClassCreditService;
import apisystem.posttraining.service.IScoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/class-credit")
@RequiredArgsConstructor
public class ClassCreditController {
    private final IClassCreditService classCreditService;
    private final IScoreService scoreService;

    @GetMapping("regis")
    public ResponseEntity<?> getAllCCToRegis(@RequestParam(name = "semesterId",required = false) Long semesterId){
        return classCreditService.getAllCCToRegis(semesterId);
    }

    @GetMapping("regis/check")
    public boolean checkClassOfLec(@RequestParam(name = "classCreditId",required = false) Long classCreditId){
        return classCreditService.checkClassOfLec(classCreditId);
    }

    @GetMapping("")
    public ResponseEntity<?> getAllCCToEnterPoint(@RequestParam(name = "semesterId",required = false) Long semesterId){
        return classCreditService.getAllCCToEnterPoint(semesterId);
    }

    @PostMapping("init-scores")
    public ResponseEntity<?> initScores(){
        return classCreditService.initScores();
    }

    @GetMapping("components")
    public ResponseEntity<?> getComponentsScore(@RequestParam(name = "classCreditId",required = false) Long classCreditId){
        return classCreditService.getComponentsScore(classCreditId);
    }

    @GetMapping("component")
    public ResponseEntity<?> getComponentFinal(){
        return classCreditService.getComponentFinal();
    }

    @PutMapping("{id}/complete")
    public ResponseEntity<?> updateIsComplete(@PathVariable("id") Long classCreditId){
        return classCreditService.updateIsComplete(classCreditId);
    }
    
    @GetMapping("attendance")
    public ResponseEntity<?> getAttendancePoint(@RequestParam(name = "id") Long classCreditId){
        return scoreService.getAttendancePoint(classCreditId);
    }

    @GetMapping("enter-ck")
    public boolean hasEnterCK(@RequestParam(name = "classCreditId") Long classCreditId){
        return classCreditService.hasEnterCK(classCreditId);
    }

}
