package org.nhnacademy.lsj;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Client {

    private static final Logger logger = LoggerFactory.getLogger(Client.class);

    private static final int LISTENING_PORT = 32007;

    public static void main(String[] args) {

        String hostName;
        Socket connection;
        BufferedReader incoming;
        PrintWriter printWriter;


        try {

            connection = new Socket("127.0.0.1", LISTENING_PORT);

            printWriter = new PrintWriter(connection.getOutputStream(), true);

            Scanner sc = new Scanner(System.in);

            printWriter.println(sc.nextLine());

            incoming = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            while ((hostName = incoming.readLine()) != null) {
                logger.info("{}", hostName);
            }


        } catch (Exception e) {

            logger.warn("안돼용");
        }


    }


}
