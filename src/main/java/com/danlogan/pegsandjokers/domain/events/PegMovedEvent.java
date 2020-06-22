package com.danlogan.pegsandjokers.domain.events;

import java.util.UUID;

import com.danlogan.pegsandjokers.domain.Color;

public class PegMovedEvent extends GameEvent {
	
	private int pegNumber;
	private Color pegColor;
	private String fromPositionId;
	private String toPositionId;

	public PegMovedEvent(UUID gameId, long currentTimeMillis, int number, Color color, String fromPPBPId, String toPositionId) {

		super(gameId, currentTimeMillis);
		this.pegNumber = number;
		this.pegColor = color;
		this.fromPositionId = fromPPBPId;
		this.toPositionId = toPositionId;
	}

	@Override
	public String toString() {
		return "PegMovedEvent [pegColor=" + pegColor + ", pegNumber=" + pegNumber + ", fromPositionId=" + fromPositionId
				+ ", toPositionId=" + toPositionId + "]";
	}

	public int getPegNumber() {
		return pegNumber;
	}

	public Color getPegColor() {
		return pegColor;
	}

	public String getFromPositionId() {
		return fromPositionId;
	}

	public String getToPositionId() {
		return toPositionId;
	}

}
