package apisystem.posttraining.DTO.ProfileDTO;

import apisystem.posttraining.entity.MatriculateStudent;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Data;
import org.hibernate.annotations.Nationalized;

import java.time.LocalDate;
import java.util.Date;

@Data
public class ProfileDTO {
    private Long profileId;
    private String fullname;
    @JsonFormat(pattern = "dd/MM/yyyy")
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate DOB;
    private Boolean gender;
    private String email;
    private String phone;
    private String address;
    private String qualification;
}
