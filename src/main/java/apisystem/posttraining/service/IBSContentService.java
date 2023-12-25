package apisystem.posttraining.service;

import apisystem.posttraining.DTO.BehaviorPattern.BCriteriaSubAdd;
import apisystem.posttraining.DTO.BehaviorPattern.BCriteriaSubView;
import apisystem.posttraining.DTO.BehaviorPattern.BCriteriaView;
import apisystem.posttraining.DTO.BehaviorPattern.BSContentAdd;
import apisystem.posttraining.entity.BCriteria;
import apisystem.posttraining.entity.BCriteriaSub;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IBSContentService {
    ResponseEntity<?> addBCriteria(Object name);

    ResponseEntity<?> addBCriteriaSub(BCriteriaSubAdd bCriteriaSubAdd);

    ResponseEntity<?> getAllBCriteria();

    ResponseEntity<?> getAllBCriteriaSub(Long bCriteriaId);

    ResponseEntity<?> addBSPattern(List<BSContentAdd> bSContentAdd);

    ResponseEntity<?> getAllBSPattern();

    ResponseEntity<?> updateBSPattern(Long bSPatternId);

    ResponseEntity<?> getAllBSPatternContent();

    ResponseEntity<?> getBCriteriaDetails(Long criteriaId);

    ResponseEntity<?> getCriteriaSubDetails(Long criteriaSubId);

    ResponseEntity<?> editBCriteria(BCriteriaView criteria);

    ResponseEntity<?> deleteBCriteria(Long criteriaId);

    ResponseEntity<?> editBCriteriaSub(BCriteriaSubView bCriteriaSub);

    ResponseEntity<?> deleteBCriteriaSub(Long bCriteriaSubId);

    ResponseEntity<?> getBSPatternContentDetails(Long bSPatternId);

    ResponseEntity<?> updateContentDetails(Long bSPatternId, List<BSContentAdd> bSContentAdd);

    ResponseEntity<?> deleteContentDetails(Long bSPatternId);

}
