package com.example.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONObject;

import com.example.util.DBUtil;
import com.google.gson.Gson;

@WebServlet("/useronly")
public class useronly extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.getWriter().append("Served at: ").append(request.getContextPath());
        System.out.println("テスト");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // セッションからユーザー名を取得
        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");

        if (username == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "ユーザーがログインしていません");
            return;
        }

        request.setCharacterEncoding("UTF-8");

        // クライアントから送信されたJSONを読み込む
        BufferedReader reader = request.getReader();
        String jsonString = reader.readLine();
        JSONObject jsonObject = new JSONObject(jsonString);

        // JSONから"message"の値を取得
        String searchname = jsonObject.getString("message");

        System.out.println("searchname=" + searchname);
        System.out.println("username=" + username);

        // SQLクエリ
        String query = "SELECT name, id FROM userinformation WHERE name LIKE ? AND NOT name = ?";
        ArrayList<String> nameList = new ArrayList<>();
        ArrayList<Integer> idList = new ArrayList<>();

        if (searchname != null && !searchname.trim().isEmpty()) {
            System.out.println("空ではありません");

            try (Connection conn = DBUtil.getConnection();
                 PreparedStatement ps = conn.prepareStatement(query)) {

                ps.setString(1, searchname + "%");  // searchname + "%" で名前の部分一致検索
                ps.setString(2, username);  // 自分以外のユーザーを検索

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        nameList.add(rs.getString("name"));
                        idList.add(rs.getInt("id"));
                    }

                    // 結果がなければメッセージを追加
                    if (nameList.isEmpty()) {
                        nameList.add("該当するユーザーが見つかりませんでした");
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                nameList.add("データベースエラー: " + e.getMessage());
            }

            sendJson(nameList, idList, response);  // 名前とIDをJSONとして送信
        } else {
            System.out.println("空です");
            nameList.add("名前が入力されていません");
            sendJson(nameList, response);
        }
    }

    // 名前リストのみを送信
    private void sendJson(ArrayList<String> nameList, HttpServletResponse response) {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try (PrintWriter out = response.getWriter()) {
            Gson gson = new Gson();
            String jsonResponse = gson.toJson(nameList);
            out.print("{\"searchList\": " + jsonResponse + "}");
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 名前リストとIDリストを一緒に送信
    private void sendJson(ArrayList<String> nameList, ArrayList<Integer> idList, HttpServletResponse response) {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try (PrintWriter out = response.getWriter()){
          

            // 名前とIDのリストをオブジェクトにまとめる
            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("searchList", new JSONArray(nameList));
            jsonResponse.put("id", new JSONArray(idList));

            // 最終的に出力するJSONオブジェクトを送信
            out.print(jsonResponse.toString());
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

