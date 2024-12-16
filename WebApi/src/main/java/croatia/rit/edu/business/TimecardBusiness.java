package croatia.rit.edu.business;

import companydata.Timecard;
import companydata.DataLayer;
import companydata.Employee;

import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;

public class TimecardBusiness {

    private DataLayer dataLayer = null;

    public TimecardBusiness() {
        this.dataLayer = new DataLayer("jp3447");
    }

    public Timecard getTimecard(int timecardId) throws Exception {
        if (timecardId <= 0) {
            throw new IllegalArgumentException("Valid timecard ID must be provided.");
        }
        return dataLayer.getTimecard(timecardId);
    }

    public List<Timecard> getAllTimecards(int empId) throws Exception {
        if (empId <= 0) {
            throw new IllegalArgumentException("Valid employee ID must be provided.");
        }
        if (!doesEmployeeExist(empId)) {
            throw new IllegalArgumentException("Employee ID does not exist.");
        }
        return dataLayer.getAllTimecard(empId);
    }

    public Timecard insertTimecard(Timecard timecard) throws Exception {
        // Validate employee existence
        if (!doesEmployeeExist(timecard.getEmpId())) {
            throw new IllegalArgumentException("Employee ID does not exist.");
        }

        // Times must be between 06:00 and 18:00
        if (timecard.getStartTime().toLocalDateTime().getHour() < 6 || timecard.getEndTime().toLocalDateTime().getHour() >= 18) {
            throw new IllegalArgumentException("Times must be between 06:00 and 18:00");
        }

        // End time must be at least 1 hour greater than start time and on the same day
        if (!timecard.getStartTime().toLocalDateTime().toLocalDate().equals(timecard.getEndTime().toLocalDateTime().toLocalDate()) || timecard.getEndTime().toLocalDateTime().isBefore(timecard.getStartTime().toLocalDateTime().plusHours(1))) {
            throw new IllegalArgumentException("End time must be at least 1 hour greater than start time and on the same day");
        }

        // weekend
        if (!isValidTimeRange(timecard.getStartTime(), timecard.getEndTime())) {
            throw new IllegalArgumentException("Start time and end time must be on a weekday.");
        }

        // Validate start_time is within the last week
        if (!isValidDateRange(timecard.getStartTime())) {
            throw new IllegalArgumentException("Start time must be within the last week.");
        }

        // Validate no conflict with other timecards for the same day
        if (isTimecardConflict(timecard.getEmpId(), timecard.getStartTime())) {
            throw new IllegalArgumentException("A timecard already exists for this employee on the same day.");
        }

        return dataLayer.insertTimecard(timecard);
    }

    public Timecard updateTimecard(Timecard timecard) throws Exception {
        if (timecard.getId() <= 0) {
            throw new IllegalArgumentException("Valid timecard ID must be provided.");
        }

        //check if id exists
        Timecard existingTimecard = getTimecard(timecard.getId());
        if (existingTimecard == null) {
            throw new IllegalArgumentException("Timecard ID does not exist in the database.");
        }

        // Times must be between 06:00 and 18:00
        if (timecard.getStartTime().toLocalDateTime().getHour() < 6 || timecard.getEndTime().toLocalDateTime().getHour() >= 18) {
            throw new IllegalArgumentException("Times must be between 06:00 and 18:00");
        }
        
        // End time must be at least 1 hour greater than start time and on the same day
        if (!timecard.getStartTime().toLocalDateTime().toLocalDate().equals(timecard.getEndTime().toLocalDateTime().toLocalDate()) || timecard.getEndTime().toLocalDateTime().isBefore(timecard.getStartTime().toLocalDateTime().plusHours(1))) {
            throw new IllegalArgumentException("End time must be at least 1 hour greater than start time and on the same day");
        }

        // weekend
        if (!isValidTimeRange(timecard.getStartTime(), timecard.getEndTime())) {
            throw new IllegalArgumentException("Start time and end time must be on a weekday.");
        }

        // Validate start_time is within the last week
        if (!isValidDateRange(timecard.getStartTime())) {
            throw new IllegalArgumentException("Start time must be within the last week.");
        }

        // Validate no conflict with other timecards for the same day
        if (isTimecardConflict(timecard.getEmpId(), timecard.getStartTime())) {
            throw new IllegalArgumentException("A timecard already exists for this employee on the same day.");
        }

        return dataLayer.updateTimecard(timecard);
    }

    public int deleteTimecard(int timecardId) throws Exception {
        if (timecardId <= 0) {
            throw new IllegalArgumentException("Valid timecard ID must be provided.");
        }
        return dataLayer.deleteTimecard(timecardId);
    }

    public boolean doesEmployeeExist(int empId) throws Exception {
        Employee employee = dataLayer.getEmployee(empId);
        return employee != null;
    }

    // check if another timecard exists for the employee on the same day
    public boolean isTimecardConflict(int empId, Timestamp startTime) throws Exception {
        List<Timecard> timecards = dataLayer.getAllTimecard(empId);
        for (Timecard timecard : timecards) {
            if (timecard.getStartTime().toLocalDateTime().toLocalDate()
                .equals(startTime.toLocalDateTime().toLocalDate())) {
                return true;
            }
        }
        return false;
    }

    // Validate if start and end times meet the required conditions
    public boolean isValidTimeRange(Timestamp startTime, Timestamp endTime) {
        LocalDateTime start = startTime.toLocalDateTime();
        LocalDateTime end = endTime.toLocalDateTime();

        // Start and end times must fall on weekdays
        DayOfWeek startDay = start.getDayOfWeek();
        DayOfWeek endDay = end.getDayOfWeek();
        return !(startDay == DayOfWeek.SATURDAY || startDay == DayOfWeek.SUNDAY || 
                 endDay == DayOfWeek.SATURDAY || endDay == DayOfWeek.SUNDAY);
    }

    // Validate that start time is within the last week
    public boolean isValidDateRange(Timestamp startTime) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime oneWeekAgo = now.minusWeeks(1);
        LocalDateTime start = startTime.toLocalDateTime();
        return !start.isAfter(now) && !start.isBefore(oneWeekAgo);
    }
}
