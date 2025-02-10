package net.todo.core.file.controller;

import lombok.RequiredArgsConstructor;
import net.todo.core.exception.CustomException;
import net.todo.core.exception.CustomExceptionCode;
import net.todo.core.file.dto.TodoFile;
import net.todo.core.file.service.FileService;
import net.todo.core.security.dto.User;
import net.todo.core.security.service.SecurityService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileController {

    @Value("${file.uploaded-path}")
    private String uploadedDir;
    private final FileService fileService;
    private final SecurityService securityService;

    @PostMapping
    public ResponseEntity<List<TodoFile.Response>> uploadFiles(@RequestParam("files") List<MultipartFile> multipartFiles) {
        User.UserAccount loginUser = securityService.getLoginUser().orElseThrow(()-> new CustomException(CustomExceptionCode.USER_UNAUTHORIZED));
        List<TodoFile.Response> fileList = fileService.uploadFiles(uploadedDir, multipartFiles, loginUser);
        return ResponseEntity.ok(fileList);
    }
}
