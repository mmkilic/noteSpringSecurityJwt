package mmk.security.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import mmk.security.app.entity.UserJwt;
import mmk.security.app.service.UserJwtService;

@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	private UserJwtService userService;
	
	
	@GetMapping("/hello")
	public String getHello() {
		return "Hello User";
	}
	@GetMapping
	public List<UserJwt> getUsers() {
		return userService.findAll();
	}
	@GetMapping("/{userId}")
	public UserJwt getUserById(@PathVariable int userId) {
		return userService.findById(userId);
	}
	@PostMapping
	public UserJwt save(@RequestBody UserJwt user) {
		return userService.save(user);
	}
	@DeleteMapping("/{userId}")
	public boolean deleteUser(@PathVariable int userId) {
		userService.delete(userId);
		return true;
	}
}