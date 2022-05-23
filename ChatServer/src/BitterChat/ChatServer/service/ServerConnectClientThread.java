package BitterChat.ChatServer.service;

import BitterChat.chatcommon.Message;
import BitterChat.chatcommon.MessageType;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;

/**
 * @author 苦瓜
 * 我亦无他，惟手熟尔。
 * Time:2022年05月21日
 */
public class ServerConnectClientThread extends Thread{
    private Socket socket;
    private String UserId; //连接到服务端的id

    public ServerConnectClientThread(Socket socket, String UserId) {
        this.socket = socket;
        this.UserId = UserId;
    }

    LeaveComments leaveComments = new LeaveComments();

    HashMap<String, ServerConnectClientThread> hm = ManageClientThreads.getHm();
    public Socket getSocket() {
        return socket;
    }

    public void allSend(Message message) {

        for (ServerConnectClientThread serverConnectClientThread : hm.values()) {
            try {
                ObjectOutputStream oos = new ObjectOutputStream(serverConnectClientThread.getSocket().getOutputStream());
                oos.writeObject(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void run() {
        while (true) {
//            try {
//                Thread.sleep(10000);
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }

            try {
                System.out.println("userid= " + UserId + "服务端和客户端保持连接，读取数据...");
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                Message message = (Message)ois.readObject();
                if (message.getType().equals(MessageType.MESSAGE_GET_ONLINE_FRIEND)) {  //收到获取在线用户请求消息
                    String onlineUsers = ManageClientThreads.getOnlineUsers();
                    Message message1 = new Message();
                    message1.setType(MessageType.MESSAGE_RET_ONLINE_FRIEND);
                    message1.setContent(onlineUsers);
                    message1.setGetter(message1.getSender());   // 写收方
                    //返回给客户端
                    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                    oos.writeObject(message1);
                } else if (message.getType().equals(MessageType.MESSAGE_CLIENT_EXIT)) { // 客户端断开链接
                    System.out.println("检测到" + message.getSender() + "退出");
                    Thread.sleep(1);
                    ManageClientThreads.delServerConnectClientThread(message.getSender());  //线程移出数组
                    socket.close(); //关闭链接
                    break; //退出线程
                } else if (message.getType().equals(MessageType.MESSAGE_COMM_MES)) {    //私聊消息
                    if (hm.get(message.getGetter()) != null && ChatServer.getValidUsers().get(message.getGetter()) != null) {
                        /** 用户存在且在线情况 */
                        //转发给用户
                        System.out.println("私聊消息");
                        ServerConnectClientThread serverConnectClientThread =
                                ManageClientThreads.getServerConnectClientThread(message.getGetter());
                        ObjectOutputStream oos = new ObjectOutputStream(serverConnectClientThread.getSocket().getOutputStream());
                        oos.writeObject(message);
//                        oos.close(); // 关闭流报错？？？
                    } else if (hm.get(message.getGetter()) == null && ChatServer.getValidUsers().get(message.getGetter()) != null) {
                        /** 用户存在但不在线情况 */
                        System.out.println("用户存在但不在线情况-触发留言机制");
                        leaveComments.addLeaveComments(message, message.getGetter());
                    } else {
                        /** 用户不存在 */
                        System.out.println("检测到用户不存在");
                        ServerConnectClientThread serverConnectClientThread =
                                ManageClientThreads.getServerConnectClientThread(message.getSender());
                        ObjectOutputStream oos = new ObjectOutputStream(serverConnectClientThread.getSocket().getOutputStream());
                        message.setContent("用户不存在，请检查用户ID是否正确");
                        message.setSender("服务器");
                        oos.writeObject(message);
                    }

                } else if (message.getType().equals(MessageType.MESSAGE_All_MES)) { //群发消息
                    allSend(message);
                } else if (message.getType().equals(MessageType.MESSAGE_FILE)) {    //文件消息
                    //转发给用户
                    ServerConnectClientThread serverConnectClientThread =
                            ManageClientThreads.getServerConnectClientThread(message.getGetter());
                    ObjectOutputStream oos = new ObjectOutputStream(serverConnectClientThread.getSocket().getOutputStream());
                    oos.writeObject(message);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
