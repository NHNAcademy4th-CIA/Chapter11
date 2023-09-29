package org.nhnacademy.lsj;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 텍스트파일을 읽고 , 구성된 줄 수를 출력한 후 , 복사해 다른 파일로 저장한다.
 */
public class Problem2 {

    private static final Logger logger = LoggerFactory.getLogger(Problem2.class);

    /**
     * 프로그램이 시작되는 main역할.
     */
    public static void problem2() {


        List<String> textName = new ArrayList<>();

        textName.add("src/main/resources/file1.txt");
        textName.add("src/main/resources/file2.txt");
        textName.add("src/main/resources/file3.txt");


        for (int i = 0; i < textName.size(); i++) {

            try (BufferedReader bf = new BufferedReader(new FileReader(textName.get(i)))) {


                BufferedWriter bw = new BufferedWriter(new FileWriter(textName.get(i) + "_Copy_By_BufferWriter"));
                PrintWriter pw = new PrintWriter(new FileWriter(textName.get(i) + "_Copy_By_PrintWriter"),true);


                int count = 0;

                String str = null;

                while ((str = bf.readLine()) != null) {
                    bw.write(str);
                    bw.newLine();
                    pw.println(str);
                    count++;
                }

                bw.flush();

                logger.info("{} 파일은 {}줄로 이루어져 있습니다.", textName.get(i), count);

            } catch (FileNotFoundException e) {
                logger.warn("파일을 찾지 못했습니다");
            } catch (IOException e) {
                logger.warn("I/O Stream에 문제가 있습니다");
            }


        }


    }

}


