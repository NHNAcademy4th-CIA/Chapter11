package org.nhnacademy.lsj;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Problem2 {

    private static final Logger logger = LoggerFactory.getLogger(Problem2.class);

    public static void problem2() {


        List<String> textName = new ArrayList<>();

        textName.add("src/main/resources/file1.txt");
        textName.add("src/main/resources/file2.txt");
        textName.add("src/main/resources/file3.txt");


        for (int i = 0; i < textName.size(); i++) {

            try (BufferedReader bf = new BufferedReader(new FileReader(textName.get(i)));
                 BufferedWriter bw = new BufferedWriter(new FileWriter(textName.get(i) + "_it's a copy"))) {

                int count = 0;

                String str = null;

                while ((str = bf.readLine()) != null) {
                    bw.write(str);
                    bw.newLine();
                    count++;
                }

                logger.info("{} 파일은 {}줄로 이루어져 있습니다.", textName.get(i), count);

            } catch (FileNotFoundException e) {
                logger.warn("파일을 찾지 못했습니다");
            } catch (IOException e) {
                logger.warn("I/O Stream에 문제가 있습니다");
            }


        }
    }

}


