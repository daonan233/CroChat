
package common.model.entity;

import java.io.Serializable;
import java.util.Date;

public class FileInfo implements Serializable {
    private static final long serialVersionUID = -5394575332459969403L;//版本控制的标识符
    /**
     * 确保反序列化时的类和序列化时的类是兼容的。如果类的结构发生了变化，
     * 比如新增了字段或者修改了方法，那么它的序列化版本号也应该随之变化，
     * 以便在反序列化时进行版本匹配，防止出现版本不一致的问题。*/
    /** 消息接收者 */
    private User toUser;
    /** 消息发送者 */
    private User fromUser;
    /** 源文件名 */
    private String srcName;
    /** 发送时间 */
    private Date sendTime;
    /** 目标地IP */
    private String destIp;
    /** 目标地端口 */
    private int destPort;
    /** 目标文件名 */
    private String destName;
    public User getToUser() {
        return toUser;
    }
    public void setToUser(User toUser) {
        this.toUser = toUser;
    }
    public User getFromUser() {
        return fromUser;
    }
    public void setFromUser(User fromUser) {
        this.fromUser = fromUser;
    }
    public String getSrcName() {
        return srcName;
    }
    public void setSrcName(String srcName) {
        this.srcName = srcName;
    }
    public Date getSendTime() {
        return sendTime;
    }
    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }
    public String getDestIp() {
        return destIp;
    }
    public void setDestIp(String destIp) {
        this.destIp = destIp;
    }
    public int getDestPort() {
        return destPort;
    }
    public void setDestPort(int destPort) {
        this.destPort = destPort;
    }
    public String getDestName() {
        return destName;
    }
    public void setDestName(String destName) {
        this.destName = destName;
    }
}
