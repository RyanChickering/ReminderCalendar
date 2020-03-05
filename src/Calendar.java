import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

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
    int[] daysPerMonth = {31,28,31,30,31,31,30,31,30,31,30,31};
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
        int eventMonth = event.month - 1;
        List<Event> eventsForMonth = events.get(eventMonth);
        int i;
        for(i = 0; i < eventsForMonth.size(); i++){
            if(event.day < eventsForMonth.get(i).day){
                break;
            }
        }
        events.get(eventMonth).add(i, event);
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

    //TODO: Method that updates an event
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

    int updateEvent(int month, int day){
        return 0;
    }

    //TODO: Method that returns all events in the week of the given date
    List<Event> getWeek(int currDate) {
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
}
