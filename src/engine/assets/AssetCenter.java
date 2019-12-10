//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package engine.assets;

import engine.ResourceNotFound;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;

public class AssetCenter {
    Map<String, ArrayList<BufferedImage>> Images = new HashMap();
    String path;

    public AssetCenter(String path) {

        this.path = path == null ? "./Images" : path + "/Images";

        File file = new File(this.path);
        String[] names = file.list();
        if (names == null) {
            throw new IllegalArgumentException("Your assets directory is empty!");
        }
        for (int i = 0; i < names.length; i++) {
            if (Pattern.matches("[^\"]*?.(jpg|png)", names[i])) {
                System.out.println(names[i]);

                try {
                    int mapAddress = -1;

                    for (int j = 0; j < names.length; ++j) {
                        if (names[j].substring(
                                0, names[j].length() - 4
                        ).equals(
                                names[i].substring(
                                        0, names[i].length() - 4
                                )
                        ) && names[j].substring(
                                names[j].length() - 4
                        ).equals(".map"))
                        {
                            mapAddress = j;
                        }
                    }

                    ArrayList<BufferedImage> newList = new ArrayList<>();
                    if (mapAddress == -1) {
                        BufferedImage img = ImageIO.read(new File(this.path + "/" + names[i]));
                        newList.add(img);
                        this.Images.put(names[i], newList);
                    } else {
                        Scanner scanner = new Scanner(new File(this.path + "/" + names[mapAddress]));
                        System.out.println(names[i]);
                        int[] aspects = new int[4];

                        while (scanner.hasNextInt()) {
                            for (int k = 0; k < 4; ++k) {
                                aspects[k] = scanner.nextInt();
                            }

                            newList.add(ImageIO.read(new File(this.path + "/" + names[i])).getSubimage(aspects[0], aspects[1], aspects[2], aspects[3]));
                        }

                        scanner.close();
                        this.Images.put(names[i], newList);
                    }
                } catch (Exception var10) {
                }
            }
        }

    }

    public Image getImage(String name, int index) throws ResourceNotFound {
        if (!this.Images.containsKey(name)) {
            throw new ResourceNotFound(name);
        } else {
            return (Image) ((ArrayList) this.Images.get(name)).get(index);
        }
    }

    public String getPath() {
        return new File(this.path).getPath();
    }

    public ArrayList<BufferedImage> getImageList(String name) throws ResourceNotFound {
        if (!this.Images.containsKey(name)) {
            throw new ResourceNotFound(name);
        } else {
            return this.Images.get(name);
        }
    }
}
