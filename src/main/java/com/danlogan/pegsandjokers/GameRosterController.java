package com.danlogan.pegsandjokers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class GameRosterController {
	
	@GetMapping("/roster")
	@ResponseBody
	public ResponseEntity<String> roster()
	{
				
		return new ResponseEntity<String>("Hello Roster", HttpStatus.OK);
	}
	

}
