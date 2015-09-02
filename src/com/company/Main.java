package com.company;

public class Main {
    private static SftpServer server = new SftpServer();

    public static void main(String[] args) {
        server.start();
    }
}
