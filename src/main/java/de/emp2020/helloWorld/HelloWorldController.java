package de.emp2020.helloWorld;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path="/helloworld")
public class HelloWorldController {
	
	@GetMapping("/")
	public String index() {
		return "hello, world\n";
	}
}
