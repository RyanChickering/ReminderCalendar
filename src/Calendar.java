import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/*
Calendar class handles reading in events from the events.cal file
Events are stored in the format mmddyy"Event Name""Event Desc"/mmddyy...
Read file, convert file into a data structure of events
Can then add events into the data structure, write the data structure to file when
completed.


 */

public class Calendar {
    private List<List> events = new ArrayList<>();
    public Calendar(){

    }

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

    int save() throws IOException {
        File calData = new File(System.getProperty("user.dir") + "/cal.dat");
        calData.createNewFile();
        FileWriter printer = new FileWriter(calData, false);
        for(List<Event> eventMonth : events){
            if(eventMonth.size() == 0){
                printer.write(" \n");
            } else {
                for (Event event : eventMonth) {
                    printer.write(event.toFormat());
                }
                printer.write("\n");
            }
        }
        printer.close();
        return 0;
    }

    List<Event> getWeek(int currDate) {
        return null;
    }

    List<Event> getMonth(int month) {
        return events.get(month);
    }

    Event getDay(int month, int day) {
        return (Event) events.get(month).get(day);
    }
}
