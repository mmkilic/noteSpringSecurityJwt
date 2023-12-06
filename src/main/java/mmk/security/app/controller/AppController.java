package mmk.security.app.controller;

import java.security.Principal;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/app")
public class AppController {
	
	@GetMapping("/hello")
	public String getHello() {
		return "Hello App";
	}
	@GetMapping("/welcome")
    public String welcome() {
        return "Hello World! this is FOLSDEV";
    }

    @GetMapping
    public String getAdminString(Authentication authentication, Principal principal) {
    	String response = String.format("authentication Name: %s \n"
    			+ "Principal Name: %s", authentication.getName(), principal.getName());
    	System.out.println(response);
        return response;
    }
}
