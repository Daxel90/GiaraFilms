package it.giara.gui.utils;

import java.awt.AlphaComposite;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ColorConvertOp;
import java.awt.image.RescaleOp;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;

import it.giara.utils.Log;

public class ImageUtils
{
	
	// ritorna un Icona della dimensione originale
	public static ImageIcon getIcon(String iconName)
	{
		return new ImageIcon(getImage(iconName));
	}
	
	public static ImageIcon getIcon(BufferedImage iconName)
	{
		return new ImageIcon(iconName);
	}
	
	// ritorna una bufferedImage della dimensione originale
	public static BufferedImage getImage(String imageName)
	{
		try
		{
			if(getResourceAsStream("/it/resources/" + imageName) != null)
				return ImageIO.read(getResourceAsStream("/it/resources/" + imageName));
			else
				return new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
		} catch (final IOException e)
		{
			Log.stack(Log.GUI, e);
			return null;
		}
	}
	
	// ritorna una bufferedImage della dimensione originale da File
	public static BufferedImage getImage(File imageFile)
	{
		try
		{
			return ImageIO.read(imageFile);
		} catch (final IOException e)
		{
			Log.stack(Log.GUI, e);
			return null;
		}
	}
	
	// download immagine dal web
	public static ImageIcon getHttpImage(String url)
	{
		try
		{
			final URL imageUrl = new URL(url);
			final InputStream in = imageUrl.openStream();
			final BufferedImage image = ImageIO.read(in);
			in.close();
			return new ImageIcon(image);
		} catch (final Exception e)
		{
			Log.stack(Log.GUI, e);
		}
		return null;
	}
	
	public static BufferedImage getHttpBufferedImage(String url)
	{
		try
		{
			final URL imageUrl = new URL(url);
			final InputStream in = imageUrl.openStream();
			final BufferedImage image = ImageIO.read(in);
			in.close();
			return image;
		} catch (final Exception e)
		{
			Log.stack(Log.GUI, e);
			// Log.stack(e);
		}
		return null;
	}
	
	// Convert Icon to Image
	public static Image imageToIcon(Icon icon)
	{
		if (icon instanceof ImageIcon)
		{
			return ((ImageIcon) icon).getImage();
		}
		else
		{
			final int w = icon.getIconWidth();
			final int h = icon.getIconHeight();
			final GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			final GraphicsDevice gd = ge.getDefaultScreenDevice();
			final GraphicsConfiguration gc = gd.getDefaultConfiguration();
			final BufferedImage image = gc.createCompatibleImage(w, h);
			final Graphics2D g = image.createGraphics();
			icon.paintIcon(null, g, 0, 0);
			g.dispose();
			return image;
		}
	}
	
	// ritorna la risorsa associata alla
	public static InputStream getResourceAsStream(String path)
	{
		return ImageUtils.class.getResourceAsStream(path);
	}
	
	// converte una BufferedImage in bianco e nero
	public static BufferedImage getGrayScale(BufferedImage original)
	{
		final BufferedImageOp op = new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY), null);
		return op.filter(original, null);
	}
	
	// ritorna una bufferedImage di una determinata dimensione
	public static BufferedImage getImage(String imageName, int w, int h)
	{
		try
		{
			return scaleImage(ImageIO.read(getResourceAsStream("/it/resources/" + imageName)), w, h);
		} catch (final IOException e)
		{
			Log.stack(Log.GUI, e);
			return null;
		}
	}
	
	// ritorna l'icona di una determinata dimensione
	public static ImageIcon getIcon(String iconName, int w, int h)
	{
		try
		{
			return new ImageIcon(scaleImage(ImageIO.read(getResourceAsStream("/it/resources/" + iconName)), w, h));
		} catch (final IOException e)
		{
			Log.stack(Log.GUI, e);
			return null;
		}
	}
	
	// font del launcher
	public static Font getFrameFont(String FontName, int size)
	{
		Font font;
		try
		{
			font = Font.createFont(Font.TRUETYPE_FONT, getResourceAsStream("/it/resources/" + FontName + ".ttf"))
					.deriveFont((float) size);
		} catch (final Exception e)
		{
			Log.stack(Log.GUI, e);
			font = new Font("Arial", Font.PLAIN, size);
		}
		return font;
	}
	
	// RenderAlpha
	public static BufferedImage alphaSet(BufferedImage img, float alpha)
	{
		final BufferedImage newImage = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
		final Graphics2D g = newImage.createGraphics();
		try
		{
			g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
			g.setComposite(ac);
			g.drawImage(img, 0, 0, img.getWidth(), img.getHeight(), null);
		} finally
		{
			g.dispose();
		}
		return newImage;
	}
	
	// RescaleOp Filter
	public static BufferedImage FilterRescaleOp(BufferedImage img, float val)
	{
		final RescaleOp op = new RescaleOp(val, 0, null);
		return op.filter(img,null);
	}
	
	// scala la BufferedImage mantenendo l' aspect ratio riferimento Larghezza
	public static BufferedImage scaleWithAspectWidth(BufferedImage img, int width)
	{
		final int imgWidth = img.getWidth();
		final int imgHeight = img.getHeight();
		final int height = imgHeight * width / imgWidth;
		return scaleImage(img, width, height);
	}
	
	// scala la BufferedImage senza conservare aspect ratio
	@Deprecated
	public static BufferedImage scaleImageOld(BufferedImage img, int width, int height)
	{
		final BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		final Graphics2D g = newImage.createGraphics();
		try
		{
			g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g.drawImage(img, 0, 0, width, height, null);
		} finally
		{
			g.dispose();
		}
		return newImage;
	}
	
	// //scalare immagine con qualita' superiore senza conservare aspect ratio
	public static BufferedImage scaleImage(BufferedImage img, int targetWidth, int targetHeight)
	{
        int type = (img.getTransparency() == Transparency.OPAQUE) ?
                BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;
        BufferedImage ret = img;
        int w, h;

		// Use multi-step technique: start with original size, then
		// scale down in multiple passes with drawImage()
		// until the target size is reached
		w = img.getWidth();
		h = img.getHeight();

        do {
            if (w > targetWidth) {
                w /= 2;
                if (w < targetWidth) {
                    w = targetWidth;
                }
            } else {
                w = targetWidth;
            }

            if (h > targetHeight) {
                h /= 2;
                if (h < targetHeight) {
                    h = targetHeight;
                }
            } else {
                h = targetHeight;
            }

            BufferedImage tmp = new BufferedImage(w,h,type);
			Graphics2D g2 = tmp.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.drawImage(ret, 0, 0, w, h, null);
            g2.dispose();

            ret = tmp;
        } while (w != targetWidth || h != targetHeight);

        return ret;
    }
	
	// scala la BufferedImage mantenendo l' aspect ratio riferimento Altezza
	public static BufferedImage scaleWithAspectHeight(BufferedImage img, int height)
	{
		final int imgWidth = img.getWidth();
		final int imgHeight = img.getHeight();
		final int width = imgWidth * height / imgHeight;
		return scaleImage(img, width, height);
	}
}
