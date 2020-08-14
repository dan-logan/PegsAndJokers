package com.danlogan.pegsandjokers.ai;

import com.danlogan.pegsandjokers.domain.Card;
import com.danlogan.pegsandjokers.domain.PlayerTurn;

public class MoveOption {

	private String resultingBoardPositionID;
	private PlayerTurn playerTurn;
	
	public MoveOption(PlayerTurn turn, String resultingBoardPositionID) {
		this.playerTurn = turn;
		this.resultingBoardPositionID = resultingBoardPositionID;
	}

	public String getResultingBoardPositionID() {
		return this.resultingBoardPositionID;
	}

	public String getSuggestedCard() {
		return this.playerTurn.getCardName();
	}

	public PlayerTurn getPlayerTurn() {
		return this.playerTurn;
	}

}
