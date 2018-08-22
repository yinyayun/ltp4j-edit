package org.yinyayun.env;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

import org.yinyayun.env.LTPRuntimeException.ERR_CODE;

import edu.hit.ir.ltp4j.SplitSentence;

/**
 * Native环境配置
 * 
 * @author yinyayun
 *
 */
public class LTPNativeLibrary {
	private static final boolean DEBUG = System.getProperty("org.yinyayun.NativeLibrary.DEBUG") != null;
	public final static String[] JNIS = { "boost_regex", "splitsnt", "split_sentence_jni", "segmentor", "segmentor_jni",
			"postagger", "postagger_jni", "ner", "ner_jni", "parser", "parser_jni" };

	private LTPNativeLibrary() {
	}

	private static void load(boolean linux, String centos) {
		String packageVersion = LTPNativeLibrary.class.getPackage().getImplementationVersion();
		String jniDir = linux ? String.format("%s-%s-%s", os(), centos, architecture())
				: String.format("%s-%s", os(), architecture());
		log(String.format("use jni dir %s ,[linux:%s,centos:%s] ", jniDir, linux, centos));
		final File tempPath = createTempDirectory(packageVersion, jniDir);
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
						throw new RuntimeException(
								String.format("Cannot find ltp native library:%s for OS: %s, architecture: %s.",
										jniResourceName, os(), architecture()));
					}
					extractResource(jniResource, tmpLib);
				}
				System.load(tmpLib.getPath());
			}
		} catch (UnsatisfiedLinkError e) {
			throw new LTPRuntimeException(ERR_CODE.LINKED_ERR,
					String.format("Unable to extract native library into a temporary file (%s)", e.toString()), e);
		} catch (Throwable e) {
			throw new LTPRuntimeException(ERR_CODE.OTHER,
					String.format("Unable to extract native library into a temporary file (%s)", e.toString()), e);
		} finally {
			deletePathOnExit(tempPath);
		}
	}

	public static void load() {
		if (isLoaded()) {
			return;
		}
		String osName = os();
		boolean linux = "linux".equals(osName);
		String centosName = System.getProperty("centos.os", "centos5");
		try {
			load(linux, centosName);
		} catch (LTPRuntimeException e) {
			if (linux && e.isLinedError()) {
				String centos = centosName.equals("centos5") ? "centos7" : "centos5";
				log("---reload jni for " + centos + "\nmessage:" + e.getMessage());
				load(linux, centos);
			} else
				throw e;
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
		} catch (Throwable e) {
			log("tryLoadLibraryFailed: " + e.getMessage());
			return false;
		}
	}

	public static void deletePathOnExit(File file) {
		if (file.isDirectory()) {
			for (File f : file.listFiles()) {
				f.deleteOnExit();
			}
		}
		file.deleteOnExit();
	}

	/**
	 * 根据系统、以及架构确定动态链接的全名
	 * 
	 * @param baseName
	 * @return
	 */
	private static String makeResourceName(String jniDir, String baseName) {
		return String.format("%s/%s/%s", "lib", jniDir, System.mapLibraryName(baseName));
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

	private static File createTempDirectory(String packageVersion, String jniDirName) {
		File baseDirectory = new File(System.getProperty("java.io.tmpdir"), "ltp_native_libraries");
		if (!baseDirectory.exists()) {
			boolean succ = baseDirectory.mkdirs();
			if (!succ)
				throw new LTPRuntimeException("创建基础目录失败!");
		}
		int hashcode = LTPNativeLibrary.class.getClassLoader().hashCode();
		String directoryName = jniDirName + "-" + packageVersion + "-" + hashcode;
		File temporaryDirectory = new File(baseDirectory, directoryName);
		if (temporaryDirectory.exists() || temporaryDirectory.mkdir()) {
			return temporaryDirectory;
		}
		throw new LTPRuntimeException("Could not create a temporary directory");
	}
}
