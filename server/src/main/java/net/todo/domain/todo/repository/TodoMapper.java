package net.todo.domain.todo.repository;

import net.todo.domain.todo.dto.Todo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.context.annotation.Profile;

import java.util.List;

@Profile({ "!jpa" })
@Mapper
public interface TodoMapper extends TodoRepository {}
