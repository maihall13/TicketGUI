package com.company;

import java.io.*;
import java.text.ParseException;
import java.util.Date;
import java.util.LinkedList;
import java.util.ListIterator;

import static com.company.Input.getStringInput;

public class TicketManager {

    LinkedList<Ticket> ticketQueue = new LinkedList<Ticket>();
    LinkedList<Ticket> resolvedTickets = new LinkedList<Ticket>();

    private void mainMenu() {

        while (true) {

            //TODO problem 4 - add two new options: Delete by Issue and Search by Issue
            System.out.println("1. Enter Ticket\n2. Search by Issue\n3. Delete Ticket by ID\n4. Delete Ticket by Issue\n5. Display All Tickets\n6. Quit");

            int task = Input.getPositiveIntInput("Enter your selection");

            if (task == 1) {
                addTickets();
            }
            else if (task == 2) {
                searchByIssue();
            }
            else if (task == 3) {
                deleteTicketById();
            }
            else if (task == 4){
                deleteTicketByIssue();
            }
            else if(task == 5){
                printAllTickets();
            }
            else if ( task == 6 ) {
                System.out.println("Quitting program");
                // TODO Problem 7 save all open tickets, and today's resolved tickets, to a file

                String resolvedTicketsFile = "Resolved_tickets_as_of_february_14_2014.txt";
                try {
                    FileWriter resolved = new FileWriter(resolvedTicketsFile);
                    BufferedWriter resolvedFile = new BufferedWriter(resolved);
                    ListIterator itr = resolvedTickets.listIterator();
                    while (itr.hasNext()) {
                        Ticket element = (Ticket) itr.next();
                        resolvedFile.write(element + "\n");
                    }
                    resolvedFile.close();
                    System.out.println("File created successfully.");
                } catch (Exception e) {
                }

                String openTicketFile = "open_tickets.txt";
                try {
                    FileWriter open = new FileWriter(openTicketFile);
                    BufferedWriter openFile = new BufferedWriter(open);
                    ListIterator itr = ticketQueue.listIterator();
                    while (itr.hasNext()) {
                        Ticket element = (Ticket) itr.next();
                        openFile.write(element + "\n");
                    }
                    openFile.close();
                    System.out.println("File created successfully.");
                } catch (Exception e) {
                }
                break;
            }
            else {
                //this will happen for 3 or any other selection that is a valid int
                //Default will be print all tickets
                printAllTickets();
            }
        }
    }



    /*
    METHOD FOR SEARCHING BY DESCRIPTION CALLED BY SEARCHBYISSUE METHOD
     */
    protected LinkedList<Ticket> searchDescription(String searchString) {

        LinkedList<Ticket> searchList = new LinkedList<Ticket>();//Linked list that will be returend

        // list of the tickets that contain the searchString in the description.
        for (Ticket ticket : ticketQueue) {
            if (ticket.getDescription().contains(searchString)){
                searchList.add(ticket);
            }
        }
        return searchList;//Return statement that returns a list
    }


    /*
    METHOD FOR SEARCHING BY ISSUE
    */
    protected void searchByIssue() {
        // Ask user for search term
        String search = Input.getStringInput("Enter the issue you would like to search.");
        for (Ticket x : searchDescription(search)){
            System.out.println(x);
        }
    }


    /*
    METHOD FOR DELETING TICKET BY THE ISSUE
    */
    protected void deleteTicketByIssue() {
        searchByIssue();

        //Slightly redundant but for revision make a delete and resolve method

        int deleteID = Input.getPositiveIntInput("Enter ID of ticket to delete");
        boolean found = false;
        for (Ticket ticket : ticketQueue) {
            if (ticket.getTicketID() == deleteID) {
                found = true;
                //Remove call from head of queue and resolve

                System.out.println("ticket id:" + ticket.getTicketID());
                Ticket resolvedTicket = ticket;    //Remove call from head of queue
                String resolution = Input.getStringInput("Enter resolution for " + resolvedTicket);
                resolvedTicket.setResolution(resolution);
                resolvedTicket.setResolvedDate(new Date());  //default resolved date is now
                resolvedTickets.add(ticket);
                ticketQueue.remove(ticket);
                System.out.println(String.format("Ticket %d deleted", deleteID));


                break; //don't need the loop any more.
            }
        }
        if (!found) {
            System.out.println("Ticket ID not found, no ticket deleted");
            //Use recursive
            deleteTicketById();
        }
        printAllTickets();  //print updated list
    }


