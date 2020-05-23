import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ReminderCalendar {
    public static void main(String[] args){
        Calendar calendar = new Calendar();
        try {
            calendar.readFile();
            //calendar.addEvent(new Event("0512Chance's Birthday"));
            calendar.save();
        } catch(Exception e){
            System.out.println("Exception caused: " + e);
        }
        buildUI(calendar);
        /*
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("E MM/dd/yyyy");
        System.out.println(dateFormat.format(date));
        System.out.print(calendar);
        */

    }

    private static void buildUI(Calendar calendar){
        JFrame frame = new JFrame("Reminder Calendar");
        JPanel buttons = new JPanel();
        JPanel calendarTable = new JPanel();
        JButton addEvent = new JButton("Add event");
        //addEvent.setBounds(300, 10, 120, 30);
        addEvent.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO: add event to the calendar and update display if in month
            }
        });

        String[] months = {"January", "February", "March", "April", "May",
                "June", "July", "August", "September", "October", "November",
                "December"};
        JComboBox month = new JComboBox(months);
        //month.setBounds(10,10,120,30);
        month.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO: Upon action rebuild displayed calendar table based on month
            }
        });
        JTextField year = new JTextField("2020");
        //year.setBounds(140,10,120, 30);
        buttons.add(month);
        buttons.add(year);
        buttons.add(addEvent);


        String[] days = {"Sun","Mon","Tue","Wed",
                "Thu","Fri","Sat"};
        JLabel sun, mon, tue, wed, thu, fri, sat;
        sun = new JLabel("Sunday"); mon = new JLabel("Monday");
        tue = new JLabel("Tuesday"); wed = new JLabel("Wednesday");
        thu = new JLabel("Thursday"); fri = new JLabel("Friday");
        sat = new JLabel("Saturday");
        calendarTable.add(sun); calendarTable.add(mon); calendarTable.add(tue);
        calendarTable.add(wed); calendarTable.add(thu); calendarTable.add(fri);
        calendarTable.add(sat);
        List<JTableInfo> caldays = calendar.buildMonth(new int[] {1,2020});
        for(JTableInfo calday: caldays){
            JTable toAdd = new JTable(calday.data, calday.headers);
            toAdd.setBounds(0,0,100,100);
            JScrollPane paneAdd = new JScrollPane(toAdd);
            //paneAdd.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
            //paneAdd.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            calendarTable.add(paneAdd);
        }


        //buttons.setLayout(new GridLayout(1,3));
        calendarTable.setLayout(new GridLayout(6,7));
        //frame.add(buttons, BorderLayout.NORTH);
        frame.add(calendarTable);

        frame.setSize(710,710);
        frame.setVisible(true);

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
