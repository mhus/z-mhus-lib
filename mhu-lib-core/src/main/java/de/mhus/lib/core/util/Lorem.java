package de.mhus.lib.core.util;

public class Lorem {

	public static String create() {
		return "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.";
	}
	
	public static String create(int paragraphs) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < paragraphs; i++)
			sb.append(create()).append('\n');
		return sb.toString();
	}

	public static String createHtml(int paragraphs) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < paragraphs; i++)
			sb.append("<p>").append(create()).append("</p>\n");
		return sb.toString();
	}

}
