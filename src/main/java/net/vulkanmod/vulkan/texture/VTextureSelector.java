package net.vulkanmod.vulkan.texture;

import net.vulkanmod.Initializer;

import java.nio.ByteBuffer;

public abstract class VTextureSelector {
    public static final int SIZE = 8;

    private static final VulkanImage[] boundTextures = new VulkanImage[SIZE];

    private static final int[] levels = new int[SIZE];

    private static final VulkanImage whiteTexture = VulkanImage.createWhiteTexture();

    private static int activeTexture = 0;

    public static void bindTexture(VulkanImage texture) {
        boundTextures[0] = texture;
    }

    public static void bindTexture(int i, VulkanImage texture) {
        if(i < 0 || i > 7) {
            Initializer.LOGGER.error(String.format("On Texture binding: index %d out of range [0, 7]", i));
            return;
        }

        boundTextures[i] = texture;
        levels[i] = -1;
    }

    public static void bindImage(int i, VulkanImage texture, int level) {
        if(i < 0 || i > 7) {
            Initializer.LOGGER.error(String.format("On Texture binding: index %d out of range [0, 7]", i));
            return;
        }

        boundTextures[i] = texture;
        levels[i] = level;
    }

    public static void uploadSubTexture(int mipLevel, int width, int height, int xOffset, int yOffset, int unpackSkipRows, int unpackSkipPixels, int unpackRowLength, ByteBuffer buffer) {
        VulkanImage texture = boundTextures[activeTexture];

        if(texture == null)
            throw new NullPointerException("Texture is null at index: " + activeTexture);

        texture.uploadSubTextureAsync(mipLevel, width, height, xOffset, yOffset, unpackSkipRows, unpackSkipPixels, unpackRowLength, buffer);
    }

    public static VulkanImage getTexture(String name) {
        return switch (name) {
            default -> boundTextures[0];
            case "Sampler1" -> boundTextures[1];
            case "Sampler2" -> boundTextures[2];
            case "Sampler3" -> boundTextures[3];
            case "Sampler4" -> boundTextures[4];
            case "Sampler5" -> boundTextures[5];
            case "Sampler6" -> boundTextures[6];
            case "Sampler7" -> boundTextures[7];
        };
    }

    public static void setLightTexture(VulkanImage texture) {
        boundTextures[2] = texture;
    }

    public static void setOverlayTexture(VulkanImage texture) {
        boundTextures[1] = texture;
    }

    public static void setActiveTexture(int activeTexture) {
        if(activeTexture < 0 || activeTexture > 7) {
            throw new IllegalStateException(String.format("On Texture binding: index %d out of range [0, 7]", activeTexture));
        }

        VTextureSelector.activeTexture = activeTexture;
    }

    public static VulkanImage getBoundTexture(int i) { return boundTextures[i]; }

    public static VulkanImage getWhiteTexture() { return whiteTexture; }
}