    /*
    METHOD FOR DELETING TICKET BY THE ID NUMBER
     */
    protected void deleteTicketById() {

        printAllTickets();   //display list for user

        if (ticketQueue.size() == 0) {    //no tickets!
            System.out.println("No tickets to delete!\n");
            return;
        }

        int deleteID = Input.getPositiveIntInput("Enter ID of ticket to delete");

        //Loop over all tickets. Delete the one with this ticket ID
        boolean found = false;
        for (Ticket ticket : ticketQueue) {
            if (ticket.getTicketID() == deleteID) {
                found = true;
                //Remove call from head of queue and resolve


                System.out.println("ticket id:" + ticket.getTicketID());

                Ticket resolvedTicket = ticket;    //Remove call from head of queue
                String resolution = Input.getStringInput("Enter resolution for " + resolvedTicket);
                resolvedTicket.setResolution(resolution);

                resolvedTicket.setResolvedDate(new Date());  //default resolved date is now
                resolvedTickets.add(ticket);

                ticketQueue.remove(ticket);
                System.out.println(String.format("Ticket %d deleted", deleteID));
                break; //don't need the loop any more.
            }
        }
        if (!found) {
            System.out.println("Ticket ID not found, no ticket deleted");
            //Use recursive
            deleteTicketById();
        }

        printAllTickets();  //print updated list

    }


    protected void addTickets() {

        while (true) {

            Date dateReported = new Date(); //Default constructor creates Date with current date/time

            String description = getStringInput("Enter problem");
            String reporter = getStringInput("Who reported this issue?");
            int priority = Input.getPositiveIntInput("Enter priority of " + description);

            Ticket t = new Ticket(description, priority, reporter, dateReported);
            addTicketInPriorityOrder(t);

            printAllTickets();

            String more = getStringInput("More tickets to add? Enter N for no, anything else to add more tickets");

            if (more.equalsIgnoreCase("N")) {
                return;
            }
        }
    }


    protected void addTicketInPriorityOrder(Ticket newTicket){

        //Logic: assume the list is either empty or sorted

        if (ticketQueue.size() == 0 ) {//Special case - if list is empty, add ticket and return
            ticketQueue.add(newTicket);
            return;
        }

        //Tickets with the HIGHEST priority number go at the front of the list. (e.g. 5=server on fire)
        //Tickets with the LOWEST value of their priority number (so the lowest priority) go at the end

        int newTicketPriority = newTicket.getPriority();

        for (int x = 0; x < ticketQueue.size() ; x++) {    //use a regular for loop so we know which element we are looking at

            //if newTicket is higher or equal priority than the this element, add it in front of this one, and return
            if (newTicketPriority >= ticketQueue.get(x).getPriority()) {
                ticketQueue.add(x, newTicket);
                return;
            }
        }

        //Will only get here if the ticket is not added in the loop
        //If that happens, it must be lower priority than all other tickets. So, add to the end.
        ticketQueue.addLast(newTicket);
    }


    protected void printAllTickets() {
        System.out.println(" ------- All open tickets ----------");

        for (Ticket t : ticketQueue ) {
            System.out.println(t); // This calls the  toString method for the Ticket object.
        }
        System.out.println(" ------- End of ticket list ----------");

    }


    /* Main is hiding down here. Create a TicketManager object, and call the mainMenu method.
    Avoids having to make all of the methods in this class static. */
    public static void main(String[] args) throws IOException, ParseException {
        TicketManager manager = new TicketManager();

        //creates new class to "override" or high public method in order to call it
        //from static main
        PreviousTickets load = new PreviousTickets();

        BufferedReader br = new BufferedReader(new FileReader("open_tickets.txt"));
        String line = null;
        while ((line = br.readLine()) != null) {
            //returns ticket and adds ticket to the que
            //Order numbers will change but the order of the priority stay the same.
            manager.ticketQueue.add(load.loadTickets(line));
        }
        manager.mainMenu();
    }
}

