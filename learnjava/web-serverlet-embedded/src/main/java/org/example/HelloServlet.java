package org.example;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(urlPatterns = "/hello")
public class HelloServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 设置响应类型:
        resp.setContentType("text/html");
        String name = req.getParameter("name");
        // 获取输出流:
        PrintWriter pw = resp.getWriter();
        // 写入响应:
        if (name == null) {
            pw.write("<h1>Hello, world!!!!!!</h1>");
        }
        else{
            pw.write("<h1>Hello, " + name + "!</h1>");
        }
        // 最后不要忘记flush强制输出:
        pw.flush();
    }

    public static void main(String[] args) {

    }
}