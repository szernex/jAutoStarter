/*
The MIT License (MIT)

Copyright (c) 2016 Szernex

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 */

package org.szernex.java.jautostarter;

import java.io.File;
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
			ProcessBuilder builder = new ProcessBuilder(setting.startArgs);

			if (!setting.workingDirectory.isEmpty()) {
				System.out.println("-- Working directory: " + setting.workingDirectory);
				builder.directory(new File(setting.workingDirectory));
			}

			builder.start();
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
