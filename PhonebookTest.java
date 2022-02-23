// Alexander Wolf
// CS 145

package awphonebook; // part of awphonebook package

import java.io.File; // import File class
import java.io.FileNotFoundException; // import FileNotFoundException class
import java.io.PrintStream; // import PrintStream class
import java.util.Scanner; // import Scanner class

// begin PhonebookTest class
public class PhonebookTest {
    // begin main method - initialize objects and run program
    public static void main(String[] args) {
        // create new PhonebookManager object - links/configures list nodes
        PhonebookManager manager = new PhonebookManager();
        // create new anInterface object - provides interface with which
        // user may add to, remove from, and otherwise edit linked list
        PhonebookInterface anInterface = new PhonebookInterface(manager);

        // add several default nodes/contacts for testing purposes
        // comment out to begin with empty linked list/phonebook
        addTestNames(manager);

        // present phonebook interface menu to user and
        // handle command input
        anInterface.giveMenu();
    } // end main method

    // post: several default contacts have been added to phonebook
    // for testing purposes
    public static void addTestNames(PhonebookManager phonebookManager) {
        phonebookManager.addSorted("Jones", "Dave", "123 Main St",
                "Bellingham", "(360) 123-4567");
        phonebookManager.addSorted("Smith", "Mary", "456 Front St",
                "Bellingham", "(360) 111-2222");
        phonebookManager.addSorted("Miller", "Ann", "7891 Smith Rd",
                "Bellingham", "(360) 248-1357");
        phonebookManager.addSorted("Clark", "Charles", "2120 Scenic Ave",
                "Bellingham", "(360) 998-8776");
        phonebookManager.addSorted("Wilson", "Sam", "227 Mountain Rd",
                "Bellingham", "(360) 120-4059");
    } // end addTestNames
} // end PhonebookTest class

// begin PhonebookNode class
class PhonebookNode {
    // declare private fields - node data
    private String first, last, address, city, phoneNum;
    private PhonebookNode next; // link to next node in list

    // post: constructs a PhonebookNode with String null, data 0
    // and null link
    public PhonebookNode() {
        // this(...) notation calls third constructor
        this(null, null, null, null, null, null);
    } // end PhonebookNode constructor

    // post: constructs a node with given data and null link
    public PhonebookNode(String last, String first, String address,
         String city, String phoneNum) {
            // this(...) notation calls third constructor
            this(last, first, address, city, phoneNum, null);
    } // end PhonebookNode constructor

    // post: constructs a node with given data and given link
    public PhonebookNode(String last, String first, String address,
        String city, String phoneNum, PhonebookNode next) {
            this.last = last; // last name String passed in
            this.first = first; // first name String passed in
            this.address = address; // address String passed in
            this.city = city; // city String passed in
            this.phoneNum = phoneNum; // phone number String passed in
            this.next = next; // PhonebookNode reference passed in
    } // end PhonebookNode constructor

    // begin accessors/get methods
    public String getLast() {
        return last;
    } // end getLast
    public String getFirst() {
        return first;
    } // end getFirst
    public String getAddress() {
        return address;
    } // end getAddress
    public String getCity() {
        return city;
    } // end getCity
    public String getPhoneNum() {
        return phoneNum;
    } // end getter methods
    public PhonebookNode getNext() {
        return next;
    }

    // begin mutators/set methods
    public void setLast(String last) {
        this.last = last;
    } // end setLast
    public void setFirst(String first) {
        this.first = first;
    } // end setFirst
    public void setAddress(String address) {
        this.address = address;
    } // end setAddress
    public void setCity(String city) {
        this.city = city;
    } // end setCity
    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    } // end setter methods
    public void setNext(PhonebookNode next) {
        this.next = next;
    }
} // end PhonebookNode class

// begin PhonebookManager class
class PhonebookManager {
    // first node in list
    private PhonebookNode front; // first node in list

    // post: constructs an empty list
    public PhonebookManager() {
        front = null;
    } // end PhonebookManager constructor

