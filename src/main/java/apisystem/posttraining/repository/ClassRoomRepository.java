package apisystem.posttraining.repository;

import apisystem.posttraining.entity.Classroom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ClassRoomRepository extends JpaRepository<Classroom, Long> {

    @Query("SELECT c FROM Classroom c WHERE c.classroomId NOT IN " +
            "(SELECT tt.classroom.classroomId FROM TimeTable tt JOIN tt.shiftSystems ts " +
            "WHERE tt.lessonDate >= :dateStart AND tt.lessonDate <= :dateEnd " +
            "AND ts.shiftSystemId = 5 AND ts.shiftSystemId = 6) ")
    List<Classroom> findAllByFreeForExam(@Param("dateStart") LocalDate dateStart,
                                         @Param("dateEnd") LocalDate dateEnd);

    @Query(value = "SELECT cr FROM Classroom cr WHERE cr.roomType = :roomtype order by cr.capacity desc")
    List<Classroom> findAllByRoomtype(@Param("roomtype") String roomtype);

//    @Query("SELECT c FROM Classroom c " +
//            "WHERE c.roomType = :roomType AND c.maxSize = (SELECT MAX(c2.maxSize) FROM Classroom c2 " +
//            "                    WHERE c2.roomType = :roomType AND c2 NOT IN (" +
//            "                        SELECT DISTINCT t.classroom FROM TimeTable t JOIN t.shiftSystems s " +
//            "                        WHERE (t.lessonDate = :datee) " +
//            "                            AND s.timeStart <= :timee AND s.timeClose >= :timee )" +
//            "                )")
//        Optional<Classroom> findLargestAvailableClassroom(
//                @Param("roomType") String roomType,
//                @Param("datee") LocalDate datee,
//                @Param("timee") LocalTime timee);

//    @Query("SELECT c FROM Classroom c " +
//            "WHERE c.roomType IN :roomType " +
//            "AND c.classroomId NOT IN " +
//            "(SELECT DISTINCT t.classroom.classroomId FROM TimeTable t JOIN t.shiftSystems s " +
//            "WHERE (t.lessonDate = :datee) " +
//            "AND s.timeStart <= :timee AND s.timeClose >= :timee) order by c.capacity desc ")
//    List<Classroom> findAllAvailableClassroom(
//            @Param("roomType") String[] roomType,
//            @Param("datee") LocalDate datee,
//            @Param("timee") LocalTime timee);

    // old
    @Query("SELECT c FROM Classroom c " +
            "WHERE c.roomType IN :roomType " +
            "AND NOT EXISTS " +
            "(SELECT 1 FROM TimeTable t JOIN t.shiftSystems s " +
            "WHERE t.classroom = c " +
            "AND t.lessonDate = :datee " +
            "AND s.timeStart <= :timee AND s.timeClose >= :timee) order by c.capacity desc")
    List<Classroom> findAllAvailableClassroom(
            @Param("roomType") String[] roomType,
            @Param("datee") LocalDate datee,
            @Param("timee") LocalTime timee);

    @Query("SELECT DISTINCT c FROM Classroom c " +
            "JOIN c.exams e " +
            "WHERE c.roomType IN :roomType " +
            "AND e.examDate = :datee " +
            "AND (e.shiftSystem.timeStart <= :timee AND e.shiftSystem.timeClose >= :timee)")
    List<Classroom> findAllBusyClassroomExam(
            @Param("roomType") String[] roomType,
            @Param("datee") LocalDate datee,
            @Param("timee") LocalTime timee);

    @Query("SELECT c FROM Classroom c " +
            "WHERE c.roomType IN :roomType " +
            "AND NOT EXISTS " +
            "(SELECT 1 FROM TimeTable t JOIN t.shiftSystems s " +
            "WHERE (t.classroom = c OR t.classroom is null) " +
            "AND t.lessonDate = :datee " +
            "AND s.timeStart <= :timee AND s.timeClose >= :timee) " +
            "AND NOT EXISTS " +
            "(SELECT 1 FROM Exam e JOIN e.shiftSystem es " +
            "WHERE (e.classroom = c OR e.classroom is null) " +
            "AND e.examDate = :datee " +
            "AND es.timeStart <= :timee AND es.timeClose >= :timee) " +
            "order by c.capacity desc")
    List<Classroom> findAllAvailableClassroomWithExams(
            @Param("roomType") String[] roomType,
            @Param("datee") LocalDate datee,
            @Param("timee") LocalTime timee);



}
