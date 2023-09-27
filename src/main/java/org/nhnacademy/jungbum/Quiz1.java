package org.nhnacademy.jungbum;

import java.io.File;
import java.util.Scanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/***
 * 디렉터리 파일 읽어오기
 */
public class Quiz1 {
    DirectoryList directoryList = new DirectoryList();
}

/***
 * 디렉토리 리스트
 */
class DirectoryList {
    private Logger logger = LoggerFactory.getLogger(Quiz1.class);

    public DirectoryList() {

        String directoryName;  // Directory name entered by the user.
        File directory;        // File object referring to the directory.
        Scanner scanner;       // For reading a line of input from the user.

        scanner = new Scanner(System.in);  // scanner reads from standard input.

        System.out.print("Enter a directory name: ");
        directoryName = scanner.nextLine().trim();
        directory = new File(directoryName);

        if (directory.isDirectory() == false) {
            if (directory.exists() == false)
                System.out.println("There is no such directory!");
            else
                System.out.println("That file is not a directory.");
        } else {
            searchDirectory(directory);
        }

    } // end main()

    /***
     * 디렉토리 검색
     * @param dir 파일 Or 디렉토리 디렉토리면 재귀
     */
    private void searchDirectory(File dir) {
        String[] files;
        logger.info("폴더이름 : {}", dir.getName());
        files = dir.list();
        for (int i = 0; i < files.length; i++) {
            File f = new File(dir, files[i]);
            if (f.isDirectory())
                searchDirectory(f);
            else
                logger.info("ㄴ {}", files[i]);
        }
    }

}