package apisystem.posttraining.entity;

import apisystem.posttraining.entity.enumreration.EGraduation;
import apisystem.posttraining.entity.id.GraduationID;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

@Getter
@Setter
@Entity
@Table(name = "graduation")
//@IdClass(GraduationID.class)
public class Graduation {
    @Id
    private Long regisId;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.MERGE)
    @JoinColumn(name = "regis_id",referencedColumnName = "id")
    private ClassCreditsStudents creditsStudents;
//    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.MERGE)
//    @JoinColumn(name = "student_id")
//    private Student student;
//
//    @Id
//    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.MERGE)
//    @JoinColumn(name = "class_credit_id")
//    private ClassCredit classCredit;

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.MERGE)
    @JoinColumn(name = "supervisor_id",referencedColumnName = "lecturer_id")
    private Lecturer supervisor;

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.MERGE)
    @JoinColumn(name = "thesis_advisor_id",referencedColumnName = "lecturer_id")
    private Lecturer thesisAdvisor;

    @Column(name = "make_year")
    private String makeYear;

    @Column(name = "result")
    private boolean result;

    @Enumerated(EnumType.STRING)
    @Column(name = "graduation_type")
    private EGraduation graduationType;

    @Nationalized
    @Column(name = "name")
    private String name;

    @Lob
    @Nationalized
    @Column(name = "description")
    private String description;

    public Graduation(ClassCreditsStudents creditsStudents, String makeYear, boolean result, EGraduation graduationType) {
        this.creditsStudents = creditsStudents;
        this.makeYear = makeYear;
        this.result = result;
        this.graduationType = graduationType;
    }

    public Graduation() {
    }
}
