package org.nhnacademy.jungbum;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.StringTokenizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Quiz2 {
    public Quiz2() {
        new CustomFile();

    }
}

class CustomFile {
    private Logger logger = LoggerFactory.getLogger(Quiz2.class);
    File file;

    public CustomFile() {
        StringBuilder stringBuilder = new StringBuilder();
        Scanner scanner = new Scanner(System.in);
        String files = scanner.nextLine();
        StringTokenizer stringTokenizer = new StringTokenizer(files);
        while (stringTokenizer.hasMoreTokens()) {
            stringBuilder.append("asd/");
            stringBuilder.append(stringTokenizer.nextToken());
            try (
                    BufferedReader bufferedReader = new BufferedReader(new FileReader(stringBuilder.toString()));
            ) {
                logger.info("{} : {}", stringBuilder.toString(), fileLineSize(bufferedReader));
            } catch (IOException e) {
                logger.info("해당파일은 존재하지 않습니다");
            }
            stringBuilder.delete(0, stringBuilder.length());
        }
    }

    private int fileLineSize(BufferedReader bufferedReader) {
        int count = 0;
        try {
            while (bufferedReader.readLine()!=null) {
                count++;
            }
        } catch (IOException e) {
            logger.error(e.toString());
            return 0;
        }
        return count;
    }
}