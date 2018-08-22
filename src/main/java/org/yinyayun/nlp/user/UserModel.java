package org.yinyayun.nlp.user;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.yinyayun.nlp.LTPBaseModel;

/**
 * 加载用户自定义内容
 * 
 * @author yinyayun
 *
 */
public abstract class UserModel extends LTPBaseModel {
	public UserModel() {
	}

	public UserModel(String modelFilePath) {
		super(modelFilePath);
	}

	public abstract void load(String word, String other);

	@Override
	public void loadModel() throws IOException {
		return;
	}

	/**
	 * 通过stram流进行加载
	 * 
	 * @param modelAlias
	 * @param inputStream
	 * @throws IOException
	 */
	public void loadModel(String modelAlias, InputStream inputStream) throws IOException {
		File tmpFile = null;
		File dir = createTempDirectory();
		try {
			tmpFile = File.createTempFile(modelAlias, ".tmp", dir);
			tmpFile.deleteOnExit();
			Files.copy(inputStream, Paths.get(tmpFile.getAbsolutePath()), StandardCopyOption.REPLACE_EXISTING);
			loadModel(tmpFile);
		} finally {
			if (tmpFile != null && tmpFile.exists()) {
				tmpFile.delete();
			}
		}
	}

	public void loadModel(String modelName) throws IOException {
		try (InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(modelName)) {
			loadModel(modelName, inputStream);
		}
	}

	@Override
	public String getModelName() {
		throw new RuntimeException("不允许调用UserModel的getModelName()方法");
	}

	@Override
	public boolean isloaded() {
		return false;
	}
}
