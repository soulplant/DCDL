package com.dcdl.swingutils;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyListener;

public class TestApp implements App {

  private static final int FPS = 100;

  public static void main(String[] args) {
    AppRunner.runApp(new TestApp(), FPS);
  }

  private int counter;
  private long startTime = System.currentTimeMillis();
  private final ImageStore imageStore = new ImageStore();

  @Override
  public KeyListener getKeyListener() {
    return new KeyAdapter() {};
  }

  private final Dimension preferredSize = new Dimension(320, 240);

  @Override
  public Dimension getPreferredSize() {
    return preferredSize;
  }

  @Override
  public String getTitle() {
    return "Blah";
  }

  @Override
  public void render(Graphics g) {
    g.setColor(Color.BLUE);
    g.clearRect(0, 0, 320, 240);
    g.drawString("frame: " + counter, 50, 50);
    g.drawImage(imageStore.getImage("gfx/blue_block.png"), 0, 0, null);
  }

  @Override
  public void tick() {
    if (counter == 0) {
      System.out.println("Got through " + FPS + " frames in: "
          + (System.currentTimeMillis() - startTime));
      startTime = System.currentTimeMillis();
    }
    counter += 1;
    counter %= FPS;
  }

  @Override
  public void setAppListener(Listener listener) {
  }
}
