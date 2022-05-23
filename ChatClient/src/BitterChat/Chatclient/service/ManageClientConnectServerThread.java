package BitterChat.Chatclient.service;

import java.util.HashMap;

/**
 * @author 苦瓜
 * 我亦无他，惟手熟尔。
 * Time:2022年05月21日
 * 管理客户端连接服务端线程类
 */
public class ManageClientConnectServerThread {
    // Key 使用了用户id，val是线程
    private static HashMap<String, ClientConnectServerThread> hm = new HashMap<>();

    public static void addClientConnectServerThread(String userId, ClientConnectServerThread clientConnectServerThread) {
        hm.put(userId, clientConnectServerThread);
    }

    //提供线程获取方法
    public static ClientConnectServerThread getClientConnectServerThread(String userId) {
        return hm.get(userId);
    }
}
