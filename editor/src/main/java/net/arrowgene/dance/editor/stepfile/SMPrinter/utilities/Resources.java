package net.arrowgene.dance.editor.stepfile.SMPrinter.utilities;

import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import net.arrowgene.dance.editor.stepfile.SMPrinter.models.stepmetadata.NotesType;

/**
 * A utility class that manages resource providers. Requests loads and initializations
 * from resource providers, and also returns the propeer resource provider given a game mode.
 *
 * @author Dan
 */

public class Resources {

    private DanceResourceProvider danceResourceProvider;
    private PumpResourceProvider pumpResourceProvider;

    public String notesDir = "notes/stepmania5/";

    public boolean stepAssetsLoaded = false;

    public Font pageHeader = new Font("Arial", Font.PLAIN, 14);
    public Font measureNumber = new Font("sans-serif", Font.BOLD, 20);

    private Resources() {
        danceResourceProvider = new DanceResourceProvider(this);
        pumpResourceProvider = new PumpResourceProvider(this);
    }

    public ResourceProvider getProvider(NotesType notesType) {
        switch (notesType.getGameMode()) {
            case DANCE:
                return danceResourceProvider;
            case PUMP:
                return pumpResourceProvider;
        }
        return null;
    }

    public static BufferedImage loadImage(String path) {
        return loadImage(new File(path));
    }

    public static BufferedImage loadImage(File file) {
        if (!file.exists()) {
            return null;
        }

        try {
            return ImageIO.read(file);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static BufferedImage[][] getImageSubImages(BufferedImage img,
                                                      int subImgWidth, int subImgHeight) {
        int horizontalSubImages = img.getWidth() / subImgWidth;
        int verticalSubImages = img.getHeight() / subImgHeight;
        BufferedImage[][] subImages = new BufferedImage[verticalSubImages][horizontalSubImages];

        for (int i = 0; i < verticalSubImages; i++) {
            for (int j = 0; j < horizontalSubImages; j++) {
                subImages[i][j] = img.getSubimage(j * subImgWidth, i * subImgHeight, subImgWidth, subImgHeight);
            }
        }
        return subImages;
    }

    public static BufferedImage[] getImageSubImages(BufferedImage img, int subImgHeight) {
        int verticalSubImages = img.getHeight() / subImgHeight;
        BufferedImage[] subImages = new BufferedImage[verticalSubImages];

        for (int i = 0; i < verticalSubImages; i++) {
            subImages[i] = img.getSubimage(0, i * subImgHeight, img.getWidth(), subImgHeight);
        }
        return subImages;
    }

    public void loadStepAssetsIfNotLoaded() {
        danceResourceProvider.loadImagesIfNotLoaded();
        pumpResourceProvider.loadImagesIfNotLoaded();
    }

    //singleton pattern declarations
    private static class ResourcesHolder {
        public static final Resources INSTANCE = new Resources();
    }

    public static Resources getInstance() {
        return ResourcesHolder.INSTANCE;
    }
}
