package net.todo.core.file.dto;

import lombok.*;

import java.io.File;
import java.time.LocalDateTime;

public class FileInfo {
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Domain {
        private int fileId;
        private int writerId;
        private String fileName;
        private String sysFileName;
        private String dirPath;
        private long size;
        private String type;
        private LocalDateTime createDt;

        public String getSysFilePath() {
            StringBuilder sb = new StringBuilder();

            sb.append(dirPath);
            sb.append(File.separator);
            sb.append(sysFileName);

            return sb.toString();
        }

        public Response toResponse() {
           return Response.builder()
                   .fileId(fileId)
                   .fileName(fileName)
                   .uniqueFileName(sysFileName)
                   .build();
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private int fileId;
        private String fileName;
        private String uniqueFileName;
    }

}
