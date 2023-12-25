package apisystem.posttraining.repository;

import apisystem.posttraining.entity.TimeTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface TimeTableRepository extends JpaRepository<TimeTable,Long> {
    List<TimeTable> findAllByLessonDate(LocalDate date);

    @Query("SELECT tt FROM TimeTable tt where tt.classCredit.classCreditId = :classCreditId " +
            "AND tt.lessonDate <= :current")
    List<TimeTable> findByClassToCurrentDate(@Param("classCreditId") Long classCreditId,
                                             @Param("current") LocalDate current);

    @Query("SELECT tt FROM TimeTable tt WHERE tt.lessonDate = :datee AND tt.classroom.classroomId = :classroomId")
    Optional<TimeTable> findByDate(@Param("classroomId") Long classroomId,@Param("datee") LocalDate date);


    @Query("SELECT tt FROM TimeTable tt WHERE tt.lessonDate >= :dateStart AND tt.lessonDate <= :dateEnd")
    List<TimeTable> findAllByDateExamPlan(@Param("dateStart") LocalDate dateStart,
                                          @Param("dateEnd") LocalDate dateEnd);

}
