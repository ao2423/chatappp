package com.example.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.json.JSONObject;

import com.example.util.DBUtil;

@WebServlet("/dialog")
public class dialog  extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("GETリクエストを受理しました");
        response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED); // GETは禁止
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	 response.setContentType("application/json; charset=UTF-8");
    	 response.setCharacterEncoding("UTF-8");
    	HttpSession session = request.getSession();
        String userIdString = (String) session.getAttribute("userid");
        String message="";
        if (userIdString == null) {
            System.out.println("ログインセッションが無効です");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        int userId;
        try {
            userId = Integer.parseInt(userIdString);
        } catch (NumberFormatException e) {
            System.out.println("ユーザーIDの形式が無効です");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        request.setCharacterEncoding("UTF-8");
        BufferedReader reader = request.getReader();
        String jsonString = reader.readLine();
        
        JSONObject jsonObject = new JSONObject(jsonString);
        int friendId = jsonObject.getInt("friendid");
        String data = jsonObject.getString("data");

        System.out.println("friendId=" + friendId + " data=" + data + " userId=" + userId);

        try (Connection conn = DBUtil.getConnection()) {
            if ("add".equals(data)) {
                handleAddRequest(conn, userId, friendId);
                
                String getname = GetName(conn,friendId);
                
                message = getname+"さんにリクエストを送信しました";
                
              
                
            } else if ("agree".equals(data)) {
                handleAgreeRequest(conn, userId, friendId);
                
                String getname = GetName(conn,friendId);
                
                message = getname+"さんのリクエストを承諾しました";			
                
            } else {
                System.out.println("未知のデータタイプ: " + data);
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
            
            try (PrintWriter out = response.getWriter()) {
                JSONObject jsonResponse = new JSONObject();
                jsonResponse.put("message",message);
                out.print(jsonResponse.toString());
                out.flush();
            }
            
            
            
            
            
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private void handleAddRequest(Connection conn, int userId, int friendId) throws SQLException {
        String checkQuery = "SELECT * FROM friendrequests WHERE user_id = ? AND friend_user_id = ? AND status = ?";
        String insertQuery = "INSERT INTO friendrequests (user_id, friend_user_id, status) VALUES (?, ?, ?)";

        if (dataExists(conn, userId, friendId, checkQuery, "pending")) {
            System.out.println("既存のリクエストが存在します");
            return;
        }

        try (PreparedStatement ps = conn.prepareStatement(insertQuery)) {
            ps.setInt(1, userId);
            ps.setInt(2, friendId);
            ps.setString(3, "pending");
            ps.executeUpdate();
            System.out.println("新規リクエストを追加しました");
        }
    }

    private void handleAgreeRequest(Connection conn, int userId, int friendId) throws SQLException {
        String updateQuery = "UPDATE friendrequests SET status = 'accepted' WHERE friend_user_id = ? AND user_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(updateQuery)) {
            ps.setInt(1, userId);
            ps.setInt(2, friendId);
            int rowsUpdated = ps.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("リクエストを承認しました");
            } else {
                System.out.println("承認対象のリクエストが見つかりませんでした");
            }
        }
    }

    private boolean dataExists(Connection conn, int userId, int friendId, String sql, String status) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, friendId);
            ps.setString(3, status);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }
    
    private String GetName(Connection conn, int friendId) throws SQLException {
        String sql = "SELECT name FROM userinformation WHERE id = ?";
        String name = null; // 名前を格納する変数

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, friendId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) { // 結果が存在する場合
                    name = rs.getString("name"); // name列の値を取得
                }
            }
        }

        return name; // 取得した名前を返す
    }
    
}
