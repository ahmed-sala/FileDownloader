package com.filedownloader.filedownloader;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import javax.swing.*;

public class FileDownloaderGUI extends javax.swing.JFrame {
    private final BlockingQueue<Downloader> sequentialQueue = new LinkedBlockingQueue<>();
    private final Thread sequentialWorker;

    /**
     * Creates new form FileDownloaderGUI
     */
    public FileDownloaderGUI() {
        initComponents();
        sequentialWorker = new Thread(() -> {
            while (true) {
                try {
                    Downloader downloader = sequentialQueue.take();
                    Thread thread = new Thread(downloader);
                    thread.start();
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        sequentialWorker.start();
        this.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
    }

    private void addDownload(String url, boolean isSequential) {
        JPanel downloadPanel = new JPanel();
        JLabel fileLabel = new JLabel("Downloading: " + extractFileName(url));
        JProgressBar progressBar = new JProgressBar();
        progressBar.setStringPainted(true);
        JButton pauseButton = new JButton("Pause");
        JButton cancelButton = new JButton("Cancel");
        JLabel downloadMethod = new JLabel("Download Method: " + (isSequential ? "Sequential" : "Parallel"));

        downloadPanel.add(fileLabel);
        downloadPanel.add(progressBar);
        downloadPanel.add(pauseButton);
        downloadPanel.add(cancelButton);
        downloadPanel.add(downloadMethod);

        SwingUtilities.invokeLater(() -> {
            downloadsPanel.add(downloadPanel);
            downloadsPanel.revalidate();
            downloadsPanel.repaint();
        });

        Downloader downloader = new Downloader(url, progressBar, fileLabel);

        pauseButton.addActionListener(e -> {
            if (pauseButton.getText().equals("Pause")) {
                downloader.pause();
                pauseButton.setText("Resume");
            } else {
                downloader.resume();
                pauseButton.setText("Pause");
            }
        });

        cancelButton.addActionListener(e -> {
            downloader.cancel();
            SwingUtilities.invokeLater(() -> {
                downloadsPanel.remove(downloadPanel);
                downloadsPanel.revalidate();
                downloadsPanel.repaint();
            });
        });

        if (isSequential) {
            startSequentialDownload(downloader);
        } else {
            startParallelDownload(downloader);
        }
    }
    
        private void startSequentialDownload(Downloader downloader) {
        // Add the downloader to the queue, the worker thread will handle it
        sequentialQueue.add(downloader);
    }

//    private void startSequentialDownload(Downloader downloader) {
//        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
//            @Override
//            protected Void doInBackground() throws Exception {
//                // Start the download task in the background thread
//                Thread thread = new Thread(downloader);
//                thread.start();
//                thread.join(); // Wait for the download to finish in the background thread
//                return null;
//            }
//        };
//        // Execute the SwingWorker to run the download task in the background
//        worker.execute();
//    }

    private void startParallelDownload(Downloader downloader) {
        Thread thread = new Thread(downloader);
        thread.start();
    }

    private String extractFileName(String url) {
        String[] parts = url.split("/");
        return parts[parts.length - 1].isEmpty() ? "downloaded_file" : parts[parts.length - 1];
    }

    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        jButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        downloadsPanel = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("URL");

        buttonGroup1.add(jRadioButton1);
        jRadioButton1.setText("Sequential");

        buttonGroup1.add(jRadioButton2);
        jRadioButton2.setText("Parallel");

        jButton1.setText("Download");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout downloadsPanelLayout = new javax.swing.GroupLayout(downloadsPanel);
        downloadsPanel.setLayout(downloadsPanelLayout);
        downloadsPanelLayout.setHorizontalGroup(
            downloadsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 726, Short.MAX_VALUE)
        );
        downloadsPanelLayout.setVerticalGroup(
            downloadsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 376, Short.MAX_VALUE)
        );

        downloadsPanel.setLayout(new BoxLayout(downloadsPanel, BoxLayout.Y_AXIS));

        jScrollPane1.setViewportView(downloadsPanel);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 388, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jRadioButton1)
                        .addGap(31, 31, 31)
                        .addComponent(jRadioButton2)
                        .addGap(18, 18, 18)
                        .addComponent(jButton1)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jRadioButton2)
                    .addComponent(jRadioButton1)
                    .addComponent(jButton1))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 378, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        String url = jTextField1.getText();
        if (url.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a valid URL", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!jRadioButton1.isSelected() && !jRadioButton2.isSelected()) {
            JOptionPane.showMessageDialog(this, "Please select a download method", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        addDownload(url, jRadioButton1.isSelected());
        jTextField1.setText("");
    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FileDownloaderGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FileDownloaderGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FileDownloaderGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FileDownloaderGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FileDownloaderGUI().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JPanel downloadsPanel;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}
