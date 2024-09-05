package com.example.hugbo_team_13;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class SupController {
    
	private static final String template = "Whasssup, %s!?";
	private final AtomicLong counter = new AtomicLong();

	@GetMapping("/sup")
	public Sup greeting(@RequestParam(defaultValue = "boii") String name) {
		return new Sup(counter.incrementAndGet(), String.format(template, name));
	}
}