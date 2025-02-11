package net.todo.core.file.service;

import net.todo.core.file.dto.FileInfo;
import net.todo.core.security.dto.User;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {
    List<FileInfo.Response> uploadFiles(String uploadedDir, List<MultipartFile> multipartFiles, User.UserAccount user);

    Resource getFile(String uniqueFileName);
}
