package com.dcdl.swingutils;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

public class AppRenderer extends JPanel {
  private static final long serialVersionUID = -153681560652326318L;
  private Image backBuffer;
  private final App app;

  public AppRenderer(App app) {
    this.app = app;
    setPreferredSize(app.getPreferredSize());
    setDoubleBuffered(true);
  }

  public void render() {
    ensureBackBufferExists();
    Graphics backBufferGraphics = backBuffer.getGraphics();
    app.render(backBufferGraphics);

    Graphics g = getGraphics();
    g.drawImage(backBuffer, 0, 0, null);
    g.dispose();
    backBufferGraphics.dispose();
  }

  private void ensureBackBufferExists() {
    if (backBuffer == null) {
      Dimension preferredDimensions = app.getPreferredSize();
      backBuffer = createImage(preferredDimensions.width, preferredDimensions.height);
    }
  }

  public void setSize(Dimension size) {
    if (!hasSize(backBuffer, size)) {
      backBuffer = null; // Back buffer is now the wrong size.
    }
    setPreferredSize(size);
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
