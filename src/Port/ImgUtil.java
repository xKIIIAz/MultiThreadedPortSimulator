package Port;

import java.awt.Image;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class ImgUtil {

    private static HashMap<String, ImageIcon> cache = new HashMap<String, ImageIcon>();

    public static ImageIcon loadImage(String path) {
        ImageIcon image = null;

        if (cache.containsKey(path)) {
            return cache.get(path);
        }

        image = new ImageIcon(path);

        if (!cache.containsKey(path)) {
            cache.put(path, image);
        }
        return image;
    }
}
