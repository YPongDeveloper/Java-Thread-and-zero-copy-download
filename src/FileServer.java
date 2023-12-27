import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class FileServer extends Thread {
    public  void run() {
        int port = 12345;

        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Server started. Waiting for clients...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress());

                // Start a new thread to handle the client
                new Thread(new ClientHandler(clientSocket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class ClientHandler implements Runnable {
    private Socket clientSocket;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try {
            // Get the output stream of the client socket
            ObjectOutputStream outputStream = new ObjectOutputStream(clientSocket.getOutputStream());

            // Get the list of files in the specified directory
            File directory = new File("/home/pongsakorn/OSVideo/");
            File[] files = directory.listFiles();

            // Send the list of files to the client
            List<FileInfo> fileInfos = new ArrayList<>();
            assert files != null;
            for (File file : files) {
                fileInfos.add(new FileInfo(file.getName(), getFileType(file), file.length()));
            }
            outputStream.writeObject(fileInfos);

            // Close the client socket
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getFileType(File file) {
        if (file.isDirectory()) {
            return "Directory";
        } else {
            return "File";
        }
    }
}

class FileInfo implements Serializable {
    private String fileName;
    private String fileType;
    private long fileSize;

    public FileInfo(String fileName, String fileType, long fileSize) {
        this.fileName = fileName;
        this.fileType = fileType;
        this.fileSize = fileSize;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public long getFileSize() {
        return fileSize;
    }
}
