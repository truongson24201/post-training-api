package apisystem.posttraining.controller.students;

import apisystem.posttraining.service.IBehaviorScoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/student-behaviors/")
@RequiredArgsConstructor
public class BehaviorScores {
    private final IBehaviorScoreService behaviorScoreService;

    // cua student
    @GetMapping("score")
    public ResponseEntity<?> viewBSheet(@RequestParam(name = "semesterId") Long semesterId,
                                        @RequestParam(name = "username") String username){
        return behaviorScoreService.viewBSheet(semesterId,username);
    }
}
