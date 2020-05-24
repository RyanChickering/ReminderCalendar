import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ReminderCalendar {
    private static JPanel calendarTable;
    private static String[] months = {"January", "February", "March", "April", "May",
            "June", "July", "August", "September", "October", "November",
            "December"};

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
    }

    private static void buildUI(Calendar calendar){
        JFrame frame = new JFrame("Reminder Calendar");
        JButton addEvent = new JButton("Add event");
        //addEvent.setBounds(300, 10, 120, 30);
        addEvent.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Creates a new frame that prompts the user for input about the event they
                //would like to add.
                JFrame eventAdder = new JFrame("Add Event");
                JTextField name = new JTextField();
                JComboBox month = new JComboBox(months);
                JTextField day = new JTextField();
                JCheckBox annual = new JCheckBox("Annual");
                JTextField year = new JTextField();
                JButton confirm = new JButton("Confirm");
                JButton cancel = new JButton("Cancel");
                eventAdder.add(name);
                eventAdder.add(month);
                eventAdder.add(day);
                eventAdder.add(annual);
                eventAdder.add(year);
                eventAdder.add(confirm);
                eventAdder.setSize(300, 300);
                eventAdder.setLayout(new GridLayout(6,1));
                eventAdder.setVisible(true);
                eventAdder.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                //Action for the confirm button writes out a new event using the
                //parameters provided in the other fields
                confirm.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        int monthNum =
                                strMonthToInt(month.getItemAt(month.getSelectedIndex()).toString().toLowerCase());
                        int dayNum = Integer.parseInt(day.getText());
                        int yearNum = Integer.parseInt(year.getText());
                        String eventName = name.getText();
                        Boolean annualVal = annual.isContentAreaFilled();
                        String description = "";
                        if(calendar.daysPerMonth[monthNum] < dayNum || dayNum == 29){
                            JFrame error = new JFrame("Error");
                            JLabel errorMessage = new JLabel("That day does not exist in that month");
                            error.add(errorMessage);
                            JButton ok = new JButton("ok");
                            error.add(ok);
                            error.setLayout(new GridLayout(2,1));
                            error.setSize(200,100);
                            error.setVisible(true);
                            error.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                            ok.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    error.dispose();
                                }
                            });
                        }
                        else {
                            Event eventToAdd = new Event(monthNum, dayNum, yearNum, annualVal, eventName, description);
                            calendar.addEvent(eventToAdd);
                            eventAdder.dispose();
                            List<JTableInfo> newdays = calendar.buildMonth(monthNum, 2020);
                            refreshCalendar(frame, newdays);
                        }
                    }
                });
                //Action for the cancel button closes the window without saving anything
                cancel.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        eventAdder.dispose();
                    }
                });
            }
        });
        JTextField year = new JTextField("2020");
        year.setBounds(140,10,120, 30);


        final JComboBox month = new JComboBox(months);
        //month.setBounds(10,10,120,30);

        String[] days = {"Sun","Mon","Tue","Wed",
                "Thu","Fri","Sat"};

        List<JTableInfo> caldays = calendar.buildMonth(0,2020);
        calendarTable = calGrid(caldays);

        frame.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.ipadx = 140;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        frame.add(month, constraints);
        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.ipadx = 140;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        frame.add(year, constraints);
        constraints.gridx = 2;
        constraints.gridy = 0;
        constraints.ipadx = 140;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        frame.add(addEvent, constraints);
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 3;
        constraints.ipadx = 500;
        constraints.ipady = 500;
        frame.add(calendarTable, constraints);

        frame.setSize(710,710);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        month.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String inputmonth = "" + month.getItemAt(month.getSelectedIndex());
                int inputyear = Integer.parseInt(year.getText().toLowerCase());
                List<JTableInfo> newdays = calendar.buildMonth(strMonthToInt(inputmonth), inputyear);
                refreshCalendar(frame, newdays);
            }
        });

        year.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String inputmonth = "" + month.getItemAt(month.getSelectedIndex());
                int inputyear = Integer.parseInt(year.getText().toLowerCase());
                List<JTableInfo> newdays = calendar.buildMonth(strMonthToInt(inputmonth), inputyear);
                refreshCalendar(frame, newdays);
            }
        });
    }

    //event format: 4 bits month, 5 bits day, name of event

    private static void refreshCalendar(JFrame frame, List<JTableInfo> newdays){
        GridBagConstraints constraints = new GridBagConstraints();
        frame.remove(calendarTable);
        calendarTable = calGrid(newdays);
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.ipadx = 500;
        constraints.ipady = 500;
        constraints.gridwidth = 3;
        frame.add(calendarTable, constraints);
        frame.revalidate();
        frame.repaint();
    }

    private static JPanel calGrid(List<JTableInfo> info){
        JPanel calendarTable = new JPanel();
        JLabel sun, mon, tue, wed, thu, fri, sat;
        sun = new JLabel("Sunday"); mon = new JLabel("Monday");
        tue = new JLabel("Tuesday"); wed = new JLabel("Wednesday");
        thu = new JLabel("Thursday"); fri = new JLabel("Friday");
        sat = new JLabel("Saturday");
        calendarTable.add(sun); calendarTable.add(mon); calendarTable.add(tue);
        calendarTable.add(wed); calendarTable.add(thu); calendarTable.add(fri);
        calendarTable.add(sat);
        for(JTableInfo calday: info){
            JTable toAdd = new JTable(calday.data, calday.headers);
            toAdd.setBounds(0,0,100,100);
            JScrollPane paneAdd = new JScrollPane(toAdd);
            calendarTable.add(paneAdd);
        }
        calendarTable.setLayout(new GridLayout(ceil(info.size()/7.0)+1,7));
        return calendarTable;
    }

    private static int ceil(double input){
        int compare = (int) input;
        if(compare < input){
            return compare + 1;
        }
        return compare;
    }

    private static int strMonthToInt(String month){
        switch(month.toLowerCase()){
            case "january":
                return 0;
            case "february":
                return 1;
            case "march":
                return 2;
            case "december":
                return 11;
            case "april":
                return 3;
            case "may":
                return 4;
            case "june":
                return 5;
            case "july":
                return 6;
            case "august":
                return 7;
            case "september":
                return 8;
            case "october":
                return 9;
            case "november":
                return 10;
            default:
                return -1;

        }
    }
}
