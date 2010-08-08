package com.dcdl.swingutils;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

public class AppRenderer {
  private static final long serialVersionUID = -153681560652326318L;
  private Image backBuffer;
  private final App app;
  private final JPanel jpanel;

  public AppRenderer(App app, JPanel jpanel) {
    this.app = app;
    this.jpanel = jpanel;
    jpanel.setPreferredSize(app.getPreferredSize());
    jpanel.setDoubleBuffered(true);
  }

  public void render() {
    ensureBackBufferExists();
    Graphics backBufferGraphics = backBuffer.getGraphics();
    app.render(backBufferGraphics);

    Graphics g = jpanel.getGraphics();
    g.drawImage(backBuffer, 0, 0, null);
    g.dispose();
    backBufferGraphics.dispose();
  }

  public void setSize(Dimension size) {
    if (!hasSize(backBuffer, size)) {
      backBuffer = null; // Back buffer is now the wrong size.
    }
    jpanel.setPreferredSize(size);
  }

  private void ensureBackBufferExists() {
    if (backBuffer == null) {
      Dimension preferredDimensions = app.getPreferredSize();
      backBuffer = jpanel.createImage(preferredDimensions.width, preferredDimensions.height);
    }
  }

  /**
   * @param image to check the size of.
   * @param size to compare image size against.
   * @returns {@code true} if the image has the given size.
   */
  private boolean hasSize(Image image, Dimension size) {
    return image.getWidth(null) == size.getWidth() && image.getHeight(null) == size.getHeight();
  }
}
