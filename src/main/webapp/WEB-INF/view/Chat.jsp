<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    
    <title>チャットアプリ</title>
    <style>
        .chat-container {
            display: flex;
            flex-direction: column;
            height: 100%;
            max-height: 80vh;
            width: 100%;
            background-color: #ffffff;
            border-radius: 8px;
            box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
            padding: 15px;
        }

        .message-box {
            flex: 1;
            overflow-y: auto;
            margin-bottom: 10px;
            height: calc(100vh - 180px); /* チャット入力部分を考慮して調整 */
            display: flex;
            flex-direction: column;
            align-items: flex-start; /* 左側に配置 */
        }

        /* メッセージ共通スタイル */
        .message {
            max-width: 60%;
            padding: 12px 15px;
            margin: 5px;
            border-radius: 20px; /* 丸みを帯びた四角形 */
            word-wrap: break-word; /* 長い単語は折り返す */
            display: inline-block;
        }

    
    
    
    
      /* 自分のメッセージ */
        /* 自分のメッセージ */
.user-message {
    background-color: #ffcccb; /* 赤系 */
    align-self: flex-end; /* 右側に配置 */
    text-align: right;
    border-radius: 20px 20px 5px 20px; /* 角丸を強調 */
    padding: 12px 15px; /* 内側の余白 */
    margin: 8px 15px; /* 上下8px、左右15pxの外側余白 */
    max-width: 60%; /* 幅を制限 */
    word-wrap: break-word; /* 長い単語は折り返す */
}

/* 相手のメッセージ */
.bot-message {
    background-color: #add8e6; /* 青系 */
    align-self: flex-start; /* 左側に配置 */
    text-align: left;
    border-radius: 20px 20px 20px 5px; /* 角丸を強調 */
    padding: 12px 15px; /* 内側の余白 */
    margin: 8px 15px; /* 上下8px、左右15pxの外側余白 */
    max-width: 60%; /* 幅を制限 */
    word-wrap: break-word; /* 長い単語は折り返す */
}
        
        .friendname {
    background-color: #90ee90; /* 薄い緑色 */
    color: #ffffff; /* 文字色を白に */
    font-size: 18px; /* 文字を大きく */
    font-weight: bold; /* 文字を太字に */
    text-align: center; /* テキストを中央寄せ */
    padding: 15px 20px; /* 上下15px、左右20pxの余白 */
    border-radius: 30px; /* 角を丸く */
    margin: 10px 0; /* 上下10pxの余白 */
    display: inline-block; /* 幅を内容に合わせる */
    max-width: 80%; /* 最大幅を80%に制限 */
    word-wrap: break-word; /* 長い単語の折り返し */
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); /* 軽い影を追加 */
}
        
        

        .input-box {
            display: flex;
            align-items: center;
            padding: 10px;
            border-top: 1px solid #ccc;
        }

        .input-box input {
            flex: 1;
            padding: 10px;
            border: 1px solid #ccc;
            border-radius: 5px;
            font-size: 16px;
        }

        .input-box button {
            padding: 10px 15px;
            margin-left: 5px;
            background-color: #4CAF50;
            color: white;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            font-size: 16px;
        }

        .input-box button:hover {
            background-color: #45a049;
        }

        .timestamp {
            font-size: 12px;
            color: #888;
            display: none;
        }

        /* スクロールバーのカスタマイズ */
        .message-box::-webkit-scrollbar {
            width: 8px;
        }

        .message-box::-webkit-scrollbar-thumb {
            background-color: #888;
            border-radius: 4px;
        }

        .message-box::-webkit-scrollbar-thumb:hover {
            background-color: #555;
        }
    </style>
</head>
<body>

<%
    String friendName = (String) request.getAttribute("friendName");
    String userIdString = (String) session.getAttribute("userid");
    int userId = Integer.parseInt(userIdString);
    
    String friendString = (String) request.getAttribute("friendId");
    int friendId = Integer.parseInt(friendString);  
%>

<div class="chat-container">
 <div class="message friendname">
            チャット中の友達: <%= friendName %> (ID: <%= friendId %>)
        </div>
        
    <div class="message-box" id="messageBox">
        
    </div>
    <div class="input-box">
        <input type="text" id="userMessage" name="userMessage" placeholder="メッセージを入力..." required>
        <button type="button" id="sendButton">送信</button>
    </div>
</div>

<script>
    const userId = <%= userId %>;
    const friendId = <%= friendId %>;

    const socket = new WebSocket("ws://localhost:8080/chat-app/Messages?userId=" + userId);

    socket.onopen = function() {
        console.log("WebSocket connection opened");
        const userMessage = document.getElementById('userMessage').value;
        const data = {
            sender: userId,
            receiver: friendId,
            content : userMessage
        };
        socket.send(JSON.stringify(data));
    };

    socket.onmessage = function(event) {
        console.log("onmessageが読み込まれました");
        
        const messageBox = document.getElementById('messageBox');
        messageBox.innerHTML = "";
        try {
            const jsonResponse = JSON.parse(event.data);
            const contentList = jsonResponse.contentList;  // メッセージのリスト
            const timeList = jsonResponse.timeList;        // タイムスタンプのリスト

            // メッセージの履歴を表示
            contentList.forEach((map, index) => {
                const time = timeList[index];
                Object.entries(map).forEach(([key, value]) => {

                    const div = document.createElement("div");
                    const timeStamp = document.createElement("p");
                    timeStamp.textContent = time;
                    timeStamp.classList.add("timestamp");

                    div.textContent = value;
                    if (key == userId) {
                        div.classList.add("user-message");
                    } else if (key == friendId) {
                        div.classList.add("bot-message");
                    }

                    div.appendChild(timeStamp);
                    messageBox.appendChild(div);

                    div.addEventListener("mouseenter", () => {
                        timeStamp.style.display = "block";
                    });

                    div.addEventListener("mouseleave", () => {
                        timeStamp.style.display = "none";
                    });
                });
            });

            messageBox.scrollTop = messageBox.scrollHeight;  // スクロールを一番下にする

        } catch (e) {
            console.error("JSONの解析に失敗しました:", e);
        }
    };

    // メッセージ送信
    document.getElementById('sendButton').addEventListener('click', function() {
        const userMessage = document.getElementById('userMessage').value;

        if (userMessage.trim() !== "") {
            const data = {
                action: 'newMessage', // 新しいメッセージを送信
                sender: userId,
                receiver: friendId,
                content: userMessage
            };

            socket.send(JSON.stringify(data));
            document.getElementById('userMessage').value = ''; // 入力フィールドをクリア
        }
    });

    document.getElementById('userMessage').addEventListener('keydown', function(event) {
        if (event.key === 'Enter') {
            document.getElementById('sendButton').click();
        }
    });
</script>

</body>
</html>
