package utils;

import io.qameta.allure.Description;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class FileMan {

    private static Logger logger = LoggerFactory.getLogger(FileMan.class);

    public static void waitForDownloading(String path) {
        Wait.waitItem(() -> FileMan.fileExists(path), "Error! File is not downloaded!", 30);
    }

    public static boolean fileExists(String filePath) {
        logger.info("Check does file exist [" + filePath + "]");
        File file = new File(filePath);
        return file.exists() && !file.isDirectory();
    }

    public static File createFile(String filePath) {
        File file = new File(filePath);
        createDirectory(file.getParentFile().getAbsolutePath());
        if (!fileExists(filePath)) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                logger.error("File [" + filePath + "] is not created! /\n" + e.getMessage());
                throw new AssertionException(e.getMessage());
            }
        }
        return file;
    }

    public static boolean folderContainsFile(String folderPath, String fileNamePart) {
        File folder = new File(folderPath);
        List<File> listOfFiles = Arrays.asList(folder.listFiles());
        for (File file : listOfFiles) {
            if (!file.isDirectory() && file.getName().contains(fileNamePart) && file.canRead() && file.canWrite()) {
                return true;
            }
        }
        return false;
    }


    public static List<String> extractZipContent(String zipFileName) {
        List<String> unzippedFilesNames = new ArrayList<>();
        byte[] buffer = new byte[1024];

        // Creating directory to unpacking
        String dstDirectory = zipFileName.substring(0, zipFileName.lastIndexOf("."));
        File dstDir = new File(dstDirectory);
        if (!dstDir.exists()) {
            dstDir.mkdir();
        }

        try {
            // getting zip content
            ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFileName));
            ZipEntry ze = zis.getNextEntry();
            String nextFileName;
            while (ze != null) {
                nextFileName = ze.getName();
                File nextFile = new File(dstDirectory + File.separator + nextFileName);
                logger.info("Unziping: " + nextFile.getAbsolutePath());
                unzippedFilesNames.add(nextFile.getName());
                // if we unzipping catalogue - we should create it
                if (ze.isDirectory()) {
                    nextFile.mkdir();
                } else {
                    // Creating parrent catalogues
                    new File(nextFile.getParent()).mkdirs();
                    // Writing the file content
                    try (FileOutputStream fos = new FileOutputStream(nextFile)) {
                        int length;
                        while ((length = zis.read(buffer)) > 0) {
                            fos.write(buffer, 0, length);
                        }
                    }
                }
                ze = zis.getNextEntry();
            }
            zis.closeEntry();
            zis.close();
        } catch (FileNotFoundException ex) {
            throw new AssertionException("File not found!");
        } catch (IOException ex) {
            throw new AssertionException("File reading error!");
        }
        return unzippedFilesNames;
    }

    public static boolean isFileDownloaded(String filePath) {
        logger.info("Checking is file downloaded");
        int seconds = 30;
        for (int i = 0; i < seconds; i++) {
            if (FileMan.fileExists(filePath)) {
                if (FileUtils.sizeOf(new File(filePath)) != 0L) {
                    return true;
                }
            }
            TimeMan.sleep(1);
        }
        return false;
    }

    public static boolean isFileDownloaded(String filePath, String fileNameStartsWith) {
        logger.info("Checking is file downloaded");
        int seconds = 60;
        File[] dirContents;
        File dir = new File(filePath);
        for (int i = 0; i < seconds; i++) {
            dirContents = dir.listFiles();
            if (dirContents != null && folderContainsFile(filePath, fileNameStartsWith)) {
                return true;
            }
            TimeMan.sleep(1);
        }
        return false;
    }

    public static List<String> getFilesNamesFromFolder(String pathToFolder) {
        List<String> filesToReturn = new ArrayList<>();
        File[] files = new File(pathToFolder).listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    filesToReturn.add(file.getName());
                }
            }
        } else {
            logger.error(pathToFolder + " folder doesn't contain any files");
        }
        return filesToReturn;
    }

    public static List<String> getRidOfExtensionsInFilesNames(List<String> fileNames) {
        List<String> fileNamesWithoutExt = new ArrayList<>();
        for (String file : fileNames) {
            fileNamesWithoutExt.add(FilenameUtils.removeExtension(file));
        }
        return fileNamesWithoutExt;
    }

//    public static String downloadFile(String fileUrl) {
//        return downloadFile(fileUrl, BuildAgent.getDownloadDirPath());
//    }
//
//    public static String downloadFileToTempDir(String fileUrl) {
//        Path tempDirWithPrefix = null;
//        try {
//            tempDirWithPrefix = Files.createTempDirectory("autotest_");
//        } catch (IOException e) {
//            throw new AssertionException(e.getMessage());
//        }
//
//        return downloadFile(fileUrl, tempDirWithPrefix.toString());
//    }

    public static String downloadFile(String fileUrl, String destinationUrl) {
        URL url = null;
        try {
            url = new URL(fileUrl);
        } catch (MalformedURLException e) {
            throw new AssertionException(e.getMessage());
        }
        String fileName = FilenameUtils.getName(url.getPath());
        String filePath = destinationUrl + "/" + fileName;
        Assert.assertFalse(fileExists(filePath), "File with name [" + fileName + "] exist in [" + filePath + "]");
        try {
            FileUtils.copyURLToFile(url, new File(filePath));
        } catch (IOException e) {
            throw new AssertionException(e.getMessage());
        }
        logger.info("File Path: " + filePath);
        Assert.assertTrue(isFileDownloaded(filePath), "File is not downloaded");
        return filePath;
    }

    public static boolean createDirectory(String path) {
        File folder = new File(path);
        if (!(folder.exists() && folder.isDirectory())) {
            folder.setReadable(true, false);
            folder.setExecutable(true, false);
            folder.setWritable(true, false);
            return folder.mkdirs();
        }
        return true;
    }

    public static void cleanDirectory(String path) {
        if (Files.exists(Paths.get(path))) {
            try {
                FileUtils.cleanDirectory(new File(path));
            } catch (IOException e) {
                logger.error("Cannot clean directory: " + path);
            }
        }
    }

    public static void cleanDirectoryFiles(String path) {
        File dir = new File(path);

        if (!dir.isDirectory()) {
            logger.warn("Not a directory: " + dir);
            return;
        }

        File[] listFiles = dir.listFiles();

        if (listFiles != null) {
            Arrays.stream(listFiles)
                    .filter(File::isFile)
                    .forEach(file -> {
                        logger.info("Delete file:" + file);
                        Assert.assertTrue(file.delete(), "Cannot delete file: " + file);
                    });
        }
    }

    public static String fileSizeByUrl(String fileUrl) {
        try {
            URL url = new URL(fileUrl);
            URLConnection urlConnection = url.openConnection();
            urlConnection.connect();
            int fileSize = urlConnection.getContentLength();
            return String.valueOf(fileSize);
        } catch (IOException e) {
            throw new AssertionException(e.getMessage());
        }
    }

    public static boolean isDirEmpty(final Path directory) throws IOException {
        try (DirectoryStream<Path> dirStream = Files.newDirectoryStream(directory)) {
            return !dirStream.iterator().hasNext();
        }
    }

}
