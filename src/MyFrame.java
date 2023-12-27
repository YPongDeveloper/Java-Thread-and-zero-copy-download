import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.table.DefaultTableModel;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.List;
public class MyFrame extends JFrame   {
    private static final String SERVER_IP = "127.0.0.1";
    private static final int SERVER_PORT = 12345;
    private DefaultTableModel tableModel;
    private static JLabel userLabel;
    private static JLabel passLabel;
    private static ImageIcon gifIcon2 ;  // เปลี่ยนเป็นพาธของไฟล์ GIF
    private static JLabel gifLabel2 ;
    private static JTextField userTextField;
    private static JPasswordField passwordText;
    private static JButton jButton;
    private static JButton closeButton;
    private JTable table;
    private JPanel cardPanel;
    private CardLayout cardLayout;
    String filePathDownload="";
    JProgressBar progressBar = new JProgressBar(0, 100);
    String dir = "/home/";
    boolean option;
    long timeMulti;
    long timeZero;
    public String ip="127.0.0.1";
    private DefaultTableModel model;
    double nt;
    int count=0;
    private String serverName="pongsakorn";
    double present =0;
    void updateTimeMulti(long time,int row){

        if (row != -1) {
            tableModel.setValueAt( time/1000.0 +" `วินาที",row, 3);
        }
    }
    void updateZero(long time,int row){

        if (row != -1) {
            tableModel.setValueAt( time/1000.0+" วินาที",row, 4);
        }
    }
    void setProgessBar(long start,int current,boolean mode,int row){
        progressBar.setValue(current);

        if(current==100&&mode){
            long end = System.currentTimeMillis();
            long time = end-start;
            timeMulti = time;
            showSS(time,mode,row);

            count=0;
        }
    }
    void showSS(long time,boolean mode,int Row){
        if(!mode){
            timeZero = time;
            updateZero(time,Row);
        }else{
            updateTimeMulti(time,Row);
        }
        String message="Download Successful!! "+time/1000.0+" Second";
        JOptionPane.showMessageDialog(null, message);
    }

    void countSS(long start,int row)throws IOException {
        count++;

        present = (count/nt)*100;
        int ps = (int) present;
        setProgessBar(start,ps,true,row);

    }
    String fileSave(){
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY);
        int fileChooserResult = fileChooser.showOpenDialog(null);
        String filePath = "";

        if (fileChooserResult == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            filePath = selectedFile.getAbsolutePath();
        }
        return filePath;
    }
    void setNt(int num){
        nt = num;
    }
    void setDir(String dir){
        this.dir = dir;
    }
    void setFilePath(String filename){
        filePathDownload = filename;
    }
    void popupConnectFall(){
        JOptionPane.showMessageDialog(null, "Connect Server Fall");
    }
    private static void openFile(File file) {
        Desktop desktop = Desktop.getDesktop();
        try {
            desktop.open(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public MyFrame () {
        setTitle("Client Request");
        setSize(580, 410);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JLabel label = new JLabel("Choose option :");

        tableModel = new DefaultTableModel();
        tableModel.addColumn("Name");
        tableModel.addColumn("File Type");
        tableModel.addColumn("File Size");
        tableModel.addColumn("Time Multi-Thread");
        tableModel.addColumn("Time zero");

        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);


        JButton buttonConnect = new JButton("Connect");
        JButton openFileDownload = new JButton("Open File");

        buttonConnect.setBounds(50,100,95,30);

        progressBar.setStringPainted(true);

        JButton DownloadZeroButton = new JButton("Multi-Thread");
        DownloadZeroButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // โหลดเเบบ zero copy
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {

                    String nameVideo = (String)tableModel.getValueAt( selectedRow, 0);
                    String filePath = fileSave();
                    System.out.println(selectedRow);
                    connectServerT conT = new connectServerT(ip, nameVideo, filePath, serverName,dir,false,selectedRow);

                    setFilePath(filePath+"/"+nameVideo);
                    conT.start();
                }else{

                    JOptionPane.showMessageDialog(null, "Please SelectRow!!");
                }
            }
        });
        JButton DownloadMultiButton = new JButton("Zero Copy");
        DownloadMultiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // โหลดเเบบ multithread

                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {

                    String nameVideo = (String)tableModel.getValueAt( selectedRow, 0);
                    String filePath = fileSave();
                    System.out.println(selectedRow);
                    connectServerT conT = new connectServerT(ip, nameVideo, filePath, serverName,dir,true,selectedRow);

                    setFilePath(filePath+"/"+nameVideo);
                    conT.start();
                }else{

                    JOptionPane.showMessageDialog(null, "Please SelectRow!!");
                }
            }
        });
        JPanel buttonPanel = new JPanel();

        buttonPanel.add(DownloadZeroButton);
        buttonPanel.add(DownloadMultiButton);


        openFileDownload.addActionListener((ActionEvent e) -> {
            String f = filePathDownload;
            if(!f.equalsIgnoreCase("")){
                File filePath = new File(f);
                openFile(filePath);
            }else{

                JOptionPane.showMessageDialog(null, "You haven't downloaded yet.");
            }
        });

        JButton openDialogButton = new JButton("Exit");
        openDialogButton.setBounds(50,100,95,30);
        openDialogButton.addActionListener(e -> {
            int choice = showCustomDialog();
            if (choice == JOptionPane.YES_OPTION) {
                System.exit(0);
            } else if (choice == JOptionPane.NO_OPTION) {
                System.out.println("You clicked No");
            }
        });
        cardPanel = new JPanel();
        cardLayout = new CardLayout();
        cardPanel.setLayout(cardLayout);

        // สร้างหน้าจอที่ 1
        JPanel panel1 = new JPanel();
        userLabel = new JLabel("Username");
        passLabel = new JLabel("Password");
        userTextField = new JTextField(20);
        passwordText = new JPasswordField();
        jButton = new JButton("Login");
        gifIcon2 = new ImageIcon("/home/pongsakorn/Pictures/image.jpg");  // เปลี่ยนเป็นพาธของไฟล์ GIF
        gifLabel2 = new JLabel(gifIcon2);

