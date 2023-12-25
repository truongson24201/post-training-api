package apisystem.posttraining.service;

import apisystem.posttraining.DTO.Graduations.GraAddRequest;
import apisystem.posttraining.DTO.Graduations.GraInternshipPreviewRequest;
import apisystem.posttraining.DTO.Graduations.GraThesisPreviewRequest;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IGraduationService {
    ResponseEntity<?> previewGraduationsThesis(GraThesisPreviewRequest preview);

    ResponseEntity<?> addGraduationThesis(Long facultyId,List<GraAddRequest> request);

    ResponseEntity<?> getGraThesis(Long facultyId, String makeYear);

    ResponseEntity<?> previewGraduationsInternship(GraInternshipPreviewRequest preview);

    ResponseEntity<?> addGraduationInternship(Long facultyId, List<GraAddRequest> request);

    ResponseEntity<?> getGraInternship(Long facultyId, String makeYear);
}
