package org.example;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Main {

    public static void main(String[] args) throws IOException {
        ServerSocket ss = new ServerSocket(8080);
        System.out.println("Server is running on port 8080");
        while (true) {
            Socket sock = ss.accept();
            System.out.println("Accepted connection from " + sock.getRemoteSocketAddress());
            Thread t = new Handler(sock);
            t.start();
        }

    }
}

class Handler extends Thread {
    private Socket sock;
    Handler(Socket sock){
        this.sock = sock;
    }

    @Override
    public void run() {
        try(InputStream is = this.sock.getInputStream()){
            try(OutputStream os = this.sock.getOutputStream()) {
                handler(is, os);
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
        finally {
            try{
               this.sock.close();
            }
            catch (IOException ioe){
                ioe.printStackTrace();
            }
            System.out.println("connection closed");
        }
    }

    private void handler(InputStream input, OutputStream output) throws IOException {
        System.out.println("Process new http request...");
        var reader = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8));
        var writer = new BufferedWriter(new OutputStreamWriter(output, StandardCharsets.UTF_8));
        // 读取HTTP请求:
        boolean requestOk = false;
        String first = reader.readLine();
        if (first.startsWith("GET / HTTP/1.")) {
            requestOk = true;
        }
        for (;;) {
            String header = reader.readLine();
            if (header.isEmpty()) { // 读取到空行时, HTTP Header读取完毕
                break;
            }
            System.out.println(header);
        }
        System.out.println(requestOk ? "Response OK" : "Response Error");
        if (!requestOk) {
            // 发送错误响应:
            writer.write("HTTP/1.0 404 Not Found\r\n");
            writer.write("Content-Length: 0\r\n");
            writer.write("\r\n");
            writer.flush();
        } else {
            // 发送成功响应:
            String data = "<html><body><h1>Hello, world!</h1></body></html>";
            int length = data.getBytes(StandardCharsets.UTF_8).length;
            writer.write("HTTP/1.0 200 OK\r\n");
            writer.write("Connection: close\r\n");
            writer.write("Content-Type: text/html\r\n");
            writer.write("Content-Length: " + length + "\r\n");
            writer.write("\r\n"); // 空行标识Header和Body的分隔
            writer.write(data);
            writer.flush();
        }
    }
}