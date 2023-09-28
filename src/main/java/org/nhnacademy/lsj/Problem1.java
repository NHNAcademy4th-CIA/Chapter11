package org.nhnacademy.lsj;

import java.io.File;
import java.util.Scanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Problem1 {

    private static final Logger logger = LoggerFactory.getLogger(Problem1.class);

    public static void problem1() {

        String directoryName;
        File directory;


        Scanner sc = new Scanner(System.in);


        logger.info("Directory 이름을 입력해주세요");

        directoryName = sc.nextLine();

        directory = new File("src/main/", directoryName);


        try {
            if (!directory.isDirectory()) {
                logger.warn("Directory 입력이 잘못됐습니다! ");
                throw new IllegalArgumentException();
            }
        } catch (IllegalArgumentException e) {
            logger.info("프로그램을 재시작 해주세요");
            return;
        }


        printDirectory(directory, directory.list());


    }

    public static void printDirectory(File directory, String[] files) {


        for (int i = 0; i < files.length; i++) {

            File file = new File(directory + "/" + files[i], files[i]);


            if (file.isDirectory()) {

                File directories = new File(directory + "/" + files[i], files[i]);
                printDirectory(directories, directories.list());
                continue;
            }

            logger.info("파일 경로 {} 파일 이름 {}", file, file.getName());

        }

    }

}
