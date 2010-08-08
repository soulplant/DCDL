package com.dcdl.swingutils;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyListener;

public interface App {
  public interface Listener {
    void triggerResize();
  }
  void render(Graphics g);
  Dimension getPreferredSize();
  String getTitle();
  KeyListener getKeyListener();
  void tick();
  void setAppListener(Listener listener);
}