    // post: appends the given values to the end of the list
    public void add(String last, String first, String address,
                    String city, String phoneNum) {
        if (front == null) {
            // create new node at front of list
            front = new PhonebookNode(last, first, address, city,
                    phoneNum);
        } else {
            // traverse list to find first null reference, indicating
            // end of list
            PhonebookNode current = front.getNext();
            while (current.getNext() != null) {
                current = current.getNext();
            } // end while loop
            // create new node at null reference at end of list
            current.setNext(new PhonebookNode(last, first, address,
                    city, phoneNum));
        } // end if/else
    } // end add

    // post: adds name to list in alphabetical sort order
    // uses String compareTo() method to compare Strings lexicographically
    public void addSorted(String last, String first, String address,
                          String city, String phoneNum) {
        // if last name of front node is alphabetically out of order from
        // last name passed in, insert new node using String parameters at
        // front of list
        if (front == null || front.getLast().compareTo(last) > 0) {
            front = new PhonebookNode(last, first, address,
                    city, phoneNum, front);
        } else {
            PhonebookNode current = front;
            // iterate through list, comparing alphabetical order of
            // last name of node ahead and last name parameter
            while (current.getNext() != null
                    && current.getNext().getLast().compareTo(last) < 0) {
                current = current.getNext(); // progress to next position
            }
            // stop one position early to add new node at final element
            current.setNext(new PhonebookNode(last, first, address,
                    city, phoneNum, current.getNext()));
        } // end if/else
    } // end addSorted

    // post: list has been sorted alphabetically
    public void sort() {
        PhonebookNode current = front;
        // if size is 0 or 1, no sorting needed
        if (size() > 1) {
            // outer loop consists of one pass over entire list
            while (current.getNext() != null) {
                PhonebookNode index = current.getNext();
                // inner loop compares pairs of nodes
                while (index != null) {
                    // if second node comes before first alphabetically,
                    // switch node data
                    if (firstAlphabetically(index, current)) {
                        switchData(current, index);
                    } // end if
                    // move reference one node further after each
                    // pair comparison
                    index = index.getNext();
                } // end inner while
                // move reference one node further after each pass
                current = current.getNext();
            } // end outer while
        } // end if
    } // end sort method

    // post: two node last name fields have been compared
    // lexicographically
    public boolean firstAlphabetically(PhonebookNode one,
                                       PhonebookNode two) {
        return one.getLast().compareTo(two.getLast()) < 0;
    } // end firstAlphabetically

    // post: data in fields of both nodes passed in has been replaced
    // with data of other node
    public void switchData(PhonebookNode one, PhonebookNode two) {
        // temporarily hold data from one node
        PhonebookNode temp = one;

        // replace data in node one with data of node two
        one.setLast(two.getLast());
        one.setFirst(two.getFirst());
        one.setAddress(two.getAddress());
        one.setCity(two.getCity());
        one.setPhoneNum(two.getPhoneNum());

        // replace data in node two with data of node one
        two.setLast(last);
        two.setFirst(first);
        two.setAddress(address);
        two.setCity(city);
        two.setPhoneNum(phoneNum);
    } // end switchData

    // post: removes entry given name
    public void remove(String last, String first) {
        // use String contains() method to check if first and last
        // name fields of node match parameters passed in
        if (front.getLast().contains(last)
                && front.getFirst().contains(first)) {
            // move reference to second node, disconnecting first node
            front = front.getNext();
        } else {
            PhonebookNode current = front;
            try { // scan for entry
                while (!current.getNext().getLast().contains(last)
                        || !current.getNext().getFirst().contains(first)) {
                    current = current.getNext();
                }
                current.setNext(current.getNext().getNext());
            } catch (NullPointerException e) {
                // do nothing, entry does not exist
            } // end try/catch
        } // end if/else
    } // end remove

    // post: removes value at the given index
    public void remove(int index) {
        if (index == 0) {
            front = front.getNext();
        } else {
            PhonebookNode current = nodeAt(index - 1);
            current.setNext(current.getNext().getNext());
        } // end if/else
    } // end remove

