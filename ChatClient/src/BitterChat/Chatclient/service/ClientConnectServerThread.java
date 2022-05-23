package BitterChat.Chatclient.service;

import BitterChat.chatcommon.Message;
import BitterChat.chatcommon.MessageType;

import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * @author 苦瓜
 * 我亦无他，惟手熟尔。
 * Time:2022年05月21日
 */
public class ClientConnectServerThread extends Thread {
    //持有socket
    private Socket socket;

    //接收socket对象
    public ClientConnectServerThread(Socket socket) {
        this.socket = socket;
    }

    FileClientService fileClientService = new FileClientService();

    @Override
    public void run() {
        //Thread使用循环与服务器保持连接
        while (true) {
//            System.out.println("等待读取服务端返回消息...");
            try {
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                Message message = (Message)ois.readObject(); //无数据将阻塞

                if (message.getType().equals(MessageType.MESSAGE_RET_ONLINE_FRIEND)) {  //判断服务端信息为在线好友
                    //取出数据显示
                    String[] onlineusers = message.getContent().split(" ");
                    System.out.println("===========当前在线用户列表===========");
                    for (int i = 0; i < onlineusers.length; i++) {
                        System.out.println(onlineusers[i]);
                    }
                    System.out.println("===================================");
                } else if (message.getType().equals(MessageType.MESSAGE_COMM_MES)) {    //普通聊天消息
                    //展示消息
                    System.out.print("\n");
                    System.out.println(message.getSendTime());
                    System.out.println("\t" + message.getSender() + "对你说：" + message.getContent());
                } else if (message.getType().equals(MessageType.MESSAGE_All_MES)) {
                    System.out.print("\n");
                    System.out.println(message.getSendTime());
                    System.out.println("\t" + message.getSender() + "对所有人说：" + message.getContent());
                } else if (message.getType().equals(MessageType.MESSAGE_FILE)) {
                    System.out.print("\n");
                    System.out.println(message.getSendTime());
                    System.out.println("\t" + message.getSender() + "向你发送文件：" + message.getFileName());
                    //文件接收方法
                    fileClientService.acceptFile(message);

                } else {
                    System.out.println("其他类型Message，暂不处理");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //为方便之后得到socket提供get方法
    public Socket getSocket() {
        return socket;
    }

}
