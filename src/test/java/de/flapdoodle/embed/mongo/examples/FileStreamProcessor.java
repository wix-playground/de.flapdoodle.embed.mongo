package de.flapdoodle.embed.mongo.examples;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import de.flapdoodle.embed.process.io.IStreamProcessor;

// ...
public class FileStreamProcessor implements IStreamProcessor {

	private final FileOutputStream outputStream;

	public FileStreamProcessor(File file) throws FileNotFoundException {
		outputStream = new FileOutputStream(file);
	}

	@Override
	public void process(String block) {
		try {
			outputStream.write(block.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onProcessed() {
		try {
			outputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
// ...
// <-