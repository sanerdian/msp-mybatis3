package com.jnetdata.msp.member.user.util;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

public class ImgCodeUtil {
	private static char[] captchars = new char[] { '2', '3', '4', '5', '6',
			'7', '8', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'j', 'k', 'm',
			'n', 'p', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'A', 'B', 'C',
			'D', 'E', 'F', 'G', 'H', 'J', 'K', 'M', 'N', 'P', 'Q', 'R', 'S',
			'T', 'U', 'V', 'W', 'X', 'Y' };// 0，1，2易和字母的o，l,z易混淆，不生成,可以生成汉字


	public String generatorCode(){
		// 随机写字母或汉字
		int num = 4;// 字符的多少
		int car = captchars.length - 1;
		String code = "";
		for (int i = 0; i < num; i++) {
			// 1.随机得到num个字母或数字
			code += captchars[random.nextInt(car) + 1];
		}
		return code;
	}

	public void imgCode(String code,HttpServletRequest req, HttpServletResponse resp) throws IOException {
		//修改的信息在此
		int ImageWidth = code.length() * 35;//图片的宽
		int ImageHeight = 65;//图片的高
		int line_left = 7;//从左上到右下的线条个数
		int line_right = 8;//从右上到左下的线条个数

		Font font = new Font("Fixedsys", Font.PLAIN, 40);

		ServletOutputStream sos = resp.getOutputStream();
		//这个会导致报错 java.io.IOException: reading encoded JPEG Stream
		//JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(sos);// 得到输出流

		BufferedImage bi = new BufferedImage(ImageWidth, ImageHeight,
				BufferedImage.TYPE_BYTE_INDEXED);

		Graphics2D graphics = bi.createGraphics();

		Color c = getRandColor(200, 250);
		graphics.setColor(c);// 设置背景色

		graphics.fillRect(0, 0, bi.getWidth(), bi.getHeight());//填充背景色

		graphics.setColor(Color.black);

		graphics.setFont(font);
		graphics.setColor(this.getRandColor());
		graphics.drawString(code, 10, 50);// 将字写到图片上

		TextLayout textTl = new TextLayout(code, new Font("Fixedsys",
				Font.PLAIN, 20), new FontRenderContext(null, true, false));// 获得字体一样的字，20是字体的大小
		//textTl.draw(graphics, 30, 60);// 对字体加投影，第二个是左右相距，越大越远，第三个参数是上下两层相距距离，越大越近

		int w = bi.getWidth();
		int h = bi.getHeight();

		// 从左上到右下加上多道干扰线
		//graphics.setColor(getRandColor(160, 200));// 设置线条的颜色
		for (int i = 0; i < line_left; i++) {
			int x = random.nextInt(ImageWidth - 1);
			int y = random.nextInt(ImageHeight - 1);
			int xl = random.nextInt(6) + 1;
			int yl = random.nextInt(12) + 1;
			graphics.drawLine(x, y, x + xl + 40, y + yl + 20);
		}
		// 从右上到左下加多道干扰线
		for (int i = 0; i < line_right; i++) {
			int x = random.nextInt(ImageWidth - 1);
			int y = random.nextInt(ImageHeight - 1);
			int xl = random.nextInt(12) + 1;
			int yl = random.nextInt(6) + 1;
			graphics.drawLine(x, y, x - xl + 40, y - yl);
		}
		// 添加噪点
		float yawpRate = 0.012f;// 噪声率
		int area = (int) (yawpRate * w * h);
		for (int i = 0; i < area; i++) {
			int x = random.nextInt(w);
			int y = random.nextInt(h);
			int rgb = getRandomIntColor();
			bi.setRGB(x, y, rgb);
		}

		// 设置图片类型
		resp.setContentType("image/jpeg");
		// 写出
		ImageIO.write(bi, "JPEG",sos);
		bi=null;
		sos.flush();
	}

	/**
	 * 产生随机字体
	 */
	private Font getFont() {
		Random random = new Random();
		Font font[] = new Font[5];
		font[0] = new Font("Ravie", Font.PLAIN, 40);
		font[1] = new Font("Antique Olive Compact", Font.PLAIN, 40);
		font[2] = new Font("Fixedsys", Font.PLAIN, 40);
		font[3] = new Font("Wide Latin", Font.PLAIN, 40);
		font[4] = new Font("Gill Sans Ultra Bold", Font.PLAIN, 40);
		return font[random.nextInt(5)];
	}

	private Random random = new Random();// 随机器

	/**
	 * 随机产生定义的颜色
	 */
	private Color getRandColor() {
		Random random = new Random();
		Color color[] = new Color[10];
		color[0] = new Color(32, 158, 25);
		color[1] = new Color(218, 42, 19);
		color[2] = new Color(31, 75, 208);
		return color[random.nextInt(3)];
	}

	private Color getRandColor(int fc, int bc) {
		if (fc > 255)
			fc = 255;
		if (bc > 255)
			bc = 255;
		int r = fc + random.nextInt(bc - fc);
		int g = fc + random.nextInt(bc - fc);
		int b = fc + random.nextInt(bc - fc);
		return new Color(r, g, b);
	}

	// 添加噪点的方法
	private int getRandomIntColor() {
		int[] rgb = this.getRandomRgb();
		int color = 0;
		for (int c : rgb) {
			color = color << 8;
			color = color | c;
		}
		return color;
	}

	private int[] getRandomRgb() {
		int[] rgb = new int[3];
		for (int i = 0; i < 3; i++) {
			rgb[i] = random.nextInt(255);
		}
		return rgb;
	}

}
