package apisystem.posttraining.DTO.BehaviorScore;

import apisystem.posttraining.DTO.BehaviorSheetDTO.BehaviorSheetDTO;
import apisystem.posttraining.DTO.StudentDTO.StudentDTO;
import apisystem.posttraining.DTO.StudentDTO.StudentShort;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class BehaviorScoreView {
    @JsonProperty("bSheetId")
    private Long bSheetId;
    private StudentShort student;
//    private List<BehaviorScoreDTO> behaviorScores;
    @JsonProperty("bScoreContent")
    private List<BScoreView> bScoreContent;

//    public BehaviorScoreView(BehaviorSheetDTO behaviorSheet, List<BehaviorScoreDTO> behaviorScore) {
//        this.behaviorSheet = behaviorSheet;
//        this.behaviorScore = behaviorScore;
//    }
//
//    public BehaviorScoreView() {
//    }
}
