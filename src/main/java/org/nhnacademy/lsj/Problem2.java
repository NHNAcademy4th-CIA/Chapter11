package org.nhnacademy.lsj;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Problem2 {

    private static final Logger logger = LoggerFactory.getLogger(Problem2.class);

    public static void problem2() {

        String[] textName = {"src/main/resources/file1.txt",
                "src/main/resources/file2.txt", "src/main/resources/file3.txt"};

        for (int i = 0; i < 3; i++) {

            try (BufferedReader bf = new BufferedReader(new FileReader(textName[i]))) {

                int count = 0;

                while (bf.readLine() != null) {
                    count++;
                }

                logger.info("{} 파일은 {}줄로 이루어져 있습니다.", textName[i], count);

            } catch (FileNotFoundException e) {
                logger.warn("파일을 찾지 못했습니다");
            } catch (IOException e) {
                logger.warn("I/O Stream에 문제가 있습니다");
            }


        }


    }

}
