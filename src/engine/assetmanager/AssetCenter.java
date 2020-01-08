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

        // Get the lists of files we'll need
        File[] imageListing = directory.listFiles(new ImageFileFilter());
        File[] mapListing = directory.listFiles(new MapFileFilter());

        if (imageListing == null) {
            /*
             If imageListing is null, there are no matching files in
             the specified directory
             */
            throw new IllegalArgumentException(
                    "No matching files in your Assets directory!"
            );
        }

        // For each file in the list of image files
        for (File imageFile : imageListing) {
            String imageFileName = imageFile.getName();

            System.out.println(imageFileName);

            try {
                File imageMap = null;
                if (mapListing != null) {

                    // mapListing only contains files that end in ".map"
                    for (File mapFile : mapListing) {
                        String mapFileName = mapFile.getName();

                        // Find the index at which the extension begins
                        int imgExtStart = imageFile.getName().lastIndexOf('.');
                        int mapExtStart = mapFileName.lastIndexOf('.');

                        /*
                         Ensure the extensions are at the same index, and
                         then that the names leading up to it are equal
                         */
                        if (imgExtStart == mapExtStart &&
                                mapFileName.substring(0, mapExtStart)
                                .equals(imageFileName.substring(0, imgExtStart))) {
                            imageMap = mapFile;
                        }
                    }
                }

                ArrayList<BufferedImage> newList = new ArrayList<>();

                if (imageMap == null) {
                    /*
                     If the map file does not exist, read the entire image in
                     as one sprite
                     */
                    BufferedImage img = ImageIO.read(imageFile);
                    newList.add(img);
                } else {
                    /*
                    If there is an associated map file, read the contents in groups
                    of 4. The contents represent pixel locations and are given as
                    x, y, width, and height.
                     */
                    Scanner scanner = new Scanner(imageMap);
                    System.out.println(imageMap.getName());
                    int[] subImageBounds = new int[4];

                    BufferedImage newImage = ImageIO.read(imageFile);

                    while (scanner.hasNextInt()) {
                        for (int k = 0; k < 4; ++k) {
                            // Construct the bounds from a group of 4 numbers
                            subImageBounds[k] = scanner.nextInt();
                        }

                        /*
                         Using the coordinates, snip the image file and add the
                         snipping to the array
                         */

                        newList.add(newImage.getSubimage(subImageBounds[0], subImageBounds[1], subImageBounds[2], subImageBounds[3]));
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
