package com.danlogan.pegsandjokers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.servlet.http.HttpServletResponse;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.danlogan.pegsandjokers.domain.Roster;
import com.danlogan.pegsandjokers.domain.events.RosterEvent;

import lombok.extern.java.Log;

@Log
@Controller
public class SSEController {

  private final CopyOnWriteArrayList<SseEmitter> emitters = new CopyOnWriteArrayList<>();

  @GetMapping("/roster/{id}/events/subscribe")
  public SseEmitter handle(@PathVariable String id, HttpServletResponse response) {
	log.info("Received rosters subscription request for game id: " + id);
    response.setHeader("Cache-Control", "no-store");

    //SseEmitter emitter = new SseEmitter();
    //SseEmitter emitter = new SseEmitter(180_000L);
    SseEmitter emitter = new RosterEventSseEmitter(Long.MAX_VALUE, id);

    this.emitters.add(emitter);

    emitter.onCompletion(() -> this.emitters.remove(emitter));
    emitter.onTimeout(() -> this.emitters.remove(emitter));

    return emitter;
  }

  @EventListener
  public void onRoster(RosterEvent rosterEvent) {
	log.info("Received roster event: " + rosterEvent.toString());
    List<SseEmitter> deadEmitters = new ArrayList<>();
    this.emitters.stream().filter(e -> e.getClass() == RosterEventSseEmitter.class && ((RosterEventSseEmitter) e).getGameId().equals(rosterEvent.getGameId())).forEach(emitter -> {
      try {
        emitter.send(rosterEvent);

        // close connnection, browser automatically reconnects
        // emitter.complete();

        // SseEventBuilder builder = SseEmitter.event().name("second").data("1");
        // SseEventBuilder builder =
        // SseEmitter.event().reconnectTime(10_000L).data(memoryInfo).id("1");
        // emitter.send(builder);
      }
      catch (Exception e) {
        deadEmitters.add(emitter);
      }
    });

    this.emitters.removeAll(deadEmitters);
  }
}