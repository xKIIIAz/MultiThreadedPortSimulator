package Port;

import javax.swing.*;
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
