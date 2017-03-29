package com.company;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Maia on 3/28/2017.
 */
public class PreviousTickets extends TicketManager{

    //made to hide public class to call public and static options.
    public Ticket loadTickets(String e) throws ParseException {

        ArrayList<String> chars = new ArrayList<String>();
        String testS = e;

        //split each line by the colon and adds to each separation to the array list
        for (String x : testS.split("\\w+:\\s")){
            chars.add(x);
        }

        //since each colon is attached to the beginning of the word each represents
        //and because each line is the same can use exact indexes to save each to the correct variable
        Integer id = Integer.valueOf(chars.get(1).replaceAll("\\s",""));
        String description = chars.get(2);
        Integer priority = Integer.valueOf(chars.get(3).replaceAll("\\s",""));
        String reporter = chars.get(4);
        SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM d kk:mm:ss z yyyy");
        String dateString = chars.get(5);
        Date date = formatter.parse(dateString);
        Date dateReported = date;

        //send to create a new ticket and then returns that ticket.
        Ticket t = new Ticket(description, priority, reporter, dateReported);
        t.ticketID = id;
        return t;
    }
}
