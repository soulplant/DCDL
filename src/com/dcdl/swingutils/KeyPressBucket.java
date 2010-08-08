package com.dcdl.swingutils;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Accumulates key events and sends them synchronously in bulk when the
 * {@link #emptyBucket()} method is called. This is used to control when key
 * events fire so they don't happen in the middle of a game's logic tick.
 *
 * @author james
 */
public class KeyPressBucket implements KeyListener {
  private enum Type {
    PRESS, RELEASE, TYPE
  }
  private static class StoredKeyEvent {
    private final Type type;
    private final KeyEvent event;

    public StoredKeyEvent(Type type, KeyEvent event) {
      this.type = type;
      this.event = event;
    }

    public void sendToKeyListener(KeyListener keyListener) {
      switch (type) {
      case PRESS:
        keyListener.keyPressed(event);
        break;
      case RELEASE:
        keyListener.keyReleased(event);
        break;
      case TYPE:
        keyListener.keyTyped(event);
        break;
      }
    }
  }

  private final KeyListener keyListener;
  private final List<StoredKeyEvent> keyPressedEvents =
    Collections.synchronizedList(new ArrayList<StoredKeyEvent>());

  public KeyPressBucket(KeyListener keyListener) {
    this.keyListener = keyListener;
  }

  @Override
  public void keyPressed(KeyEvent e) {
    keyPressedEvents.add(new StoredKeyEvent(Type.PRESS, e));
  }

  @Override
  public void keyReleased(KeyEvent e) {
    keyPressedEvents.add(new StoredKeyEvent(Type.RELEASE, e));
  }

  @Override
  public void keyTyped(KeyEvent e) {
    keyPressedEvents.add(new StoredKeyEvent(Type.TYPE, e));
  }

  public void emptyBucket() {
    synchronized (keyPressedEvents) {
      for (StoredKeyEvent keyPressedEvent : keyPressedEvents) {
        keyPressedEvent.sendToKeyListener(keyListener);
      }
      keyPressedEvents.clear();
    }
  }
}
