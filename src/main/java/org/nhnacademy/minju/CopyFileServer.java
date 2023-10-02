package org.nhnacademy.minju;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

/**
 * .FileServer에 파일을 복사해 보내는 것이 추가된 문제
 */
public class CopyFileServer {

    public static final int LISTENING_PORT = 32007;

    /**
     * .FileInputSteam.read()으로 파일을 바이트 단위로 읽고 outputstream으로 전송한다.
     *
     * @param args args
     */
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

        try (BufferedReader incoming = new BufferedReader(new InputStreamReader(
                client.getInputStream()))) {


            String messageIn = incoming.readLine();
            System.out.println(messageIn);

            if (messageIn.equals("INDEX")) {
                File directory = new File(System.getProperty("user.home"));
                String[] files = directory.list();
                System.out.println(Arrays.toString(files));
                sendMsg(client, Arrays.toString(files));
            } else if (messageIn.startsWith("GET")) {
                String fileName = messageIn.substring(3).trim();
                File file = new File(fileName);
                if (file.isFile()) {
                    // send "OK"
                    // send content of file and close connection
                    sendMsg(client, "OK");

                    try (FileInputStream fileInputStream = new FileInputStream(file)) {
                        OutputStream outputStream = client.getOutputStream(); // 전송
                        int temp;
                        while ((temp = fileInputStream.read()) != -1) { // 파일로부터 바이트로 입력 받아 바이트 단위로 출력
                            outputStream.write(temp);
                        }
                    } // 파일

                } else {
                    // // send "ERROR, error msg" and close connection
                    sendMsg(client, "ERROR : " + fileName + " is not file name");
                }

            } else {
                sendMsg(client, "입력은 INDEX거나 GET <fileName>이어야 합니다.");
            }
            client.close();
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