    // post: edits specified field of entry
    public void edit(String last, String first, Field field,String edit) {
        if (front.getLast().contains(last)
                && front.getFirst().contains(first)) {
            fieldEditSwitch(field, edit, front);
        } else {
            PhonebookNode current = front;
            try { // scan for entry
                while (!current.getNext().getLast().contains(last)
                        || !current.getNext().getFirst().contains(first)) {
                    current = current.getNext();
                }
                fieldEditSwitch(field, edit, current.getNext());
            } catch (NullPointerException e) {
                // do nothing, entry does not exist
            } // end try/catch
        } // end if/else
    } // end edit

    // post: returns a reference to the node at the given index
    public PhonebookNode nodeAt(int index) {
        PhonebookNode current = front;
        for (int i = 0; i < index; i++) {
            // traverse linked list to index value
            current = current.getNext();
        }
        return current;
    } // end nodeAt

    // post: returns a reference to first node matching a given name
    public PhonebookNode findNode(String last, String first) {
        int index = 0; // initialize index counter
        PhonebookNode current = front;
        // call String equals() method to compare node fields with
        // first and last name parameters
        while (current != null && (!current.getLast().equals(last)
                || !current.getFirst().equals(first))) {
            index++; // increment counter
            current = current.getNext();
        } // end while loop
        return nodeAt(index); // return PhonebookNode
    } // end findNode

    // post: returns the current number of elements in the list
    public int size() {
        int count = 0; // initialize index counter
        PhonebookNode current = front;
        while (current != null) {
            current = current.getNext();
            count++; // increment counter
        }
        return count;
    } // end size

    // post: returns comma-separated, bracketed version of list
    public String toString() {
        if (front == null) {
            return "[]"; // empty list
        } else {
            // concatenate String from node fields
            String result = "[" + front.getLast() + " " + front.getFirst()
                    + " " + front.getAddress() + " " + front.getCity() + " "
                    + front.getPhoneNum();
            PhonebookNode current = front.getNext();
            while (current != null) {
                result += ", " + current.getLast() + " " + current.getFirst()
                        + " " + current.getAddress() + " " + current.getCity()
                        + " " + current.getPhoneNum();
                current = current.getNext();
            }
            result += "]";
            return result;
        } // end if/else
    } // end toString

    // post: returns list in column
    public String toColumn() {
        if (front == null) {
            return "Contact list is empty.\n";
        } else {
            // concatenate String from node fields
            String result =  front.getLast() + " " + front.getFirst()
                    + "  " + front.getAddress();
            result += getSpacer(result) + " "+ front.getCity() + " "
                    + front.getPhoneNum() + "\n";
            PhonebookNode current = front.getNext();
            while (current != null) {
                String result2 = "";
                result2 += current.getLast() + " " + current.getFirst()
                        + "  " + current.getAddress();
                result2 += getSpacer(result2) + " " + current.getCity()
                        + " " + current.getPhoneNum() + "\n";
                result += result2;
                current = current.getNext();
            } // end while
            return result;
        } // end if/else
    } // end toColumn

    // post: linked list has been cleared of nodes
    public void clear() {
        front = null;
    } // end clear

    // pot: returns boolean indicating if list is empty
    public boolean isEmpty() {
        return front == null;
    } // end isEmpty

    // post: returns array of Strings with first and last name
    // in 0 and 1 indices
    public String[] splitFirstAndLast(String fullName) {
        // call String split() method and separate name by space
        String[] splitFirstAndLast = fullName.split(" ");
        for (int i = 0; i <= 1; i++) {
            splitFirstAndLast[i] =
                    // make first character of name upper case
                    splitFirstAndLast[i].substring(0, 1).toUpperCase()
                            + splitFirstAndLast[i].substring(1);
        }
        return splitFirstAndLast; // return String array
    } // end splitFirstAndLast

    // post: calls set method for given node field with switch
    // using Field enum
    public void fieldEditSwitch(Field field, String edit,
                                PhonebookNode node) {
        switch (field) {
            case LAST: // call last name setter method
                node.setLast(edit);
                break;
            case FIRST: // call first name setter method
                node.setFirst(edit);
                break;
            case ADDRESS: // call address setter method
                node.setAddress(edit);
                break;
            case CITY: // call city setter method
                node.setCity(edit);
                break;
            default: // call phone number setter method
                node.setPhoneNum(edit);
                break;
        } // end switch
    } // end fieldEditSwitch

