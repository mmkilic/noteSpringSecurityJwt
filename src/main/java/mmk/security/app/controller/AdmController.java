package mmk.security.app.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/adm")
public class AdmController {
	
	@GetMapping("/hello")
	public String getHello() {
		return "Hello Adm";
	}
}
