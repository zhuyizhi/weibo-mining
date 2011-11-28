package edu.sjtu.ltlab.word.split;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

public class SplitterFactory {
	public static String ICTCLAS_SPLIT = "edu.sjtu.ltlab.word.split.ICTCSplit";

	public static String HL_SPLIT = "edu.sjtu.ltlab.word.split.HLSplit";
	
	public static String IR_SPLIT = "edu.sjtu.ltlab.word.split.IRSplit";
	

	public static Splitter getSpliter(String split) {
		try {
			return (Splitter) (Class.forName(split).newInstance());
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	/**
	 * a simple tag transfer
	 * 
	 * @param content the content should be already splitted
	 * @param map
	 * @return
	 */
	public static String transPOSTag(String content, HashMap<String, String> map) {
		StringBuilder result = new StringBuilder();
		StringBuilder pos = new StringBuilder();

		int size = content.length();
		char c;
		for (int i = 0; i < size; i++) {
			c = content.charAt(i);
			if (c == POSGB.oldPOS) {
				i++;
				c = content.charAt(i);

				while (Character.isLetter(c)) {
					pos.append(c);
					i++;
					c = content.charAt(i);
				}

				result.append(POSGB.newPOS);
				result.append(map.get(pos.toString()));
				result.append(POSGB.SEPERATOR);
				pos.delete(0, pos.length());
			} else if (c != ' ')
				result.append(c);
		}

		return result.toString();
	}

	/**
	 * split file
	 * 
	 * @param split
	 * @param source
	 * @param target
	 * @return
	 */
	public static boolean fileProcess(Splitter split, String source, String target) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(source));
			BufferedWriter writer = new BufferedWriter(new FileWriter(target));

			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null)
				sb.append(line);

			String result = split.paragraphProcess(sb.toString());

			writer.append(result);

			reader.close();
			writer.close();

			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}

		return false;
	}
	
	/**
	 * Use Spliter.fileProcess(src, des) spliting the file in the src directory
	 * repeatly, the result is saved in des directory use the same file name.
	 * 
	 * @param src
	 * @param des
	 * @return
	 */
	public static boolean batchProcess(String src, String des) {
		File srcDir = new File(src);
		File desDir = new File(des);

		if (!srcDir.isDirectory() || !desDir.isDirectory()) {
			System.out.println("input should be directory");
			return false;
		}

		/**
		 * file already splited
		 */
		HashSet<String> splitedFile = new HashSet<String>();
		File[] files = desDir.listFiles();
		for (File file : files) {
			String name = file.getName();
			splitedFile.add(name);
		}

		files = srcDir.listFiles();
		Splitter spliter = SplitterFactory.getSpliter(SplitterFactory.HL_SPLIT);
		for (File file : files) {
			if (file.isFile()) {
				if (!splitedFile.contains(file.getName())) {
					String srcPath = file.getPath();
					String desPath = des + File.separator + file.getName(); // System.getProperty("file.separator")
					SplitterFactory.fileProcess(spliter, srcPath, desPath);
				}
			} else {
				File desfile = new File(des + File.separator + file.getName());
				if (!desfile.exists())
					desfile.mkdir();
				batchProcess(file.getAbsolutePath(), desfile.getAbsolutePath());
			}

		}

		return true;

	}
	
	public static void main(String[] args) {
	
		Splitter split1 = SplitterFactory.getSpliter(SplitterFactory.HL_SPLIT);
		System.out.println(split1.paragraphProcess("根据我国能源可持续发展的需求，国家制定了积极发展核电的战略政策"));
	}

}
