package com.company;

import java.util.Date;

public class Ticket {

    private int priority;
    private String reporter; //Stores person or department who reported issue
    private String description;
    private Date dateReported;
    private Date resolvedDate;
    protected String resolution;
    private boolean isResolved;

    //STATIC Counter - one variable, shared by all Ticket objects.
    //If any Ticket object modifies this counter, all Ticket objects will have the modified value
    //Make it private - only Ticket objects should have access
    private static int ticketIdCounter = 1;

    //The ID for each ticket - an instance variable. Each Ticket will have it's own ticketID variable
    protected int ticketID;

    // Either add them to this class or create another class called ResolvedTicket - which
    // do you think is the better approach?

    public Ticket(String desc, int p, String rep, Date date) {
        this.description = desc;
        this.priority = p;
        this.reporter = rep;
        this.dateReported = date;
        this.ticketID = ticketIdCounter;
        ticketIdCounter++;
    }

    public void setResolvedDate(Date resolvedDate) {
        this.resolvedDate = resolvedDate;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    //public String getResolution(){return resolution;}

    protected int getPriority() {
        return priority;
    }

    public String getDescription(){return description;}

    public int getTicketID() {
        return ticketID;
    }

    public void setIsResolved(boolean resolvedTrueOrFalse){this.isResolved = resolvedTrueOrFalse;}


    public String toString(){

        if (isResolved == true){
            return("ID: " + this.ticketID + " Issue: " + this.description + " Priority: " + this.priority + " Reported_by: "
                    + this.reporter + " Reported_on: " + this.dateReported + " Resolution: " + this.resolution + " Resolved_on: " + this.resolvedDate);
        }
        else{
            return("ID: " + this.ticketID + " Issue: " + this.description + " Priority: " + this.priority + " Reported_by: "
                    + this.reporter + " Reported_on: " + this.dateReported);
        }

    }
}

