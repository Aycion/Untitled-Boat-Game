package engine.assetmanager;

import engine.ResourceNotFound;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import javax.imageio.ImageIO;

public class AssetCenter {
    Map<String, ArrayList<BufferedImage>> images = new HashMap<>();
    private static final String[] imageExtensions = new String[]{".jpg", ".png"};
    private static final String mapExtension = ".map";

    String path;

    public AssetCenter(String path) {

        this.path = path == null ? "./Images" : path + "/Images";

        File directory = new File(this.path);

        // Get the lists of files that satisfy the provided filters
        File[] imageListing = directory.listFiles(new ImageFileFilter());
        File[] mapListing = directory.listFiles(new MapFileFilter());

        if (imageListing == null) {
            throw new IllegalArgumentException("Your assets directory is empty!");
        }

        for (File imageFile : imageListing) {
            String imageFileName = imageFile.getName();

            System.out.println(imageFile.getName());

            try {
                File imageMap = null;
                if (mapListing != null) {

                    // mapListing only contains files that end in ".map"
                    for (File mapFile : mapListing) {
                        String mapFileName = mapFile.getName();

                        // Find the index at which the extension begins
                        int extStart = imageFile.getName().lastIndexOf('.');

                        if (mapFileName.length() > extStart && mapFileName.substring(0, extStart).equals(
                                imageFileName.substring(0, extStart))) {
                            imageMap = mapFile;
                        }
                    }
                }

                ArrayList<BufferedImage> newList = new ArrayList<>();

                if (imageMap == null) {
                    BufferedImage img = ImageIO.read(imageFile);
                    newList.add(img);
                } else {
                    Scanner scanner = new Scanner(imageMap);
                    System.out.println(imageMap.getName());
                    int[] aspects = new int[4];

                    while (scanner.hasNextInt()) {
                        for (int k = 0; k < 4; ++k) {
                            aspects[k] = scanner.nextInt();
                        }

                        newList.add(ImageIO.read(imageFile).getSubimage(aspects[0], aspects[1], aspects[2], aspects[3]));
                    }

                    scanner.close();
                }


                this.images.put(imageFileName, newList);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public BufferedImage getImage(String name, int index) throws ResourceNotFound {
        if (!this.images.containsKey(name)) {
            throw new ResourceNotFound(name);
        } else {
            return this.images.get(name).get(index);
        }
    }

    public String getPath() {
        return new File(this.path).getPath();
    }

    public ArrayList<BufferedImage> getImageList(String name) throws ResourceNotFound {
        if (!this.images.containsKey(name)) {
            throw new ResourceNotFound(name);
        } else {
            return this.images.get(name);
        }
    }

    private static class ImageFileFilter implements FileFilter {

        @Override
        public boolean accept(File file) {
            for (String ext : imageExtensions) {
                if (file.getName().endsWith(ext)) {
                    return true;
                }
            }
            return false;
        }
    }

    private static class MapFileFilter implements FileFilter {

        @Override
        public boolean accept(File file) {
            return file.getName().endsWith(mapExtension);
        }
    }
}
