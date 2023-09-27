package org.nhnacademy.jungbum;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.StringTokenizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/***
 * 각 파일의 라인 읽어오기
 */
public class Quiz2 {
    public Quiz2() {
        new CustomFile();

    }
}

/***
 * 커스텀 파일 클래스
 */
class CustomFile {
    private Logger logger = LoggerFactory.getLogger(Quiz2.class);

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

    /***
     * 파일에 라인 사이즈를 반환해주는 메소드
     * @param bufferedReader 입력
     * @return 라인 수
     */
    private int fileLineSize(BufferedReader bufferedReader) {
        int count = 0;
        try {
            while (bufferedReader.readLine() != null) {
                count++;
            }
        } catch (IOException e) {
            logger.error(e.toString());
            return 0;
        }
        return count;
    }
}