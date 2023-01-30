package com.spruce;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    //该类为项目的主类，在该类中存在着项目的唯一主方法，在jar包运行的时候以该类中的主方法作为切入点运行，项目的总体逻辑就在其中
    //我的计划是在该类中书写tcp的主要逻辑，以及在后续可能会加入配置文件以及命令行交互的方式
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket();

        serverSocket.bind(new InetSocketAddress(9999));
        while(true){
            Socket socket = serverSocket.accept();
            //System.out.println(1);
            new Thread(){
                @Override
                public void run() {
                    InputStream inputStream = null;
                    OutputStream outputStream = null;
                    try {
                        inputStream = socket.getInputStream();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    byte[] buf = new byte[1024*1024];
                    int len;
                    while (true){
                        try {
                            if (!((len = inputStream.read(buf)) != -1)) break;
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        System.out.println(new String(buf,0,len));
                        String reply = "Do you like Van you see?";

                        try {
                            outputStream = socket.getOutputStream();
                            outputStream.write(reply.getBytes());
                            System.out.println("输出成功!");
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }


                    try {
                        outputStream.close();
                        inputStream.close();
                        socket.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                }
            }.run();


        }
    }
}
