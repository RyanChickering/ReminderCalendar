public class ReminderCalendar {
    public static void main(String[] args){
        Calendar calendar = new Calendar();
        try {
            calendar.readFile();
            calendar.addEvent(new Event("0306CS514 HW Due"));
            calendar.save();
        } catch(Exception e){
            System.out.println("Exception caused: " + e);
        }
        System.out.print(calendar);

    }
}
