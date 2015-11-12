package me.qyh.helper.im4java;

import java.util.Iterator;
import java.util.List;

import me.qyh.exception.SystemException;
import me.qyh.upload.server.inner.BadImageException;
import me.qyh.utils.Validators;

import org.im4java.core.ConvertCmd;
import org.im4java.core.IMOperation;
import org.im4java.core.IdentifyCmd;
import org.im4java.process.ArrayListOutputConsumer;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Im4javas implements InitializingBean {

	private static final String IMAGEPREFIX = "image/";
	private static final boolean windows = (System.getProperty("os.name")
			.toLowerCase().indexOf("win") != -1);
	@Value("${config.magick.path}")
	private String magickPath;

	public void zoom(String absPath, String destPath, int size)
			throws Exception {
		IMOperation op = new IMOperation();
		op.thumbnail(size);
		op.addImage();
		op.addImage();
		getConvertCmd().run(op, absPath, destPath);
	}

	public void crop(String absPath, String destPath, int x, int y, int width,
			int height) throws Exception {
		IMOperation op = new IMOperation();
		op.addImage();
		op.crop(width, height, x, y);
		op.addImage();
		getConvertCmd().run(op, absPath, destPath);
	}

	public void writeFirstFrameOfGif(String absPath, String destPath)
			throws Exception {
		IMOperation op = new IMOperation();
		op.addImage();
		op.addImage();
		getConvertCmd().run(op, absPath + "[0]", destPath);
	}

	public ImageInfo getImageInfo(String absPath) throws BadImageException {
		IMOperation localIMOperation = new IMOperation();
		localIMOperation.ping();
		localIMOperation.format("%w\n%h\n%m\n");
		localIMOperation.addImage();
		IdentifyCmd localIdentifyCmd = new IdentifyCmd(true);
		if (windows) {
			localIdentifyCmd.setSearchPath(magickPath);
		}
		ArrayListOutputConsumer localArrayListOutputConsumer = new ArrayListOutputConsumer();
		localIdentifyCmd.setOutputConsumer(localArrayListOutputConsumer);
		try{
			localIdentifyCmd.run(localIMOperation, absPath + "[0]");
			List<String> atts = localArrayListOutputConsumer.getOutput();
			Iterator<String> it = atts.iterator();
			return new ImageInfo(Integer.parseInt(it.next()), Integer.parseInt(it.next()), it.next());
		}catch (Exception e) {
			throw new BadImageException(e);
		}
	}
	
	public static boolean maybeImage(String contentType) {
		return Validators.isEmptyOrNull(contentType, true) ? false
				: contentType.startsWith(IMAGEPREFIX);
	}

	public class ImageInfo {
		private int width;
		private int height;
		private String type;

		public int getWidth() {
			return width;
		}

		public int getHeight() {
			return height;
		}

		public String getType() {
			return type;
		}

		private ImageInfo(int width, int height, String type) {
			this.width = width;
			this.height = height;
			this.type = type;
		}

		@Override
		public String toString() {
			return "ImageInfo [width=" + width + ", height=" + height
					+ ", type=" + type + "]";
		}
	}

	private ConvertCmd getConvertCmd() {
		ConvertCmd cmd = new ConvertCmd(true);
		if (windows) {
			cmd.setSearchPath(magickPath);
		}
		return cmd;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		if (windows && Validators.isEmptyOrNull(magickPath, true)) {
			throw new SystemException("在windows下GraphicsMagick的路径必须配置且不能为空");
		}
	}
}
