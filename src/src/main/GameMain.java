package main;

import java.lang.management.ManagementFactory;

import javafx.application.Application;
import view.GameView;

public class GameMain {

	
	
	
	
	
	public static void main(String[] args) {
		/* This was used for testing VM arguments because eclipse hates running javaFX. 
		 * Left so you guys can use it if you want. If your output doesn't match this
		 * exactly and the view isn't running, I can probably diagnose the problem pretty 
		 * fast. 
		 * Expected output:
		 * VM args: main.GameMain
  		 *	--enable-native-access=javafx.graphics
  		 *	--add-modules=javafx.controls,javafx.fxml
  		 *	--module-path="Path to the bin of your javaFX library. Make sure versions match"
  		 *	-Dfile.encoding=UTF-8
  		 *	-Dstdout.encoding=UTF-8
  		 *	-Dstderr.encoding=UTF-8
  		 *	--module-path="Path to all the files in your javaFX library"
		 * Something happened
		 * 
		 */
		/*
		System.out.println("VM args: " + System.getProperty("sun.java.command"));
		for (String arg : ManagementFactory.getRuntimeMXBean().getInputArguments()) {
	        System.out.println("  " + arg);
	    }
	    */
		Application.launch(GameView.class, args);
	}
}
