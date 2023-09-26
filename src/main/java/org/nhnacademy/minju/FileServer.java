package org.nhnacademy.minju;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

/**
 * This program is a server that takes connection requests on
 * the port specified by the constant LISTENING_PORT.  When a
 * connection is opened, the program sends the current time to
 * the connected socket.  The program will continue to receive
 * and process connections until it is killed (by a CONTROL-C,
 * for example).  Note that this server processes each connection
 * as it is received, rather than creating a separate thread
 * to process the connection.
 */
public class FileServer {

    public static final int LISTENING_PORT = 32007;

    public static void main(String[] args) {

        ServerSocket listener;  // Listens for incoming connections.
        Socket connection;      // For communication with the connecting program.

        /* Accept and process connections forever, or until some error occurs.
           (Note that errors that occur while communicating with a connected
           program are caught and handled in the sendDate() routine, so
           they will not crash the server.) */

        try {
            listener = new ServerSocket(LISTENING_PORT);
            System.out.println("Listening on port " + LISTENING_PORT);
            while (true) {
                // Accept next connection request and handle it.
                connection = listener.accept();
                clientCommand(connection);
            }
        } catch (Exception e) {
            System.out.println("Sorry, the server has shut down.");
            System.out.println("Error:  " + e);
            return;
        }

    }  // end main()

    private static void clientCommand(Socket client) {
        int BUF_SIZE = 1024 * 7;

        try (BufferedReader incoming = new BufferedReader(new InputStreamReader(
                client.getInputStream()))) {


            String messageIn = incoming.readLine();
            System.out.println(messageIn);

            if (messageIn.equals("INDEX")) {
                File directory = new File("src/main/java/org/nhnacademy");
                String[] files = directory.list();
                System.out.println(Arrays.toString(files));
                sendMsg(client, Arrays.toString(files));
                client.close();
            }
            else if (messageIn.startsWith("GET")) {
                String fileName = messageIn.substring(3).trim();
                StringBuilder sb = new StringBuilder();
                File file = new File(fileName);
                if (file.isFile()) {
                    // send "OK"
                    // send content of file and close connection
                    sendMsg(client, "OK");
                    try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
                        String line;
                        while ((line = br.readLine()) != null) {
                            sb.append(line).append('\n');
                        }
                        System.out.println(sb);
                    }
                    sendMsg(client, sb.toString());
                    client.close();
                }
                else {
                    // // send "ERROR, error msg" and close connection
                    sendMsg(client, "ERROR : " + fileName + " is not file name");
                }
            } else {
                sendMsg(client, "입력은 INDEX거나 GET <fileName>이어야 합니다.");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    /**
     * The parameter, client, is a socket that is already connected to another
     * program.  Get an output stream for the connection, send the current time,
     * and close the connection.
     */
    private static void sendMsg(Socket client, String response) {
        try {
            System.out.println("Connection from " +
                    client.getInetAddress().toString());
            PrintWriter outgoing;   // Stream for sending data.
            outgoing = new PrintWriter(client.getOutputStream());
            outgoing.println(response);
            outgoing.flush();  // Make sure the data is actually sent!
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
    } // end sendDate()


} //end class DateServer
