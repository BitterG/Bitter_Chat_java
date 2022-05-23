package BitterChat.chatcommon;

import java.io.Serializable;

/**
 * @author 苦瓜
 * 我亦无他，惟手熟尔。
 * Time:2022年05月21日
 * 用户信息
 */
public class User implements Serializable {

    private static final long serialVersionUID = 1l;
    private String userId;  //用户id
    private String password; //密码

    public User(String userId, String password) {
        this.userId = userId;
        this.password = password;
    }

    public User() {}

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
