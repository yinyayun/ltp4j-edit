package org.yinyayun.nlp;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.yinyayun.env.LTPNativeLibrary;
import org.yinyayun.nlp.common.LTPContext;

/**
 * 统一模型文件的处理
 * 
 * @author yinyayun
 *
 */
public abstract class LTPBaseModel {
	private String modelFilePath;

	public LTPBaseModel() {
	}

	public LTPBaseModel(String modelFilePath) {
		this.modelFilePath = modelFilePath;
	}

	public abstract void doAction(LTPContext context);

	public void loadModel() throws IOException {
		if (isloaded()) {
			return;
		}
		if (modelFilePath != null && modelFilePath.length() > 0) {
			File modelFile = new File(modelFilePath);
			if (!modelFile.exists()) {
				throw new FileNotFoundException(modelFilePath);
			}
			loadModel(modelFile);
		} else {
			File modelFile = null;
			File dir = createTempDirectory();
			try (InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(getModelName())) {
				modelFile = unzip(inputStream, dir);
				loadModel(modelFile);
			} finally {
				if (modelFile != null && modelFile.exists()) {
					modelFile.delete();
				}
			}
		}
	}

	public abstract void loadModel(File modelFile);

	public abstract boolean isloaded();

	private File unzip(InputStream inputStream, File dir) {
		try (ZipInputStream zin = new ZipInputStream(inputStream)) {
			ZipEntry entry = null;
			while ((entry = zin.getNextEntry()) != null) {
				String name = entry.getName();
				if (name.endsWith("model")) {
					File extractFile = new File(dir, name + "-" + System.currentTimeMillis());
					byte[] buffer = new byte[4096];
					try (BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(extractFile))) {
						int count = -1;
						while ((count = zin.read(buffer)) != -1)
							out.write(buffer, 0, count);
					}
					return extractFile;
				}
			}
			throw new RuntimeException("can not find model file in zipFile!");
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	private File createTempDirectory() {
		String packageVersion = LTPNativeLibrary.class.getPackage().getImplementationVersion();
		File baseDirectory = new File(System.getProperty("java.io.tmpdir"));
		String directoryName = "ltp_fnlp_models-" + packageVersion;
		for (int attempt = 0; attempt < 10; attempt++) {
			File temporaryDirectory = new File(baseDirectory, directoryName + attempt);
			if (temporaryDirectory.exists() || temporaryDirectory.mkdir()) {
				return temporaryDirectory;
			}
		}
		throw new IllegalStateException("Could not create a temporary directory");
	}

	public abstract String getModelName();
}
