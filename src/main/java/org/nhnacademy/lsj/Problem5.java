package org.nhnacademy.lsj;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * This program lets the user keep a persistent "phone book" that
 * contains names and phone numbers.  The data for the phone book
 * is stored in a file in the user's home directory.
 * <p>
 * The program is meant only as a demonstration of files and XML.
 * The data file uses XML to represent the phone book data.
 */
public class Problem5 {

    /**
     * The name of the file in which the phone book data is kept.  The
     * file is stored in the user's home directory.  The "." at the
     * beginning of the file name means that the file will be a
     * "hidden" file on Unix-based computers, including Linux and
     * MacOS.
     */
    private static String DATA_FILE_NAME = ".phone_book_demo";


    public static void problem5() {


        String name, number;  // Name and number of an entry in the directory
        // (used at various places in the program).

        TreeMap<String, String> phoneBook;   // Phone directory data structure.
        // Entries are name/number pairs.

        phoneBook = new TreeMap<String, String>();


        /* Create a dataFile variable of type File to represent the
         * data file that is stored in the user's home directory.
         */

        File userHomeDirectory = new File(System.getProperty("user.home"));
        File dataFile = new File(userHomeDirectory, DATA_FILE_NAME);


        /* If the data file already exists, then the data in the file is
         * read and is used to initialize the phone directory.
         */

        if (!dataFile.exists()) {
            System.out.println("No phone book data file found.  A new one");
            System.out.println("will be created, if you add any entries.");
            System.out.println("File name:  " + dataFile.getAbsolutePath());
        } else {
            System.out.println("Reading phone book data...");
            try {

                DocumentBuilder docReader = DocumentBuilderFactory.newInstance().newDocumentBuilder();

                // DocumentBuilder 를 가지고 XML을 파싱할 수 있음

                Document xmldoc = docReader.parse(dataFile); // 실제 작업을 수행함


                // xmldoc = xml문서를 나타내는 객체


                // DOM 에 필요한 Type은 Document , Node , Element , NodeList 임

                // XML 이랑 비슷하게 표준으로 정해져 있는 거야

                // Document Type 객체는 전체 XML 문서를 나타냄 .

                Element root = xmldoc.getDocumentElement();

                // 문서의 root 요소를 나타내는 Element Type의 값을 반환합니다. , 다른 element를 포함하는 최상위 요소


                if (!root.getTagName().equals("phone_directory")) // 이름이 포함된 문자열을 반환한다. <curve>태그의 이름은 curve라는 문자열
                {
                    throw new Exception();
                }

                NodeList nodes = root.getChildNodes(); // 루트의 모든 자식노드를을 저장해 , 배열은 아니지만 for 돌릴수 있음

                for (int i = 0; i < nodes.getLength(); i++) {
                    if (nodes.item(i) instanceof Element) { // node 유형을 반환함

                        Element entry = (Element) nodes.item(i);

                        if (!entry.getTagName().equals("entry")) {
                            throw new Exception();
                        }

                        String entryName = entry.getAttribute("name"); // 해당 속성의 값을 넣어준다.
                        String entryNumber = entry.getAttribute("number");

                        if (entryNumber.isEmpty()) {
                            throw new Exception();
                        }

                        phoneBook.put(entryName, entryNumber);
                    }
                }
            } catch (Exception e) {
                System.out.println("Error in read phone book data file.");
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
                out.print("  <entry name='");
                out.print(entry.getKey());
                out.print("' number='");
                out.print(entry.getValue());
                out.println("'/>");
            }

            out.println("</phone_directory>");
            out.close();
            if (out.checkError()) {
                System.out.println("ERROR: Some error occurred while writing data file.");
            } else {
                System.out.println("Done.");
            }
        }

    }

}


class Practice {

    /**
     *
     */


    private static String DATA_FILE_NAME = ".phone_book_demo";


    public static void main(String[] args) {


        String name, number;  // Name and number of an entry in the directory
        // (used at various places in the program).

        TreeMap<String, String> phoneBook;   // Phone directory data structure.
        // Entries are name/number pairs.

        phoneBook = new TreeMap<String, String>();


        /* Create a dataFile variable of type File to represent the
         * data file that is stored in the user's home directory.
         */

        File userHomeDirectory = new File(System.getProperty("user.home"));
        File dataFile = new File(userHomeDirectory, DATA_FILE_NAME);

        File dataFile2 = new File(userHomeDirectory, ".phone_book_demo2");



        /* If the data file already exists, then the data in the file is
         * read and is used to initialize the phone directory.
         */


        if (!dataFile.exists()) {
            System.out.println("No phone book data file found.  A new one");
            System.out.println("will be created, if you add any entries.");
            System.out.println("File name:  " + dataFile.getAbsolutePath());
            return;
        }

        System.out.println("Reading phone book data...");


        try {

            DocumentBuilder docReader = DocumentBuilderFactory.newInstance().newDocumentBuilder();

            // DocumentBuilder 를 가지고 XML을 파싱할 수 있음

            Document xmldoc = docReader.parse(dataFile); // 실제 작업을 수행함


            // xmldoc = xml문서를 나타내는 객체


            // DOM 에 필요한 Type은 Document , Node , Element , NodeList 임

            // XML 이랑 비슷하게 표준으로 정해져 있는 거야

            // Document Type 객체는 전체 XML 문서를 나타냄 .

            Element root = xmldoc.getDocumentElement();

            // 문서의 root 요소를 나타내는 Element Type의 값을 반환합니다. , 다른 element를 포함하는 최상위 요소


            System.out.println(root.getTagName());


            if (!root.getTagName().equals("phone_directory")) // 이름이 포함된 문자열을 반환한다. <curve>태그의 이름은 curve라는 문자열
            {
                throw new Exception();
            }
            NodeList nodes = root.getChildNodes(); // 루트의 모든 자식노드를을 저장해 , 배열은 아니지만 for 돌릴수 있음

            for (int i = 0; i < nodes.getLength(); i++) {
                if (nodes.item(i) instanceof Element) { // node 유형을 반환함

                    Element entry = (Element) nodes.item(i);

                    if (!entry.getTagName().equals("entry")) {
                        throw new Exception();
                    }

                    String entryMoney = entry.getAttribute("name"); // 해당 속성의 값을 넣어준다.
                    String entryNumber = entry.getAttribute("number");


                    phoneBook.put(entryMoney, entryNumber);
                }
            }
        } catch (Exception e) {
            System.out.println("Error in read phone book data file.");
            System.out.println("File name:  " + dataFile.getAbsolutePath());
            System.out.println("This program cannot continue.");
            System.exit(1);
        }


        PrintWriter out;

        try {
            out = new PrintWriter(new FileWriter(dataFile2));
        } catch (
                IOException e) {
            System.out.println("ERROR: Can't open data file for output.");
            return;
        }

        out.println("<?xml version=\"1.0\"?>");
        out.println("<phone_directory>");

        for (
                Map.Entry<String, String> entry : phoneBook.entrySet()) {
            out.print("  <entry name='");
            out.print(entry.getKey());
            out.print("' number='");
            out.print(entry.getValue());
            out.println("'/>");
        }

        out.println("</phone_directory>");
        out.close();
        if (out.checkError()) {
            System.out.println("ERROR: Some error occurred while writing data file.");
        } else {
            System.out.println("Done.");
        }


    }
}
