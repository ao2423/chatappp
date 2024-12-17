package com.example.servlet;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;

import org.json.JSONArray;
import org.json.JSONObject;

import com.example.util.DBUtil;

@ServerEndpoint("/Messages")
public class Messages {

    // 接続しているすべてのセッションを保持
    private static CopyOnWriteArrayList<Session> sessions = new CopyOnWriteArrayList<>();
    
    // データ格納用のフィールド

   List<Map<Integer,String>> contentList = new ArrayList<>();
  
   List<String> timeList = new ArrayList<>();
   
    
    @OnOpen
    public void onOpen(Session session) {
        sessions.add(session);
        System.out.println("New connection: " + session.getId());

        Map<String, List<String>> params = session.getRequestParameterMap();
        
        if (params.containsKey("userId")) {
        	System.out.println("userIdが読み込まれました");
            session.getUserProperties().put("userId", Integer.parseInt(params.get("userId").get(0)));
            
        }
    }

    @OnClose
    public void onClose(Session session) {
        sessions.remove(session);
        System.out.println("Connection closed: " + session.getId());
    }


    @OnMessage
    public void onMessage(Session session, String message) {
        try {
            // メッセージの解析
        	
        	System.out.println("onMessageを受け取りました");
        	
            JSONObject jsonMessage = new JSONObject(message);
            int sender = jsonMessage.getInt("sender"); // 自分
            int receiver = jsonMessage.getInt("receiver"); // 相手
            String content = jsonMessage.getString("content");
            
            System.out.println("sender="+sender+"receiver="+receiver+"content="+content);

            if (content != null && !content.trim().isEmpty()) {
            	 
            	System.out.println("if文が読み込まれました");
            	 
                try (Connection conn = DBUtil.getConnection();
                     PreparedStatement ps = conn.prepareStatement("INSERT INTO Messages (sender_id, receiver_id, content) VALUES (?, ?, ?);")) {
                    ps.setInt(1, sender);
                    ps.setInt(2, receiver);
                    ps.setString(3, content);
                    ps.executeUpdate();
                } catch (SQLException e) {
                    System.err.println("Failed to save message to database: " + e.getMessage());
                    e.printStackTrace();
                    return; // エラー時は処理を中断
                }
            }

            // メッセージ履歴を取得
            String selectQuery = "SELECT content, sender_id, created_at FROM messages WHERE (sender_id = ? AND receiver_id = ?) OR (sender_id = ? AND receiver_id = ?) ORDER BY created_at ASC";
            try (Connection conn = DBUtil.getConnection()) {
                retrieveMessages(conn, sender, receiver, selectQuery);
            } catch (SQLException e) {
                System.err.println("Failed to retrieve messages: " + e.getMessage());
                e.printStackTrace();
                return; // エラー時は処理を中断
            }

            // JSONレスポンスを構築
            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("contentList", new JSONArray(contentList));
            jsonResponse.put("timeList", new JSONArray(timeList));

            // 接続しているすべてのセッションにメッセージを送信
            for (Session s : sessions) {
                try {
                    Integer sessionUserId = (Integer) s.getUserProperties().get("userId");
                    if (sessionUserId != null && (sessionUserId == sender || sessionUserId == receiver)) {
                        s.getBasicRemote().sendText(jsonResponse.toString());
                    }
                } catch (Exception e) {
                    System.err.println("Failed to send message to session " + s.getId() + ": " + e.getMessage());
                    e.printStackTrace();
                }
            }

        } catch (Exception e) {
            System.err.println("Error in onMessage: " + e.getMessage());
            e.printStackTrace();
        }
    }

    

    @OnError
    public void onError(Session session, Throwable throwable) {
        System.err.println("Error occurred in session " + session.getId() + ": " + throwable.getMessage());
        throwable.printStackTrace();
    }

    private void retrieveMessages(Connection conn, int sender, int receiver, String query) throws SQLException {
        contentList.clear();
        timeList.clear();
        
        System.out.println("retrieveMessagesが読み込まれました");
        
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, sender);
            stmt.setInt(2, receiver);
            stmt.setInt(3, receiver);
            stmt.setInt(4, sender);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int senderId = rs.getInt("sender_id");
                    String content = rs.getString("content");
                    String createdAt = rs.getString("created_at");

                    System.out.println("senderId="+senderId+"content="+content+"createdAt="+createdAt);
                    
                    if(sender == senderId){
                   Map<Integer,String> contentMap= new HashMap<>();
                   contentMap.put(sender, content);
                   contentList.add(contentMap);
                   timeList.add(createdAt);
                   
                    }else if(receiver == senderId) {
                    Map<Integer,String> contentMap= new HashMap<>();
                        contentMap.put(receiver, content);
                        contentList.add(contentMap);
                        timeList.add(createdAt);

                    }
                    
                    }
                    
                   
                }
            }
        
    }
}
