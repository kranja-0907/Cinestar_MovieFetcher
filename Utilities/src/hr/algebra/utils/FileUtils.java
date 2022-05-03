/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.utils;

import hr.algebra.factory.UrlConnectionFactory;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Optional;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import javax.xml.bind.JAXBException;

/**
 *
 * @author dnlbe
 */
public class FileUtils {

    private static final String UPLOAD = "Upload";
    private static final String SAVE = "Save";
    private static final String TEXT_DOCUMENTS = "Text documents (*.txt)";
    private static final String TXT = "txt";

    private static final String XML_DOCUMENTS = "*.xml";
    private static final String XML = "xml";

    public static Optional<File> uploadOptionalFile(String description, String... extensions) {
        JFileChooser chooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        chooser.setFileFilter(new FileNameExtensionFilter(description, extensions));
        chooser.setDialogTitle(UPLOAD);
        chooser.setApproveButtonText(UPLOAD);
        chooser.setApproveButtonToolTipText(UPLOAD);
        chooser.setAcceptAllFileFilterUsed(false);
        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            File selectedFile = chooser.getSelectedFile();
            String extension = selectedFile.getName().substring(selectedFile.getName().lastIndexOf(".") + 1);
            return Arrays.asList(extensions).contains(extension.toLowerCase()) ? Optional.of(selectedFile) : Optional.empty();
        }
        return Optional.empty();
    }

    public static File uploadFile(String description, String... extensions) {
        JFileChooser chooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        chooser.setFileFilter(new FileNameExtensionFilter(description, extensions));
        chooser.setDialogTitle(UPLOAD);
        chooser.setApproveButtonText(UPLOAD);
        chooser.setApproveButtonToolTipText(UPLOAD);
        chooser.setAcceptAllFileFilterUsed(false);
        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            File selectedFile = chooser.getSelectedFile();
            String extension = selectedFile.getName().substring(selectedFile.getName().lastIndexOf(".") + 1);
            return Arrays.asList(extensions).contains(extension.toLowerCase()) ? selectedFile : null;
        }
        return null;
    }

    public static void copyFromUrl(String source, String destination) throws MalformedURLException, IOException {
        //sa neta kod nas spremi
        createDirHierarchy(destination);
        HttpURLConnection con = UrlConnectionFactory.getHttpUrlConnection(source);
        //URL url = new URL(source);
        try (InputStream in = con.getInputStream()) {
            Files.copy(in, Paths.get(destination));
        }
    }

    public static void copy(String source, String destination) throws FileNotFoundException, IOException {
        createDirHierarchy(destination);
        Files.copy(Paths.get(source), Paths.get(destination));
    }

    private static void createDirHierarchy(String destination) throws IOException {
        String dir = destination.substring(0, destination.lastIndexOf(File.separator));
        if (!Files.exists(Paths.get(dir))) {
            Files.createDirectories(Paths.get(dir));
        }
    }

    public static boolean filenameHasExtension(String filename, int length) {
        return /*!filename.isEmpty() && */ filename.contains(".") && filename.substring(filename.lastIndexOf(".") + 1).length() == length;
    }

    //save i saveas
    public static Optional<File> saveTextInFile(String text, Optional<File> optFile) throws IOException {
        if (!optFile.isPresent()) {
            //save as
            JFileChooser chooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
            chooser.setFileFilter(new FileNameExtensionFilter(TEXT_DOCUMENTS, TXT));
            chooser.setDialogTitle(SAVE);
            chooser.setApproveButtonText(SAVE);
            chooser.setApproveButtonToolTipText(SAVE);
            if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                File selectedFile = chooser.getSelectedFile();
                if (!selectedFile.toString().endsWith(TXT)) {
                    selectedFile = new File(selectedFile.toString().concat(".").concat(TXT));
                }
                Files.write(selectedFile.toPath(), text.getBytes());
                optFile = Optional.of(selectedFile);
            }
        } else {
            Files.write(optFile.get().toPath(), text.getBytes());
        }
        return optFile;
    }

    public static Optional<String> loadTextFromFile() throws IOException {
        JFileChooser chooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        chooser.setFileFilter(new FileFilter() {
            //sta sve taj filechoser pokazuje
            @Override
            public boolean accept(File file) {
                return file.isDirectory() || file.toString().endsWith(TXT);
            }

            //ono dole pored file name: kakve exktenzije vuce
            @Override
            public String getDescription() {
                return TEXT_DOCUMENTS;
            }
        });
        //ak je reko taj file dovuci njegov text
        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            return Optional.of(new String(Files.readAllBytes(chooser.getSelectedFile().toPath())));
        }
        //odusto
        return Optional.empty();
    }

    public static void saveToXml(Object object) throws JAXBException {
        JFileChooser chooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        chooser.setFileFilter(new FileNameExtensionFilter(XML_DOCUMENTS, XML));
        chooser.setDialogTitle(SAVE);
        chooser.setApproveButtonText(SAVE);
        chooser.setApproveButtonToolTipText(SAVE);
        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            if (!file.toString().endsWith(XML)) {
                file = new File(file.toString().concat(".").concat(XML));
            }
            JAXBUtils.save(object, file.toPath().toString());
        }
    }

}
