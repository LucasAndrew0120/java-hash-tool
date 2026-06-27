package src.main.java.top.lris625;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;
import java.awt.FileDialog;
import java.awt.Font;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.SwingUtilities;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.JTextPane;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.awt.event.ActionEvent;

public class hash
{

    // 后端文本哈希计算部分
    public static class TextHash
    {

        String text;
        String sha1;
        String sha256;
        String md5;

        private String bytesToHex(byte[] bytes)
        {
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes)
            {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        }

        public String getsha1()
        {

            try
            {
                MessageDigest md = MessageDigest.getInstance("SHA-1");
                byte[] hash = md.digest(text.getBytes());
                sha1 = bytesToHex(hash);
                return sha1;
            } catch (NoSuchAlgorithmException e)
            {
                System.out.print(e);
                return null;
            }
        }

        public String getsha256()
        {

            try
            {
                MessageDigest md = MessageDigest.getInstance("SHA-256");
                byte[] hash = md.digest(text.getBytes());
                sha256 = bytesToHex(hash);
                return sha256;
            } catch (NoSuchAlgorithmException e)
            {
                System.out.print(e);
                return null;
            }
        }

        public String getmd5()
        {

            try
            {
                MessageDigest md = MessageDigest.getInstance("MD5");
                byte[] hash = md.digest(text.getBytes());
                md5 = bytesToHex(hash);
                return md5;
            } catch (NoSuchAlgorithmException e)
            {
                System.out.print(e);
                return null;
            }
        }
    }

    // 后端文件哈希计算部分
    public static class FileHash
    {
        String gethash(JComboBox<String> comboBox, File selectedFile)
        {
            int index = comboBox.getSelectedIndex();
            String algorithm;
            
            // 根据索引选择算法
            if (index == 0)
            {
                algorithm = "SHA-1";
            } 
            else if (index == 1)
            {
                algorithm = "SHA-256";
            } 
            else if (index == 2)
            {
                algorithm = "MD5";
            } 
            else
            {
                return "不支持的算法";
            }
            
            // 计算哈希
            try (FileInputStream fis = new FileInputStream(selectedFile)) {
                MessageDigest md = MessageDigest.getInstance(algorithm);
                byte[] buffer = new byte[8192];
                int len;
                while ((len = fis.read(buffer)) != -1) {
                    md.update(buffer, 0, len);
                }
                return bytesToHex(md.digest());
            } catch (Exception e) {
                return "错误: " + e.getMessage();
            }
        }
    
        private String bytesToHex(byte[] bytes) {
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        }
    }

    // 后端比对部分
    public static class Compare
    {
        String CompareString;
        String hashnumber;

        boolean Compare()
        {

            if (CompareString.equals(hashnumber))
                return true;
            else
                return false;
        }
    }

    // 获取时间
    public static class Time
    {
        String strtime1;

        String gettime()
        {
            LocalDateTime dt = LocalDateTime.now();
            strtime1 = dt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            return strtime1;
        }
    }

    // 日志输出
    public static class LogTurnTxt
    {
        String hash;
        String outtime;
        String orgintext;
        String outtext;

        public LogTurnTxt(String hash, String outtime, String orgintext, String outtext) {
            this.hash = hash;
            this.outtime = outtime;
            this.orgintext = orgintext;
            this.outtext = outtext;
        }

        void logouttextarea(JTextArea textArea)
        {
            String formatted = String.format("[%s]用户计算了 \"%s\" 的 %s 值，结果是 %s \n", this.outtime, this.orgintext,
                    this.hash, this.outtext);
            textArea.append(formatted);
            textArea.setCaretPosition(textArea.getDocument().getLength()); // 光标锁定新输出
        }
    }

    // 导出日志
    public static class LogOut
    {
        String logouttext = new String();

        public LogOut(JTextArea jt) {
            this.logouttext = jt.getText();
        }

