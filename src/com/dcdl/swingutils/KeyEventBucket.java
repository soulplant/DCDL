package com.dcdl.swingutils;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Accumulates key events and sends them synchronously in bulk when the
 * {@link #emptyBucket()} method is called. This is used to control when key
 * events fire so they don't happen in the middle of a game's logic tick.
 *
 * @author james
 */
public class KeyEventBucket implements KeyListener {
  private final KeyListener keyListener;
  private final List<KeyEvent> queuedEvents = new ArrayList<KeyEvent>();

  public KeyEventBucket(KeyListener keyListener) {
    this.keyListener = keyListener;
  }

  private void queueEvent(KeyEvent e) {
    synchronized (queuedEvents) {
      queuedEvents.add(e);
    }
  }

  @Override
  public void keyPressed(KeyEvent e) {
    queueEvent(e);
  }

  @Override
  public void keyReleased(KeyEvent e) {
    queueEvent(e);
  }

  @Override
  public void keyTyped(KeyEvent e) {
    queueEvent(e);
  }

  public void emptyBucket() {
    synchronized (queuedEvents) {
      for (KeyEvent event : queuedEvents) {
        switch (event.getID()) {
        case KeyEvent.KEY_PRESSED:
          keyListener.keyPressed(event);
          break;
        case KeyEvent.KEY_RELEASED:
          keyListener.keyReleased(event);
          break;
        case KeyEvent.KEY_TYPED:
          keyListener.keyTyped(event);
          break;
        }
      }
      queuedEvents.clear();
    }
  }
}
