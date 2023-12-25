package apisystem.posttraining.entity.id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RewardDisSemesterID implements Serializable {
    private Long reward;
    private Long curriculum;
    private String student;
}
