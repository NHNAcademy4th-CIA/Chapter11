package org.nhnacademy.minju;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * .GET / INDEX 명령을 서버에 보내고
 * 응답을 받아 출력한다.
 */
public class FileClient {
    private static final Logger logger = LoggerFactory.getLogger(FileClient.class);
    public static final int LISTENING_PORT = 32007;

    /**
     * .먼저 ip address나 호스트 이름을 입력받는다. 같은 컴퓨터 내에서 동작하므로 127.0.0.1이나 localhost를 입력하면 된다.
     * 입력 받은 호스트 이름과 포트 번호로 새로운 소켓을 만든다.
     * INDEX나 GET <fileName> 입력을 받아 서버로 전송한다. -> incoming
     *
     * @param args args
     */
    public static void main(String[] args) {

        String hostName;
        Socket connection;
        BufferedReader incoming;
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
            // INDEX / GET 사용자의 입력을 전달
            writer = new PrintWriter(connection.getOutputStream(), true); // outputstream으로부터 새로운 printwriter 객체를 생성
            Scanner scanner = new Scanner(System.in);
            writer.println(scanner.nextLine());

            String lineFromServer;
            incoming = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));
            // 파일 contents를 받아왔을 때 여러 줄일 수 있음
            while ((lineFromServer = incoming.readLine()) != null) {
                System.out.println(lineFromServer);
            }
        } catch (Exception e) {
            System.out.println("Error:  " + e);
        }

    }  // end main()


} //end class DateClient
