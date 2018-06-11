package org.yinyayun.env;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

import edu.hit.ir.ltp4j.SplitSentence;

/**
 * Native环境配置
 * 
 * @author yinyayun
 *
 */
public class LTPNativeLibrary {
	private static final boolean DEBUG = System.getProperty("org.yinyayun.NativeLibrary.DEBUG") != null;
	public final static String[] JNIS = { "splitsnt", "split_sentence_jni", "segmentor", "segmentor_jni", "postagger",
			"postagger_jni", "ner", "ner_jni", "parser", "parser_jni" };

	private LTPNativeLibrary() {
	}

	public static void load() {
		if (isLoaded()) {
			return;
		}
		String packageVersion = LTPNativeLibrary.class.getPackage().getImplementationVersion();
		String jniDir = makeJniDir();
		final File tempPath = createTempDirectory(packageVersion);
		try {
			for (String libName : JNIS) {
				// 确定待生成lib的绝对路径
				final File tmpLib = new File(tempPath, System.mapLibraryName(libName));
				if (!tmpLib.exists()) {
					final String jniResourceName = makeResourceName(jniDir, libName);
					log("jniResourceName: " + jniResourceName);
					final InputStream jniResource = LTPNativeLibrary.class.getClassLoader()
							.getResourceAsStream(jniResourceName);
					if (jniResource == null) {
						throw new UnsatisfiedLinkError(
								String.format("Cannot find ltp native library:%s for OS: %s, architecture: %s.",
										jniResourceName, os(), architecture()));
					}
					extractResource(jniResource, tmpLib);
				}
				System.load(tmpLib.getPath());
			}
		} catch (IOException e) {
			throw new UnsatisfiedLinkError(
					String.format("Unable to extract native library into a temporary file (%s)", e.toString()));
		}
	}

	/**
	 * 尝试调用native方法
	 * 
	 * @return
	 */
	private static boolean isLoaded() {
		try {
			new SplitSentence().splitSentence("hello ltp", new ArrayList<String>());
			return true;
		} catch (UnsatisfiedLinkError e) {
			log("tryLoadLibraryFailed: " + e.getMessage());
			return false;
		}
	}

	/**
	 * 根据系统、以及架构确定动态链接的全名
	 * 
	 * @param baseName
	 * @return
	 */
	private static String makeResourceName(String jniDir, String baseName) {
		return String.format("%s/%s", jniDir, System.mapLibraryName(baseName));
	}

	/**
	 * 根据系统、以及架构确定动态链接文件的目录
	 * 
	 * @param prefix
	 * @param baseName
	 * @return
	 */
	private static String makeJniDir() {
		return String.format("%s-%s", os(), architecture());
	}

	/**
	 * 机器架构
	 * 
	 * @return
	 */
	private static String architecture() {
		final String arch = System.getProperty("os.arch").toLowerCase();
		return (arch.equals("amd64")) ? "x86_64" : arch;
	}

	private static String os() {
		final String p = System.getProperty("os.name").toLowerCase();
		if (p.contains("linux")) {
			return "linux";
		} else if (p.contains("os x") || p.contains("darwin")) {
			return "darwin";
		} else if (p.contains("windows")) {
			return "windows";
		} else {
			return p.replaceAll("\\s", "");
		}
	}

	private static void log(String msg) {
		if (DEBUG) {
			System.err.println("org.yinyayun.NativeLibrary: " + msg);
		}
	}

	private static void extractResource(InputStream resource, File tmpLib) throws IOException {
		Files.copy(resource, tmpLib.toPath(), StandardCopyOption.REPLACE_EXISTING);
		log(String.format("copied jniResource to %s", tmpLib.getPath()));
	}

	private static File createTempDirectory(String packageVersion) {
		File baseDirectory = new File(System.getProperty("java.io.tmpdir"));
		String directoryName = "ltp_native_libraries-" + packageVersion;
		for (int attempt = 0; attempt < 10; attempt++) {
			File temporaryDirectory = new File(baseDirectory, directoryName + attempt);
			if (temporaryDirectory.exists() || temporaryDirectory.mkdir()) {
				return temporaryDirectory;
			}
		}
		throw new IllegalStateException("Could not create a temporary directory");
	}
}
