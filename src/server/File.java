package server;

public class File {
    String fileName;
    String filePath;
    int userId;

    public File( int userId, String fileName, String filePath) {
        this.fileName = fileName;
        this.filePath = filePath;
        this.userId = userId;
    }

    public File(String fileName, String filePath) {
        this.fileName = fileName;
        this.filePath = filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
