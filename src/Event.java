/*
Events are stored in the format mmddEvent Name.Event Desc/mmddyy...
 */

public class Event {
    String[] months = {"January", "February", "March",
        "April", "May", "June", "July", "August", "September",
        "October", "November", "December"};
    int month, day;
    String name;
    String desc;
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

    Event(String month, int day){

    }

    /*
    returns string of format:
    March 4: Production started
    First day I started coding this project
     */
    @Override
    public String toString(){
        if(desc != null) {
            return months[month - 1] + " " + day + ": " + name + "\n" +
                    desc + "\n";
        } else {
            return months[month - 1] + " " + day + ": " + name + "\n";
        }
    }

    String toFormat(){
        if(desc != null) {
            return String.format("%02d%02d%s.%s/", month, day, name, desc);
        } else {
            return String.format("%02d%02d%s/", month, day, name);
        }
    }
}