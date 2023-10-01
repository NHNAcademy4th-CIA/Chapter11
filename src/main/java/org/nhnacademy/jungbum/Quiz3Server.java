package org.nhnacademy.jungbum;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.Scanner;
import java.util.StringTokenizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/***
 * 파일검색 및 다운로드 서버
 */
public class Quiz3Server {
    private static final Logger logger = LoggerFactory.getLogger(Quiz3Server.class);
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
            logger.info("열린 포트 {}", LISTENING_PORT);
            while (true) {
                // Accept next connection request and handle it.
                connection = listener.accept();
                search(connection);
            }
        } catch (Exception e) {
            System.out.println("Sorry, the server has shut down.");
            System.out.println("Error:  " + e);
            return;
        }

    }  // end main()

    /***
     * 입력한 기능 검색.
     * @param connection 연결된 클라이언트
     */
    public static void search(Socket connection) {

        File directory;        // File object referring to the directory.
        StringTokenizer stringTokenizer;
        directory = new File("Disk");
        logger.info("연결된 ip : {}", connection.getInetAddress());
        String input = null;
        BufferedReader incoming;
        try {
            incoming = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));
            logger.info("입력한 명령어 : {}", input = incoming.readLine());
        } catch (NullPointerException | IOException e) {
            logger.warn(e.getMessage());
        }
        logger.info("입력값: {}", input);
        stringTokenizer = new StringTokenizer(input);
        if (input.equals("INDEX")) {
            if (directory.isDirectory() == false) {
                if (directory.exists() == false)
                    logger.warn("디렉토리가 없습니다.");
                else
                    logger.warn("파일이 디렉토리에 존재하지 않습니다.");
            } else {

                try {
                    PrintWriter outgoing;   // Stream for sending data.
                    outgoing = new PrintWriter(connection.getOutputStream());
                    outgoing.println(searchDirectory(directory));
                    outgoing.flush();  // Make sure the data is actually sent!
                    connection.close();
                } catch (IOException e) {
                    logger.warn("Error: {}", e);
                }
            }
        } else if (stringTokenizer.nextToken().equals("GET")) {
            logger.info("test");
            try {
                File file = new File(directory+"/" + stringTokenizer.nextToken()); // 로딩 파일 인스턴스 생성
                FileInputStream fis = new FileInputStream(file); // file이 들어올 InputStream 을 개방
                DataInputStream fsis = new DataInputStream(fis); // 사용하기 편하게 만듬
                DataOutputStream dos = new DataOutputStream(connection.getOutputStream()); // 파일을 보낼 Stream 개방
                byte[] filecontants = new byte[(int) file.length()];    // 파일을 담을 공간을 RAM에 생성
                fsis.readFully(filecontants); // RAM에 파일 업로드
                dos.writeLong(filecontants.length); // 클라이언트 쪽에 파일의 크기를 전달
                dos.write(filecontants); // 파일 전송
                dos.flush(); // 버퍼 비우기
                connection.close();
            } catch (Exception e) {
                logger.info(e.getMessage());
            }
        }
    } // end main()

    /***
     * 파일 목록 검색.
     * @param dir 파일 혹은 디렉토리
     * @return 파일 목록
     */
    private static String searchDirectory(File dir) {
        String[] files;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("폴더이름 : ");
        stringBuilder.append(dir.getName());
        stringBuilder.append("\n");
        files = dir.list();
        for (int i = 0; i < files.length; i++) {
            File f = new File(dir, files[i]);
            if (f.isDirectory())
                searchDirectory(f);
            else {
                stringBuilder.append("ㄴ ");
                stringBuilder.append(files[i]);
                stringBuilder.append("\n");
            }
        }
        logger.info("{}", stringBuilder);
        return stringBuilder.toString();
    }

//    public static FileOutputStream customFile(String file) {
//        logger.info(file);
//        try (
//                FileOutputStream fileOutputStream = new FileOutputStream(file);
//        ) {
//            logger.info("{}", fileOutputStream);
//        } catch (IOException e) {
//            logger.info("해당파일은 존재하지 않습니다");
//        }
//        return
//    }
}
