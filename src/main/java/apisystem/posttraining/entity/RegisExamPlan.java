//package apisystem.posttraining.entity;
//
//import apisystem.posttraining.entity.enumreration.EExamType;
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import java.io.Serializable;
//
//@Entity
//@Table(name = "regis_exam_plan")
//@Data
//@Builder
//@NoArgsConstructor
//@AllArgsConstructor
////@IdClass(ClassCredit.class)
//public class RegisExamPlan implements Serializable {
//    @Id
//    @Column(name = "class_credit_id")
//    private Long classCreditId;
//
//    @MapsId
//    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "class_credit_id")
//    private ClassCredit classCredit;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "exam_plan_id")
//    private ExamPlan examPlan;
//
//    @Column(name = "number_group")
//    private Integer numberGroup;
//
//    @Enumerated(EnumType.STRING)
//    @Column(name = "exam_type")
//    private EExamType examType;
//
//}
