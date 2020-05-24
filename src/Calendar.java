import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/*
Calendar class handles reading in events from the events.cal file
Events are stored in the format mmddyy"Event Name""Event Desc"/mmddyy...
Read file, convert file into a data structure of events
Can then add events into the data structure, write the data structure to file when
completed.
 */

public class Calendar {
    private List<List> events = new ArrayList<>();
    int[] daysPerMonth = {31,28,31,30,31,30,31,31,30,31,30,31};
    Calendar(){

    }

    //method that reads in events from the cal.dat storage file
    void readFile() throws IOException {
        //The filepath for the calendar data should be the current directory + cal.data
        String filePath = System.getProperty("user.dir") + "/cal.dat";
        File calData = new File(filePath);
        //checks if the file exists
        if(!calData.createNewFile()) {
            //if the file does exist, read in all of the lines
            List<String> lines = Files.readAllLines(calData.toPath());
            for (String line : lines) {
                List<Event> lineEvents = new ArrayList<>();
                //keep track of the first index of each event
                int startIdx = 0;
                int endIdx;
                //while there are still more events
                while(line.substring(startIdx).contains("/")){
                    endIdx = line.substring(startIdx).indexOf("/") + startIdx;
                    lineEvents.add(new Event(line.substring(startIdx,endIdx)));
                    //update the starting index to be the end of the previous index
                    startIdx = endIdx + 1;
                }
                events.add(lineEvents);
            }
            //if no data exists yet, create a blank 13 line file
        } else {
            System.out.println("No calendar data found, creating new data");
            PrintWriter printWriter = new PrintWriter(filePath);
            for(int i = 0; i < 13; i++){
                events.add(new ArrayList<Event>());
                printWriter.println(" ");
            }
            printWriter.close();
        }

    }

    int addEvent(Event event) {
        int eventMonth = event.month;
        List<Event> eventsForMonth = events.get(eventMonth);
        int i;
        for(i = 0; i < eventsForMonth.size(); i++){
            if(event.day < eventsForMonth.get(i).day){
                break;
            }
        }
        events.get(eventMonth).add(i, event);
        try {
            this.save();
        }catch (Exception e){
            System.out.println("An exception occurred while writing data");
        }
        return 0;
    }

    @Override
    public String toString(){
        StringBuilder out = new StringBuilder();
        for(List<Event> month : events){
            for(Event event: month){
                out.append(event.toString());
            }
        }
        return out.toString();
    }

    //TODO: remove an event from the calendar
    int deleteEvent(String event) {
        return 0;
    }

    //Method that saves the list of events into the cal.dat file
    void save() throws IOException {
        //sets up the file path
        File calData = new File(System.getProperty("user.dir") + "/cal.dat");
        //just to make sure the file exists (in case it got deleted while program was
        //running or something)
        calData.createNewFile();
        //creates a file printer
        FileWriter printer = new FileWriter(calData, false);
        //iterate through all the different months in the event list
        for(List<Event> eventMonth : events){
            //if there are no events in a month, print a blank line to keep the
            //formatting good
            if(eventMonth.size() == 0){
                printer.write(" \n");
            } else {
                //iterate through the events in the month
                for (Event event : eventMonth) {
                    printer.write(event.toFormat());
                }
                printer.write("\n");
            }
        }
        printer.close();
    }

    //Method that takes an event name and pulls it up to be updated
    int updateEvent(String eventName){
        Scanner scan = new Scanner(System.in);
        for(List<Event> monthEvents : events){
            for(Event event : monthEvents){
                if(event.name.equals(eventName)) {
                    System.out.println("Update which field?\n" +
                            "a: name, b:desc, c:day, d:month");
                    String input = scan.next();
                    switch (input) {
                        case "a":
                            System.out.println("Input the new name");
                            event.name = scan.next();
                            break;
                        case "b":
                            System.out.println("Input the new description");
                            event.desc = scan.next();
                            break;
                        case "c":
                            System.out.println("Input the new day");
                            event.day = scan.nextInt();
                            break;
                        case "d":
                            System.out.println("Input the new month");
                            event.month = scan.nextInt();
                            break;
                        default:
                            System.out.println("Invalid option");
                            return 0;
                    }
                    return 1;
                }
            }
        }
        System.out.println("No event of that name found");
        return -1;
    }

    //TODO: Method that updates an event on a given day
    int updateEvent(int month, int day){
        return 0;
    }

    //TODO: Method that returns all events in the week of the given date
    List<Event> getWeek(int currDate) {
        /*
         Tuesday     Wednesday   Thursday    Friday      Saturday    Sunday      Monday
        +-----------+-----------+-----------+-----------+-----------+-----------+-----------+
        |03         |04         |05         |06         |07         |08         |09         |
        |           |           |           |           |           |           |           |
        |           |           |           |           |           |           |           |
        |           |           |           |           |           |           |           |
        +-----------+-----------+-----------+-----------+-----------+-----------+-----------+

         */
        return null;
    }

    //Method that returns all the events in the given month
    List<Event> getMonth(int month) {
        return events.get(month);
    }

    //Method that returns the events for a given day
    Event getDay(int month, int day) {
        return (Event) events.get(month).get(day);
    }

