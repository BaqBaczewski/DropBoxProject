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
        } else if (duration.toMinutes() < 2) {
            return "just now";
        } else if (duration.toDays() < 2) {
            return duration.toMinutes() + " minutes ago";
        } else {
            return duration.toDays() + " days ago";
        }
    }

    public String relative(Instant date) {
        return relative(date, Instant.now());
    }
}
