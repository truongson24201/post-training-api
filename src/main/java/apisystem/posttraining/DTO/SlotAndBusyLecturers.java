package apisystem.posttraining.DTO;

import apisystem.posttraining.entity.Classroom;
import apisystem.posttraining.entity.Lecturer;
import apisystem.posttraining.entity.ShiftSystem;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Data
public class SlotAndBusyLecturers {
    private Map<LocalDate, Map<ShiftSystem, List<Classroom>>> freeSlotsTimetable;
    private Map<LocalDate, Map<ShiftSystem, List<Lecturer>>> freeLecturersTimetable;
    private Map<LocalDate, Map<ShiftSystem, List<Classroom>>> BusySlotsExam;
    private Map<LocalDate, Map<ShiftSystem, List<Lecturer>>> BusyLecturersExam;


    private Map<LocalDate, Map<ShiftSystem, List<Classroom>>> freeClassroom;
    private Map<LocalDate, Map<ShiftSystem, List<Lecturer>>> freeLecturer;

    public SlotAndBusyLecturers(Map<LocalDate, Map<ShiftSystem, List<Classroom>>> freeSlotsTimetable, Map<LocalDate, Map<ShiftSystem, List<Lecturer>>> freeLecturersTimetable, Map<LocalDate, Map<ShiftSystem, List<Classroom>>> busySlotsExam, Map<LocalDate, Map<ShiftSystem, List<Lecturer>>> busyLecturersExam) {
        this.freeSlotsTimetable = freeSlotsTimetable;
        this.freeLecturersTimetable = freeLecturersTimetable;
        BusySlotsExam = busySlotsExam;
        BusyLecturersExam = busyLecturersExam;
    }

    public SlotAndBusyLecturers(Map<LocalDate, Map<ShiftSystem, List<Classroom>>> freeClassroom, Map<LocalDate, Map<ShiftSystem, List<Lecturer>>> freeLecturer) {
        this.freeClassroom = freeClassroom;
        this.freeLecturer = freeLecturer;
    }
}
