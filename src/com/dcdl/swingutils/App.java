package com.dcdl.swingutils;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public interface App {
  public interface Listener {
    void triggerResize();
  }
  void render(Graphics g);
  Dimension getPreferredSize();
  String getTitle();
  KeyListener getKeyListener();
  MouseListener getMouseListener();
  MouseMotionListener getMouseMotionListener();
  void tick();
  void setAppListener(Listener listener);
}
