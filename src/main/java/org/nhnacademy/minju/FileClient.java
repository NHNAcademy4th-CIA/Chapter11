package org.nhnacademy.minju;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * This program opens a connection to a computer specified
 * as the first command-line argument.  If no command-line
 * argument is given, it prompts the user for a computer
 * to connect to.  The connection is made to
 * the port specified by LISTENING_PORT.  The program reads one
 * line of text from the connection and then closes the
 * connection.  It displays the text that it read on
 * standard output.  This program is meant to be used with
 * the server program, DateServer, which sends the current
 * date and time on the computer where the server is running.
 */
public class FileClient {

    public static final int LISTENING_PORT = 32007;

    public static void main(String[] args) {

        String hostName;         // Name of the server computer to connect to.
        Socket connection;       // A socket for communicating with server.
        BufferedReader incoming; // For reading data from the connection.
        OutputStream outputStream;
        PrintWriter writer;

        /* Get computer name from command line. */

        if (args.length > 0) {
            hostName = args[0];
        } else {
            Scanner stdin = new Scanner(System.in);
            System.out.print("Enter computer name or IP address: ");
            hostName = stdin.nextLine();
        }

        /* Make the connection, then read and display a line of text. */

        try {
            connection = new Socket(hostName, LISTENING_PORT);
            // INDEX / GET
            writer = new PrintWriter(connection.getOutputStream(), true);
            Scanner scanner = new Scanner(System.in);
            writer.println(scanner.nextLine());

            String lineFromServer;
            incoming = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));
            while ((lineFromServer = incoming.readLine()) != null) {

//                if (lineFromServer == null) {
//                    // A null from incoming.readLine() indicates that
//                    // end-of-stream was encountered.
//                    throw new IOException("Connection was opened, " +
//                            "but server did not send any data.");
//                }
                System.out.println(lineFromServer);
            }
        } catch (Exception e) {
            System.out.println("Error:  " + e);
        }

    }  // end main()


} //end class DateClient
