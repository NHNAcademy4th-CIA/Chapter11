package org.nhnacademy.jungbum;

import java.io.File;
import java.util.Scanner;
import java.net.*;
import java.io.*;

/***
 * 파일 검색 및 다운 클라리언트
 */
public class Quiz3 {
    public static final int LISTENING_PORT = 32007;
    public Quiz3(){
        String hostName;
        Socket connection;
        BufferedReader incoming;

        /* Get computer name from command line. */
            Scanner stdin = new Scanner(System.in);
//            System.out.print("Enter computer name or IP address: ");
//            hostName = stdin.nextLine();
        hostName="127.0.0.1";
        /* Make the connection, then read and display a line of text. */

        try {
            connection = new Socket( hostName, LISTENING_PORT);
            System.out.println("test");
            PrintWriter outgoing;
            outgoing = new PrintWriter(connection.getOutputStream());
            outgoing.println(stdin.next());
            incoming = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()) );
            String lineFromServer;
            while ((lineFromServer=incoming.readLine())!=null) {
                if (lineFromServer == null) {
                    // A null from incoming.readLine() indicates that
                    // end-of-stream was encountered.
                    throw new IOException("Connection was opened, " +
                            "but server did not send any data.");
                }
                System.out.println(lineFromServer);
            }
            incoming.close();
        }
        catch (Exception e) {
            System.out.println("Error:  " + e);
        }
    }

}
