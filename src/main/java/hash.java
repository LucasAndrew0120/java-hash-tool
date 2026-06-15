package src.main.java;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.awt.Font;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class hash
{

    // 后端计算部分
    public static class TextHash // 文本哈希计算
    {

        String text;
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

    // 前端部分
    public static class MyWindow extends JFrame
    {
        TextHash textHash = new TextHash();
        Compare compare = new Compare();

        JButton Calculation; // 计算按钮
        JButton Compare;
        JTabbedPane tabbedPane = new JTabbedPane();
        

        public MyWindow() {

            setTitle("Hash计算器");
            setBounds(300, 300, 800, 650); // 设置大小位置
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 关闭释放资源
            setLocationRelativeTo(null); // 居中

            JLabel emoji = new JLabel("❓");  // 状态Emoji
            emoji.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
            emoji.setAlignmentX(CENTER_ALIGNMENT);
            emoji.setPreferredSize(new Dimension(50, 40));
            emoji.setMaximumSize(new Dimension(50, 40));

            JComboBox<String> comboBox = new JComboBox<>(); // 增加下拉列表
            comboBox.addItem("SHA-256");
            comboBox.addItem("MD5");

            comboBox.setPreferredSize(new Dimension(200, 25)); // 下拉列表大小控制
            comboBox.setMinimumSize(new Dimension(200, 25));
            comboBox.setMaximumSize(new Dimension(200, 25));
            comboBox.setAlignmentX(CENTER_ALIGNMENT);

            Calculation = new JButton("计算Hash值");
            Compare = new JButton("比对Hash值");
            JTextArea jt1 = new JTextArea(); //文本哈希输入
            jt1.setLineWrap(true); // 自动换行
            jt1.setWrapStyleWord(true);
            
            
            JTextArea jt2 = new JTextArea();  //一号比对框
            jt2.setLineWrap(true);
            jt2.setWrapStyleWord(true);
            JTextArea jt3 = new JTextArea(); //二号比对框
            jt3.setLineWrap(true);
            jt3.setWrapStyleWord(true);
            JTextArea jt4 = new JTextArea();  //用于计算输出
            jt4.setLineWrap(true);
            jt4.setWrapStyleWord(true);
            jt4.setEditable(false); //设置输出框只读
            JTextArea jt5 = new JTextArea(); // 日志区
            jt5.setLineWrap(true);
            jt5.setWrapStyleWord(true);
            jt5.setEditable(false); // 设置输出框只读


            jt1.setFont(new Font("微软雅黑", Font.PLAIN, 16)); // 输入框
            jt2.setFont(new Font("微软雅黑", Font.PLAIN, 14)); // 比对框1
            jt3.setFont(new Font("微软雅黑", Font.PLAIN, 14)); // 比对框2
            jt4.setFont(new Font("Consolas", Font.BOLD, 14)); // 哈希输出
            jt5.setFont(new Font("Consolas", Font.BOLD, 14));// 日志输出

            JPanel jp1 = new JPanel();  //计算页
            jp1.add(Box.createVerticalStrut(60)); // 空白元素

            jp1.setLayout(new BoxLayout(jp1, BoxLayout.Y_AXIS));

            jt1.setPreferredSize(new Dimension(50, 200)); // 文字哈希计算文本框大小控制
            jt1.setMaximumSize(new Dimension(300, 200));
            jt1.setMinimumSize(new Dimension(200, 200));

            jt4.setPreferredSize(new Dimension(100, 20)); // 文字哈希计算文本框大小控制
            jt4.setMaximumSize(new Dimension(500, 30));
            jt4.setMinimumSize(new Dimension(50, 30));

            jt1.setAlignmentX(CENTER_ALIGNMENT); // 文本框和按钮水平居中
            Calculation.setAlignmentX(CENTER_ALIGNMENT);

            


            // 计算按钮事件
            Calculation.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    Time time = new Time();
                    textHash.text = jt1.getText(); // 文本输入至后端
                    if (comboBox.getSelectedIndex() == 0)
                    {
                        jt4.setText(textHash.getsha256());
                        System.out.printf("[%s]用户计算了“%s”的SHA-256值，结果是%s\n",time.gettime(), textHash.text, textHash.getsha256());
                    } 
                    else if (comboBox.getSelectedIndex() == 1)
                    {
                        jt4.setText(textHash.getmd5());
                        System.out.printf("[%s]用户计算了“%s”的MD5值，结果是%s\n",time.gettime(), textHash.text, textHash.getmd5());
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
                    if (compare.Compare()==true)
                    {
                        emoji.setText("✅");
                        System.out.println("比对正确");

                    }
                    else
                    {
                        emoji.setText("❌");
                        System.out.println("比对错误");
                    }
                }
            });

            jp1.add(comboBox);    //引入组件和排序
            jp1.add(Box.createVerticalStrut(30));
            jp1.add(jt1);
            jp1.add(Box.createVerticalStrut(20));
            jp1.add(Calculation);
            jp1.add(Box.createVerticalStrut(20));
            jp1.add(jt4);

            tabbedPane.addTab("Hash计算", jp1);

            JPanel jp2 = new JPanel();   //比对页
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
            jp2.add(emoji);
            tabbedPane.addTab("Hash对比", jp2);
            jp2.add(Box.createVerticalStrut(30));
            tabbedPane.setSelectedIndex(0);
            getContentPane().add(tabbedPane);


            JPanel jp3 = new JPanel(); // 日志页
            jp3.add(Box.createVerticalStrut(100));

            jp3.setLayout(new BoxLayout(jp3, BoxLayout.Y_AXIS));
            jt5.setPreferredSize(new Dimension(100, 200));
            jt5.setMaximumSize(new Dimension(300, 200));
            jt5.setMinimumSize(new Dimension(200, 0));

            jp3.add(jt5);
            tabbedPane.addTab("日志", jp3);

            
        }
    }

    // 主函数
    public static void main(String[] args)
    {
        System.setProperty("awt.useSystemAAFontSettings", "on"); //文字抗锯齿
        System.setProperty("swing.aatext", "true");

        Font font = new Font("微软雅黑", Font.PLAIN, 16); // 字号16
        UIManager.put("Button.font", font);
        UIManager.put("Label.font", font);
        UIManager.put("TextField.font", font);
        UIManager.put("TextArea.font", font);
        UIManager.put("ComboBox.font", font);
        UIManager.put("TabbedPane.font", font);
        UIManager.put("Panel.font", font);
        UIManager.put("Frame.font", font);

        SwingUtilities.invokeLater(() -> // GUI启动
        {
            new MyWindow().setVisible(true);
        });

    }
}
