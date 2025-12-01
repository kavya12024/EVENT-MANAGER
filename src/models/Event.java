package models;

import java.time.LocalDate;

public class Event {
    private int eventId;
    private String eventName;
    private LocalDate eventDate;
    private String eventDescription;
    private String eventLocation;
    private double eventFees;
    private int organiserId;
    private int maxParticipants;

    public Event() {}

    public Event(int eventId, String eventName, LocalDate eventDate, String eventDescription, String eventLocation, double eventFees, int organiserId, int maxParticipants) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.eventDescription = eventDescription;
        this.eventLocation = eventLocation;
        this.eventFees = eventFees;
        this.organiserId = organiserId;
        this.maxParticipants = maxParticipants;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public LocalDate getEventDate() {
        return eventDate;
    }

    public void setEventDate(LocalDate eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public String getEventLocation() {
        return eventLocation;
    }

    public void setEventLocation(String eventLocation) {
        this.eventLocation = eventLocation;
    }

    public double getEventFees() {
        return eventFees;
    }

    public void setEventFees(double eventFees) {
        this.eventFees = eventFees;
    }

    public int getOrganiserId() {
        return organiserId;
    }

    public void setOrganiserId(int organiserId) {
        this.organiserId = organiserId;
    }

    public int getMaxParticipants() {
        return maxParticipants;
    }

    public void setMaxParticipants(int maxParticipants) {
        this.maxParticipants = maxParticipants;
    }

    @Override
    public String toString() {
        return eventName;
    }
}
