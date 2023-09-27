package org.nhnacademy.jungbum;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/***
 * XML처리
 */
public class Quiz5 {
    public Quiz5() {
        new XML();
    }
}

/***
 * 제공된 XML클래스
 */
class XML {
    private Logger logger = LoggerFactory.getLogger(Quiz5.class);
    private static String DATA_FILE_NAME = ".phone_book_demo";


    public XML() {
        String name, number;

        TreeMap<String, String> phoneBook;   // Phone directory data structure.
        // Entries are name/number pairs.

        phoneBook = new TreeMap<String, String>();


        /* Create a dataFile variable of type File to represent the
         * data file that is stored in the user's home directory.
         */

        File userHomeDirectory = new File(System.getProperty("user.home"));
        File dataFile = new File(userHomeDirectory, DATA_FILE_NAME);


        /* If the data file already exists, then the data in the file is
         * read and is used to initialize the phone directory.  The format
         * of the file must be as follows:  Each line of the file represents
         * one directory entry, with the name and the number for that entry
         * separated by the character '%'.  If a file exists but does not
         * have this format, then the program terminates; this is done to
         * avoid overwriting a file that is being used for another purpose.
         */

        if (!dataFile.exists()) {
            logger.info("No phone book data file found.  A new one");
            logger.info("will be created, if you add any entries.");
            logger.info("File name:  {}", dataFile.getAbsolutePath());
        } else {
            logger.info("Reading phone book data...");
            try (Scanner scanner = new Scanner(dataFile)) {
                DocumentBuilder docReader =
                        DocumentBuilderFactory.newInstance().newDocumentBuilder();
                Document xmldoc = docReader.parse(dataFile);
                Element root = xmldoc.getDocumentElement();
                if (!root.getTagName().equals("phone_directory"))
                    throw new IllegalArgumentException("루트가존재하지 않음");
                NodeList nodes = root.getChildNodes();
                for (int i = 0; i < nodes.getLength(); i++) {
                    if (nodes.item(i) instanceof Element) {
                        Element entry = (Element) nodes.item(i);
                        if (!entry.getTagName().equals("entry"))
                            throw new IllegalArgumentException("entry가 존재하지 않음");
                        String entryName = entry.getAttribute("name");
                        String entryNumber = entry.getAttribute("number");
                        if (entryName.length() == 0 || entryNumber.length() == 0)
                            throw new IllegalArgumentException("이름이나 번호가 0이다.");
                        phoneBook.put(entryName, entryNumber);
                    }
                }
            } catch (IOException | ParserConfigurationException | SAXException e) {
                logger.info("Error in phone book data file.");
                logger.info("File name:  {}", dataFile.getAbsolutePath());
                logger.info("This program cannot continue.");
                logger.info("{}", 1);
            }
        }


        /* Read commands from the user and carry them out, until the
         * user gives the "Exit from program" command.
         */

        Scanner in = new Scanner(System.in);
        boolean changed = false;  // Have any changes been made to the directory?

        mainLoop:
        while (true) {
            logger.info("\nSelect the action that you want to perform:");
            logger.info("   1.  Look up a phone number.");
            logger.info("   2.  Add or change a phone number.");
            logger.info("   3.  Remove an entry from your phone directory.");
            logger.info("   4.  List the entire phone directory.");
            logger.info("   5.  Exit from the program.");
            logger.info("Enter action number (1-5):  ");
            int command;
            if (in.hasNextInt()) {
                command = in.nextInt();
                in.nextLine();
            } else {
                logger.info("\nILLEGAL RESPONSE.  YOU MUST ENTER A NUMBER.");
                in.nextLine();
                continue;
            }
            switch (command) {
                case 1:
                    logger.info("\nEnter the name whose number you want to look up: ");
                    name = in.nextLine().trim().toLowerCase();
                    number = phoneBook.get(name);
                    if (number == null)
                        logger.info("\nSORRY, NO NUMBER FOUND FOR " + name);
                    else
                        logger.info("\nNUMBER FOR " + name + ":  " + number);
                    break;
                case 2:
                    logger.info("\nEnter the name: ");
                    name = in.nextLine().trim().toLowerCase();
                    if (name.length() == 0)
                        logger.info("\nNAME CANNOT BE BLANK.");
                    else if (name.indexOf('%') >= 0)
                        logger.info("\nNAME CANNOT CONTAIN THE CHARACTER \"%\".");
                    else {
                        logger.info("Enter phone number: ");
                        number = in.nextLine().trim();
                        if (number.length() == 0)
                            logger.info("\nPHONE NUMBER CANNOT BE BLANK.");
                        else {
                            phoneBook.put(name, number);
                            changed = true;
                        }
                    }
                    break;
                case 3:
                    logger.info("\nEnter the name whose entry you want to remove: ");
                    name = in.nextLine().trim().toLowerCase();
                    number = phoneBook.get(name);
                    if (number == null)
                        logger.info("\nSORRY, THERE IS NO ENTRY FOR {}", name);
                    else {
                        phoneBook.remove(name);
                        changed = true;
                        logger.info("\nDIRECTORY ENTRY REMOVED FOR {}", name);
                    }
                    break;
                case 4:
                    logger.info("\nLIST OF ENTRIES IN YOUR PHONE BOOK:\n");
                    for (Map.Entry<String, String> entry : phoneBook.entrySet())
                        logger.info("   {}: {}", entry.getKey(), entry.getValue());
                    break;
                case 5:
                    logger.info("\nExiting program.");
                    break mainLoop;
                default:
                    logger.info("\nILLEGAL ACTION NUMBER.");
            }
        }


        /* Before ending the program, write the current contents of the
         * phone directory, but only if some changes have been made to
         * the directory.
         */

        if (changed) {
            logger.info("Saving phone directory changes to file {} ...", dataFile.getAbsolutePath());
            PrintWriter out;
            try {
                out = new PrintWriter(new FileWriter(dataFile));
            } catch (IOException e) {
                logger.info("ERROR: Can't open data file for output.");
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
            if (out.checkError())
                logger.info("ERROR: Some error occurred while writing data file.");
            else
                logger.info("Done.");
        }

    }

}