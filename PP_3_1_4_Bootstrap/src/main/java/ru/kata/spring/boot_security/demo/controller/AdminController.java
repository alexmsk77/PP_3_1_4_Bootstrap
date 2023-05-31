package ru.kata.spring.boot_security.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;
import ru.kata.spring.boot_security.demo.validator.UserValidator;



@Controller
@RequestMapping("/admin")
public class AdminController {


    private final UserService userService;
    private final RoleService roleService;
     private final UserValidator userValidator;

    public AdminController(UserService userService, RoleService roleService, UserValidator userValidator) {
        this.userService = userService;
        this.roleService = roleService;
        this.userValidator = userValidator;
    }

    @GetMapping(value = "/")
    public String showAllUsers(ModelMap modelUser) {
        modelUser.addAttribute("userList", userService.getUsers());
        return "admin";
    }
    @GetMapping(value = "/addNewUser")
    public String addNewUser(ModelMap modelUser) {
        User addUser = new User();
        modelUser.addAttribute("userList", addUser);
        modelUser.addAttribute("roleList", roleService.getAllRole());
        return "addUser";
    }
    @PostMapping(value = "/saveUser")
    public String saveUser(@ModelAttribute("userList") @Validated User userSave,
                           BindingResult bindingResult,
                           @ModelAttribute("roleList") Role roleSave, ModelMap modelUser) {
        userValidator.validate(userSave, bindingResult);

        if (bindingResult.hasErrors()) {
            modelUser.addAttribute("roleList", roleService.getAllRole());
            modelUser.addAttribute("userList", userSave);
            return "addUser";
        }
          userSave.addRolesUsers(roleSave);
          userService.saveUser(userSave);
          return "redirect:/admin/";
    }

    @DeleteMapping(value = "/{id}/deleteUser")
    public String deleteUser(@PathVariable("id") Long idDelete) {
        userService.deleteUser(idDelete);
        return "redirect:/admin/";
    }
    @GetMapping(value = "/{id}/editUser")
    public String editUser(Model modelUser, @PathVariable("id") Long idEdit) {
        modelUser.addAttribute("userList", userService.findUser(idEdit));
        modelUser.addAttribute("roleList", roleService.getAllRole());
        return "editUser";
    }
    @PatchMapping(value = "/{id}/updateUser")
    public String updateUser(@ModelAttribute("userList") User userUpdate) {
        userService.updateUser(userUpdate);
        return "redirect:/admin/";
    }

}
