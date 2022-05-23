package BitterChat.Chatclient.service;

import BitterChat.chatcommon.Message;
import BitterChat.chatcommon.MessageType;
import BitterChat.chatcommon.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

/**
 * @author 苦瓜
 * 我亦无他，惟手熟尔。
 * Time:2022年05月21日
 * 用户登录验证，注册类
 */
public class UserClientService {
    private boolean result = false;
    private User user = new User(); //作成属性方便以后直接使用信息
    private Socket socket;  //作成属性其他位置也可以调用同一个socket

    //登录
    public boolean checkUser(String userId, String pwd) throws IOException, ClassNotFoundException {
        //发送数据到服务器
        user.setUserId(userId); //给user属性赋值发送到服务器
        user.setPassword(pwd);

        //连接服务器发送数据
        socket = new Socket(InetAddress.getByName("192.168.226.162"), 9999);
        System.out.println(socket.getClass());
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
        oos.writeObject(user);  //发送

        //读取从服务端回送的数据
        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
        Message message = (Message)ois.readObject();    //读取回送信息

        if (message.getType().equals(MessageType.MESSAGE_LOGIN_SUCCEED)) {   //成功
            //创建一个和服务器连接的线程 线程类（ClientConnectServerThread）
            ClientConnectServerThread clientConnectServerThread = new ClientConnectServerThread(socket);
            clientConnectServerThread.start();
            //为后续扩展，将线程法入集合中管理
            ManageClientConnectServerThread.addClientConnectServerThread(userId, clientConnectServerThread);
            result = true;
        } else {    //失败
            //  关闭socket
            socket.close();
        }
        return result;
    }

    //向服务器端请求在线用户列表
    public void getOnlineUsers() throws IOException {
        //发送msg请求
        Message message = new Message();
        message.setType(MessageType.MESSAGE_GET_ONLINE_FRIEND);
        //发送
        ObjectOutputStream oos = new ObjectOutputStream
                (ManageClientConnectServerThread.getClientConnectServerThread(user.getUserId()).getSocket().getOutputStream());
        oos.writeObject(message);
    }

    //客户端安全退出方法
     public void logout() {
         Message message = new Message();
         message.setType(MessageType.MESSAGE_CLIENT_EXIT);
         message.setSender(user.getUserId());   //指定客户端

         try {
//             ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
             ObjectOutputStream oos = new ObjectOutputStream
                     (ManageClientConnectServerThread.getClientConnectServerThread(user.getUserId()).getSocket().getOutputStream());
             oos.writeObject(message);
             System.out.println(user.getUserId() + " 向服务器发送请求退出系统");
             System.exit(0);    // 关闭程序
         } catch (IOException e) {
             e.printStackTrace();
         }
     }
}

