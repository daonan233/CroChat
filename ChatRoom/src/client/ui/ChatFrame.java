package client.ui;

import client.ClientThread;
import client.DataBuffer;
import client.model.entity.MyCellRenderer;
import client.model.entity.OnlineUserListModel;
import client.util.ClientUtil;
import client.util.JFrameShaker;
import common.model.entity.FileInfo;
import common.model.entity.Message;
import common.model.entity.Request;
import common.model.entity.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ChatFrame extends JFrame {
    private static final long serialVersionUID = -2310785591507878535L;
    private JLabel otherInfoLbl;  // 聊天对方的信息Label
    private JLabel currentUserLabel;  // 当前用户信息Lbl
    public static JTextArea msgListArea;  // 聊天信息列表区域
    public static JTextArea sendArea;  // 要发送的信息区域
    public static JList onlineList;  // 在线用户列表
    public static JLabel onlineCountLabel;  // 在线用户数统计Label
    public static FileInfo sendFile;  // 准备发送的文件
    public JCheckBox privateChatCheckBox;  // 私聊选项勾选框

    public ChatFrame() {
        this.init();
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setVisible(true);
    }

    // 初始化聊天框界面
    public void init() {
        this.setTitle("聊天室（用户：" + DataBuffer.currentUser.getNickname() + "）");
        this.setSize(550, 500);
        this.setResizable(false);

        // 设置默认窗体在屏幕中央
        int x = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
        int y = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
        this.setLocation((x - this.getWidth()) / 2, (y - this.getHeight()) / 2);

        // 左边主面板
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        // 右边用户面板
        JPanel userPanel = new JPanel();
        userPanel.setLayout(new BorderLayout());

        // 创建一个分隔窗格
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                mainPanel, userPanel);
        splitPane.setDividerLocation(380);
        splitPane.setDividerSize(10);
        splitPane.setOneTouchExpandable(true);
        this.add(splitPane, BorderLayout.CENTER);

        // 左上边信息显示面板
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BorderLayout());
        // 右下连发送消息面板
        JPanel sendPanel = new JPanel();
        sendPanel.setLayout(new BorderLayout());

        // 创建一个分隔窗格
        JSplitPane splitPane2 = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                infoPanel, sendPanel);
        splitPane2.setDividerLocation(300);
        splitPane2.setDividerSize(1);
        mainPanel.add(splitPane2, BorderLayout.CENTER);

        otherInfoLbl = new JLabel("当前状态：群聊中...");
        infoPanel.add(otherInfoLbl, BorderLayout.NORTH);

        msgListArea = new JTextArea();
        msgListArea.setLineWrap(true);
        infoPanel.add(new JScrollPane(msgListArea,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));

        JPanel tempPanel = new JPanel();
        tempPanel.setLayout(new BorderLayout());
        sendPanel.add(tempPanel, BorderLayout.NORTH);

        // 聊天按钮面板
        JPanel btnPanel = new JPanel();
        btnPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        tempPanel.add(btnPanel, BorderLayout.CENTER);


        // 窗口振动按钮
        JButton shakeBtn = new JButton(new ImageIcon("ChatRoom/images/shakeit.png"));
        shakeBtn.setMargin(new Insets(0, 0, 0, 0));
        shakeBtn.setToolTipText("向对方发送窗口振动");
        btnPanel.add(shakeBtn);

        // 发送文件按钮
        JButton sendFileBtn = new JButton(new ImageIcon("ChatRoom/images/file.png"));
        sendFileBtn.setMargin(new Insets(0, 0, 0, 0));
        sendFileBtn.setToolTipText("向对方发送文件");
        btnPanel.add(sendFileBtn);

        // 私聊勾选框
        privateChatCheckBox = new JCheckBox("私聊");
        tempPanel.add(privateChatCheckBox, BorderLayout.EAST);

        // 要发送的信息的区域
        sendArea = new JTextArea();
        sendArea.setLineWrap(true);
        sendPanel.add(new JScrollPane(sendArea,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));

        // 聊天按钮面板
        JPanel btn2Panel = new JPanel();
        btn2Panel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        this.add(btn2Panel, BorderLayout.SOUTH);
        JButton closeBtn = new JButton("关闭");
        closeBtn.setToolTipText("退出整个程序");
        btn2Panel.add(closeBtn);
        JButton submitBtn = new JButton("发送");
        submitBtn.setToolTipText("按Enter键发送消息");
        btn2Panel.add(submitBtn);
        sendPanel.add(btn2Panel, BorderLayout.SOUTH);

        // 在线用户列表面板
        JPanel onlineListPane = new JPanel();
        onlineListPane.setLayout(new BorderLayout());
        onlineCountLabel = new JLabel("在线用户列表(1)");
        onlineListPane.add(onlineCountLabel, BorderLayout.NORTH);

        // 当前用户面板
        JPanel currentUserPane = new JPanel();
        currentUserPane.setLayout(new BorderLayout());
        currentUserPane.add(new JLabel("个人账号信息 ---------"), BorderLayout.NORTH);

        // 右边用户列表创建一个分隔窗格
        JSplitPane splitPane3 = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                onlineListPane, currentUserPane);
        splitPane3.setDividerLocation(340);
        splitPane3.setDividerSize(1);
        userPanel.add(splitPane3, BorderLayout.CENTER);

        // 获取在线用户并缓存
        DataBuffer.onlineUserListModel = new OnlineUserListModel(DataBuffer.onlineUsers);

        // 在线用户列表
        onlineList = new JList(DataBuffer.onlineUserListModel);
        onlineList.setCellRenderer(new MyCellRenderer());

        // 设置为单选模式
        onlineList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        onlineListPane.add(new JScrollPane(onlineList,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));

        // 当前用户信息Label
        currentUserLabel = new JLabel();
        currentUserPane.add(currentUserLabel);

        /**
         * 注册监听事件
         */
        // 关闭窗口
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                logout();
            }
        });

        // 关闭按钮的监听事件
        closeBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                logout();
            }
        });

        // 私聊勾选框的监听事件
        privateChatCheckBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // 如果私聊被勾选
                if (privateChatCheckBox.isSelected()) {
                    // 获取选中的用户
                    User selectedUser = (User) onlineList.getSelectedValue();
                    if (null == selectedUser) {
                        // 没有选中用户
                        otherInfoLbl.setText("当前状态：私聊(选中在线用户列表中某个用户进行私聊)...");
                    } else if (DataBuffer.currentUser.getId() == selectedUser.getId()) {
                        // 选中的用户为自己
                        otherInfoLbl.setText("当前状态：不可以自言自语哦❌");
                    } else {
                        otherInfoLbl.setText("当前状态：与 " + selectedUser.getNickname()
                                + "(" + selectedUser.getId() + ") 私聊中...");
                    }
                } else {
                    otherInfoLbl.setText("当前状态：群聊...");
                }
            }
        });

        // onlineList上的鼠标监听事件（选择某个用户）
        onlineList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                // selectedUser为选中的用户
                User selectedUser = (User) onlineList.getSelectedValue();

                // 如果私聊被勾选
                if (privateChatCheckBox.isSelected()) {
                    // 选中的用户为自己
                    if (DataBuffer.currentUser.getId() == selectedUser.getId()) {
                        otherInfoLbl.setText("当前状态：不可以自言自语哦❌");
                    } else {
                        otherInfoLbl.setText("当前状态：与 " + selectedUser.getNickname()
                                + "(" + selectedUser.getId() + ") 私聊中...");
                    }
                } else {
                    otherInfoLbl.setText("当前状态：群聊...");
                }
            }
        });

        // 发送文本消息
        sendArea.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == Event.ENTER) {
                    sendTxtMsg();
                }
            }
        });
        submitBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                sendTxtMsg();
            }
        });

        // 发送振动
        shakeBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                sendShakeMsg();
            }
        });

        // 发送文件
        sendFileBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                sendFile();
            }
        });

        this.loadData();  // 加载初始数据（用户列表、个人信息）
    }

    /**
     * 加载数据
     */
    public void loadData() {
        // 加载当前用户数据
        if (null != DataBuffer.currentUser) {
            currentUserLabel.setIcon(
                    new ImageIcon("ChatRoom/images/" + DataBuffer.currentUser.getHead() + ".jpg"));
            currentUserLabel.setText(DataBuffer.currentUser.getNickname()
                    + " (id: " + DataBuffer.currentUser.getId() + ")");
        }
        // 设置在线用户列表
        onlineCountLabel.setText("当前在线用户(" + DataBuffer.onlineUserListModel.getSize() + ") -------");

        // 启动监听服务器消息的线程（用于接收系统消息）
        new ClientThread(this).start();
    }

    /**
     * 发送振动
     */
    public void sendShakeMsg() {
        User selectedUser = (User) onlineList.getSelectedValue();
        if (null != selectedUser) {
            if (DataBuffer.currentUser.getId() == selectedUser.getId()) {
                JOptionPane.showMessageDialog(ChatFrame.this, "不能给自己发送振动!",
                        "不能发送", JOptionPane.ERROR_MESSAGE);
            } else {
                Message msg = new Message();
                msg.setFromUser(DataBuffer.currentUser);
                msg.setToUser(selectedUser);
                msg.setSendTime(new Date());

                DateFormat df = new SimpleDateFormat("HH:mm:ss");
                StringBuffer sb = new StringBuffer();
                sb.append(" ").append(msg.getFromUser().getNickname())
                        .append("(").append(msg.getFromUser().getId()).append(") ")
                        .append(df.format(msg.getSendTime()))
                        .append("\n  给").append(msg.getToUser().getNickname())
                        .append("(").append(msg.getToUser().getId()).append(") ")
                        .append("发送了一个窗口抖动\n");
                msg.setMessage(sb.toString());

                Request request = new Request();
                request.setAction("shake");
                request.setAttribute("msg", msg);
                try {
                    ClientUtil.sendTextRequest2(request);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ClientUtil.appendTxt2MsgListArea(msg.getMessage());
                new JFrameShaker(ChatFrame.this).startShake();
            }
        } else {
            JOptionPane.showMessageDialog(ChatFrame.this, "不能在群里发送振动!",
                    "不能发送", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * 发送文本消息
     */
    public void sendTxtMsg() {
        String content = sendArea.getText();
        if ("".equals(content)) {
            // 无内容
            JOptionPane.showMessageDialog(ChatFrame.this, "不能发送空消息!",
                    "不能发送", JOptionPane.ERROR_MESSAGE);
        } else {
            // 发送
            User selectedUser = (User) onlineList.getSelectedValue();
//			if(null != selectedUser &&
//					DataBuffer.currentUser.getId() == selectedUser.getId()){
//				JOptionPane.showMessageDialog(ChatFrame.this, "不能给自己发送消息!",
//						"不能发送", JOptionPane.ERROR_MESSAGE);
//				return;
//			}

            // 如果设置了ToUser表示私聊，否则群聊
            Message msg = new Message();

            // 私聊
            if (privateChatCheckBox.isSelected()) {
                if (null == selectedUser) {
                    JOptionPane.showMessageDialog(ChatFrame.this, "没有选择私聊对象!",
                            "不能发送", JOptionPane.ERROR_MESSAGE);
                    return;
                } else if (DataBuffer.currentUser.getId() == selectedUser.getId()) {
                    JOptionPane.showMessageDialog(ChatFrame.this, "不能给自己发送消息!",
                            "不能发送", JOptionPane.ERROR_MESSAGE);
                    return;
                } else {
                    msg.setToUser(selectedUser);
                }
            }
            msg.setFromUser(DataBuffer.currentUser);
            msg.setSendTime(new Date());

            DateFormat df = new SimpleDateFormat("HH:mm:ss");
            StringBuffer sb = new StringBuffer();
            sb.append(" ").append(df.format(msg.getSendTime())).append(" ")
                    .append(msg.getFromUser().getNickname())
                    .append("(").append(msg.getFromUser().getId()).append(") ");

            // 群聊
            if (!this.privateChatCheckBox.isSelected()) {
                sb.append("对大家说");
            }
            sb.append("\n  ").append(content).append("\n");
            msg.setMessage(sb.toString());

            Request request = new Request();
            request.setAction("chat");
            request.setAttribute("msg", msg);
            try {
                ClientUtil.sendTextRequest2(request);
            } catch (IOException e) {
                e.printStackTrace();
            }

            // JTextArea中按“Enter”时，清空内容并回到首行
            InputMap inputMap = sendArea.getInputMap();
            ActionMap actionMap = sendArea.getActionMap();
            Object transferTextActionKey = "TRANSFER_TEXT";
            inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), transferTextActionKey);
            actionMap.put(transferTextActionKey, new AbstractAction() {
                private static final long serialVersionUID = 7041841945830590229L;

                public void actionPerformed(ActionEvent e) {
                    sendArea.setText("");
                    sendArea.requestFocus();
                }
            });
            sendArea.setText("");
            ClientUtil.appendTxt2MsgListArea(msg.getMessage());
        }
    }

    /**
     * 发送文件
     */
    private void sendFile() {
        User selectedUser = (User) onlineList.getSelectedValue();
        if (null != selectedUser) {
            if (DataBuffer.currentUser.getId() == selectedUser.getId()) {
                JOptionPane.showMessageDialog(ChatFrame.this, "不能给自己发送文件!",
                        "不能发送", JOptionPane.ERROR_MESSAGE);
            } else {
                JFileChooser jfc = new JFileChooser();
                if (jfc.showOpenDialog(ChatFrame.this) == JFileChooser.APPROVE_OPTION) {
                    File file = jfc.getSelectedFile();
                    sendFile = new FileInfo();
                    sendFile.setFromUser(DataBuffer.currentUser);
                    sendFile.setToUser(selectedUser);
                    try {
                        sendFile.setSrcName(file.getCanonicalPath());
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    sendFile.setSendTime(new Date());

                    Request request = new Request();
                    request.setAction("toSendFile");
                    request.setAttribute("file", sendFile);
                    try {
                        ClientUtil.sendTextRequest2(request);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    ClientUtil.appendTxt2MsgListArea("【文件消息】向 "
                            + selectedUser.getNickname() + "("
                            + selectedUser.getId() + ") 发送文件 ["
                            + file.getName() + "]，等待对方接收...\n");
                }
            }
        } else {
            JOptionPane.showMessageDialog(ChatFrame.this, "不能给所有在线用户发送文件!",
                    "不能发送", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * 关闭客户端
     */
    private void logout() {
        int select = JOptionPane.showConfirmDialog(ChatFrame.this,
                "确定退出吗？\n\n退出程序将中断与服务器的连接!", "退出聊天室",
                JOptionPane.YES_NO_OPTION);
        if (select == JOptionPane.YES_OPTION) {
            Request req = new Request();
            req.setAction("exit");
            req.setAttribute("user", DataBuffer.currentUser);
            try {
                ClientUtil.sendTextRequest(req);
            } catch (IOException ex) {
                ex.printStackTrace();
            } finally {
                System.exit(0);
            }
        } else {
            this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        }
    }

    /* 踢除 */
    public static void remove() {
        int select = JOptionPane.showConfirmDialog(sendArea,
                "您已被踢除？\n\n", "系统通知",
                JOptionPane.YES_NO_OPTION);

        Request req = new Request();
        req.setAction("exit");
        req.setAttribute("user", DataBuffer.currentUser);
        try {
            ClientUtil.sendTextRequest(req);
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            System.exit(0);
        }

    }
}
