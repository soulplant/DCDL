package com.dcdl.swingutils;

import java.awt.Image;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;

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
    Image image = new ImageIcon(filename).getImage();
    return cachedImages.put(filename, image);
  }
}
