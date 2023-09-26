package org.nhnacademy.minju;

import java.io.File;
import java.util.Scanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * .한 디렉토리에 대해 타고 타고 타고 내려간다.
 * ex) input = src/main/java/org -> nhnacacdemy, App.java -> minju -> java files
 */
public class Exercise1 {
    private static final Logger logger = LoggerFactory.getLogger(Exercise1.class);

    /**
     * 입력 값에 대해 File(fileName)으로 파일 객체를 생성한다.
     * 디렉토리가 아니라면 문장을 출력한다.
     */
    public static void exercise1() {

        String directoryName;  // Directory name entered by the user.
        File directory;        // File object referring to the directory.
        String[] files;        // Array of file names in the directory.
        Scanner scanner;       // For reading a line of input from the user.

        scanner = new Scanner(System.in);  // scanner reads from standard input.

        System.out.print("Enter a directory name: ");
        directoryName = scanner.nextLine().trim();
        directory = new File(directoryName, "");

        if (!directory.isDirectory()) {
            if (!directory.exists()) {
                System.out.println("There is no such directory!");
            } else {
                System.out.println("That file is not a directory.");
            }
        } else {
            try {
                directoryList(directory, "");
            } catch (NullPointerException e) {
                logger.info(e.getMessage());
            }
        }
    }

    /**
     * File(dir, dirList[i]) -> dir.path/dirList[i] 형태
     * => dir 객체가 나타내는 디렉토리에 dirList[i]이라는 이름을 가진 File 객체(인스턴스)를 생성한다
     * 처음 File로 받아온 dir은 해당 디렉토리, list()를 통해 파일과 디렉토리를 dirList에 넣는다.
     * dirList에 대해 반복문을 돌려 dir/dirList[i] 파일 객체를 만들고 디렉토리라면 다시 타고 내려가고
     * 파일이라면 그대로 파일명을 출력해준다.
     * 같은 위치의 디렉토리나 파일을 구분하기 위해 fileName에 --를 넣고 재귀할때마다 --를 증가시켰다.
     *
     * @param dir      parent abstract pathname
     * @param fileName child pathname string
     */
    private static void directoryList(File dir, String fileName) {
        logger.info("{} (directory) {}", fileName, dir);
        fileName += "--";
        String[] dirList = dir.list();
        for (int i = 0; i < dirList.length; i++) {
            File file = new File(dir, dirList[i]);
            if (file.isDirectory()) {
                directoryList(file, fileName);
            } else {
                logger.info("{} {}", fileName, dirList[i]);
            }
        }
    }
}
