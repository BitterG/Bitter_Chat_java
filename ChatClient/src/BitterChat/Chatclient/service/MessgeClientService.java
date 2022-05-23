package BitterChat.Chatclient.service;

import BitterChat.chatcommon.Message;
import BitterChat.chatcommon.MessageType;

import java.io.IOException;
import java.io.ObjectOutputStream;
/**
 * @author 苦瓜
 * 我亦无他，惟手熟尔。
 * Time:2022年05月22日
 * 消息方法
 */
public class MessgeClientService {  //私聊方法
    public void sendMessagetoOne(String content, String senderId, String getterId) {
        Message message = new Message();
        message.setSender(senderId);
        message.setContent(content);
        message.setGetter(getterId);
        message.setType(MessageType.MESSAGE_COMM_MES);
        message.setSendTime(new java.util.Date().toString());
        System.out.println("私聊消息已发送");

        try {
            ObjectOutputStream oos = new ObjectOutputStream
                    (ManageClientConnectServerThread.getClientConnectServerThread(senderId).getSocket().getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessagetoEveryone(String contentEveryone, String senderId) {
        Message message = new Message();
        message.setContent(contentEveryone);
        message.setSender(senderId);
        message.setType(MessageType.MESSAGE_All_MES);
        message.setSendTime(new java.util.Date().toString());
        System.out.println("群发消息已发送");

        try {
            ObjectOutputStream oos = new ObjectOutputStream
                    (ManageClientConnectServerThread.getClientConnectServerThread(senderId).getSocket().getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
