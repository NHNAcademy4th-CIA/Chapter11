package org.nhnacademy.lsj;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Server {

    private static final Logger logger = LoggerFactory.getLogger(Server.class);

    private static final int LISTENING_PORT = 32007;

    private static final String path = "src/main/resources";

    public static void main(String[] args) {


        ServerSocket server;

        Socket connection;

        try {

            server = new ServerSocket(LISTENING_PORT);
            logger.info("Listening on port " + LISTENING_PORT);

            while (true) {
                connection = server.accept();
                sendData(connection);
            }
        } catch (Exception e) {
            logger.warn("서버 다운");
        }


    }

    private static void isValidate(PrintWriter printWriter, File file) {

        if (file.isFile() || file.isDirectory()) {
            printWriter.println("OK");
            return;
        }

        logger.warn("잘못된 입력입니다.");
        throw new IllegalArgumentException();


    }


    private static void sendData(Socket client) {


        try {

            logger.info("Connection from " + client.getInetAddress());


            // index -> 디렉토리 목록 알려줌

            BufferedReader bf = new BufferedReader(new InputStreamReader(client.getInputStream()));
            // 클라이언트의 입력

            PrintWriter outputStreamFromServer;

            outputStreamFromServer = new PrintWriter(client.getOutputStream(), true); // 서버가 클라이언트에게

            String str = bf.readLine();

            File file;

            System.out.println(str);


            if (str.equals("INDEX")) {

                file = new File(path);

                isValidate(outputStreamFromServer, file);

                String[] files = file.list();

                for (int i = 0; i < files.length; i++) {
                    outputStreamFromServer.println(files[i]);
                }

            } else if (str.substring(0, 3).equals("GET")) {

                System.out.println(str.substring(4));

                file = new File(path + "/" + str.substring(4));

                isValidate(outputStreamFromServer, file);

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));

                String temp;

                while ((temp = bufferedReader.readLine()) != null) {
                    logger.info(temp); // 서버 로그
                    outputStreamFromServer.println(temp); // 클라이언트가 받음
                }

                outputStreamFromServer.println(file);


            } else {
                logger.warn("잘못된 명령어입니다");
                throw new IllegalArgumentException();
            }

            outputStreamFromServer.println("Socket Close");
            client.close();

        } catch (IOException e) {
            logger.info("Error: " + e.getMessage());
        }

    }


}
