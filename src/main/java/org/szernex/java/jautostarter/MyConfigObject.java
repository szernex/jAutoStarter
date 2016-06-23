package org.szernex.java.jautostarter;

import java.util.LinkedList;
import java.util.List;

public class MyConfigObject {
	List<StartSetting> startSettings = new LinkedList<>();
}

class StartSetting {
	String name = "ExampleApplication";
	long delayBefore = 1000;
	long delayAfter = 1000;
	String workingDirectory = "";
	List<String> startArgs = new LinkedList<>();
}