    /*
    Given the date, create the full calendar for the year with days accurately placed
    per month. Takes date of the format Wed 03/04/2020
     */
    void fullCalendar(String date){
        //Have the day of the current day, want the day for the first day of the month
        String[] intToDay = {"Sun","Mon","Tue","Wed",
                                "Thu","Fri","Sat"};
        String[] firstDays = new String[12];
        String[] months = {"January", "February", "March",
                "April", "May", "June", "July", "August", "September",
                "October", "November", "December"};
        int month = Integer.parseInt(date.substring(4,6));
        int day = Integer.parseInt(date.substring(7,9));
        int year = Integer.parseInt(date.substring(10));
        //Find how far you are from the first of the month, modulo that
        day -= 1;
        day %= 7;
        int diff = dayToInt(date.substring(0,3)) - day;
        if(diff < 0){
            diff += 7;
        }
        //Add whatever day of the week it is to the first days array
        firstDays[month-1] = intToDay[diff];
        int dayFromJan1 = Integer.parseInt(date.substring(7,9));
        //Checks for leap year
        if(year%4 == 0 && (year%100 != 0 || year%400 == 0)){
            daysPerMonth[1] = 29;
        }
        //Finds the number of days that have passed since Jan 1 to the
        //Beginning of the month. Month-2 because we don't count the current month
        for(int i = month-2; i > -1; i--){
            dayFromJan1 += daysPerMonth[i];
        }
        //Find out what day each month starts on
        diff = dayToInt(date.substring(0,3)) - (dayFromJan1-1)%7;
        if(diff < 0){
            diff += 7;
        }
        firstDays[0] = intToDay[diff];
        for(int i = 0; i < 11; i++){
            diff = dayToInt(firstDays[i]) + ((daysPerMonth[i])%7);
            if(diff > 6){
                diff -= 7;
            }
            firstDays[i+1] = intToDay[diff];
        }

        //
        for(int monthi = 0; monthi < 12; monthi++){
            int firstDay = dayToInt(firstDays[monthi]);
            System.out.println(months[monthi]);
            System.out.println(" Sun Mon Tue Wed Thu Fri Sat");
            for(int j = 0; j < firstDay; j++){
                System.out.print("    ");
            }
            System.out.print("  1 ");
            for(int dayi = 2; dayi < daysPerMonth[monthi]+1; dayi++){
                if(((dayi-1+firstDay)%7) == 0){
                    System.out.println();
                }
                System.out.print(String.format(" %2d ", dayi));
            }
            System.out.println();
        }
        //resets the days in february to 28
        daysPerMonth[1] = 28;
    }

    ArrayList<JTableInfo> buildMonth(int month, int year){
        //Find out what day of the week january first is of that year, then build the month
        //based on that
        if ((year) % 4 == 0 && ((year) % 100 != 0 || (year) % 400 == 0)) {
            daysPerMonth[1] = 29;
        }
        String[] intToDay = {"Sun","Mon","Tue","Wed",
                "Thu","Fri","Sat"};
        int startday = 3;
        if(year >= 2020) {
            //Calculates how many years away from 2020 we are
            while (year != 2020) {
                //Leap year check. Subtract 1 from the year because you look at the year
                //before the year you are going for
                if ((year - 1) % 4 == 0 && ((year - 1) % 100 != 0 || (year - 1) % 400 == 0)) {
                    startday += 366;
                } else {
                    startday += 365;
                }
                year--;
            }
            for (int i = 0; i < month; i++) {
                startday += daysPerMonth[i];
            }
            startday %= 7;
        }
        else {
            //If we are in a year before the root year of 2020, have to build up backwards
            //from the last day of 2019
            //work from the last day of 2020
            int lastday = 2;
            //calculate backwards
            while (year < 2019) {
                //Leap year check. Add 1 to the year because you look at the year
                //after the year you are going for
                if ((year) % 4 == 0 && ((year) % 100 != 0 || (year) % 400 == 0)) {
                    lastday += 366;
                } else {
                    lastday += 365;
                }
                year++;
            }
            //Work up from december, adding the missing days to the thing
            for (int i = 11; i > month; i--) {
                lastday -= daysPerMonth[i];
            }
            //calculates the day of the week that the last day of the month is
            //uses the last day of the month to calculate the first day of the month
            //firstday + daysinmonth - 1 = lastday
            //lastday + 1 - daysinmonth = firstday
            lastday %= 7;
            //if we are negative, add 7 to get the correct day
            if (lastday < 0) {
                lastday += 7;
            }
            startday = lastday + 1 - daysPerMonth[month];
            startday %= 7;
            //if we are negative, add 7 to get the correct day
            if (startday < 0) {
                startday += 7;
            }
        }
        ArrayList<JTableInfo> out = new ArrayList<>();
        for (int i = 0; i < startday; i++) {
            JTableInfo toadd = new JTableInfo(new String[][] {{""}}, new String[] {""});
            out.add(toadd);
        }
        List<Event> events = this.getMonth(month);
        int eventi = 0;
        for (int dayi = startday; dayi < daysPerMonth[month] + startday; dayi++) {
            ArrayList<Event> dayEvents = new ArrayList<>();
            while(eventi < events.size() && events.get(eventi).day <= (dayi-startday+1)){
                if(events.get(eventi).day == (dayi-startday+1)) {
                    dayEvents.add(events.get(eventi));
                }
                eventi++;
            }
            String[][] eventArray;
            if(dayEvents.size()!= 0) {
                eventArray = new String[dayEvents.size()][1];
                for (int i = 0; i < dayEvents.size(); i++) {
                    eventArray[i][0] = dayEvents.get(i).toString();
                }
            } else {
                eventArray = new String[][] {{" "}};
            }
            JTableInfo toadd = new JTableInfo(eventArray, new String[] {"" + (dayi-startday+1)});
            out.add(toadd);
        }
        daysPerMonth[1] = 28;

        return out;
    }

    private int dayToInt(String day){
        switch(day){
            case "Sun":
                return 0;
            case "Mon":
                return 1;
            case "Tue":
                return 2;
            case "Wed":
                return 3;
            case "Thu":
                return 4;
            case "Fri":
                return 5;
            case "Sat":
                return 6;
            default:
                return -1;
        }
    }
}
