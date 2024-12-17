<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <title>キャッチトーク</title>
    <style>
* {
    margin: 0;
    padding: 0;
    box-sizing: border-box;
}

body {
    font-family: Arial, sans-serif;
    background-color: #f4f7fc;
    height: 100vh;
    display: flex;
    flex-direction: column;
    justify-content: flex-start;
    overflow: auto; /* overflowをautoに変更 */
}


.container {
    display: flex;
    flex: 1;
    width: 100%;
    height: 100%;
    max-width: 1200px;
    margin-top: 20px;
    padding: 0 10px;
    box-sizing: border-box;
}

.menu {
    width: 300px;
    padding: 15px;
    background-color: #ffffff;
    border-radius: 8px;
    box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
    margin-right: 15px;
    display: flex;
    flex-direction: column;
    gap: 20px;
    overflow-y: auto;
}

.chat-box {
    flex: 1;
    display: flex;
    flex-direction: column;
    background-color: #ffffff;
    border-radius: 8px;
    box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
    height: 80vh;
    padding: 15px;
    overflow: hidden;
}

p {
    font-size: 18px;
    font-weight: bold;
    color: #2d3436;
    padding: 0;
    margin: 0;
}

input[type="text"] {
    width: 100%;
    padding: 12px;
    border-radius: 25px;
    border: 1px solid #ccc;
    font-size: 16px;
    transition: all 0.3s ease;
}

input[type="text"]:focus {
    border-color: #74b9ff;
    outline: none;
}

button {
    padding: 12px 18px;
    background-color: #74b9ff;
    color: white;
    border: none;
    border-radius: 25px;
    cursor: pointer;
    font-size: 16px;
    transition: background-color 0.3s ease;
}

button:hover {
    background-color: #0984e3;
}

#dialog {
    display: none;
    padding: 20px;
    border: 1px solid #aaa;
    box-shadow: 2px 2px 4px #888;
    text-align: center;
    background: #ffffff;
    position: fixed;
    top: 30%;
    left: 50%;
    transform: translate(-50%, -30%);
    width: 300px;
    border-radius: 8px;
    animation: fadeIn 0.3s ease-out;
    z-index: 1000;
}

#dialog h2 {
    font-size: 20px;
    color: #2d3436;
    margin-bottom: 15px;
}

#dialog p {
    font-size: 16px;
    color: #636e72;
    margin-bottom: 20px;
}

#dialog button {
    padding: 10px 15px;
    background-color: #74b9ff;
    color: white;
    border: none;
    border-radius: 8px;
    cursor: pointer;
    font-size: 16px;
    width: 100%;
    transition: background-color 0.3s ease;
}

#dialog button:hover {
    background-color: #0984e3;
}

#monitor {
    display: none;
    padding: 20px;
    border: 1px solid #ddd;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
    background: #ffffff;
    position: fixed;
    top: 20%;
    left: 50%;
    transform: translateX(-50%);
    width: 80%;
    max-width: 600px;
    border-radius: 8px;
    z-index: 100;
    overflow: hidden;
    animation: fadeIn 0.3s ease-out;
}

#monitor table {
    width: 100%;
    border-collapse: collapse;
}

#monitor th,
#monitor td {
    padding: 12px 15px;
    text-align: center;
    font-size: 16px;
    border-bottom: 1px solid #eee;
}

#monitor th {
    background-color: #74b9ff;
    color: white;
    cursor: pointer;
    font-weight: bold;
}

#monitor td {
    background-color: #f9f9f9;
    cursor: pointer;
    transition: background-color 0.3s ease;
}

#monitor td:hover {
    background-color: #dfe6e9;
}

#monitor th:hover {
    background-color: #0984e3;
}

#monitor #view tr:nth-child(odd) {
    background-color: #fafafa;
}

#monitor #view tr:nth-child(even) {
    background-color: #f1f1f1;
}

#monitor #view tr:hover {
    background-color: #e1e6e9;
}

#monitor button {
    padding: 10px 15px;
    background-color: #e74c3c;
    color: white;
    border: none;
    border-radius: 8px;
    cursor: pointer;
    font-size: 16px;
    transition: background-color 0.3s ease;
    margin-top: 15px;
    width: 100%;
}

#monitor button:hover {
    background-color: #c0392b;
}

@keyframes fadeIn {
    from {
        opacity: 0;
        transform: translateY(-20px);
    }
    to {
        opacity: 1;
        transform: translateY(0);
    }
}

#friendList, #userList {
    margin-top: 15px;
    padding: 10px;
    background-color: #f9f9f9;
    border-radius: 8px;
    box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
}

#friendList p, #userList p {
    margin: 5px 0;
    padding: 8px;
    border: 1px solid #ccc;
    border-radius: 5px;
    cursor: pointer;
}

#friendList p:hover, #userList p:hover {
    background-color: #dfe6e9;
}

img {
    width: 15%;
    height: auto;
    cursor: pointer;
    margin-top: 20px;
}



@media screen and (max-width: 768px) {
    .container {
        flex-direction: column;
    }

    .menu {
        width: 100%;
        margin-bottom: 20px;
    }

    .chat-box {
        margin-top: 20px;
    }
}
@keyframes fadeIn {
    from {
        opacity: 0;
        /* transformの位置を削除 */
        /* translateY(-20px); */
    }
    to {
        opacity: 1;
        /* transformの位置を削除 */
        /* translateY(0); */
    }
}

    </style>
