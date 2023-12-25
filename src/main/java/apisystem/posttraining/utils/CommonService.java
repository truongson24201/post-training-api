package apisystem.posttraining.utils;

import apisystem.posttraining.DTO.Reward.GpaReward;
import apisystem.posttraining.DTO.StudentsScores.ComPointView;
import apisystem.posttraining.DTO.StudentsScores.GPA;
import apisystem.posttraining.DTO.StudentsScores.Student.SubjectPointView;
import apisystem.posttraining.entity.StudentsScores;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommonService {

//    private static final GPA[] gradeTable = {
//            new GPA(9.0, 10.0, "A+", true),
//            new GPA(8.5, 8.9, "A", true),
//            new GPA(8.0, 8.4, "B+", true),
//            new GPA(7.0, 7.9, "B", true),
//            new GPA(6.5, 6.9, "C+", true),
//            new GPA(5.5, 6.4, "C", true),
//            new GPA(5.0, 5.4, "D+", true),
//            new GPA(4.0, 4.9, "D", true)
//    };

    public static void calRewardRate(GpaReward gpaReward){
        Map<Integer, String> rewards = Map.of(
                1, "Xuất sắc",
                2, "Giỏi",
                3, "Khá"
        );
        if (gpaReward.getTotalFourPoint() >= 3.6 && gpaReward.getTotalBehaviorPoint() >= 90)
            gpaReward.setContent(rewards.get(1));
        if(gpaReward.getTotalFourPoint() >= 3.2 && gpaReward.getTotalBehaviorPoint() < 90)
            gpaReward.setContent(rewards.get(2));
        else gpaReward.setContent(rewards.get(3));

    }



    public static void convertTenToFour(GPA gpa){
        if (gpa.getTotalTenPoint() >= 9.00 && gpa.getTotalTenPoint() <= 10.0){
            gpa.setTotalFourPoint(4.0);
            gpa.setLetterPoint("A+");
            gpa.setResult(true);
        }else if (gpa.getTotalTenPoint() >= 8.50 && gpa.getTotalTenPoint() < 9.00){
            gpa.setTotalFourPoint(3.7);
            gpa.setLetterPoint("A");
            gpa.setResult(true);
        }else if (gpa.getTotalTenPoint() >= 8.00 && gpa.getTotalTenPoint() < 8.50){
            gpa.setTotalFourPoint(3.5);
            gpa.setLetterPoint("B+");
            gpa.setResult(true);
        }else if (gpa.getTotalTenPoint() >= 7.00 && gpa.getTotalTenPoint() < 8.00){
            gpa.setTotalFourPoint(3.0);
            gpa.setLetterPoint("B");
            gpa.setResult(true);
        }else if (gpa.getTotalTenPoint() >= 6.50 && gpa.getTotalTenPoint() < 7.00){
            gpa.setTotalFourPoint(2.5);
            gpa.setLetterPoint("C+");
            gpa.setResult(true);
        }else if (gpa.getTotalTenPoint() >= 5.50 && gpa.getTotalTenPoint() < 6.50){
            gpa.setTotalFourPoint(2.0);
            gpa.setLetterPoint("C");
            gpa.setResult(true);
        }else if (gpa.getTotalTenPoint() >= 5.00 && gpa.getTotalTenPoint() < 5.50){
            gpa.setTotalFourPoint(1.5);
            gpa.setLetterPoint("D+");
            gpa.setResult(true);
        }else if (gpa.getTotalTenPoint() >= 4.00 && gpa.getTotalTenPoint() < 5.00){
            gpa.setTotalFourPoint(1.0);
            gpa.setLetterPoint("D");
            gpa.setResult(true);
        }else {
            gpa.setTotalFourPoint(0.0);
            gpa.setLetterPoint("F");
            gpa.setResult(false);
        }

    }

    public static double convertTenToFour(Double tenPoint){
        if (tenPoint >= 9.00){
            return 4.0;
        } else if (tenPoint  >= 8.50 && tenPoint  < 9.00){
            return 3.7;
        } else if (tenPoint  >= 8.00 && tenPoint  < 8.50){
            return 3.5;
        } else if (tenPoint  >= 7.00 && tenPoint  < 8.00){
            return 3.0;
        } else if (tenPoint  >= 6.50 && tenPoint  < 7.00){
            return 2.5;
        } else if (tenPoint  >= 5.50 && tenPoint  < 6.50){
            return 2.0;
        } else if (tenPoint  >= 5.00 && tenPoint  < 5.50){
            return 1.5;
        } else if (tenPoint  >= 4.00 && tenPoint  < 5.00){
            return 1.0;
        } else {
            return 0.0;
        }
    }

    public static Double calTenPointOneSubject(List<ComPointView> comPointViews){
        return comPointViews.stream()
                .mapToDouble(comPointView -> comPointView.getPointNumber() * (comPointView.getComponentSubject().getPercentNumber())/100)
                .sum();
    }

    public static Double calTenPointOneSubject1(List<StudentsScores> comPointViews){
        return comPointViews.stream()
                .mapToDouble(comPointView -> comPointView.getPointNumber() * (comPointView.getComponentSubject().getPercentNumber())/100)
                .sum();
    }

    public static Double calSemesterAverageTen(List<GPA> gpas){
        return gpas.stream()
                .mapToDouble(GPA::getTotalTenPoint)
                .average()
                .orElse(0.0);
    }

    public static Double calSemesterAverageFour(List<GPA> gpas){
        return gpas.stream()
                .mapToDouble(GPA::getTotalFourPoint)
                .average()
                .orElse(0.0);

    }

    public static Integer calSemesterCreditNum(List<SubjectPointView> subjectPoint){
        return subjectPoint.stream()
                .filter(subjectPointView -> subjectPointView.getGpa() != null && subjectPointView.getGpa().isResult())
                .mapToInt(subjectPointView -> subjectPointView.getSubject().getCreditNum())
                .sum();

    }

    public static String trimString(String s){
        return s.trim().replaceAll("\\s+", " ");
    }

}
