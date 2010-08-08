package com.dcdl.swingutils;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * Runs {@link App}s.
 *
 * @author james
 *
 * TODO Record performance statistics here (how many ticks per frame we get, FPS, etc.)
 */
public class AppRunner {
  public static void runApp(final App app, final int fps) {
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        createAndShowGui(app, fps);
      }
    });
  }

  private static void createAndShowGui(final App app, final int fps) {
    final JFrame frame = new JFrame(app.getTitle());
    frame.setResizable(false);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    JPanel jpanel = new JPanel(); // JPanel to render to.
    final AppRenderer appRenderer = new AppRenderer(app, jpanel);
    frame.getContentPane().add(jpanel);
    frame.pack();
    frame.setVisible(true);
    final KeyPressBucket keyPressBucket = new KeyPressBucket(app.getKeyListener());
    frame.addKeyListener(keyPressBucket);

    app.setAppListener(new App.Listener() {
      @Override
      public void triggerResize() {
        appRenderer.setSize(app.getPreferredSize());
        frame.pack();
      }
    });

    new Thread() {
      long lastTickTime = System.currentTimeMillis();
      long millisPerTick = 1000 / fps;
      @Override
      public void run() {
        while (true) {
          long curTime = System.currentTimeMillis();
          while (curTime - lastTickTime > millisPerTick) {
            lastTickTime += millisPerTick;
            keyPressBucket.emptyBucket();
            app.tick();
          }
          appRenderer.render();
        }
      }
    }.start();
  }
}
