import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ReminderCalendar {
    public static void main(String[] args){
        Calendar calendar = new Calendar();
        try {
            calendar.readFile();
            calendar.updateEvent("New");
            calendar.save();
        } catch(Exception e){
            System.out.println("Exception caused: " + e);
        }
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("EEEE MM/dd/yyyy");
        System.out.println(dateFormat.format(date));
        System.out.print(calendar);

    }

    private static int strMonthToInt(String month){
        switch(month.toLowerCase()){
            case "january":
                return 1;
            case "february":
                return 2;
            case "march":
                return 3;
            case "december":
                return 12;
            case "april":
                return 4;
            case "may":
                return 5;
            case "june":
                return 6;
            case "july":
                return 7;
            case "august":
                return 8;
            case "september":
                return 9;
            case "october":
                return 10;
            case "november":
                return 11;
                default:
                    return -1;

        }
    }
}
