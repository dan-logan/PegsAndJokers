package com.danlogan.pegsandjokers;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Controller;

import com.danlogan.pegsandjokers.domain.PlayerTurn;

import lombok.extern.java.Log;

@Log
@Controller
public class AIController {
	
	@EventListener
	public void onTurnTakenEvent(TurnTakenEvent turnTakenEvent)
	{
		log.info("TurnTaken event being handled by AIController: " + turnTakenEvent.toString());
		
		//wait 5 seconds to simulate computer think time
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	

}
