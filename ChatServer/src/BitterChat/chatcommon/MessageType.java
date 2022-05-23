package BitterChat.chatcommon;

/**
 * @author 苦瓜
 * 我亦无他，惟手熟尔。
 * Time:2022年05月21日
 */
public interface MessageType {
    String MESSAGE_LOGIN_SUCCEED = "1"; //登录成功
    String MESSAGE_LOGIN_FAIL = "2"; //登录失败
    String MESSAGE_COMM_MES = "3"; //普通信息
    String MESSAGE_GET_ONLINE_FRIEND = "4"; //请求返回在线用户列表
    String MESSAGE_RET_ONLINE_FRIEND = "5"; //返回在线用户列表
    String MESSAGE_CLIENT_EXIT = "6"; //客户端请求退出
    String MESSAGE_All_MES = "7"; //普通信息
    String MESSAGE_FILE = "8"; //文件消息
}
