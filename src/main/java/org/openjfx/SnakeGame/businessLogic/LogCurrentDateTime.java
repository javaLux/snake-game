
package org.openjfx.SnakeGame.businessLogic;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javafx.application.Platform;

/**
 * @author CSD
 *
 */
public class LogCurrentDateTime {

	public static void logExecutedProgram(Path pathForLogFile) {
		
		if (pathForLogFile != null) {
			// file object of logfile path
			File logFile = new File(pathForLogFile.toString());

			// if logFile curenntly not exists -> create
			if (!logFile.exists()) {
				try {
					
					logFile.createNewFile();
	
				} catch (IOException ex) {
					// show an error message box an quit program
					ErrorBoxSwing.showExceptionStacktrace(ex, "Snake - Game: An error occurent", null);
					Platform.exit();
					System.exit(1);
				}
			}

			// DateTime Formatter (for example: 2021/09/01 12:56:03)
			DateTimeFormatter dtFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
			// get the current executing program with the executing time stamp
			String contenOfLog = ">>> " + System.getProperty("sun.java.command") + " <<<" + "\tstarted at:\t"
					+ dtFormatter.format(LocalDateTime.now()) + "\n";

			// write data in file use "try with resources"
			// -> The file does not have to be explicitly closed at the end of the write
			// process
			try (BufferedWriter writer = Files.newBufferedWriter(pathForLogFile, StandardCharsets.UTF_8,
					StandardOpenOption.APPEND)) {
				writer.write(contenOfLog);
				writer.newLine();

			} catch (Exception ex) {
				// show an error message box an quit program
				ErrorBoxSwing.showExceptionStacktrace(ex, "Snake - Game: An error occurent", null);
			}
		}

	}

}
