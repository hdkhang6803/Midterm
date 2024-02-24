package com.example.midterm.Object;

import java.io.Serializable;
import java.time.Duration;
import java.util.Objects;
import java.time.LocalTime;

public class TimePeriod implements Serializable {

    private LocalTime startTime;
    private LocalTime endTime;

    public TimePeriod(LocalTime startTime, LocalTime endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public boolean overlaps(TimePeriod other) {
        if (this.startTime.isBefore(other.endTime) && other.endTime.isBefore(this.endTime))
            return true;
        else if (this.startTime.isBefore(other.startTime) && other.startTime.isBefore(this.endTime)) {
            return true;
        } else if (other.startTime.isBefore(this.startTime) && this.startTime.isBefore(other.endTime )) {
            return true;
        } else if (other.startTime.isBefore(this.endTime) && this.endTime.isBefore(other.endTime )) {
            return true;
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TimePeriod that = (TimePeriod) o;
        return Objects.equals(startTime, that.startTime) && Objects.equals(endTime, that.endTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startTime, endTime);
    }

    public int getDurationInMinutes(){
        Duration duration = Duration.between(startTime, endTime);
        return (int)duration.toMinutes();
    }
}


