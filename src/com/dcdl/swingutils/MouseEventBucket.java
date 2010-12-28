package com.dcdl.swingutils;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;

public class MouseEventBucket implements MouseListener, MouseMotionListener {
  private final List<MouseEvent> events = new ArrayList<MouseEvent>();
  private final MouseListener listener;
  private final MouseMotionListener motionListener;

  public MouseEventBucket(MouseListener listener, MouseMotionListener motionListener) {
    this.listener = listener;
    this.motionListener = motionListener;
  }

  private void queueEvent(MouseEvent e) {
    synchronized (events) {
      events.add(e);
    }
  }

  @Override
  public void mouseClicked(MouseEvent e) {
    queueEvent(e);
  }

  @Override
  public void mouseEntered(MouseEvent e) {
    queueEvent(e);
  }

  @Override
  public void mouseExited(MouseEvent e) {
    queueEvent(e);
  }

  @Override
  public void mousePressed(MouseEvent e) {
    queueEvent(e);
  }

  @Override
  public void mouseReleased(MouseEvent e) {
    queueEvent(e);
  }


  // Motion events.
  @Override
  public void mouseDragged(MouseEvent e) {
    queueEvent(e);
  }

  @Override
  public void mouseMoved(MouseEvent e) {
    queueEvent(e);
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
        // Motion events.
        case MouseEvent.MOUSE_DRAGGED:
          motionListener.mouseDragged(event);
          break;
        case MouseEvent.MOUSE_MOVED:
          motionListener.mouseMoved(event);
          break;
        }
      }
      events.clear();
    }
  }
}
