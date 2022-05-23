package BitterChat.ClientView;

import BitterChat.Chatclient.service.UserClientService;
import BitterChat.Chatclient.service.MessgeClientService;
import BitterChat.utils.Utility;
import BitterChat.Chatclient.service.FileClientService;

import java.io.IOException;

/**
 * @author 苦瓜
 * 我亦无他，惟手熟尔。
 * Time:2022年05月21日
 */
public class chatClientGui {
    private String key = ""; // 获取用户输入
    private boolean loop = true;    //控制菜单循环
    private UserClientService userClientService = new UserClientService();  //用于登录服务器
    private MessgeClientService messgeClientService = new MessgeClientService();
    private FileClientService fileClientService = new FileClientService();

    public static void main(String[] args) throws Exception {    // 测试
        chatClientGui chatClientGui = new chatClientGui();
        chatClientGui.mainMenu();
    }
    //显示主菜单
    private void mainMenu() throws IOException, ClassNotFoundException {
        while (loop) {

            System.out.println("=================菜主单=================");
            System.out.println("\t1.登录系统");
            System.out.println("\t9.退出系统");
            System.out.print("请输入先择；");
            key = Utility.readString(1);

            //根据用户输入处理不同逻辑
            switch (key) {
                case "1":
                    System.out.print("输入账号:");
                    String userId = Utility.readString(10);
                    System.out.print("输入密码:");
                    String password = Utility.readString(15);
                    //获取数据发送给服务器判断
//                    System.out.println(userId + "-" + password);
                    if (userClientService.checkUser(userId, password)) {
                        System.out.println("=========欢迎====" + userId + "=============");
                        while (loop) {
                            System.out.println("\t1.显示在线用户列表");
                            System.out.println("\t2.群发消息");
                            System.out.println("\t3.私聊消息");
                            System.out.println("\t4.发送文件");
                            System.out.println("\t9.退出系统");

                            System.out.print("请输入先择:");
                            key = Utility.readString(1);
                            switch (key) {
                                case "1":
//                                    System.out.println("在线列表");
                                    userClientService.getOnlineUsers();
                                    break;
                                case "2":
//                                    System.out.println("群发消息");
                                    System.out.print("输入群发消息内容：");
                                    String contentEveryone = Utility.readString(500);
                                    //群发方法
                                    messgeClientService.sendMessagetoEveryone(contentEveryone, userId);
                                    break;
                                case "3":
//                                    System.out.println("私聊消息");
                                    System.out.print("请输入发送方(在线):");
                                    String getterId = Utility.readString(50);
                                    System.out.print("请输入发送内容:");
                                    String content = Utility.readString(500);
                                    //编写方法将消息发送给服务端
                                    messgeClientService.sendMessagetoOne(content, userId, getterId);
                                    break;
                                case "4":
//                                    System.out.println("发送文件");
                                    System.out.print("请输入文件接收者：");
                                    String fileGetterId = Utility.readString(50);
                                    System.out.print("请输入文件地址：");
                                    String dest = Utility.readString(50);
                                    fileClientService.sendFileToOne(userId, fileGetterId, dest);
                                    break;
                                case "9":
                                    System.out.println("退出系统");
                                    userClientService.logout(); //调用无异常退出方法
                                    loop = false;
                                    break;
                            }
                        }
                    } else {
                        System.out.println("======登录失败======");
                    }
                    break;
                case "9":
                    System.out.println("退出系统");
                    loop = false;
                    break;
            }
        }
    }
}
