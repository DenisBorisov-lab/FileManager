package com.fileManager;

import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Getter
@Setter
public class FileInformation {
    private String fileName;
    private long value;
    private FileType type;
    private LocalDateTime lastModified;

    public FileInformation(Path path) throws IOException {

        try {
            this.fileName = path.getFileName().toString();
            this.value = Files.size(path);
            this.type = Files.isDirectory(path) ? FileType.DIRECTORY : FileType.FILE;
            if (type.equals(FileType.DIRECTORY.getName())){
                this.value = -1;
            }
            this.lastModified = LocalDateTime.ofInstant(Files.getLastModifiedTime(path).toInstant(), ZoneOffset.ofHours(0));
        } catch (IOException e) {
            throw new RuntimeException("Can't construct information about the file");
        }

    }
    public static String getType(String fileName){
        if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
            return fileName.substring(fileName.lastIndexOf(".")+1);
        else return "";
    }
}
