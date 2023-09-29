package org.nhnacademy.minju;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * .요청을 기다리고 클라이언트가 스트림을 보내면 보낸 스트림을 받는다.
 * GET / INDEX, 해당하지 않는 경우를 조건문으로 확인하고
 * 파일이면 OK, 디렉토리면 파일 이름이어야 한다는 문자열을 전송한다.
 */
public class FileServer {
    private static final Logger logger = LoggerFactory.getLogger(FileServer.class);
    public static final int LISTENING_PORT = 32007;

    public static void main(String[] args) {

        ServerSocket listener;
        Socket connection;

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
            logger.warn("Sorry, the server has shut down.");
            logger.warn("Error:  " + e);
            return;
        }

    }  // end main()

    /**
     * .getInputStream으로 client로부터 입력을 받아온다. bufferedReader로 랩해 readLine으로 한 줄의 명령을 받을 수 있게 한다.
     * input이 INDEX이면 서버에서 사용할 수 있는 모든 파일 이름 목록을 보낸다.
     * input이 GET으로 시작하면
     * 1. OK 전송 후 뒤에 오는 파일의 content 전송
     * 2. 파일이 아닌 디렉토리 명이라면 ERROR message를 전송한다.
     *
     * @param client socket
     */
    private static void clientCommand(Socket client) {
        int BUF_SIZE = 1024 * 7;

        try (BufferedReader incoming = new BufferedReader(new InputStreamReader(
                client.getInputStream()))) {


            String messageIn = incoming.readLine();

            if (messageIn.equals("INDEX")) {
                File directory = new File(System.getProperty("user.home"));
                String[] files = directory.list();
                sendMsg(client, Arrays.toString(files));
            } else if (messageIn.startsWith("GET")) {
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
                    }
                    sendMsg(client, sb.toString());
                } else {
                    // // send "ERROR, error msg" and close connection
                    throw new IllegalArgumentException("ERROR : " + fileName + " is not file name");
                }
            } else {
                throw new IllegalArgumentException("입력은 INDEX거나 GET <fileName>이어야 합니다.");
            }
            client.close();

        } catch (Exception e) {
            logger.warn(e.getMessage());
        }
    }


    /**
     * PrintWriter 객체를 생성해 입력값에 대한 응답을 클라이언트에 보낸다.
     * GET일 경우 OK를 보낸 후 content를 보내야하기 때문에 close는 clientCommand에서 처리한다.
     *
     * @param client   socket
     * @param response 응답값
     */
    private static void sendMsg(Socket client, String response) {
        try {
            logger.info("Connection from {}", client.getInetAddress());
            PrintWriter outgoing;   // Stream for sending data.
            outgoing = new PrintWriter(client.getOutputStream());
            outgoing.println(response);
            outgoing.flush();  // Make sure the data is actually sent!
        } catch (Exception e) {
            logger.warn("Error: {}", e);
        }
    } // end sendDate()


} //end class DateServer
