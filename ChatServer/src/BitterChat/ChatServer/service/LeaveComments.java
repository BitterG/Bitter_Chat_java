package BitterChat.ChatServer.service;

import BitterChat.chatcommon.Message;

import java.util.HashMap;

/**
 * @author 苦瓜
 * 我亦无他，惟手熟尔。
 * Time:2022年05月23日
 */
public class LeaveComments {

    private static HashMap<String , Message> leaveComments = new HashMap<>();  //留言保存

    public void addLeaveComments(Message message, String userId) {
        leaveComments.put(userId, message);
//        System.out.println("保存get留言消息时的 内容" + leaveComments.keySet());
//        System.out.println("留言内容 " + leaveComments.get(userId).getContent());
    }

    public HashMap<String, Message> getLeaveComments() {
//        System.out.println("调用get留言消息时的 内容" + leaveComments.keySet());
        return leaveComments;
    }
}