</head>

<body>
   <p>こんにちは、<%= (String)session.getAttribute("username") %> さん</p>

    <input type="text" placeholder="お友達検索" onchange="handleInputChange(event)">
    <div id="userList"></div>

    <div id="dialog">
        <p id="dialogFriendName"></p>
        <button onclick="handleFriendRequest('yes')">はい</button>
        <button onclick="handleFriendRequest('no')">いいえ</button>
    </div>

    <button onclick="fetchFriendList()">お友達一覧へ</button>
    <div id="friendList"></div>

    <div onclick="showNotice()">
        <img src="images/responsebox.png" alt="Response Box">
    </div>

    <div id="monitor">
        <table>
            <thead>
                <tr>
                    <th onclick="showNotice()">お知らせ</th>
                    <th onclick="fetchRequestList()">フレンドリクエスト</th>
                    <th onclick="hideMonitor()">閉じる</th>
                </tr>
            </thead>
            <tbody id="view"></tbody>
        </table>
    </div>
    
    <div class="chat-container">
        <div class="message-box" id="messageBox">
            <div class="message bot-message">
                こんにちは！チャットアプリへようこそ！
            </div>
        </div>
    
    </div>
    
<form action="indexChat" method="POST" id="paramform">
    <input type="hidden" id="itemId" name="itemId">
    <input type="hidden" id="itemName" name="itemName">
</form>


    <script>

    var count = 0; 
    
        function handleInputChange(event) {
            const searchname = event.target.value;
            sendRequest('/chat-app/useronly', { message: searchname }, updateUserList);
        }

        function updateUserList(response) {
            const userListDiv = document.getElementById("userList");
            userListDiv.innerHTML = "";

            if (response.searchList) {
                response.searchList.forEach((user, index) => {
                    const userid = response.id[index];
                    const p = document.createElement("p");
                    p.textContent = user;

                    p.addEventListener("click", () => {
                        document.getElementById("dialog").dataset.method = "add";
                        showDialog(user, userid);
                    });

                    userListDiv.appendChild(p);
                });
            } else {
                console.log("該当する searchname がありません");
            }
        }

        function showDialog(user, userid) {
            document.getElementById("dialogFriendName").textContent = user+"をお友達に追加しますか？";
            const dialog = document.getElementById("dialog");
            dialog.style.display = "block";
            dialog.dataset.user = userid;
            
        }

        function handleFriendRequest(action) {
            const dialog = document.getElementById("dialog");
            const userid = dialog.dataset.user;
            const method = dialog.dataset.method;

            console.log("userid="+userid);
            console.log("method="+method);
            
            if (action === "yes") {
                sendRequest('/chat-app/dialog', { friendid: userid, data:method }, response => {
                   const message =  response.message;
                   alert(message);
                });
            }

            dialog.style.display = "none";
        }

        function showNotice() {
            const viewElement = document.getElementById("view");
            viewElement.innerHTML = "";

            const tr = document.createElement("tr");
            const td = document.createElement("td");
            td.textContent = "現在新着情報はありません";
            tr.appendChild(td);
            viewElement.appendChild(tr);

            document.getElementById("monitor").style.display = "block";
        }

        function hideMonitor() {
            document.getElementById("monitor").style.display = "none";
        }

        function fetchRequestList() {
            sendRequest('/chat-app/friendlist', { message: "requestlist" }, response => {
                const viewElement = document.getElementById("view");
                viewElement.innerHTML = "";

                response.requestlist.forEach((item, index) => {
                    const itemid = response.requestidlist[index];
                    const row = document.createElement("tr");
                    const cell = document.createElement("td");

                    cell.textContent = item;
                    cell.addEventListener("click", () => {
                        document.getElementById("dialog").dataset.method = "agree";

                        
                        
                        console.log("item"+item);
                        console.log("itemid"+itemid);
                        
                        showDialog(item, itemid);
                    });

                    row.appendChild(cell);
                    viewElement.appendChild(row);
                });
            });
        }

        function fetchFriendList() {
            console.log("count=" + count);
            if (count % 2 === 0) {

                sendRequest('/chat-app/friendlist', { message: "search" }, response => {
                    const friendListDiv = document.getElementById("friendList");
                    friendListDiv.innerHTML = "";

                    response.requestlist.forEach((item, index) => {
                        const itemid = response.requestidlist[index];

                        const p = document.createElement("p");
                        p.textContent = item;

                        

                        // フォームを送信
                        
                        // クリックイベントを設定
                        p.addEventListener("click", function() {
                        	document.getElementById("itemId").value = itemid;
                            document.getElementById("itemName").value = item;
                        	 document.getElementById("paramform").submit();     	   
                        });

                        friendListDiv.appendChild(p);
                        
                    });
                    count++;
                });
            } else {
                const friendListDiv = document.getElementById("friendList");
                friendListDiv.innerHTML = "";

                count++;
            }
        }
         

        function sendRequest(url, data, callback) {
            fetch(url, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(data),
            })
                .then(response => response.json())
                .then(callback)
        }


  
    </script>
</body>
</html>
