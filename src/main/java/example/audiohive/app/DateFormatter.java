package example.audiohive.app;

import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;

@Component("dateFormatter")
public class DateFormatter {

    public String relative(Instant date, Instant now) {
        if (date == null || now == null) {
            return "???";
        }

        Duration duration = Duration.between(date, now);
        if (duration.isNegative()) {
            return "in the future";
        } else if (duration.toSeconds() <= 15) {
            return "just now";
        } else if (duration.toSeconds() < 60) {
            return duration.toSeconds() + " seconds ago";
        } else if (duration.toSeconds() < 90) {
            return duration.toMinutes() + " minute ago";
        } else if (duration.toMinutes() < 60) {
            return duration.toMinutes() + " minutes ago";
        } else if (duration.toMinutes() == 60) {
            return duration.toHours() + " hour ago";
        } else if (duration.toHours() < 2) {
            return duration.toHours() + " hour and " + (duration.toMinutes() % 60) + " minutes ago";
        } else if (duration.toMinutes() == 120) {
            return duration.toHours() + " hours ago";
        } else if (duration.toHours() < 3) {
            return duration.toHours() + " hours and " + (duration.toMinutes() % 60) + " minutes ago";
        } else if (duration.toMinutes() == 180) {
            return duration.toHours() + " hours ago";
        } else if (duration.toMinutes() < 210) {
            return (int) Math.floor(duration.toMinutes() / 60.0) + " hours ago";
        } else if (duration.toMinutes() < 240) {
            return (int) Math.ceil(duration.toMinutes() / 60.0) + " hours ago";
        } else if (duration.toHours() < 24) {
            return duration.toHours() + " hours ago";
        } else if (duration.toHours() == 24) {
            return duration.toDays() + " day ago";
        } else if (duration.toDays() < 28) {
            return (int) Math.ceil(duration.toHours() / 24.0) + " days ago";
        } else if (duration.toDays() <= 30) {
            return (int) Math.ceil(duration.toDays() / 30.0) + " month ago";
        } else if (duration.toDays() < 60) {
            return (int) Math.floor(duration.toDays() / 30.0) + " month ago";
        } else {
            return (duration.toDays() / 30) + " months ago";
        }
    }

    public String relative(Instant date) {
        return relative(date, Instant.now());
    }
}
