package in.rajendrapatil.ghibliapi.util;

import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Utility class to resize and compress images to match Stability AI API requirements.
 * Stability AI SDXL v1.0 supports the following dimensions:
 * 1024x1024, 1152x896, 1216x832, 1344x768, 1536x640, 640x1536, 768x1344, 832x1216, 896x1152
 */
public class ImageResizingUtil {

    // Valid dimensions for Stability AI SDXL v1.0
    private static final List<Dimension> VALID_DIMENSIONS = Arrays.asList(
            new Dimension(1024, 1024),
            new Dimension(1152, 896),
            new Dimension(1216, 832),
            new Dimension(1344, 768),
            new Dimension(1536, 640),
            new Dimension(640, 1536),
            new Dimension(768, 1344),
            new Dimension(832, 1216),
            new Dimension(896, 1152)
    );

    /**
     * Finds the best matching dimension for the given width and height.
     * Uses aspect ratio matching to determine the closest valid dimension.
     */
    public static Dimension findBestMatchingDimension(int width, int height) {
        double targetAspectRatio = (double) width / height;

        Dimension bestMatch = VALID_DIMENSIONS.get(0);
        double minAspectDifference = Math.abs((double) bestMatch.width / bestMatch.height - targetAspectRatio);

        for (Dimension dim : VALID_DIMENSIONS) {
            double dimAspectRatio = (double) dim.width / dim.height;
            double aspectDifference = Math.abs(dimAspectRatio - targetAspectRatio);

            if (aspectDifference < minAspectDifference) {
                minAspectDifference = aspectDifference;
                bestMatch = dim;
            }
        }

        return bestMatch;
    }

    /**
     * Resizes a MultipartFile image to a valid Stability AI dimension AND compresses it.
     * Always compresses the image regardless of dimensions to ensure file size stays under 5MB limit.
     */
    public static MultipartFile resizeImageIfNeeded(MultipartFile originalFile) throws IOException {
        // Read the original image
        BufferedImage originalImage = ImageIO.read(originalFile.getInputStream());

        if (originalImage == null) {
            throw new IOException("Unable to read image file");
        }

        int originalWidth = originalImage.getWidth();
        int originalHeight = originalImage.getHeight();
        
        BufferedImage processedImage;

        // Check if image is already a valid dimension
        if (isValidDimension(originalWidth, originalHeight)) {
            // Already valid dimensions, but still need to compress
            processedImage = originalImage;
        } else {
            // Find the best matching dimension
            Dimension targetDimension = findBestMatchingDimension(originalWidth, originalHeight);
            // Resize the image
            processedImage = resizeImage(originalImage, targetDimension.width, targetDimension.height);
        }

        // Always compress the image to ensure file size is under 5MB limit
        return convertBufferedImageToMultipartFile(processedImage, originalFile.getOriginalFilename());
    }

    /**
     * Checks if a dimension is valid for Stability AI API.
     */
    public static boolean isValidDimension(int width, int height) {
        for (Dimension dim : VALID_DIMENSIONS) {
            if (dim.width == width && dim.height == height) {
                return true;
            }
        }
        return false;
    }

    /**
     * Resizes an image to the target width and height while preserving aspect ratio.
     * If aspect ratio doesn't match, pads the image with a background color.
     */
    private static BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
        int originalWidth = originalImage.getWidth();
        int originalHeight = originalImage.getHeight();
        double originalAspectRatio = (double) originalWidth / originalHeight;
        double targetAspectRatio = (double) targetWidth / targetHeight;

        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = resizedImage.createGraphics();

        // Fill background with white
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, targetWidth, targetHeight);

        // Calculate dimensions to preserve aspect ratio
        int newWidth, newHeight, xOffset, yOffset;

        if (originalAspectRatio > targetAspectRatio) {
            // Original is wider than target
            newWidth = targetWidth;
            newHeight = (int) (targetWidth / originalAspectRatio);
            xOffset = 0;
            yOffset = (targetHeight - newHeight) / 2;
        } else {
            // Original is taller than target
            newHeight = targetHeight;
            newWidth = (int) (targetHeight * originalAspectRatio);
            xOffset = (targetWidth - newWidth) / 2;
            yOffset = 0;
        }

        // Draw the resized image
        Image scaledImage = originalImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
        g2d.drawImage(scaledImage, xOffset, yOffset, null);
        g2d.dispose();

        return resizedImage;
    }

    /**
     * Converts a BufferedImage to a MultipartFile-like object with aggressive JPEG compression to reduce file size.
     * JPEG compression ensures the file size stays within Stability AI's 5MB limit.
     */
    private static MultipartFile convertBufferedImageToMultipartFile(BufferedImage image, String filename) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        
        // Use JPEG compression with quality setting to reduce file size
        // Quality 0.70 provides aggressive compression while maintaining acceptable quality
        ImageWriter jpegWriter = ImageIO.getImageWritersByFormatName("jpeg").next();
        ImageOutputStream ios = ImageIO.createImageOutputStream(baos);
        jpegWriter.setOutput(ios);
        
        // Set compression quality to 0.70 for aggressive compression
        var writeParam = jpegWriter.getDefaultWriteParam();
        writeParam.setCompressionMode(javax.imageio.ImageWriteParam.MODE_EXPLICIT);
        writeParam.setCompressionQuality(0.70f);
        
        jpegWriter.write(null, new javax.imageio.IIOImage(image, null, null), writeParam);
        jpegWriter.dispose();
        ios.close();
        
        byte[] imageBytes = baos.toByteArray();
        
        // Log file size for debugging
        System.out.println("Compressed image size: " + (imageBytes.length / 1024.0 / 1024.0) + " MB");

        return new MultipartFile() {
            @Override
            public String getName() {
                return "file";
            }

            @Override
            public String getOriginalFilename() {
                return filename;
            }

            @Override
            public String getContentType() {
                return "image/jpeg";
            }

            @Override
            public boolean isEmpty() {
                return imageBytes.length == 0;
            }

            @Override
            public long getSize() {
                return imageBytes.length;
            }

            @Override
            public byte[] getBytes() throws IOException {
                return imageBytes;
            }

            @Override
            public java.io.InputStream getInputStream() throws IOException {
                return new ByteArrayInputStream(imageBytes);
            }

            @Override
            public void transferTo(java.io.File dest) throws IOException {
                java.nio.file.Files.write(dest.toPath(), imageBytes);
            }

            @Override
            public void transferTo(java.nio.file.Path dest) throws IOException {
                java.nio.file.Files.write(dest, imageBytes);
            }
        };
    }
}
