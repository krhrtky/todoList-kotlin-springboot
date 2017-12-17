package com.example.todolist.repository.impl

import com.example.todolist.domain.Task
import com.example.todolist.repository.TaskRepository
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Repository

@Repository
class JdbcTaskRepository(private val jdbcTemplate: JdbcTemplate) : TaskRepository {

    private val rowMapper = RowMapper<Task> { rs, _ ->
        Task(
                rs.getLong("id"),
                rs.getString("content"),
                rs.getBoolean("done")
        )
    }

    override fun create(content: String): Task {
        jdbcTemplate.update("INSERT INTO task(content) VALUES(?)", content)
        return Task(
                jdbcTemplate.queryForObject("SELECT last_insert_id()", Long::class.java),
                content,
                false
        )
    }

    override fun update(task: Task) {
        jdbcTemplate.update("UPDATE task SET content = ?, done = ? WHERE id = ?",
                task.content,
                task.done,
                task.id)
    }

    override fun findAll(): List<Task> =
            jdbcTemplate.query("SELECT id, content, done FROM task", rowMapper)

    override fun findById(id: Long): Task? =
            jdbcTemplate.query("SELECT id, content, done FROM task WHERE id = ?",
                    rowMapper,
                    id)
                    .firstOrNull()

}
