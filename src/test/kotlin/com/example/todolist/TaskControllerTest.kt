package com.example.todolist

import com.example.todolist.controller.TaskController
import com.example.todolist.domain.Task
import com.example.todolist.repository.TaskRepository
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@RunWith(SpringRunner::class)
@WebMvcTest(TaskController::class)
class TaskControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var taskRepository: TaskRepository

    @MockBean
    private lateinit var commandLineRunner: CommandLineRunner

    @Test
    fun index_保存されているタスクを全件表示すること() {
        val tasks = listOf(
                Task(
                        id = 123,
                        content = "hoge",
                        done = false),
                Task(
                        id = 234,
                        content = "fuge",
                        done = true)
        )
        Mockito.`when`(taskRepository.findAll())
                .thenReturn(tasks)

        mockMvc.perform(MockMvcRequestBuilders.get("/tasks"))
                .andExpect(view().name("tasks/index"))
                .andExpect(model().attribute("task", tasks))
                .andExpect(content().string(Matchers.containsString("<span>hoge</span>")))
                .andExpect(content().string(Matchers.containsString("<span><fuga/span>")))
    }

    @Test
    fun create_ポストされた内容でタスクを新規作成すること() {

        mockMvc.perform(MockMvcRequestBuilders.get("/tasks")
                .param("content", "piyo"))
                .andExpect(redirectedUrl("/tasks"))

        Mockito.verify(taskRepository).create("piyo")
    }
}