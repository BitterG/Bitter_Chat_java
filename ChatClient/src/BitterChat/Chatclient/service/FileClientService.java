package BitterChat.Chatclient.service;

import BitterChat.chatcommon.Message;
import BitterChat.chatcommon.MessageType;

import java.io.*;

/**
 * @author 苦瓜
 * 我亦无他，惟手熟尔。
 * Time:2022年05月22日
 */
public class FileClientService {
    /**
     *
     * @param sender    发送者
     * @param getter    接收者
//     * @param fileBytes 文件存储数组
//     * @param fileLen   纪录文件字节大小
     * @param dest  文件路径
     */
    public void sendFileToOne(String sender, String getter, String dest) throws IOException {
        //读取文件封装到Message对象
        Message message = new Message();
        message.setType(MessageType.MESSAGE_FILE);
        message.setSender(sender);
        message.setGetter(getter);
        message.setSendTime(new java.util.Date().toString());
        message.setDest(dest);
        //获取文件名
        String[] b = dest.split("\\\\");
        message.setFileName(b[b.length-1]);
//        System.out.println(message.getFileName() + "================++++++");

        System.out.println("正在读取本地文件...");
        //读取文件
        FileInputStream fis = new FileInputStream(dest);
        message.setFileBytes(fis.readAllBytes());
        fis.close();

        try {
            ObjectOutputStream oos = new ObjectOutputStream
                    (ManageClientConnectServerThread.getClientConnectServerThread(sender).getSocket().getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void acceptFile(Message message) {
        /** 是否接收文件 判断
        Scanner scanner = new Scanner(System.in);
        boolean loop_file = true;
        while (loop_file) {
            System.out.println("是否接收文件: Y / N");
            System.out.println("请选择:");
            String accept = scanner.next();

            if (accept.equals("y") || accept.equals("Y")) { //同意接收
                System.out.println("同意");
            } else if (accept.equals("n") || accept.equals("N")) {  //拒绝接收
                loop_file = false;
            } else {
                System.out.println("请输入有效选择");
            }
        }
         */
        String fileSaveFile = "e:\\test_\\接收位置\\" + message.getFileName();
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(fileSaveFile);
            fileOutputStream.write(message.getFileBytes());
            System.out.println("文件接收成功");
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
