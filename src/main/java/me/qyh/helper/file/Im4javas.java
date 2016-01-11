package me.qyh.helper.file;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.apache.commons.io.IOUtils;
import org.im4java.core.ConvertCmd;
import org.im4java.core.IMOperation;
import org.im4java.core.IdentifyCmd;
import org.im4java.process.ArrayListOutputConsumer;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import me.qyh.bean.Crop;
import me.qyh.exception.SystemException;
import me.qyh.utils.Validators;

@Component
public class Im4javas implements InitializingBean , ImageProcessing {

	private static final boolean windows = (System.getProperty("os.name").toLowerCase().indexOf("win") != -1);
	private static final String GIF = "GIF";
	
	@Value("${config.magick.path}")
	private String magickPath;

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

	@Override
	public void zoom(File src, File dest, Resize size) throws Exception {
		IMOperation op = new IMOperation();
		op.addImage();
		op.thumbnail(size.getSize(),size.getSize(),">");
		op.strip();
		op.p_profile("*");
		op.addImage();
		getConvertCmd().run(op, src.getAbsolutePath() +"[0]", dest.getAbsolutePath());
	}

	@Override
	public void crop(Crop crop, File dest) throws Exception {
		IMOperation op = new IMOperation();
		op.addImage();
		op.crop(crop.getW(), crop.getH(), crop.getX(), crop.getY());
		op.addImage();
		getConvertCmd().run(op, crop.getFile().getAbsolutePath() + "[0]", dest.getAbsolutePath());
	}

	@Override
	public void format(File src, File dest, String format) throws Exception {
		IMOperation op = new IMOperation();
		op.addImage();
		op.format(format);
		op.addImage();
		getConvertCmd().run(op, src.getAbsolutePath() + "[0]", dest.getAbsolutePath() + "." + format);
	}

	@Override
	public ImageInfo read(File src) throws BadImageException{
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
		try {
			localIdentifyCmd.run(localIMOperation, src.getAbsolutePath() + "[0]");
			List<String> atts = localArrayListOutputConsumer.getOutput();
			Iterator<String> it = atts.iterator();
			return new ImageInfo(Integer.parseInt(it.next()), Integer.parseInt(it.next()), it.next());
		} catch (Exception e) {
			throw new BadImageException(e);
		}
	}

	@Override
	public int getFrameNumsOfGif(File gif) throws Exception {
		ImageInputStream in = null;
		try{
			ImageReader ir = ImageIO.getImageReadersBySuffix(GIF).next();
			in = ImageIO.createImageInputStream(gif);
			ir.setInput(in);
			return ir.getNumImages(true);
		}finally{
			IOUtils.closeQuietly(in);
		}
	}

	@Override
	public void writeFirstFrameOfGif(File gif, File desc) throws Exception {
		IMOperation op = new IMOperation();
		op.addImage();
		op.addImage();
		getConvertCmd().run(op, gif.getAbsolutePath() + "[0]", desc.getAbsolutePath());
	}

	@Override
	public void compress(File src, File desc) throws Exception {
		IMOperation op = new IMOperation();
		op.addImage();
		op.strip();
		op.p_profile("*");
		op.quality(85D);
		op.addImage();
		getConvertCmd().run(op, src.getAbsolutePath() + "[0]", desc.getAbsolutePath());
	}
}
