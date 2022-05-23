package BitterChat.ChatServer.service;

import BitterChat.chatcommon.Message;
import BitterChat.chatcommon.User;
import BitterChat.chatcommon.MessageType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 苦瓜
 * 我亦无他，惟手熟尔。
 * Time:2022年05月21日
 */
public class ChatServer {
    private ServerSocket serverSocket = null;
    // 创建一个存放用户的集合（之后用数据库）
    private static ConcurrentHashMap<String, User> validUsers = new ConcurrentHashMap<>();
    HashMap<String, ServerConnectClientThread> hm = ManageClientThreads.getHm();

    LeaveComments leaveComments = new LeaveComments();

    static {    //在静态代码块初始化用户集合
        validUsers.put("10001", new User("10001", "123456"));
        validUsers.put("10002", new User("10002", "123456"));
        validUsers.put("10003", new User("10003", "123456"));
        validUsers.put("苦瓜", new User("苦瓜", "123456"));
        validUsers.put("10005", new User("10004", "123456"));
    }

    // 验证用户合法方法
    public boolean checkUser(String userId, String password) {
        if (validUsers.get(userId) == null) {
            return false;
        }
        if (!validUsers.get(userId).getPassword().equals(password)) {
            return false;
        }
        return true;
    }

    public void send_leavecomments() {
        System.out.println("发送模块");
        System.out.println(leaveComments.getLeaveComments().keySet());
        System.out.println(hm.keySet());
        for (String getter : leaveComments.getLeaveComments().keySet()) {
//            System.out.println("getter= " + getter); //
            for (String onlineUser : hm.keySet()) {
//                System.out.println("onlineUser= " + onlineUser); //
                if (getter.equals(onlineUser)) {
                    System.out.println("检测到离线用户上线，发送离线消息");
                    /** 用户存在且在线情况 */
                    leaveComments.getLeaveComments().get(getter).setContent
                            ("(留言消息)" + leaveComments.getLeaveComments().get(getter).getContent());
                    //转发给用户
                    try {
                        ServerConnectClientThread serverConnectClientThread =
                                ManageClientThreads.getServerConnectClientThread(getter);
                        ObjectOutputStream oos = new ObjectOutputStream(serverConnectClientThread.getSocket().getOutputStream());
                        oos.writeObject(leaveComments.getLeaveComments().get(getter));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }

    public static ConcurrentHashMap<String, User> getValidUsers() {
        return validUsers;
    }

    public ChatServer() {
        try {
            System.out.println("服务端在9999端口监听");
            serverSocket = new ServerSocket(9999);
            while (true) {  //监听是循环的，建立连接后继续继续监听
                Socket socket = serverSocket.accept();
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                 User user = (User) ois.readObject();
                System.out.println(user.getUserId() + "\t" + user.getPassword());
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                //创建一个对象，准备回复客户端
                Message message = new Message();
                //调用验证方法
                if (checkUser(user.getUserId(), user.getPassword())) {    //成功
                    message.setType(MessageType.MESSAGE_LOGIN_SUCCEED);
                    //将message返回给客户都端
                    oos.writeObject(message);
                    //创建线程和客户端保持连接，线程持有socket对象
                    ServerConnectClientThread serverConnectClientThread = new ServerConnectClientThread(socket, user.getUserId());
                    serverConnectClientThread.start();
                    //将线程对象放入到集合中管理
                    ManageClientThreads.addServerConnectClientThread(user.getUserId(), serverConnectClientThread);
                    /** 每成功登录一个用户就检测发送一遍留言模块 */
                    send_leavecomments();
                } else {    //失败
                    message.setType(MessageType.MESSAGE_LOGIN_FAIL);
                    oos.writeObject(message);
                    socket.close(); //关闭链接
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally { // 若退出while循环则，服务器关闭释放资源
            try {
                serverSocket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
