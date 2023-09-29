package org.nhnacademy.lsj;

import java.io.File;
import java.util.Scanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * directory 이름을 입력받으면 해당하는 경로 아래에 있는 모든 파일 및 directory의 경로와 이름을 출력함.
 */
public class Problem1 {

    private static final Logger logger = LoggerFactory.getLogger(Problem1.class);

    /**
     * 프로그램이 시작됨 .
     */
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

    /**
     * 해당하는 dicrectory 아래에 있는 모든 것을 읽음 , 그게 또 다른 directory면 재귀적으로 불러 모든 파일 읽음.
     *
     * @param directory 출력할 directory .
     * @param files     directory가 기지고 있는 파일 or directory의 list.
     */
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
