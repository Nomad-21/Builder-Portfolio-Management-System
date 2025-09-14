package tech.zeta.util;

import tech.zeta.model.Task;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class GanttChartUtil {

    public static void printGanttChart(List<Task> tasks) {
        if (tasks == null || tasks.isEmpty()) {
            System.out.println("No tasks available to display Gantt chart.");
            return;
        }

        // Find overall project start and end
        LocalDate minStart = tasks.stream().map(Task::getStartDate).min(LocalDate::compareTo).orElseThrow();
        LocalDate maxEnd = tasks.stream().map(Task::getEndDate).max(LocalDate::compareTo).orElseThrow();

        long totalDays = ChronoUnit.DAYS.between(minStart, maxEnd) + 1;

        // Choose scale
        String scale;
        if (totalDays <= 60) {
            scale = "DAY";
        } else if (totalDays <= 365) {
            scale = "WEEK";
        } else {
            scale = "MONTH";
        }

        System.out.println("Timeline scale: " + scale);

        // Print header
        System.out.print("Timeline                 : ");
        LocalDate cursor = minStart;
        while (!cursor.isAfter(maxEnd)) {
            switch (scale) {
                case "DAY" -> {
                    System.out.print(String.format("%02d ", cursor.getDayOfMonth()));
                    cursor = cursor.plusDays(1);
                }
                case "WEEK" -> {
                    System.out.print(String.format("W%02d ", cursor.get(java.time.temporal.IsoFields.WEEK_OF_WEEK_BASED_YEAR)));
                    cursor = cursor.plusWeeks(1);
                }
                case "MONTH" -> {
                    System.out.print(cursor.getMonth().toString().substring(0, 3) + " ");
                    cursor = cursor.plusMonths(1);
                }
            }
        }
        System.out.println();

        // Print tasks
        for (Task task : tasks) {
            System.out.printf("%-25s: ", task.getName());

            long startOffset;
            long durationUnits;

            switch (scale) {
                case "DAY" -> {
                    startOffset = ChronoUnit.DAYS.between(minStart, task.getStartDate());
                    durationUnits = ChronoUnit.DAYS.between(task.getStartDate(), task.getEndDate()) + 1;
                }
                case "WEEK" -> {
                    startOffset = ChronoUnit.WEEKS.between(minStart, task.getStartDate());
                    durationUnits = ChronoUnit.WEEKS.between(task.getStartDate(), task.getEndDate()) + 1;
                }
                default -> { // MONTH
                    startOffset = ChronoUnit.MONTHS.between(minStart, task.getStartDate());
                    durationUnits = ChronoUnit.MONTHS.between(task.getStartDate(), task.getEndDate()) + 1;
                }
            }

            // spaces before bar
            for (long i = 0; i < startOffset; i++) {
                System.out.print("    ");
            }

            int progressUnits = (int) Math.round((task.getProgress() / 100.0) * durationUnits);

            for (int i = 0; i < durationUnits; i++) {
                if (i < progressUnits) {
                    System.out.print("\u2588\u2588");
                } else {
                    System.out.print("# ");
                }
            }
            System.out.println();
            System.out.println("\u2588 : Completed, # : Pending");
        }
    }
}
