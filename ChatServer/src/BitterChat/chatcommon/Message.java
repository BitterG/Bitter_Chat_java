package BitterChat.chatcommon;

import java.io.Serializable;

/**
 * @author 苦瓜
 * 我亦无他，惟手熟尔。
 * Time:2022年05月21日
 * 客户端和服务器端的消息对象
 */
public class Message implements Serializable {

    private static final long serialVersionUID = 1l;
    private String sender; //发送方
    private String getter; //接收者
    private String content; //消息内容
    private String sendTime; //发送时间
    private String msgType; // 消息类型[接口中定义消息类型]

    private String fileName; //文件名

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    //文件成员
    private byte[] fileBytes;
    private int fileLen;

    public byte[] getFileBytes() {
        return fileBytes;
    }

    public void setFileBytes(byte[] fileBytes) {
        this.fileBytes = fileBytes;
    }

    private String dest; //文件路径

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getGetter() {
        return getter;
    }

    public void setGetter(String getter) {
        this.getter = getter;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public String getType() {
        return msgType;
    }

    public void setType(String msgType) {
        this.msgType = msgType;
    }
}
