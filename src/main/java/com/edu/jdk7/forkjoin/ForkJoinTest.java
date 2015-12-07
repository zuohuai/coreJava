package com.edu.jdk7.forkjoin;

import java.io.File;
import java.util.concurrent.ForkJoinPool;

import org.apache.commons.lang3.StringUtils;

public class ForkJoinTest {
	private final ForkJoinPool forkJoinPool = new ForkJoinPool();
	private static String path = StringUtils.EMPTY;
	private static String word = "Hello";
	static {
		path = System.getProperty("user.dir") + File.separator + "file";
	}

	public static void main(String[] args) throws Exception {
		long start = System.currentTimeMillis();
		Folder folder = Folder.fromDirectory(new File(path));
		ForkJoinTest forkJoinTest = new ForkJoinTest();
		Long count = forkJoinTest.countOccurrencesInParallel(folder, "Hello");
		if (count != null) {
			System.out.println(count);
		}
		long end = System.currentTimeMillis();
		System.out.println("fork/join统计时间是:" + (end - start));
		
		
		start = System.currentTimeMillis();
		WordCounter wordCounter = new WordCounter();
		folder = Folder.fromDirectory(new File(path));
		count = wordCounter.countOccurrencesOnSingleThread(folder, word);
		System.out.println(count);
		end = System.currentTimeMillis();
		System.out.println("Single/Thread统计时间是:" + (end - start));
	}

	public Long countOccurrencesInParallel(Folder folder, String searchedWord) {
		return forkJoinPool.invoke(new FolderSearchTask(folder, searchedWord));
	}

}
