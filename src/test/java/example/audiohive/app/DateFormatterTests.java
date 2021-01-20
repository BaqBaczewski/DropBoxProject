package example.audiohive.app;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class DateFormatterTests {

    @Test
    public void testJustNow() {
        DateFormatter dateFormatter = new DateFormatter();
        Instant now = Instant.now();
        Instant date = now.minus(10, ChronoUnit.SECONDS);
        assertEquals("just now", dateFormatter.relative(date, now));
    }

    @Test
    public void testSecondsAgo() {
        DateFormatter dateFormatter = new DateFormatter();
        Instant now = Instant.now();
        Instant date = now.minus(20, ChronoUnit.SECONDS);
        assertEquals("20 seconds ago", dateFormatter.relative(date, now));
    }

    @Test
    public void test1MinuteAgo() {
        DateFormatter dateFormatter = new DateFormatter();
        Instant now = Instant.now();
        Instant date = now.minus(75, ChronoUnit.SECONDS);
        assertEquals("1 minute ago", dateFormatter.relative(date, now));
    }

    @Test
    public void test2MinutesAgo() {
        DateFormatter dateFormatter = new DateFormatter();
        Instant now = Instant.now();
        Instant date = now.minus(135, ChronoUnit.SECONDS);
        assertEquals("2 minutes ago", dateFormatter.relative(date, now));
    }

    @Test
    public void test30MinutesAgo() {
        DateFormatter dateFormatter = new DateFormatter();
        Instant now = Instant.now();
        Instant date = now.minus(30, ChronoUnit.MINUTES);
        assertEquals("30 minutes ago", dateFormatter.relative(date, now));
    }

    @Test
    public void test70MinutesAgo() {
        DateFormatter dateFormatter = new DateFormatter();
        Instant now = Instant.now();
        Instant date = now.minus(4210, ChronoUnit.SECONDS);
        assertEquals("1 hour and 10 minutes ago", dateFormatter.relative(date, now));
    }

    @Test
    public void test2HoursAgo() {
        DateFormatter dateFormatter = new DateFormatter();
        Instant now = Instant.now();
        Instant date = now.minus(140, ChronoUnit.MINUTES);
        assertEquals("2 hours and 20 minutes ago", dateFormatter.relative(date, now));
    }

    @Test
    public void test3HoursAgo() {
        DateFormatter dateFormatter = new DateFormatter();
        Instant now = Instant.now();
        Instant date = now.minus(195, ChronoUnit.MINUTES);
        assertEquals("3 hours ago", dateFormatter.relative(date, now));
    }

    @Test
    public void test4HoursAgo() {
        DateFormatter dateFormatter = new DateFormatter();
        Instant now = Instant.now();
        Instant date = now.minus(230, ChronoUnit.MINUTES);
        assertEquals("4 hours ago", dateFormatter.relative(date, now));
    }

    @Test
    public void test18HoursAgo() {
        DateFormatter dateFormatter = new DateFormatter();
        Instant now = Instant.now();
        Instant date = now.minus(18, ChronoUnit.HOURS);
        assertEquals("18 hours ago", dateFormatter.relative(date, now));
    }

    @Test
    public void test1DayAgo() {
        DateFormatter dateFormatter = new DateFormatter();
        Instant now = Instant.now();
        Instant date = now.minus(24, ChronoUnit.HOURS);
        assertEquals("1 day ago", dateFormatter.relative(date, now));
    }

    @Test
    public void test3DaysAgo() {
        DateFormatter dateFormatter = new DateFormatter();
        Instant now = Instant.now();
        Instant date = now.minus(61, ChronoUnit.HOURS);
        assertEquals("3 days ago", dateFormatter.relative(date, now));
    }

    @Test
    public void test20DaysAgo() {
        DateFormatter dateFormatter = new DateFormatter();
        Instant now = Instant.now();
        Instant date = now.minus(20, ChronoUnit.DAYS);
        assertEquals("20 days ago", dateFormatter.relative(date, now));
    }

    @Test
    public void test1MonthAgo() {
        DateFormatter dateFormatter = new DateFormatter();
        Instant now = Instant.now();
        Instant date = now.minus(28, ChronoUnit.DAYS);
        assertEquals("1 month ago", dateFormatter.relative(date, now));
    }

    @Test
    public void testMonthsAgoDown() {
        DateFormatter dateFormatter = new DateFormatter();
        Instant now = Instant.now();
        Instant date = now.minus(40, ChronoUnit.DAYS);
        assertEquals("1 month ago", dateFormatter.relative(date, now));
    }

    @Test
    public void testMonthsAgoUp() {
        DateFormatter dateFormatter = new DateFormatter();
        Instant now = Instant.now();
        Instant date = now.minus(80, ChronoUnit.DAYS);
        assertEquals("2 months ago", dateFormatter.relative(date, now));
    }
}