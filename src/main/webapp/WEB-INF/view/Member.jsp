<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>会員登録</title>


<style>
        /* 全体のスタイル */
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f7f6;
            color: #333;
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            min-height: 100vh;
            margin: 0;
        }

        h1 {
            color: #4CAF50;
            font-size: 32px;
            margin-bottom: 20px;
        }

        p {
            font-size: 18px;
            margin-bottom: 20px;
            color: #555;
        }

        /* フォーム共通スタイル */
        form {
            background-color: #ffffff;
            padding: 20px 30px;
            border-radius: 10px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
            margin-bottom: 20px;
            width: 100%;
            max-width: 400px;
        }

        input[type="text"], input[type="submit"] {
            width: 100%;
            padding: 10px;
            margin-bottom: 15px;
            border: 1px solid #ccc;
            border-radius: 5px;
            box-sizing: border-box;
            font-size: 16px;
        }

        input[type="submit"] {
            background-color: #4CAF50;
            color: white;
            border: none;
            cursor: pointer;
            font-size: 16px;
            transition: background-color 0.3s ease;
        }

        input[type="submit"]:hover {
            background-color: #45a049;
        }

        /* エラーメッセージ */
        p.error {
            color: red;
            font-size: 14px;
            text-align: center;
        }

        /* ボタンのスペース調整 */
        .back-button {
            text-align: center;
            margin-top: 10px;
        }
    </style>
</head>
<body>
  <h1>チャットアプリ</h1>
    <p>Welcome to our chat service.</p>
    <form action="new" method="POST">
    <input type = "text" name="newname"/>
    <input type = "text" name="newpassword"/>
    <input type ="submit" value="会員登録"/>
    </form>
    
    
    <form action="login" method="GET">    
    <input type="submit" value="ログイン画面に戻ります">
    </form> 
      <%
    String errormsg = (String) request.getAttribute("errormsg");
    if (errormsg != null) {
    %>
        <p style="color:red;"><%= errormsg %></p>
    <%
    }
    %>
</body>
</html>