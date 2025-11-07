package me.olios.hardcoremode.Librrary;

public class ConvertTime {

    public static String min(Double time)
    {
        return (int) Math.floor(time) + "min";
    }

    public static String sec(Double time)
    {
        time = time - (int) Math.floor(time);
        if (time != 0) return (int) Math.floor(time * 60) + "sec";
        return "";
    }

    public static int secTime(Double time)
    {
        time = time - (int) Math.floor(time);
        if (time != 0) return (int) Math.floor(time * 60);
        return 0;
    }

    /**
     *
     * @param time
     * @return
     */
    public static TimeFormat convertTime(Double time)
    {
        TimeFormat timeFormat = new TimeFormat();

        int min = 0;
        double sec = 0;

        String[] splitTime = time.toString().split("\\.");

        if (splitTime.length >= 2)
        {
            min = Integer.parseInt(splitTime[0]);
            sec = Double.parseDouble(splitTime[1]);

            for (int i = 0; i < splitTime[1].length(); i++)
            {
                sec = sec / 10.0;
            }
        }
        else min = Integer.parseInt(splitTime[0]);

        // Hours
        if (min > 60)
        {
            timeFormat.unit = 2;
            String timeString = String.valueOf(Math.floor(min / 60));
            timeString = timeString.split("\\.")[0];

            timeFormat.time = Integer.valueOf(timeString) + "h";
            return timeFormat;
        }

        // Minutes with seconds
        if (sec > 0)
        {
            timeFormat.unit = 0;

            String minutes = String.valueOf(min);
            minutes = minutes.split("\\.")[0];

            String seconds = String.valueOf(Math.floor(sec * 60));
            seconds = seconds.split("\\.")[0];

            timeFormat.time = minutes + "min " + seconds + "sec";
        }
        // Minutes
        else
        {
            timeFormat.unit = 1;
            String timeString = String.valueOf(time);
            timeString = timeString.split("\\.")[0];

            timeFormat.time = timeString + "min";
        }
        return timeFormat;
    }

    public static class TimeFormat
    {
        // 0 - minutes
        // 1 - hours
        public int unit = 0;
        public String time = "";
    }
}
