//package apisystem.posttraining.entity;
//
//import jakarta.persistence.*;
//import lombok.Getter;
//import lombok.Setter;
//
//@Entity
//@Table(name = "graduation_thesis")
//@Getter
//@Setter
//public class GraduationThesis {
//    @Id
//    private String studentId;
//
//    @MapsId
//    @OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.MERGE)
//    @JoinColumn(name = "student_id",referencedColumnName = "student_id")
//    private Student student;
//
//    @Column(name = "gpa_found")
//    private Double gpaFound;
//
//    @Column(name = "credit_num")
//    private Integer creditNum;
//
//    @Column(name = "point_d_num")
//    private Integer pointDNum;
//
//    @Column(name = "result")
//    private Boolean result;
//
//    @Column(name = "make_year")
//    private String makeYear;
//
//}
