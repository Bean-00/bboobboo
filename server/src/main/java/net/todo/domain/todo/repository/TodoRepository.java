package net.todo.domain.todo.repository;

import net.todo.domain.todo.dto.Todo;

import java.util.List;

public interface TodoRepository {

    List<Todo.Domain> findAllDomain(Todo.Condition condition);

    Todo.Domain findDomainById(int id);

    void persistDomain(Todo.Domain domain);

    void updateDomain(Todo.Domain domain);

    void deleteDomain(int id);

    List<Todo.Status> findAllStatus();

    void deleteItemByDomainId(int id);

    void persistFiles(List<Todo.File> files);

    List<Todo.File> findAllFiles(int id);
}
