package org.szernex.java.jautostarter;

import java.io.IOException;
import java.nio.file.Paths;

public class Main {
	public static void main(String[] args) {
		System.out.println("Loading config");
		ConfigObject config = Config.load(Paths.get(R.CONFIG_FILE));

		if (config == null) {
			System.out.println("Failed to load config");
			return;
		}

		System.out.println("Config loaded");

		if (config.startSettings.size() == 0) {
			config.startSettings.add(new StartSetting());
			Config.save(config, Paths.get(R.CONFIG_FILE));
			System.out.println("Empty config detected. Wrote example config to " + R.CONFIG_FILE + ". Please add settings there.");
			return;
		}

		config.startSettings.forEach(Main::startApplication);
	}

	private static void startApplication(StartSetting setting) {
		if (setting.startArgs.size() == 0) {
			System.out.println("Skipping " + setting.name + ", empty argument list");
			return;
		}

		System.out.println("Starting " + setting.name);
		System.out.println("-- Delay before: " + setting.delayBefore + "ms");

		try {
			Thread.sleep(setting.delayBefore);
		} catch (InterruptedException ignored) {
		}

		try {
			System.out.println("-- Starting application: " + String.join(" ", setting.startArgs));
			new ProcessBuilder(setting.startArgs).start();
		} catch (IOException e) {
			System.err.println("-- Error starting " + setting.name + ": " + e.getMessage());
			e.printStackTrace();
			return;
		}

		System.out.println("-- Delay after: " + setting.delayAfter + "ms");

		try {
			Thread.sleep(setting.delayAfter);
		} catch (InterruptedException ignored) {
		}
	}
}
