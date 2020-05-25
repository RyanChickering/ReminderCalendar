/*
Events are stored in the format mmddEvent Name.Event Desc/mmddyy...
 */

public class Event {
    private String[] months = {"January", "February", "March",
        "April", "May", "June", "July", "August", "September",
        "October", "November", "December"};
    int month, day, year;
    String name;
    String desc;
    boolean annual;
    Event(String date){
        month = Integer.parseInt(date.substring(0,2));
        day = Integer.parseInt(date.substring(2,4));
        if(date.contains(".")) {
            name = date.substring(4, date.indexOf('.'));
            desc = date.substring(date.indexOf('.') + 1);
        } else {
            name = date.substring(4);
            desc = null;
        }
    }

    Event(int month, int day, int year, boolean annual, String name, String desc){
        this.month = month;
        this.day = day;
        this.year = year;
        this.annual = annual;
        this.name = name;
        this.desc = desc;
    }
    /*
    returns string of format:
    March 4: Production started
    First day I started coding this project
     */
    @Override
    public String toString(){
        if(desc != null) {
            return name + "\n" +
                    desc + "\n";
        } else {
            return name + "\n";
        }
    }

    //4 bits for month, 5 bits for day, 1 bit for annual, 14 bits for year.
    //Event name, event description
    //January 1, 2020 (Annual) ->   0000 0000 11 00 0111 1110 0100 -> 00C7e4
    //February 2, 2020 ->           0001 0001 00 00 0111 1110 0100 -> 1107e4
    String toFormat(){
        int total = month;
        total = total << 5;
        total += day;
        total = total << 1;
        if(annual){
            total += 1;
        }
        total = total << 14;
        total += year;
        char descSeparator = (char) 169;
        char entrySeparator = (char) 170;
        if(desc != null) {
            return String.format("%06x%s%c%s%c", total, name, descSeparator, desc, entrySeparator);
        } else {
            return String.format("%06x%s%c", total, name, entrySeparator);
        }
    }
}
