package apisystem.posttraining.utils;

import apisystem.posttraining.entity.Classroom;
import apisystem.posttraining.entity.ShiftSystem;
import lombok.Data;

import java.time.LocalDate;
import java.util.Objects;

@Data
public class ScheduleInfo {
    private LocalDate date;
    private ShiftSystem shiftSystem;
    private Classroom classroom;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ScheduleInfo that = (ScheduleInfo) o;
        return date.equals(that.date) && shiftSystem.equals(that.shiftSystem) && classroom.equals(that.classroom);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, shiftSystem, classroom);
    }

    public ScheduleInfo(LocalDate date, ShiftSystem shiftSystem, Classroom classroom) {
        this.date = date;
        this.shiftSystem = shiftSystem;
        this.classroom = classroom;
    }

    public ScheduleInfo() {
    }
}
