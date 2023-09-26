package org.nhnacademy.minju;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * .입력된 텍스트 파일에 대해 줄 수를 세고 총 합을 출력한다.
 */
public class LineCounts {

    /**
     * .CLI로 입력 받으면 파라미터는 args에 저장된다. 여기서 args는 각 텍스트 파일의 이름이다.
     * 루프 안에서 BufferedReader를 이용하여 파일을 읽는다. 줄 수에 따라 count를 늘려준다.
     *
     * @param args 파일 이름
     *
     */
    public static void main(String[] args) {
        int count = 0;
        if (args.length == 0) {
            System.out.println("한 개 이상의 파일이 입력되야 합니다.");
            return;
        }
        for (int i = 0; i < args.length; i++) {
            try (BufferedReader bufferedReader = new BufferedReader(new FileReader(args[i]))) {
                while (bufferedReader.readLine() != null) {
                    count++;
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
        System.out.println("count : " + count);
    }
}
