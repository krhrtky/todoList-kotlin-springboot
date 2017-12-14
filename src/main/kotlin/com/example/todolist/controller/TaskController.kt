package com.example.todolist.controller

import com.example.todolist.form.TaskCreateForm
import com.example.todolist.form.TaskUpdateForm
import com.example.todolist.repository.TaskRepository
import javassist.NotFoundException
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@Controller
@RequestMapping("tasks")
class  TaskController(private val taskRepository: TaskRepository) {

    @ExceptionHandler(NotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleNotFoundException(): String = "tasks/not_found"

    @GetMapping("")
    fun index(model: Model): String {

        model.addAttribute("tasks", taskRepository.findAll())

        return "tasks/index"
    }

    @GetMapping("new")
    fun new(form: TaskCreateForm): String {
        return "tasks/new"
    }

    @PostMapping("")
    fun create(@Validated form: TaskCreateForm,
               bindingResult: BindingResult): String {

        if (bindingResult.hasErrors())
            return "tasks/new"

        val content = requireNotNull(form.content)
        taskRepository.create(content)
        return "redirect:/tasks"

    }

    @GetMapping("{id}/edit")
    fun edit(@PathVariable("id") id: Long,
             form: TaskUpdateForm): String {
        val task = taskRepository.findById(id) ?: throw  NotFoundException("task not found.")

        form.content = task.content
        form.done = task.done
        return "tasks/edit"
    }

    @PatchMapping("{id}")
    fun update(@PathVariable("id") id: Long,
               @Validated form: TaskUpdateForm,
               bindingResult: BindingResult): String {

        if (bindingResult.hasErrors())
            return "tasks/edit"

        val task = taskRepository.findById(id) ?: throw  NotFoundException("task not found.")
        val newTask = task.copy(
                content = requireNotNull(form.content),
                done = form.done
        )
        taskRepository.update(newTask)
        return "redirect:/tasks"
    }

}