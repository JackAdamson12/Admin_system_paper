package org.example.playTimeManager.sourse;

public record TimeRecorder(int hours, int minutes, int seconds)
{
    @Override
    public String toString()
    {
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    public String toHoursString()
    {
        return String.format("%02d", hours);
    }

    public String toMinutesString()
    {
        return String.format("%02d", minutes);
    }

    public String toSecondsString()
    {
        return String.format("%02d", seconds);
    }

    public static TimeRecorder fromString(String time)
    {
        String[] parts = time.split(":");

        int hours = Integer.parseInt(parts[0]);
        int minutes = Integer.parseInt(parts[1]);
        int seconds = Integer.parseInt(parts[2]);

        return new TimeRecorder(hours, minutes, seconds);
    }
}
