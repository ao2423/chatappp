package com.example.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONObject;

import com.example.util.DBUtil;

@WebServlet("/friendlist")
public class friendlist extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // HTTP GETリクエストの処理
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.getWriter().append("Served at: ").append(request.getContextPath());
    }

    // HTTP POSTリクエストの処理
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // リクエストリストを格納するリスト
        List<String> requestList = new ArrayList<>();
        List<Integer> requestIdList = new LinkedList<>();
        
        //user_id リクエスト送信者
        //friend_user_id リクエスト受信者
        
        String sqlUserName = "SELECT name FROM userinformation WHERE id = ?";
        String sqlAcceptedRequests = "SELECT CASE WHEN user_id = ? THEN friend_user_id WHEN friend_user_id = ? THEN user_id END AS friend_id FROM friendrequests WHERE (user_id = ? OR friend_user_id = ?) AND status = 'accepted'";
        String sqlPendingRequests = "SELECT user_id FROM friendrequests WHERE friend_user_id = ? AND status = 'pending'"; // 保留中のリクエスト

        // セッションからユーザーIDを取得
        HttpSession session = request.getSession();
        String userIdString = (String) session.getAttribute("userid");

        // ユーザーIDを整数に変換
        int userId = Integer.parseInt(userIdString);

        // リクエストボディからJSONデータを取得
        BufferedReader reader = request.getReader();
        String jsonString = reader.readLine();
        JSONObject jsonObject = new JSONObject(jsonString);

        // メッセージを取得
        String message = jsonObject.getString("message");

        System.out.println("message=" + message);
        System.out.println("userId=" + userId);

        // データベース接続
        try (Connection conn = DBUtil.getConnection()) {
        	
            if ("requestlist".equals(message)) {
                System.out.println("requestlistが読み込まれました");

                // 保留中のリクエストを取得
                PreparedStatement psPending = conn.prepareStatement(sqlPendingRequests);
                psPending.setInt(1, userId);
                ResultSet rsPending = psPending.executeQuery();

                // 各保留リクエストのユーザー名を取得
                while (rsPending.next()) {
                    int pendingUserId = rsPending.getInt("user_id");
                    
                    System.out.println("保留中リクエストのID: " + pendingUserId);

                    // ユーザー名を取得
                    PreparedStatement psUserName = conn.prepareStatement(sqlUserName);
                    psUserName.setInt(1, pendingUserId);
                    
                    ResultSet rsUserName = psUserName.executeQuery();

                    if (rsUserName.next()) {
                        String name = rsUserName.getString("name");
                        System.out.println("保留中リクエストのNAME: " + name);
                        
                        requestList.add(name);
                        requestIdList.add(pendingUserId);                
                        
                    
                    }

                    rsUserName.close();
                    psUserName.close();
                }

                rsPending.close();
                psPending.close();
            } else if("search".equals(message)){
                
            	System.out.println("searchが読み込まれました");
            	
            	
            	
            	PreparedStatement psPending = conn.prepareStatement(sqlAcceptedRequests);
                psPending.setInt(1, userId);
                psPending.setInt(2, userId);
                psPending.setInt(3, userId);
                psPending.setInt(4, userId);
                
                ResultSet rsPending = psPending.executeQuery();

                while (rsPending.next()) {
                	
                int pendingUserId = rsPending.getInt("friend_id");
                System.out.println("保留中リクエストのID: " + pendingUserId);
                
                PreparedStatement psUserName = conn.prepareStatement(sqlUserName);
                psUserName.setInt(1, pendingUserId);
                
                ResultSet rsUserName = psUserName.executeQuery();

                if (rsUserName.next()) {
                    String name = rsUserName.getString("name");
                    System.out.println("保留中リクエストのNAME: " + name);
                    
                    requestList.add(name);
                    requestIdList.add(pendingUserId);                
                    
                
                }

                rsUserName.close();
                psUserName.close();
                
                }
            }else {
            	
            	 System.out.println("リクエストがありません");
            	
            }
            
            try (PrintWriter out = response.getWriter()) {
                JSONObject jsonResponse = new JSONObject();
                jsonResponse.put("requestlist", new JSONArray(requestList));
                jsonResponse.put("requestidlist", new JSONArray(requestIdList));
                out.print(jsonResponse.toString());
                out.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