//        panel1.setSize(800, 800);
        panel1.setLayout(null);
        panel1.add(new JLabel("เข้าสู่ระบบ"));

        panel1.add(label);
        userLabel.setBounds(90,150,120,25);
        userLabel.setForeground(Color.MAGENTA);
        Font font = new Font("Arial", Font.BOLD, 20);
        userLabel.setFont(font);
        //Add the label to the panel
        panel1.add(userLabel);

        //Adding text field
        userTextField.setBounds(250,150,165,25);
        panel1.add(userTextField);

        //Set where the label should appear in the panel
        passLabel.setBounds(90,180,120,25);
        passLabel.setForeground(Color.MAGENTA);
        passLabel.setFont(font);
        panel1.add(passLabel);

        passwordText.setBounds(250,180,165,25);
        panel1.add(passwordText);
        jButton.setBounds(130,230,120,25);

        panel1.add(jButton);

        jButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                        String userName = userTextField.getText();
        //Deprecated method is used
        String password = passwordText.getText();


        if(userName.equals("admin") && password.equals("admin")) {

            // เปลี่ยนไปยังหน้าจอที่ 2
            cardLayout.next(cardPanel);

        }
        else if(userName.equals("640710056") && password.equals("640710056")) {

            // เปลี่ยนไปยังหน้าจอที่ 2
            cardLayout.next(cardPanel);

        }else if(userName.equals("640710051") && password.equals("640710051")) {

            // เปลี่ยนไปยังหน้าจอที่ 2
            cardLayout.next(cardPanel);

        }else if(userName.equals("640710052") && password.equals("640710052")) {

            // เปลี่ยนไปยังหน้าจอที่ 2
            cardLayout.next(cardPanel);

        }else{
            JOptionPane.showMessageDialog(null, "Invalid Username Or Password!");
        }
            }
        });
        // สร้างหน้าจอที่ 2
        JPanel panel2 = new JPanel();
        closeButton = new JButton("Cancel");

        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // ปิดหน้าจอหลัก
                dispose();
            }
        });
        closeButton.setBounds(270,230,120,25);
        panel1.add(closeButton);
        gifLabel2.setBounds(0,0,580,382);
        panel1.add(gifLabel2);
        panel2.add(label);

//        panel2.add(buttonConnect);

        panel2.add(buttonPanel, BorderLayout.SOUTH);
        panel2.add(openFileDownload);
//        add(gifLabel);
        panel2.add(openDialogButton);
        panel2.add(progressBar);

        // เพิ่ม JScrollPane ลงใน Frame
        panel2.add(scrollPane, BorderLayout.CENTER);

        // แสดง Frame


        // เพิ่มหน้าจอใน CardLayout
        cardPanel.add(panel1, "Page 1");
        cardPanel.add(panel2, "Page 2");

       getContentPane().add(cardPanel);
        connectToServer();

    }
    private void connectToServer() {
        try {
            Socket socket = new Socket(SERVER_IP, SERVER_PORT);
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());

            // Receive the list of files from the server
            List<FileInfo> fileInfos = (List<FileInfo>) inputStream.readObject();

            // Display the files in the table
            for (FileInfo fileInfo : fileInfos) {
                tableModel.addRow(new Object[]{fileInfo.getFileName(), fileInfo.getFileType(), fileInfo.getFileSize()});
            }

            // Close the socket
            socket.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    private static int showCustomDialog() {
        String[] options = {"Yes", "No"};
        return JOptionPane.showOptionDialog(
                null,
                "Do you want to exit the application?",
                "Exit",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );
    }

}
class connectServerT extends Thread{
    public String ip="";
    private String serverName="";
    private String videoName="";
    private String fileSave = "";
    private String dir="";
    private boolean option;
    private int row;
     connectServerT(String ip , String videoName,String fileSave,String serverName,String dir,boolean option,int row){
        this.ip = ip;
        this.videoName = videoName;
        this.fileSave = fileSave;
        this.serverName = serverName;
        this.dir = dir;
        this.option = option;
        this.row = row;
    }

    @Override
    public void run() {
        Client cl = new Client(ip,videoName ,fileSave,serverName,dir,option,row);
        cl.connectToServer();


    }
}
