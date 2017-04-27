package com.company;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.LinkedList;

/**
 * Created by Maia on 3/29/2017.
 */

public class TicketForm extends JFrame{

    LinkedList<Ticket> ticketQueue = new LinkedList<Ticket>();
    LinkedList<Ticket> resolvedTickets = new LinkedList<Ticket>();


    private JPanel rootPane;
    private JTabbedPane ticketManagerTabs;
    private JPanel addTicketTab;
    private JTextField ticketDescriptionTextField;
    private JComboBox<Integer> ticketPriorityComboBox;
    private JTextField reporterTextField;
    private JButton addTicketButton;
    private JPanel openTicketsTab;
    private JList openTicketsList;
    private JButton resolveButton;
    private JList resolvedTicketList;
    private JButton searchButton;
    private JTextField searchTextField;
    private JRadioButton idRadioButton;
    private JRadioButton issueRadioButton;
    private JPanel resolvedTicketsTab;
    public  TicketManager manager = new TicketManager();

    private DefaultListModel<String> listModel;


    protected TicketForm() {
        setContentPane(rootPane);
        setLocationRelativeTo(null);
        setTicketPriorityOptions();

        //Creates a button group so that only one radio button can be selected
        ButtonGroup searchOptions = new ButtonGroup();
        searchOptions.add(idRadioButton);
        searchOptions.add(issueRadioButton);

        pack();

        //Produces form in the center of the screen
        setLocationRelativeTo(null);

        setVisible(true);


        //Find index of tab.
        ticketManagerTabs.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                System.out.println(ticketManagerTabs.getSelectedIndex());
            }
        });


        //What happens when the Add ticket button is clicked
        addTicketButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addTicketValidation();
            }
        });



        //What happens if the Resolve button is clicked
        resolveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                //Checks to see if there are any tickets in the list
                if(openTicketsList.getModel().getSize()<=0){
                    JOptionPane.showConfirmDialog(null, "There are no tickets to resolve.", "Error", JOptionPane.DEFAULT_OPTION);
                }
                //Removes the tickets from list if it is selected.
                else{
                    System.out.println("the list is NOT empty");
                    int selectedIndex = openTicketsList.getSelectedIndex();
                    Ticket t = ticketQueue.get(selectedIndex);
                    System.out.println(t.toString());

                    String resolution = JOptionPane.showInputDialog("Enter resolution for ticket");
                    System.out.println(resolution);

                    //resolve the ticket and add to resolved list
                    t.setIsResolved(true);
                    t.setResolution(resolution);
                    t.setResolvedDate(new Date());  //default resolved date is now
                    Ticket resolvedTicket = t;
                    resolvedTickets.add(t);
                    ticketQueue.remove(t);
                    listModel.remove(selectedIndex);
                    JOptionPane.showConfirmDialog(null, "Ticket has been resolved", "Confirm", JOptionPane.DEFAULT_OPTION);
                    addToResolvedTickets();
                }
            }
        });


        //What happens if the search press is selected.
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //If the search field is empty - returns dialog box and
                //reprint list.
                if(searchTextField.getText().equals("")){
                    System.out.println("There is nothing to search"); //make dialog
                    JOptionPane.showConfirmDialog(null, "Please enter a search term", "Error", JOptionPane.DEFAULT_OPTION);
                    addToOpenTickets();
                }
                else {
                    if (idRadioButton.isSelected()) {
                        try{
                            listModel = new DefaultListModel<String>();
                            openTicketsList.setModel(listModel);
                            listModel.clear();

                            for (Ticket ticket : ticketQueue) {
                                if (ticket.getTicketID() == Integer.valueOf(searchTextField.getText())) {
                                    listModel.addElement(ticket.toString());
                                }
                            }
                            searchTextField.setText("");
                        }
                        catch (NumberFormatException fne){
                            System.out.println("Enter a valid number");
                            JOptionPane.showConfirmDialog(null, "Please enter a valid ID Number", "Error", JOptionPane.DEFAULT_OPTION);
                            addToOpenTickets();
                        }
                    }
                    if (issueRadioButton.isSelected()) {

                        //System.out.println("Search by description");

                        try{
                            listModel = new DefaultListModel<String>();
                            openTicketsList.setModel(listModel);
                            listModel.clear();

                            boolean found = false;

                            for (Ticket ticket : ticketQueue) {
                                if (ticket.getDescription().contains(searchTextField.getText())){
                                    listModel.addElement(ticket.toString());
                                    found = true;
                                }
                            }
                            if (found == false){
                                JOptionPane.showConfirmDialog(null, "There is nothing matching your search", "Error", JOptionPane.DEFAULT_OPTION);
                                addToOpenTickets();
                            }
                            searchTextField.setText("");
                        }
                        catch (TypeNotPresentException fne){
                            System.out.println("Enter a valid description");
                            JOptionPane.showConfirmDialog(null, "Please enter a valid description", "Error", JOptionPane.DEFAULT_OPTION);
                            addToOpenTickets();
                        }
                    }
                }
            }
        });




    }

    public void addTicketValidation(){
        if (ticketDescriptionTextField.getText().equals("")) {
            JOptionPane.showConfirmDialog(null, "Please add a ticket description", "Error", JOptionPane.DEFAULT_OPTION);
        }
        else if(reporterTextField.getText().equals("")){
            JOptionPane.showConfirmDialog(null, "Please add a reporter for the ticket", "Error", JOptionPane.DEFAULT_OPTION);
        }
        else{
            addTicket();
        }
    }


    private void setTicketPriorityOptions(){
        for (int x = 1 ; x<=3; x++) {ticketPriorityComboBox.addItem(x);}
    }



    private void addTicket() {
        String description = ticketDescriptionTextField.getText();
        String p = ticketPriorityComboBox.getSelectedItem().toString();
        Integer priority = Integer.valueOf(p);
        String reporter = reporterTextField.getText();
        Date dateReported = new Date(); //Default constructor creates Date with current date/time
        Ticket t = new Ticket(description, priority, reporter, dateReported);
        addTicketInPriorityOrder(t);

        addToOpenTickets();
        ticketDescriptionTextField.setText("");
        reporterTextField.setText("");

        JOptionPane.showConfirmDialog(null, "Ticket has been added to Open Tickets", "Confirm", JOptionPane.DEFAULT_OPTION);


    }

    protected void addTicketInPriorityOrder(Ticket newTicket){
        //Logic: assume the list is either empty or sorted
        if (ticketQueue.size() == 0 ) {//Special case - if list is empty, add ticket and return
            ticketQueue.add(newTicket);
            return;}
        //Tickets with the HIGHEST priority number go at the front of the list. (e.g. 5=server on fire)
        //Tickets with the LOWEST value of their priority number (so the lowest priority) go at the end
        int newTicketPriority = newTicket.getPriority();
        for (int x = 0; x < ticketQueue.size() ; x++) {//use a regular for loop so we know which element we are looking at
            //if newTicket is higher or equal priority than the this element, add it in front of this one, and return
            if (newTicketPriority >= ticketQueue.get(x).getPriority()) {
                ticketQueue.add(x, newTicket);
                return;}}
        //Will only get here if the ticket is not added in the loop
        //If that happens, it must be lower priority than all other tickets. So, add to the end.
        ticketQueue.addLast(newTicket);
    }

    private void addToOpenTickets(){
        listModel = new DefaultListModel<String>();
        openTicketsList.setModel(listModel);
        listModel.clear();
        for (Ticket tik : ticketQueue ) {
            listModel.addElement(tik.toString());
        }
    }

    private void addToResolvedTickets(){
        listModel = new DefaultListModel<String>();
        resolvedTicketList.setModel(listModel);
        listModel.clear();
        for (Ticket tik : resolvedTickets) {
            listModel.addElement(tik.toString());
        }
    }
}