        void logout()
        {
            String timestamp = java.time.LocalDateTime.now() // 生成时间戳
                    .format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd-hh-mm-ss"));
            String filename = "哈希日志_" + timestamp + ".txt";

            try (PrintWriter writer = new PrintWriter(new FileWriter(filename)))
            {
                writer.print(logouttext);
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    // 前端部分
    public static class MyWindow extends JFrame
    {
        File currentSelectedFile = null;
        TextHash textHash = new TextHash();
        Compare compare = new Compare();

        JButton Filechoose; // 文件选择按钮
        JButton Calculation0; // 文件计算按钮
        JButton Calculation1; // 文本计算按钮
        JButton Compare; // 对比按钮
        JButton LogoutButton; // 日志导出按钮
        JTabbedPane tabbedPane = new JTabbedPane();
        JTextPane jt0;
        JTextArea jt0_1;
        JTextArea jt5;

        public MyWindow() {

            setTitle("Hash计算器");
            setBounds(300, 300, 800, 650); // 设置大小位置
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 关闭释放资源
            setLocationRelativeTo(null); // 居中

            JLabel emoji1 = new JLabel("❓"); // 状态Emoji
            emoji1.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
            emoji1.setAlignmentX(CENTER_ALIGNMENT);
            emoji1.setPreferredSize(new Dimension(50, 40));
            emoji1.setMaximumSize(new Dimension(50, 40));

            JLabel emoji2 = new JLabel("📁"); // 文件emoji
            emoji2.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
            emoji2.setAlignmentX(CENTER_ALIGNMENT);
            emoji2.setPreferredSize(new Dimension(50, 40));
            emoji2.setMaximumSize(new Dimension(50, 40));

            JComboBox<String> comboBox1 = new JComboBox<>(); // 增加下拉列表
            comboBox1.addItem("SHA-1");
            comboBox1.addItem("SHA-256");
            comboBox1.addItem("MD5");

            comboBox1.setPreferredSize(new Dimension(200, 25)); // 下拉列表大小控制
            comboBox1.setMinimumSize(new Dimension(200, 25));
            comboBox1.setMaximumSize(new Dimension(200, 25));
            comboBox1.setAlignmentX(CENTER_ALIGNMENT);


            JComboBox<String> comboBox2 = new JComboBox<>(); // 增加下拉列表
            comboBox2.addItem("SHA-1");
            comboBox2.addItem("SHA-256");
            comboBox2.addItem("MD5");

            comboBox2.setPreferredSize(new Dimension(300, 30)); // 下拉列表大小控制
            comboBox2.setMinimumSize(new Dimension(300, 30));
            comboBox2.setMaximumSize(new Dimension(300, 30));
            comboBox2.setAlignmentX(CENTER_ALIGNMENT);

            Filechoose = new JButton("导入文件");
            Calculation0 = new JButton("计算Hash值");
            Calculation1 = new JButton("计算Hash值");
            Compare = new JButton("比对Hash值");
            LogoutButton = new JButton("导出日志为TXT");
            
            jt0 = new JTextPane();  // 文件哈希输入
            jt0.setEditable(false); // 设置输出框只读
            jt0.setCaretColor(new Color(0, 0, 0, 0)); // 设置光标颜色为透明
            jt0_1 = new JTextArea(); // 文件哈希输出
            jt0_1.setLineWrap(true);
            jt0_1.setWrapStyleWord(true);
            JTextArea jt1 = new JTextArea(); // 文本哈希输入
            jt1.setLineWrap(true); // 自动换行
            jt1.setWrapStyleWord(true);
            JTextArea jt2 = new JTextArea(); // 一号比对框
            jt2.setLineWrap(true);
            jt2.setWrapStyleWord(true);
            JTextArea jt3 = new JTextArea(); // 二号比对框
            jt3.setLineWrap(true);
            jt3.setWrapStyleWord(true);
            JTextArea jt4 = new JTextArea(); // 用于计算输出
            jt4.setLineWrap(true);
            jt4.setWrapStyleWord(true);
            jt4.setEditable(false); // 设置输出框只读
            jt5 = new JTextArea(); // 日志区
            jt5.setLineWrap(true);
            jt5.setWrapStyleWord(true);
            jt5.setEditable(false); // 设置输出框只读

            jt0.setFont(new Font("微软雅黑", Font.PLAIN, 16)); // 文件哈希框
            jt0_1.setFont(new Font("Consolas", Font.BOLD, 14)); // 文件哈希输出
            jt1.setFont(new Font("微软雅黑", Font.PLAIN, 16)); // 输入框
            jt2.setFont(new Font("微软雅黑", Font.PLAIN, 14)); // 比对框1
            jt3.setFont(new Font("微软雅黑", Font.PLAIN, 14)); // 比对框2
            jt4.setFont(new Font("Consolas", Font.BOLD, 14)); // 哈希输出
            jt5.setFont(new Font("微软雅黑", Font.BOLD, 14));// 日志输出

            JPanel jp0 = new JPanel(); // 文件哈希页
            JPanel jp0_1 = new JPanel(); // 文件嵌套哈希页

            jt0.setPreferredSize(new Dimension(500, 200)); // 文件哈希文本框大小控制
            jt0.setMaximumSize(new Dimension(500, 300));
            jt0.setMinimumSize(new Dimension(500, 200));
            jt0.setMargin(new Insets(70, 0, 0, 0));
            jt0.insertComponent(emoji2);
            try
            {
                StyledDocument doc = jt0.getStyledDocument();
                doc.insertString(doc.getLength(), "\n拖动文件至此", null);
                SimpleAttributeSet center = new SimpleAttributeSet();
                StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
                doc.setParagraphAttributes(0, 1, center, false);
                doc.setParagraphAttributes(doc.getLength() - 1, 1, center, false);
            } 
            catch (Exception e)
            {
                e.printStackTrace();
            }

            jt0.setDropTarget(new DropTarget() {
                public synchronized void drop(DropTargetDropEvent evt) {
                    try {
                        evt.acceptDrop(DnDConstants.ACTION_COPY);
                        @SuppressWarnings("unchecked")
                        List<File> droppedFiles = (List<File>) evt.getTransferable()
                                .getTransferData(DataFlavor.javaFileListFlavor);
                        if (!droppedFiles.isEmpty()) {
                            showFileInfo(droppedFiles.get(0));
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });

            jt0_1.setPreferredSize(new Dimension(100, 50)); // 文字哈希计算文本框大小控制
            jt0_1.setMaximumSize(new Dimension(500, 50));
            jt0_1.setMinimumSize(new Dimension(50, 50));
            

            jp0.setLayout(new BoxLayout(jp0, BoxLayout.Y_AXIS));
            jp0_1.setLayout(new BoxLayout(jp0_1, BoxLayout.X_AXIS));

            comboBox1.setAlignmentX(CENTER_ALIGNMENT);
            Filechoose.setAlignmentX(CENTER_ALIGNMENT); // 文件选择按钮水平居中

            Filechoose.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    FileDialog fileDialog = new FileDialog(MyWindow.this, "选择文件", FileDialog.LOAD);
                    fileDialog.setDirectory(System.getProperty("user.home"));
                    fileDialog.setVisible(true);
                    String filename = fileDialog.getFile();
                    if (filename != null)
                    {
                        File file = new File(fileDialog.getDirectory(), filename);
                        showFileInfo(file);
                    }
                }
            });


            Calculation0.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    // 检查是否已选择文件
                    if (currentSelectedFile == null)
                    {
                        jt0_1.setText("⚠️ 请先点击「导入文件」选择文件！");
                        return;
                    }

                    if (!currentSelectedFile.exists())
                    {
                        jt0_1.setText("⚠️ 文件不存在，请重新选择！");
                        currentSelectedFile = null;
                        return;
                    }

                    // 获取当前时间
                    Time time = new Time();

                    // 获取用户选择的算法
                    String algorithm = (String) comboBox2.getSelectedItem();

                    // 计算哈希
                    FileHash filehash = new FileHash();
                    String hash = filehash.gethash(comboBox2, currentSelectedFile);

                    // 显示哈希结果
                    jt0_1.setText(hash);

                    // ⭐ 日志输出：记录哈希计算结果
                    String logMsg = String.format("[%s] 用户计算了文件 \"%s\" 的 %s 值，结果是 %s\n", time.gettime(),
                            currentSelectedFile.getName(), algorithm, hash);
                    jt5.append(logMsg);
                    jt5.setCaretPosition(jt5.getDocument().getLength());
                }
            });

            jp0.add(Box.createVerticalStrut(60)); // 空白元素
            jp0.add(comboBox2); // 引入组件和排序 
            jp0.add(Box.createVerticalStrut(30));
            jp0.add(jt0);
            jp0.add(Box.createVerticalStrut(10)); // 空白元素
            jp0_1.add(Filechoose);
            jp0_1.add(Box.createHorizontalStrut(30));
            jp0_1.add(Calculation0);
            jp0.add(jp0_1);
            jp0.add(Box.createVerticalStrut(10)); // 空白元素
            jp0.add(jt0_1);
            jp0.add(Box.createVerticalStrut(100)); // 空白元素
            jp0.setVisible(true); // 文件哈希页暂时隐藏
            tabbedPane.addTab("文件哈希", jp0);

            JPanel jp1 = new JPanel(); // 计算页
            jp1.add(Box.createVerticalStrut(60)); // 空白元素

            jp1.setLayout(new BoxLayout(jp1, BoxLayout.Y_AXIS));

            jt1.setPreferredSize(new Dimension(50, 200)); // 文字哈希计算文本框大小控制
            jt1.setMaximumSize(new Dimension(300, 200));
            jt1.setMinimumSize(new Dimension(200, 200));

            jt4.setPreferredSize(new Dimension(100, 20)); // 文字哈希计算文本框大小控制
            jt4.setMaximumSize(new Dimension(500, 30));
            jt4.setMinimumSize(new Dimension(50, 30));

            jt1.setAlignmentX(CENTER_ALIGNMENT); // 文本框和按钮水平居中
            Calculation1.setAlignmentX(CENTER_ALIGNMENT);

            // 计算按钮事件
            Calculation1.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    Time time = new Time();
                    textHash.text = jt1.getText(); // 文本输入至后端
                    if (comboBox1.getSelectedIndex() == 0)
                    {
                        jt4.setText(textHash.getsha1());
                        LogTurnTxt lt1 = new LogTurnTxt((String) comboBox1.getSelectedItem(), time.gettime(),
                                textHash.text, textHash.getsha1());
                        lt1.logouttextarea(jt5);

                    } else if (comboBox1.getSelectedIndex() == 1)
                    {
                        jt4.setText(textHash.getsha256());
                        LogTurnTxt lt1 = new LogTurnTxt((String) comboBox1.getSelectedItem(), time.gettime(),
                                textHash.text, textHash.getsha256());
                        lt1.logouttextarea(jt5);
                    } else if (comboBox1.getSelectedIndex() == 2)
                    {
                        jt4.setText(textHash.getmd5());
                        LogTurnTxt lt1 = new LogTurnTxt((String) comboBox1.getSelectedItem(), time.gettime(),
                                textHash.text, textHash.getmd5());
                        lt1.logouttextarea(jt5);
                    }
                }
            });

            // 比对按钮事件
            Compare.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    compare.CompareString = jt2.getText();
                    compare.hashnumber = jt3.getText();
                    if (compare.Compare() == true)
                    {
                        emoji1.setText("✅");
                        System.out.println("比对正确");

                    } else
                    {
                        emoji1.setText("❌");
                        System.out.println("比对错误");
                    }
                }
            });

            // 导出日志按钮事件
            LogoutButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    LogOut logoutString = new LogOut(jt5);
                    logoutString.logout();

                }
            });

            jp1.add(comboBox1); // 引入组件和排序
            jp1.add(Box.createVerticalStrut(30));
            jp1.add(jt1);
            jp1.add(Box.createVerticalStrut(20));
            jp1.add(Calculation1);
            jp1.add(Box.createVerticalStrut(20));
            jp1.add(jt4);

            tabbedPane.addTab("文本哈希", jp1);

            JPanel jp2 = new JPanel(); // 比对页
            jp2.add(Box.createVerticalStrut(100));

            jp2.setLayout(new BoxLayout(jp2, BoxLayout.Y_AXIS)); // 哈希比对文本框1
            jt2.setPreferredSize(new Dimension(25, 50));
            jt2.setMaximumSize(new Dimension(300, 50));
            jt2.setMinimumSize(new Dimension(200, 50));

            jt3.setPreferredSize(new Dimension(25, 50)); // 哈希比对文本框2
            jt3.setMaximumSize(new Dimension(300, 50));
            jt3.setMinimumSize(new Dimension(200, 50));

            jt2.setAlignmentX(CENTER_ALIGNMENT);
            Compare.setAlignmentX(CENTER_ALIGNMENT);

            jp2.add(jt2);
            jp2.add(Box.createVerticalStrut(10));
            jp2.add(jt3);
            jp2.add(Box.createVerticalStrut(30));
            jp2.add(Compare);
            jp2.add(emoji1);
            tabbedPane.addTab("Hash对比", jp2);
            jp2.add(Box.createVerticalStrut(30));
            tabbedPane.setSelectedIndex(0);
            getContentPane().add(tabbedPane);

            JPanel jp3 = new JPanel(); // 日志页
            jp3.setLayout(new BoxLayout(jp3, BoxLayout.Y_AXIS));
            JScrollPane jsp = new JScrollPane(jt5);

            jsp.setAlignmentX(Component.CENTER_ALIGNMENT);
            jsp.setPreferredSize(new Dimension(750, 400));
            jsp.setMinimumSize(new Dimension(600, 300));

            jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS); // 设置水平滚动条不需要

            jsp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

            jt5.setAlignmentX(Component.CENTER_ALIGNMENT);
            LogoutButton.setAlignmentX(Component.CENTER_ALIGNMENT);

            jp3.add(jsp);
            jp3.add(Box.createVerticalStrut(100));
            jp3.add(LogoutButton);
            jp3.add(Box.createVerticalStrut(50));
            tabbedPane.addTab("日志", jp3);

        }

        private void showFileInfo(File file) {
            currentSelectedFile = file;
            try {
                StyledDocument doc = jt0.getStyledDocument();
                doc.remove(0, doc.getLength());
                doc.insertString(0, "📁 " + file.getName() + "\n", null);
                doc.insertString(doc.getLength(), "📂 " + file.getAbsolutePath(), null);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            jt0_1.setText("");
            Time time = new Time();
            String logMsg = String.format("[%s] 用户导入了文件: %s\n", time.gettime(),
                    file.getAbsolutePath());
            jt5.append(logMsg);
            jt5.setCaretPosition(jt5.getDocument().getLength());
        }
    }

    // 主函数
    public static void main(String[] args)
    {
        System.setProperty("awt.useSystemAAFontSettings", "on"); // 文字抗锯齿
        System.setProperty("swing.aatext", "true");

        Font font = new Font("微软雅黑", Font.PLAIN, 16); // 字号16
        UIManager.put("Button.font", font);
        UIManager.put("Label.font", font);
        UIManager.put("TextField.font", font);
        UIManager.put("TextArea.font", font);
        UIManager.put("comboBox1.font", font);
        UIManager.put("TabbedPane.font", font);
        UIManager.put("Panel.font", font);
        UIManager.put("Frame.font", font);

        SwingUtilities.invokeLater(() -> // GUI启动
        {
            new MyWindow().setVisible(true);
        });

    }
}
