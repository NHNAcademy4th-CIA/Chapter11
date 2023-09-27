package org.nhnacademy.minju;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 */
public class Exercise5 {

    private static String DATA_FILE_NAME = ".phone_book_demo";

    /**
     *
     * @param args
     */
    public static void main(String[] args) {

        String name;
        String number;  // Name and number of an entry in the directory
        // (used at various places in the program).

        TreeMap<String, String> phoneBook;   // Phone directory data structure.
        // Entries are name/number pairs.

        phoneBook = new TreeMap<>();


        File userHomeDirectory = new File(System.getProperty("user.home"));
        File dataFile = new File(userHomeDirectory, DATA_FILE_NAME);

        if (!dataFile.exists()) {
            System.out.println("No phone book data file found.  A new one");
            System.out.println("will be created, if you add any entries.");
            System.out.println("File name:  " + dataFile.getAbsolutePath());
        } else {
            System.out.println("Reading phone book data...");
            try {
                DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                Document xmlDoc = documentBuilder.parse(dataFile);

                Element root = xmlDoc.getDocumentElement();
                NodeList nodeList = root.getChildNodes();

                if (!(root.getTagName().equals("phone_directory"))) {
                    throw new Exception();
                }
                for (int i = 0; i < nodeList.getLength(); i++) {
                    if (nodeList.item(i) instanceof Element) {
                        Element entry = (Element) nodeList.item(i);
                        if (!entry.getTagName().equals("entry")) {
                            throw new Exception();
                        }
                        String entryName = entry.getAttribute("name");
                        String entryNumber = entry.getAttribute("number");

                        if (entryName.isEmpty() || entryNumber.isEmpty()) {
                            throw new Exception();
                        }
                        phoneBook.put(entryName, entryNumber);
                    }
                }
            } catch (Exception e) {
                System.out.println("Error in phone book data file.");
                System.out.println("File name:  " + dataFile.getAbsolutePath());
                System.out.println("This program cannot continue.");
                System.exit(1);
            }
        }


        /* Read commands from the user and carry them out, until the
         * user gives the "Exit from program" command.
         */

        Scanner in = new Scanner(System.in);
        boolean changed = false;  // Have any changes been made to the directory?

        mainLoop:
        while (true) {
            System.out.println("\nSelect the action that you want to perform:");
            System.out.println("   1.  Look up a phone number.");
            System.out.println("   2.  Add or change a phone number.");
            System.out.println("   3.  Remove an entry from your phone directory.");
            System.out.println("   4.  List the entire phone directory.");
            System.out.println("   5.  Exit from the program.");
            System.out.println("Enter action number (1-5):  ");
            int command;
            if (in.hasNextInt()) {
                command = in.nextInt();
                in.nextLine();
            } else {
                System.out.println("\nILLEGAL RESPONSE.  YOU MUST ENTER A NUMBER.");
                in.nextLine();
                continue;
            }
            switch (command) {
                case 1:
                    System.out.print("\nEnter the name whose number you want to look up: ");
                    name = in.nextLine().trim().toLowerCase();
                    number = phoneBook.get(name);
                    if (number == null) {
                        System.out.println("\nSORRY, NO NUMBER FOUND FOR " + name);
                    } else {
                        System.out.println("\nNUMBER FOR " + name + ":  " + number);
                    }
                    break;
                case 2:
                    System.out.print("\nEnter the name: ");
                    name = in.nextLine().trim().toLowerCase();
                    if (name.length() == 0) {
                        System.out.println("\nNAME CANNOT BE BLANK.");
                    } else if (name.indexOf('%') >= 0) {
                        System.out.println("\nNAME CANNOT CONTAIN THE CHARACTER \"%\".");
                    } else {
                        System.out.print("Enter phone number: ");
                        number = in.nextLine().trim();
                        if (number.length() == 0) {
                            System.out.println("\nPHONE NUMBER CANNOT BE BLANK.");
                        } else {
                            phoneBook.put(name, number);
                            changed = true;
                        }
                    }
                    break;
                case 3:
                    System.out.print("\nEnter the name whose entry you want to remove: ");
                    name = in.nextLine().trim().toLowerCase();
                    number = phoneBook.get(name);
                    if (number == null) {
                        System.out.println("\nSORRY, THERE IS NO ENTRY FOR " + name);
                    } else {
                        phoneBook.remove(name);
                        changed = true;
                        System.out.println("\nDIRECTORY ENTRY REMOVED FOR " + name);
                    }
                    break;
                case 4:
                    System.out.println("\nLIST OF ENTRIES IN YOUR PHONE BOOK:\n");
                    for (Map.Entry<String, String> entry : phoneBook.entrySet()) {
                        System.out.println("   " + entry.getKey() + ": " + entry.getValue());
                    }
                    break;
                case 5:
                    System.out.println("\nExiting program.");
                    break mainLoop;
                default:
                    System.out.println("\nILLEGAL ACTION NUMBER.");
            }
        }


        /* Before ending the program, write the current contents of the
         * phone directory, but only if some changes have been made to
         * the directory.
         */

        if (changed) {
            System.out.println("Saving phone directory changes to file " +
                    dataFile.getAbsolutePath() + " ...");
            PrintWriter out;
            try {
                out = new PrintWriter(new FileWriter(dataFile));
            } catch (IOException e) {
                System.out.println("ERROR: Can't open data file for output.");
                return;
            }
            out.println("<?xml version=\"1.0\"?>");
            out.println("<phone_directory>");
            for (Map.Entry<String, String> entry : phoneBook.entrySet()) {
                out.print("\t<entry name='");
                out.print(entry.getKey());
                out.print("'");
                out.print("\tnumber='");
                out.print(entry.getValue());
                out.print("'");
                out.println("/>");
            }
            out.print("</phone_directory>");
            out.flush();
            out.close();
            if (out.checkError()) {
                System.out.println("ERROR: Some error occurred while writing data file.");
            } else {
                System.out.println("Done.");
            }
        }
    }
}
