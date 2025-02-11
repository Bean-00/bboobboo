package net.todo.core.file.repository;

import net.todo.core.file.dto.FileInfo;

public interface FileRepository {
    void persistFileInfo(FileInfo.Domain fileInfo);

    FileInfo.Domain findByUniqueFileName(String uniqueFileName);
}
