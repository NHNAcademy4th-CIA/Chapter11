package org.nhnacademy.minju;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * .서버에서 지정된 파일의 복사본을 가져와 로컬에 저장
 */
public class CopyFileClient {

    public static final int LISTENING_PORT = 32007;

    /**
     * .copy.txt 에 복사본을 쓴다.
     * 바이트 단위로 읽어
     * @param args
     */
    public static void main(String[] args) {

        String hostName;         // Name of the server computer to connect to.
        Socket connection;       // A socket for communicating with server.
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

            //
            FileOutputStream fileOutputStream = new FileOutputStream("src/main/resources/copy.txt");
            InputStream inputStream = connection.getInputStream();
            int temp;
            while ((temp = inputStream.read()) != -1) {
                fileOutputStream.write(temp);
            }

            inputStream.close();
            fileOutputStream.close();
        } catch (Exception e) {
            System.out.println("Error:  " + e);
        }

    }  // end main()


} //end class DateClient
