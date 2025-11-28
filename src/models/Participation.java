package models;

import java.time.LocalDateTime;

public class Participation {
    private int participationId;
    private int participantId;
    private int eventId;
    private int score;
    private LocalDateTime registrationTime;

    public Participation() {}

    public Participation(int participationId, int participantId, int eventId, int score, LocalDateTime registrationTime) {
        this.participationId = participationId;
        this.participantId = participantId;
        this.eventId = eventId;
        this.score = score;
        this.registrationTime = registrationTime;
    }

    public int getParticipationId() {
        return participationId;
    }

    public void setParticipationId(int participationId) {
        this.participationId = participationId;
    }

    public int getParticipantId() {
        return participantId;
    }

    public void setParticipantId(int participantId) {
        this.participantId = participantId;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public LocalDateTime getRegistrationTime() {
        return registrationTime;
    }

    public void setRegistrationTime(LocalDateTime registrationTime) {
        this.registrationTime = registrationTime;
    }
}
