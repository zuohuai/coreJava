package com.edu.jdk7.forkjoin;

import java.util.concurrent.RecursiveTask;

public class DocumentSearchTask extends RecursiveTask<Long> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final Document document;
	private final String searchedWord;

	public DocumentSearchTask(Document document, String searchedWord) {
		super();
		this.document = document;
		this.searchedWord = searchedWord;

	}

	@Override
	protected Long compute() {
		return occurrencesCount(document, searchedWord);
	}

	private String[] wordsIn(String line) {
		return line.trim().split(" ");
	}

	private Long occurrencesCount(Document document, String searchedWord) {
		System.out.println(Thread.currentThread().getName());
		long count = 0;
		for (String line : document.getLines()) {
			for (String word : wordsIn(line)) {
				if (searchedWord.equals(word)) {
					count = count + 1;
				}
			}
		}
		return count;
	}
}
