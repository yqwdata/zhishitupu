package com.example.demo.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.Result;
import com.example.demo.entity.Todo;
import com.example.demo.entity.User;
import com.example.demo.mapper.TodoMapper;
import com.example.demo.mapper.UserMapper;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/todo")
public class TodoController {

    @Resource
    TodoMapper todoMapper;

    @Resource
    UserMapper userMapper;

    @PostMapping
    public Result<?> save(@RequestBody Todo todo) {
        todoMapper.insert(todo);
        return Result.success();
    }

    @PutMapping
    public Result<?> update(@RequestBody Todo todo) {
        todoMapper.updateById(todo);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Integer id) {
        // 删除待办事件
        todoMapper.deleteById(id);
        return Result.success();
    }

    @DeleteMapping("/user/{userId}")
    public Result<?> deleteByUserId(@PathVariable Long userId) {
        // 删除该用户的所有待办事件
        LambdaQueryWrapper<Todo> wrapper = Wrappers.<Todo>lambdaQuery().eq(Todo::getUserId, userId);
        todoMapper.delete(wrapper);
        return Result.success();
    }

    @GetMapping
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer PageNum,
                              @RequestParam(defaultValue = "10") Integer PageSize,
                              @RequestParam(defaultValue = "") String search) {
        LambdaQueryWrapper<Todo> wrapper = Wrappers.<Todo>lambdaQuery();
        if (search != null && !search.isEmpty()) {
            wrapper.like(Todo::getTask, search);
        }
        Page<Todo> todoPage = todoMapper.selectPage(new Page<>(PageNum, PageSize), wrapper);
        todoPage.getRecords().forEach(todo -> {
            User user = userMapper.selectById(todo.getUserId());
            todo.setUser(user);
        });
        return Result.success(todoPage);
    }

    @GetMapping("/user/{userId}")
    public Result<?> findByUserId(@PathVariable Integer userId) {
        LambdaQueryWrapper<Todo> wrapper = Wrappers.<Todo>lambdaQuery().eq(Todo::getUserId, userId);
        return Result.success(todoMapper.selectList(wrapper));
    }
}