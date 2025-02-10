package net.todo.core.file.repository;

import net.todo.core.file.dto.TodoFile;

public interface FileRepository {
    void persistFileInfo(TodoFile.Domain fileInfo);
}
