package com.sample.jobtracker.model;

import java.time.LocalDateTime;

public class Interview {

    private String round;
    private LocalDateTime scheduledDate;
    private String interviewerName;
    private String interviewType;
    private String notes;
    private InterviewResult result;

    public Interview() {}

    public Interview(String round, LocalDateTime scheduledDate) {
        this.round = round;
        this.scheduledDate = scheduledDate;
        this.result = InterviewResult.PENDING;
    }

    public String getRound() {
        return round;
    }

    public void setRound(String round) {
        this.round = round;
    }

    public LocalDateTime getScheduledDate() {
        return scheduledDate;
    }

    public void setScheduledDate(LocalDateTime scheduledDate) {
        this.scheduledDate = scheduledDate;
    }

    public String getInterviewerName() {
        return interviewerName;
    }

    public void setInterviewerName(String interviewerName) {
        this.interviewerName = interviewerName;
    }

    public String getInterviewType() {
        return interviewType;
    }

    public void setInterviewType(String interviewType) {
        this.interviewType = interviewType;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public InterviewResult getResult() {
        return result;
    }

    public void setResult(InterviewResult result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "Interview{" +
                "round='" + round + '\'' +
                ", scheduledDate=" + scheduledDate +
                ", result=" + result +
                '}';
    }
}