package com.dcdl.swingutils;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Accumulates key events and sends them synchronously in bulk when the
 * {@link #emptyBucket()} method is called. This is used to control when key
 * events fire so they don't happen in the middle of a game's logic tick.
 *
 * @author james
 */
public class KeyEventBucket implements KeyListener {
  /**
   * On Linux, X generates fake KeyReleased events to implement auto-repeat.
   * When we see a KeyReleased it is converted into a PotentialFake which will
   * be confirmed as a fake if a KeyPressed event is seen during the
   * PotentialFake's lifetime. The lifetime is measured in frames (and we are
   * assuming no human is dexterous enough to release and press a key in 1/60th
   * of a second).
   */
  private class PotentialFake {
    private final KeyEvent released;
    private KeyEvent pressed;
    private int lifetime;

    public PotentialFake(KeyEvent released, int lifetime) {
      this.released = released;
      this.lifetime = lifetime;
    }

    public void age() {
      lifetime--;
    }

    public boolean isDead() {
      return lifetime <= 0 || this.pressed != null;
    }

    public boolean isFake() {
      return this.pressed != null;
    }

    public KeyEvent getReleased() {
      return released;
    }

    public void addFakeEvent(KeyEvent event) {
      if (event.getID() == KeyEvent.KEY_PRESSED) {
        this.pressed = event;
      } else {
        throw new IllegalStateException("Unexpected type of KeyEvent: " + event);
      }
    }

    public int getKeyCode() {
      return released.getKeyCode();
    }
  }
  private final KeyListener keyListener;
  private final List<KeyEvent> queuedEvents = new ArrayList<KeyEvent>();
  private final Map<Integer, PotentialFake> potentialFakes = new HashMap<Integer, PotentialFake>();

  public KeyEventBucket(KeyListener keyListener) {
    if (keyListener == null) {
      this.keyListener = new KeyAdapter() {};
    } else {
      this.keyListener = keyListener;
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
//      System.out.println("queuedEvents = " + queuedEvents);
//      System.out.println("# = " + queuedEvents.size());
      for (KeyEvent event : queuedEvents) {
        switch (event.getID()) {
        case KeyEvent.KEY_RELEASED:
          addPotentialFake(event);
          break;
        case KeyEvent.KEY_PRESSED:
          if (!isFake(event)) {
            keyListener.keyPressed(event);
          }
          break;
        case KeyEvent.KEY_TYPED:
          // We don't care about fake KEY_TYPED events.
          keyListener.keyTyped(event);
          break;
        }
      }

      List<Integer> toRemove = new ArrayList<Integer>();
      for (PotentialFake potentialFake : potentialFakes.values()) {
        if (potentialFake.isDead()) {
          if (!potentialFake.isFake()) {
            keyListener.keyReleased(potentialFake.getReleased());
          }
          toRemove.add(potentialFake.getKeyCode());
        }
        potentialFake.age();
      }

      for (Integer i : toRemove) {
        potentialFakes.remove(i);
      }
      queuedEvents.clear();
    }
  }

  private boolean isFake(KeyEvent event) {
    if (potentialFakes.containsKey(event.getKeyCode())) {
      potentialFakes.get(event.getKeyCode()).addFakeEvent(event);
      return true;
    }
    return false;
  }

  private void addPotentialFake(KeyEvent event) {
    potentialFakes.put(event.getKeyCode(), new PotentialFake(event, 1));
  }

  private void queueEvent(KeyEvent e) {
    if (keyListener == null) {
      return;
    }
    synchronized (queuedEvents) {
      queuedEvents.add(e);
    }
  }
}
