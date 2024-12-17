<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    
    
    <title>チャットアプリケーション</title>
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
            font-size: 36px;
            margin-bottom: 20px;
            text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.2);
        }

        p {
            font-size: 18px;
            margin-bottom: 20px;
        }

        /* フォームのスタイル */
        form {
            background-color: #ffffff;
            padding: 20px 30px;
            border-radius: 10px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
            margin-bottom: 15px;
            width: 100%;
            max-width: 400px;
        }

        label {
            font-weight: bold;
            margin-bottom: 5px;
            display: block;
        }

        input[type="text"],
        input[type="password"] {
            width: 100%;
            padding: 10px;
            margin: 10px 0 20px;
            border: 1px solid #ccc;
            border-radius: 5px;
            box-sizing: border-box;
            font-size: 16px;
        }

        input[type="submit"] {
            background-color: #4CAF50;
            color: #fff;
            padding: 10px 15px;
            border: none;
            border-radius: 5px;
            font-size: 16px;
            cursor: pointer;
            width: 100%;
            transition: background-color 0.3s ease;
        }

        input[type="submit"]:hover {
            background-color: #45a049;
        }

        /* エラーメッセージ */
        p.error {
            color: red;
            text-align: center;
            font-size: 14px;
        }

    </style>
</head>


<body>
    <h1>チャットアプリ</h1>
    <p>Welcome to our chat service.</p>
    <form action="login" method="POST">
        <label for="namea">ユーザー名:</label>
        <input type="text" id="namea" name="namea" required />
        <br>
        <label for="password">パスワード:</label>
        <input type="password" id="password" name="password" required />
        <br>
        <input type="submit" value="ログイン" />
    </form>
    
    <form action="new" method="GET">
        <input type="submit" value="会員登録">
    </form>

    <%
        // エラーメッセージがリクエストにセットされているかをチェック
        String errorMessage = (String) request.getAttribute("errorMessage");
        if (errorMessage != null) {
    %>
        <p style="color:red;"><%= errorMessage %></p>
    <%
        }
    %>

</body>
</html>
