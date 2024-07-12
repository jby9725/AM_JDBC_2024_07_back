package org.koreait;

import java.util.Scanner;

public class Container {
    private static Scanner scanner;

    // 공유 자원을 모아두는 공간 초기화
    public static void init() {
        scanner = new Scanner(System.in);

    }

    // 공유 자원을 모아두는 공간 자원 해제
    public static void close() {
        scanner.close();
    }

    public static Scanner getScanner() {
        return scanner;
    }
}