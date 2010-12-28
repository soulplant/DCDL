package com.dcdl.swingutils;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.LinkedList;
import java.util.List;

public class MouseMoveBucket implements MouseListener {
  private final List<MouseEvent> events = new LinkedList<MouseEvent>();
  private final MouseListener listener;

  public MouseMoveBucket(MouseListener listener) {
    this.listener = listener;
  }

  @Override
  public void mouseClicked(MouseEvent e) {
    events.add(e);
  }

  @Override
  public void mouseEntered(MouseEvent e) {
    events.add(e);
  }

  @Override
  public void mouseExited(MouseEvent e) {
    events.add(e);
  }

  @Override
  public void mousePressed(MouseEvent e) {
    events.add(e);
  }

  @Override
  public void mouseReleased(MouseEvent e) {
    events.add(e);
  }

  public void emptyBucket() {
    synchronized (events) {
      for (MouseEvent event : events) {
        switch (event.getID()) {
        case MouseEvent.MOUSE_CLICKED:
          listener.mouseClicked(event);
          break;
        case MouseEvent.MOUSE_ENTERED:
          listener.mouseEntered(event);
          break;
        case MouseEvent.MOUSE_EXITED:
          listener.mouseExited(event);
          break;
        case MouseEvent.MOUSE_PRESSED:
          listener.mousePressed(event);
          break;
        case MouseEvent.MOUSE_RELEASED:
          listener.mouseReleased(event);
          break;
        }
      }
      events.clear();
    }
  }

}
