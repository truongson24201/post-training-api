package apisystem.posttraining.entity.id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ComponentSubjectId implements Serializable {
    private Long subject;
    private Long component;
}
