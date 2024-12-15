package com.filedownloader.filedownloader;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

public class Downloader implements Runnable {

    final String url;
    final JProgressBar progressBar;
    final JLabel fileLabel;
    private volatile boolean paused = false;
    private volatile boolean canceled = false;

    public Downloader(String url, JProgressBar progressBar, JLabel fileLabel) {
        this.url = url;
        this.progressBar = progressBar;
        this.fileLabel = fileLabel;
    }

    @Override
    public void run() {
        long startTime = System.currentTimeMillis();

        try {
            Files.createDirectories(Paths.get("downloads"));
            String fileName = extractFileName(url);

            URL fileUrl = new URL(url);
            try (InputStream inputStream = fileUrl.openStream(); FileOutputStream outputStream = new FileOutputStream("downloads/" + fileName)) {

                int fileSize = fileUrl.openConnection().getContentLength();
                if (fileSize <= 0) {
                    throw new Exception("Unable to determine file size.");
                }
                progressBar.setMaximum(fileSize);

                byte[] buffer = new byte[1024];
                int bytesRead;
                int totalBytesRead = 0;

                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    synchronized (this) {
                        if (canceled) {
                            return;
                        }
                        while (paused) {
                            wait();  
                            if(canceled){
                                return;
                            }
                        }
                    }

                    outputStream.write(buffer, 0, bytesRead);
                    totalBytesRead += bytesRead;
                    final int progress = totalBytesRead;

                    SwingUtilities.invokeLater(() -> progressBar.setValue(progress));
                }

                SwingUtilities.invokeLater(() -> fileLabel.setText("Download complete: " + fileName));
            }
        } catch (Exception e) {
            SwingUtilities.invokeLater(() -> fileLabel.setText("Error: " + e.getMessage()));
        } finally {
            String duration = String.format("%.2f", (System.currentTimeMillis() - startTime) / 1000.0);
            System.out.println("Download took: " + duration + " seconds");
        }
    }


    private String extractFileName(String url) {
        String[] parts = url.split("/");
        return parts[parts.length - 1].isEmpty() ? "file" : parts[parts.length - 1];
    }

    
    public synchronized void pause() {
        paused = true;
    }

    public synchronized void resume() {
        paused = false;
        notify();  
    }

    public synchronized void cancel() {
        canceled = true;
        notify();  
    }
}
