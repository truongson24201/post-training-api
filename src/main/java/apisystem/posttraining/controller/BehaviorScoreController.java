package apisystem.posttraining.controller;

import apisystem.posttraining.DTO.BehaviorPattern.BCriteriaSubAdd;
import apisystem.posttraining.DTO.BehaviorPattern.BCriteriaSubView;
import apisystem.posttraining.DTO.BehaviorPattern.BCriteriaView;
import apisystem.posttraining.DTO.BehaviorPattern.BSContentAdd;
import apisystem.posttraining.DTO.BehaviorScore.BScoreAdd;
import apisystem.posttraining.service.IBSContentService;
import apisystem.posttraining.service.IBehaviorScoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/behaviors")
@RequiredArgsConstructor
public class BehaviorScoreController {
    private final IBehaviorScoreService behaviorScoreService;
    private final IBSContentService bsContentsService;

    @GetMapping("criteria")
    public ResponseEntity<?> getAllBCriteria(){
        return bsContentsService.getAllBCriteria();
    }

    @GetMapping("criteria-detail")
    public ResponseEntity<?> getBCriteriaDetails(@RequestParam(name = "id") Long criteriaId){
        return bsContentsService.getBCriteriaDetails(criteriaId);
    }
    @PreAuthorize("hasAuthority('StudentAffairs')")
    @PostMapping("criteria")
    public ResponseEntity<?> addBCriteria(@RequestBody Object name){
        return bsContentsService.addBCriteria(name);
    }

    @PreAuthorize("hasAuthority('StudentAffairs')")
    @PutMapping("criteria")
    public ResponseEntity<?> editBCriteria(@RequestBody BCriteriaView criteria){
        return bsContentsService.editBCriteria(criteria);
    }

    @PreAuthorize("hasAuthority('StudentAffairs')")
    @DeleteMapping("criteria")
    public ResponseEntity<?> deleteBCriteria(@RequestParam(name = "id") Long criteriaId){
        return bsContentsService.deleteBCriteria(criteriaId);
    }


    @GetMapping("criteria/{id}/subs")
    public ResponseEntity<?> getAllBCriteriaSub(@PathVariable("id") Long bCriteriaId){
        return bsContentsService.getAllBCriteriaSub(bCriteriaId);
    }

    @GetMapping("criteria/sub")
    public ResponseEntity<?> getCriteriaSubDetails(@RequestParam(name = "subId",required = false) Long criteriaSubId){
        return bsContentsService.getCriteriaSubDetails(criteriaSubId);
    }

    @PreAuthorize("hasAuthority('StudentAffairs')")
    @PostMapping("criteria/sub")
    public ResponseEntity<?> addBCriteriaSub(@RequestBody BCriteriaSubAdd bCriteriaSubAdd){
        return bsContentsService.addBCriteriaSub(bCriteriaSubAdd);
    }

    @PreAuthorize("hasAuthority('StudentAffairs')")
    @PutMapping("criteria/sub")
    public ResponseEntity<?> editBCriteriaSub(@RequestBody BCriteriaSubView bCriteriaSub){
        return bsContentsService.editBCriteriaSub(bCriteriaSub);
    }

    @PreAuthorize("hasAuthority('StudentAffairs')")
    @DeleteMapping("criteria/sub")
    public ResponseEntity<?> deleteBCriteriaSub(@RequestParam(name = "id") Long bCriteriaSubId){
        return bsContentsService.deleteBCriteriaSub(bCriteriaSubId);
    }

    @PreAuthorize("hasAuthority('StudentAffairs')")
    @PostMapping("pattern")
    public ResponseEntity<?> addBSPattern(@RequestBody List<BSContentAdd> bSContentAdd){
        return bsContentsService.addBSPattern(bSContentAdd);
    }

    @PreAuthorize("hasAuthority('StudentAffairs')")
    @PutMapping("pattern/{id}")
    public ResponseEntity<?> updateBSPattern(@PathVariable(name = "id") Long bSPatternId){
        return bsContentsService.updateBSPattern(bSPatternId);
    }

    @PreAuthorize("hasAuthority('StudentAffairs')")
    @GetMapping("patterns")
    public ResponseEntity<?> getAllBSPattern(){
        return bsContentsService.getAllBSPattern();
    }


    @GetMapping("pattern-contents")
    public ResponseEntity<?> getAllBSPatternContent(){
        return bsContentsService.getAllBSPatternContent();
    }

    @GetMapping("pattern-contents/details")
    public ResponseEntity<?> getBSPatternContentDetails(@RequestParam(name = "bSPatternId") Long bSPatternId){
        return bsContentsService.getBSPatternContentDetails(bSPatternId);
    }

    @GetMapping("pattern/details")
    public ResponseEntity<?> checkPatternIsBlock(@RequestParam(name = "bSPatternId") Long bSPatternId){
        return behaviorScoreService.checkPatternIsBlock(bSPatternId);
    }

    @PreAuthorize("hasAuthority('StudentAffairs')")
    @PutMapping("pattern-contents/{id}/details")
    public ResponseEntity<?> updateContentDetails(@PathVariable(name = "id") Long bSPatternId,
                                                  @RequestBody List<BSContentAdd> bSContentAdd){
        return bsContentsService.updateContentDetails(bSPatternId,bSContentAdd);
    }

    @PreAuthorize("hasAuthority('StudentAffairs')")
    @DeleteMapping("pattern/details")
    public ResponseEntity<?> deleteContentDetails(@RequestParam(name = "bSPatternId") Long bSPatternId){
        return bsContentsService.deleteContentDetails(bSPatternId);
    }

    @GetMapping("scores")
    public ResponseEntity<?> getAllBehaviorScore(@RequestParam(name = "classId") Long classId,@RequestParam(name = "semesterId") Long semesterId,
                                                 @RequestParam(name = "index", required = false) int index){
        return behaviorScoreService.getAllBehaviorScore(classId,semesterId,index);
    }

    @PutMapping("score/{id}")
    public ResponseEntity<?> addBehaviorScore(@PathVariable("id") Long bSheetId,
                                              @RequestBody List<BScoreAdd> BScoreAdd){
        return behaviorScoreService.addBehaviorScore(bSheetId,BScoreAdd);
    }


}
