package net.todo.core.file.controller;

import lombok.RequiredArgsConstructor;
import net.todo.core.exception.CustomException;
import net.todo.core.exception.CustomExceptionCode;
import net.todo.core.file.dto.FileInfo;
import net.todo.core.file.service.FileService;
import net.todo.core.security.dto.User;
import net.todo.core.security.service.SecurityService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
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
    public ResponseEntity<List<FileInfo.Response>> uploadFiles(@RequestParam("files") List<MultipartFile> multipartFiles) {
        User.UserAccount loginUser = securityService.getLoginUser().orElseThrow(()-> new CustomException(CustomExceptionCode.USER_UNAUTHORIZED));
        List<FileInfo.Response> fileList = fileService.uploadFiles(uploadedDir, multipartFiles, loginUser);
        return ResponseEntity.ok(fileList);
    }

    @GetMapping("image/{uniqueFileName}")
    public ResponseEntity<Resource> getFile(@PathVariable String uniqueFileName) {
        Resource resource = fileService.getFile(uniqueFileName);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                .contentType(MediaType.IMAGE_JPEG)
                .body(resource);
    }
}