    // post: returns String positioning phonebook information
    // into aligned columns
    public String getSpacer(String s) {
        String spacer = " ";
        for (int i = 0; i < 70 - s.length(); i++) {
            spacer += ".";
        }
        spacer += " ";
        return spacer;
    } // end getSpacer
} // end PhonebookManager class

// begin PhonebookInterface class
class PhonebookInterface {
    private PhonebookManager manager; // null

    // post: PhonebookManager field has been initialized with
    // object passed in to constructor
    public PhonebookInterface(PhonebookManager manager) {
        this.manager = manager;
    } // end constructor

    // post: PhoneBook interface menu has been printed, user
    // input has been handled until program has been terminated
    public void giveMenu() {
        // create Scanner object to handle user input
        Scanner console = new Scanner(System.in);
        String selection; // initialize user selection String
        do {
            // interface menu
            System.out.println("1 to add a contact\n"
                    + "2 to edit a contact\n"
                    + "3 to delete a contact\n"
                    + "4 to search for a contact\n"
                    + "5 to view all contacts\n"
                    + "6 to re-sort contacts\n"
                    + "7 to clear all current contacts\n"
                    + "8 to print contact list to file\n"
                    + "9 to quit");
            // user menu selection
            selection = console.nextLine();

            // select action from user input
            switch (selection) {
                case "1": // add a contact
                    addContact(console);
                    break;

                case "2": // edit a contact
                    selectEditField(console);
                    break;

                case "3": // remove a contact
                    removeContact(console);
                    break;

                case "4": // search for a contact
                    findContact(console);
                    break;

                case "5": // view all contacts in a column
                    // call PhonebookManager toColumn method
                    String listColumn = manager.toColumn();
                    System.out.println(listColumn);
                    break;

                case "6": // sort contacts alphabetically
                    if (manager.isEmpty()) {
                        System.out.println("Contact list is empty.\n");
                    } else {
                        manager.sort(); // call sort method
                        System.out.println("Contact list has been sorted.\n");
                    }
                    break;

                case "7": // remove all contacts from list
                    clearContacts(console);
                    break;

                case "8": // print contact list to file
                    printListToFile(console);
                    break;

                case "9": // quit program
                    break;

                default: // any other user input
                    System.out.println("Please enter a valid command.\n");
                    break;
            } // end outer switch
        } while (!selection.equals("9")); // end do while
    } // end giveMenu

    // post: user has provided data for PhoneBookNode fields and node
    // has been added in alphabetically sorted order to linked list
    // by calling PhonebookManager addSorted method
    public void addContact(Scanner console) {
        System.out.print("First and last name: ");
        // split full name into String array containing
        // first and last names
        String addFirstAndLast[] =
                manager.splitFirstAndLast(console.nextLine());
        System.out.print("Address: ");
        String address = console.nextLine(); // address input
        System.out.print("City: ");
        String city = console.nextLine(); // city input
        System.out.print("Phone number: ");
        String phoneNum = console.nextLine(); // phone number input

        // call PhonebookManager's addSorted method and add contact
        // to linked list, alphabetically sorted
        manager.addSorted(addFirstAndLast[1],
                addFirstAndLast[0], address, city, phoneNum);
        System.out.println("Contact has been added.\n");
    } // end addContact

    // post: user has provided name of contact to be removed and
    // node has been removed from list by calling PhonebookManager
    // remove method
    public void removeContact(Scanner console) {
        System.out.print("First and last name: ");
        // split full name into String array containing first and
        // last names
        String[] removeFirstAndLast =
                manager.splitFirstAndLast(console.nextLine());
        // call PhonebookManager remove method and remove contact
        manager.remove(removeFirstAndLast[1],
                removeFirstAndLast[0]);
        System.out.println("Contact has been removed.\n");
    } // end removeContact

