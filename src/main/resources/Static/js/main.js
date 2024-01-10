'use strict';

const usernamePage = document.querySelector('#username-page');
const chatPage = document.querySelector('#chat-page');
const usernameForm = document.getElementById('#usernameForm');
const messageForm = document.querySelector('#messageForm');
const messageInput = document.querySelector('#message');
const connectingElement = document.querySelector('.connecting');
const chatArea = document.querySelector('#chat-messages');
const logout = document.querySelector('#logout');
const drop =document.querySelector(".dropdown_menu");
const dots = document.querySelector(".three_dots");

let stompClient = null;
let nickname = null;
let password = null;
let selectedUserId = null;


function connectToChatApp(nickName) {
    var socket = new SockJS("/ws");
    stompClient = Stomp.over(socket);

     stompClient.connect({nickName: nickname}, {}, function(frame) {
        // Subscribe to a topic, send messages, etc.
        stompClient.subscribe(`/user/${nickName}/queue/messages`, onMessageReceived);
        stompClient.subscribe(`/user/public`, onMessageReceived);

    }, function(error) {
        console.log('STOMP error: ' + error);
    });
    findAndDisplayConnectedUsers().then();
}


async function findAndDisplayConnectedUsers() {
     //logged user
    const currentUserNickname = `${nickName}`;

    const connectedUsersResponse = await fetch('/users');
    let connectedUsers = await connectedUsersResponse.json();

     // Filter out the current user
    connectedUsers = connectedUsers.filter(user => user.nickName !== currentUserNickname);

    const connectedUsersList = document.getElementById('connectedUsers');
    connectedUsersList.innerHTML = '';

    connectedUsers.forEach(user => {
        appendUserElement(user, connectedUsersList);
        if (connectedUsers.indexOf(user) < connectedUsers.length - 1) {
            const separator = document.createElement('li');
            separator.classList.add('separator');
            connectedUsersList.appendChild(separator);
        }
    });
}


function appendUserElement(user, connectedUsersList) {
    const listItem = document.createElement('li');
    listItem.classList.add('user-item');
    listItem.id = user.nickName;

    const userImage = document.createElement('img');
    userImage.src = '/img/user_icon.png';
    userImage.alt = user.nickName;

    const usernameSpan = document.createElement('span');
    usernameSpan.textContent = user.nickName;

    const receivedMsgs = document.createElement('span');
    receivedMsgs.textContent = '0';
    receivedMsgs.classList.add('nbr-msg', 'hidden');

    listItem.appendChild(userImage);
    listItem.appendChild(usernameSpan);
    listItem.appendChild(receivedMsgs);

    listItem.addEventListener('click', userItemClick);

    connectedUsersList.appendChild(listItem);
}


function userItemClick(event) {
    document.querySelectorAll('.user-item').forEach(item => {
        item.classList.remove('active');
    });
    messageForm.classList.remove('hidden');

    const clickedUser = event.currentTarget;
    clickedUser.classList.add('active');

    selectedUserId = clickedUser.getAttribute('id');
    fetchAndDisplayUserChat().then();

    const nbrMsg = clickedUser.querySelector('.nbr-msg');
    nbrMsg.classList.add('hidden');
    nbrMsg.textContent = '0';

}

function displayMessage(senderId, content) {
    const messageContainer = document.createElement('div');
    messageContainer.classList.add('message');

    if (senderId === nickName) {
        messageContainer.classList.add('sender');
        chatArea.classList.add('bg-img');
    } else {
        messageContainer.classList.add('receiver');
        chatArea.classList.add('bg-img');
    }
    const message = document.createElement('p');
    message.textContent = content;
    messageContainer.appendChild(message);
    chatArea.appendChild(messageContainer);

}

async function fetchAndDisplayUserChat() {
    const userChatResponse = await fetch(`/messages/${nickName}/${selectedUserId}`);
    const userChat = await userChatResponse.json();

    chatArea.innerHTML = '';
    userChat.forEach(chat => {
        displayMessage(chat.senderId, chat.content);
    });
    chatArea.scrollTop = chatArea.scrollHeight;
}


function onError() {
    connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
    connectingElement.style.color = 'red';
}


function sendMessage(event) {
    const messageContent = messageInput.value.trim();
    if (messageContent && stompClient) {
        const chatMessage = {
            senderId: nickName,
            recipientId: selectedUserId,
            content: messageContent,
            timestamp: new Date()
        };
        stompClient.send("/app/chat", {}, JSON.stringify(chatMessage));
        displayMessage(nickName, messageContent);
        messageInput.value = '';
    }
    chatArea.scrollTop = chatArea.scrollHeight;
    event.preventDefault();
}




async function onMessageReceived(payload) {
    await findAndDisplayConnectedUsers();
    console.log('Message received', payload);
    const message = JSON.parse(payload.body);
    if (selectedUserId && selectedUserId === message.senderId) {
        displayMessage(message.senderId, message.content);
        chatArea.scrollTop = chatArea.scrollHeight;
    }

    if (selectedUserId) {
        document.querySelector(`#${selectedUserId}`).classList.add('active');
    } else {
        messageForm.classList.add('hidden');
    }

    const notifiedUser = document.querySelector(`#${message.senderId}`);
    if (notifiedUser && !notifiedUser.classList.contains('active')) {
        const nbrMsg = notifiedUser.querySelector('.nbr-msg');
        nbrMsg.classList.remove('hidden');
        nbrMsg.textContent = '';
    }
}

function onLogout() {
 if(stompClient != null){
    stompClient.send('/logout',
        {},
      JSON.stringify({nickName: nickname,})
    );
   window.location.href = "/logout/"+nickName;
    }else{
     console.error('stompClient is not initialized.');
    }
}


dots.addEventListener('click', function(){
   drop.classList.toggle("show");
})

messageForm.addEventListener('submit', sendMessage, true);
logout.addEventListener('click', onLogout, true);
window.onbeforeunload = () => onLogout();