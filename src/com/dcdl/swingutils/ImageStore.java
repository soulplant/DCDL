package com.dcdl.swingutils;

import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Loads images from the file system and caches them.
 *
 * @author james
 */
public class ImageStore {
  private final Map<String, Image> cachedImages = new HashMap<String, Image>();

  public Image getImage(String filename) {
    if (cachedImages.containsKey(filename)) {
      return cachedImages.get(filename);
    }
    if (!new File(filename).canRead()) {
      throw new IllegalArgumentException("Can't read from file '" + filename + "'");
    }
    Image image = Toolkit.getDefaultToolkit().getImage(filename);
    return cachedImages.put(filename, image);
  }
}
