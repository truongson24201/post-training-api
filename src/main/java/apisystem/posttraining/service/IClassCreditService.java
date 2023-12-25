package apisystem.posttraining.service;

import org.springframework.http.ResponseEntity;

public interface IClassCreditService {
    ResponseEntity<?> getAllCCToRegis(Long semesterId);

    ResponseEntity<?> getAllCCToEnterPoint(Long semesterId);

    ResponseEntity<?> initScores();

    ResponseEntity<?> getComponentsScore(Long classCreditId);

    ResponseEntity<?> getComponentFinal();

    ResponseEntity<?> updateIsComplete(Long classCreditId);

    boolean checkClassOfLec(Long classCreditId);

    boolean hasEnterCK(Long classCreditId);
}
