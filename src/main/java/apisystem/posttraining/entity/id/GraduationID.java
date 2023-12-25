package apisystem.posttraining.entity.id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GraduationID implements Serializable {
    private String student;
    private Long classCredit;
}
