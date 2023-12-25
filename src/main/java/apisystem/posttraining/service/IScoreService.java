package apisystem.posttraining.service;

import apisystem.posttraining.DTO.StudentsScores.StudentsScoresAdd;
import apisystem.posttraining.DTO.StudentsScores.UpExamStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IScoreService {
    ResponseEntity<?> updateStudentsScores(List<StudentsScoresAdd> studentsScoresAdd);

    ResponseEntity<?> getStudentsScores(Long classCreditId);

    ResponseEntity<?> getStudentScores(String studentId);


    ResponseEntity<?> getStudentsScoresWithExam(Long examId);

    ResponseEntity<?> saveStudentsExamStatus(List<UpExamStatus> upExamStatus);

    ResponseEntity<?> updateAttendanceScores(Long classCreditId);

    ResponseEntity<?> getAttendancePoint(Long classCreditId);

    boolean checkExpiredToEnterPoint(Long semesterId);
}
