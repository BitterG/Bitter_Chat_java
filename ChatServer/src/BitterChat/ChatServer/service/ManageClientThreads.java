package BitterChat.ChatServer.service;

import java.util.HashMap;

/**
 * @author 苦瓜
 * 我亦无他，惟手熟尔。
 * Time:2022年05月21日
 * 管理服务端和客户端的线程集合
 */
public class ManageClientThreads {
    private static HashMap<String, ServerConnectClientThread> hm = new HashMap<>();

    public static void addServerConnectClientThread(String userId, ServerConnectClientThread serverConnectClientThread) {
        hm.put(userId, serverConnectClientThread);
    }

    public static ServerConnectClientThread getServerConnectClientThread(String userId) {
        return hm.get(userId);
    }
//    public static ServerConnectClientThread getServerConnectClientThread(String userId, ServerConnectClientThread serverConnectClientThread) {
//        return hm.get(userId);
//    }

    public static void delServerConnectClientThread(String userId) {
        hm.remove(userId);  //结束线程
    }

    public static HashMap<String, ServerConnectClientThread> getHm() {
        return hm;
    }

    public static String getOnlineUsers() {
        String onlineusers = "";
        for (String onlineuser: hm.keySet()) {
            onlineusers += onlineuser + " ";
        }
        return onlineusers;
    }
}
