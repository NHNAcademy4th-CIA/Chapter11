package org.nhnacademy.lsj;

import java.io.File;
import java.util.Scanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Problem1 {

    private static final Logger logger = LoggerFactory.getLogger(Problem1.class);

    public static void problem1() {

        String directoryName;
        File_ directory;


        Scanner sc = new Scanner(System.in);


        logger.info("Directory 이름을 입력해주세요");

        directoryName = sc.nextLine();
        directory = new File_(new File("src/main/" + directoryName), directoryName);


        try {
            if (!directory.getDir().isDirectory()) {
                logger.warn("Directory 입력이 잘못됐습니다! ");
                throw new IllegalArgumentException();
            }
        } catch (IllegalArgumentException e) {
            logger.info("프로그램을 재시작 해주세요");
        }


        printDirectory(directory, directory.getDir().list());


    }

    public static void printDirectory(File_ directory, String[] files) {


        for (int i = 0; i < files.length; i++) {

            File_ file_ = new File_(new File(directory.getDir() + "/" + files[i]), files[i]);

            if (file_.getDir().isDirectory()) {

                File_ directories = new File_(new File(directory.getDir() + "/" + files[i]), files[i]);


                printDirectory(directories, directories.getDir().list());
                continue;
            }

            logger.info("파일 경로 {} 파일 이름{}", file_.getDir(), file_.getFileName());

        }

    }

}


class File_ {

    public File getDir() {
        return dir;
    }

    private File dir;

    public String getFileName() {
        return fileName;
    }

    private String fileName;


    public File_(File dir, String fileName) {
        this.dir = dir;
        this.fileName = fileName;
    }


}