    // post: user has provided data and an attempt has been made
    // to locate and print data from matching node in linked list
    public void findContact(Scanner console) {
        System.out.print("First and last name: ");
        // split full name into String array containing first and
        // last names
        String[] searchFirstAndLast =
                manager.splitFirstAndLast(console.nextLine());
        // call PhonebookManager findNode method and
        // return node matching name if found
        PhonebookNode foundNode =
                manager.findNode(searchFirstAndLast[1],
                        searchFirstAndLast[0]);

        String result;
        try { // concatenate contact information and print
            result = foundNode.getLast() + " " + foundNode.getFirst()
                    + " " + foundNode.getAddress();
            result += manager.getSpacer(result) + " "
                    + foundNode.getCity() + " "
                    + foundNode.getPhoneNum();
            System.out.println(result);
            // if findNode returned null
        } catch (NullPointerException e) {
            System.out.println("Contact not found.\n");
        } // end try/catch
    } // end findContact

    // post: field of phonebook node has been edited
    public void editContact(Field editField) {
        // create new Scanner object
        Scanner console = new Scanner(System.in);
        boolean success = false;
        // array for first and last name
        String[] editFirstAndLast = new String[1];

        System.out.print("\nContact first and last name: ");
        do {
            try {
                // split full name into String array containing first and
                // last names
                editFirstAndLast =
                        manager.splitFirstAndLast(console.nextLine());
                success = true; // becomes true if no exceptions caught
            } catch (Exception e) {
                // catch exception if user only enters one token
                System.out.println("Please enter a valid first and "
                        + "last name.");
            } // end try/catch
        } while (!success);

        // select prompt using enum parameter passed in
        if (editField == Field.PHONENUM) {
            System.out.print("Enter new phone number: ");
        } else if (editField == Field.ADDRESS) {
            System.out.print("Enter new address: ");
        } else if (editField == Field.CITY) {
            System.out.print("Enter new city: ");
        } else if (editField == Field.FIRST) {
            System.out.print("Enter new first name: ");
        } else {
            System.out.print("Enter new last name: ");
        } // end if/else

        String newInput = console.nextLine();
        // call PhonebookManager edit method and edit specified field
        // with user input
        manager.edit(editFirstAndLast[1], editFirstAndLast[0],
                editField, newInput);
        System.out.println("Contact has been updated.\n");
    } // end editContact

    // post: linked list has been printed to user-defined file in
    // formatted String
    public void printListToFile(Scanner console) {
        // initialize PrintStream object to null
        PrintStream outputToFile = null;
        System.out.println("Output file name: ");
        // user input for file name
        String output = console.next();
        console.nextLine(); // consume newline character
        try {
            outputToFile = new PrintStream(new File(output));
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
        }
        // print to output file with PrintStream object
        outputToFile.println(manager.toColumn());
        System.out.println("Your list has been printed to the file \""
                + output + "\"\n");
    } // end printListToFile

    // post: user has been prompted to keep or clear current contact
    // list, and if clear has been selected, linked list is empty
    public void clearContacts(Scanner console) {
        System.out.println("Are you sure you want to remove "
                + "all contacts(y/n)? ");
        // user chooses to permanently clear list
        if (console.nextLine().equals("y")) {
            manager.clear(); // clear list
            System.out.println("All contacts have been removed.\n");
        } else {
            System.out.println("Your contacts have not "
                    + "been removed.\n");
        } // end if/else
    } // end clearContacts

    // post: switch/case has been used to select field to edit based
    // on user input
    public void selectEditField(Scanner console) {
        System.out.print("Edit contact phone number(1), "
                + "address(2), city(3),\nfirst name(4), "
                + "or last name(5)? ");
        String editSelection = console.nextLine();
        // user selects field to edit
        switch (editSelection) {
            case "1": // edit phone number
                editContact(Field.PHONENUM);
                break;
            case "2": // edit address
                editContact(Field.ADDRESS);
                break;
            case "3": // edit city
                editContact(Field.CITY);
                break;
            case "4": // edit first name
                editContact(Field.FIRST);
                break;
            case "5": // edit last name
                editContact(Field.LAST);
                break;
            default: // any other input
                System.out.println("Invalid input.\n");
                break;
        } // end switch
    } // end editSwitch method
} // end PhonebookInterface class

// begin Field enum - store list of PhonebookNode field constants
enum Field {
    LAST, FIRST, ADDRESS, CITY, PHONENUM;
} // end Field enum