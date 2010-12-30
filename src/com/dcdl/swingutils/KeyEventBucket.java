package com.dcdl.swingutils;

import java.awt.event.KeyAdapter;
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
  private final List<KeyEvent> pendingRelease = new ArrayList<KeyEvent>();

  public KeyEventBucket(KeyListener keyListener) {
    if (keyListener == null) {
      this.keyListener = new KeyAdapter() {};
    } else {
      this.keyListener = keyListener;
    }
  }

  private void queueEvent(KeyEvent e) {
    if (keyListener == null) {
      return;
    }
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
      for (int i = 0; i < queuedEvents.size(); i++) {
        KeyEvent event = queuedEvents.get(i);

        if (event.getID() == KeyEvent.KEY_RELEASED) {
          // Could be fake - if there are any other events on this key let's kill them all.
          boolean isReleaseFake = false;
          for (int j = i + 1; j < queuedEvents.size(); j++) {
            KeyEvent maybeFake = queuedEvents.get(i);
            if (maybeFake.getKeyCode() == event.getKeyCode()) {
              // Fake!
              System.out.println("FAKE!");
              isReleaseFake = true;
              queuedEvents.remove(j);
              j--;
            }
          }
          if (isReleaseFake) {
            queuedEvents.remove(i);
            i--;
          }
        }
      }




      System.out.println("queuedEvents = " + queuedEvents);
      System.out.println("# = " + queuedEvents.size());
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
