package net.todo.core.file.service;

import lombok.RequiredArgsConstructor;
import net.todo.core.exception.CustomException;
import net.todo.core.exception.CustomExceptionCode;
import net.todo.core.file.dto.FileInfo;
import net.todo.core.file.repository.FileRepository;
import net.todo.core.security.dto.User;
import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static net.todo.core.exception.CustomExceptionCode.FILE_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final FileRepository fileRepository;

    @Override
    @Transactional
    public List<FileInfo.Response> uploadFiles(String uploadedDir, List<MultipartFile> multipartFiles, User.UserAccount user) {

        List<FileInfo.Response> result = new ArrayList<>();

        File dir = new File(uploadedDir);

        if (!dir.exists()) {
            dir.mkdirs();
        }

        for (MultipartFile multipartFile : multipartFiles) {
            FileInfo.Domain fileInfo = convertIntoDomain(uploadedDir, multipartFile, user.getId());
            File file = new File(dir, fileInfo.getSysFileName());
            try {
                multipartFile.transferTo(file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            fileRepository.persistFileInfo(fileInfo);

            result.add(fileInfo.toResponse());
        }

        return result;
    }

    @Override
    public Resource getFile(String uniqueFileName) {
        try {
            FileInfo.Domain fileInfo = fileRepository.findByUniqueFileName(uniqueFileName);
            Path path = Paths.get(fileInfo.getDirPath()).resolve(uniqueFileName);
            Resource resource = new UrlResource(path.toUri());
            return resource;
        } catch (MalformedURLException e) {
            throw new CustomException(CustomExceptionCode.FILE_NOT_FOUND);
        }
    }

    private FileInfo.Domain convertIntoDomain(String dirPath, MultipartFile multipartFile, int writerId) {
        return FileInfo.Domain.builder()
                .fileName(multipartFile.getOriginalFilename())
                .sysFileName(createSysFileName(multipartFile.getOriginalFilename()))
                .size(multipartFile.getSize())
                .type(multipartFile.getContentType())
                .dirPath(dirPath)
                .writerId(writerId)
                .build();
    }

    private String createSysFileName(String fileName) {
        StringBuilder sb = new StringBuilder();
        sb.append(UUID.randomUUID());
        sb.append(".");
        sb.append(FilenameUtils.getExtension(fileName));

        return sb.toString();
    }
}
