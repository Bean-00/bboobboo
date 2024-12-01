package net.todo.domain.todo.repository;

import net.todo.domain.todo.dto.Todo;

import java.util.List;

public interface TodoRepository {

    List<Todo.Domain> findAllDomain();

    Todo.Domain findDomainById(int id);

    void persistDomain(Todo.Domain domain);

    void updateDomain(Todo.Domain domain);

    void deleteDomain(int id);
}
