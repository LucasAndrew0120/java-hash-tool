package src.main.java;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class hash
{

    // 后端计算部分
    public static class TextHash // 文本哈希计算
    {
        Scanner scanner = new Scanner(System.in);
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
                text = scanner.nextLine();
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
                text = scanner.nextLine();
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
    public static class Comepare
    {
        Scanner scanner = new Scanner(System.in);

        boolean comepare(String hashnumber)
        {
            String comepareString = scanner.nextLine();

            if (comepareString == hashnumber)
                return true;
            else
                return false;
        }
    }

    // 前端部分
    public static class MyWindow extends JFrame
    {
        JButton Calculation; // 计算按钮
        JButton Comepare;
        JTabbedPane tabbedPane = new JTabbedPane();

        public MyWindow() {
            setTitle("Hash计算器");
            setBounds(300, 300, 500, 500); // 设置大小位置
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 关闭释放资源
            setLocationRelativeTo(null); // 居中

            Calculation = new JButton("计算Hash值");
            Comepare = new JButton("比对Hash值");
            JTextField jt1 = new JTextField(20);
            JTextField jt2 = new JTextField(10);

            JPanel jp1 = new JPanel();
            jp1.add(jt1);
            jp1.add(Calculation);
            tabbedPane.addTab("Hash计算", jp1);


            JPanel jp2 = new JPanel();
            jp2.add(jt2);
            jp2.add(Comepare);
            tabbedPane.addTab("Hash对比", jp2);

            tabbedPane.setSelectedIndex(0);

            getContentPane().add(tabbedPane);
        }
    }

    // 主函数
    public static void main(String[] args)
    {

        SwingUtilities.invokeLater(() ->
        {
            new MyWindow().setVisible(true);
        });
    }
}
