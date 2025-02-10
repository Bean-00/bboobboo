package net.todo.core.file.service;

import net.todo.core.file.dto.TodoFile;
import net.todo.core.security.dto.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {
    List<TodoFile.Response> uploadFiles(String uploadedDir, List<MultipartFile> multipartFiles, User.UserAccount user);
}
