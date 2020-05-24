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

    //4 bits for month, 5 bits for day, 1 bit for annual, 21 bits for year.
    //Event name, event description
    String toFormat(){
        int total = month;
        total = total << 4;
        total += day;
        total = total << 5;
        if(annual){
            day += 1;
        }
        total = total << 22;
        total += year;
        char gs = (char) 35;
        if(desc != null) {
            return String.format("%04d%s.%s%c", total, name, desc, gs);
        } else {
            return String.format("%04d%s%c", total, name, gs);
        }
    }
}
