package de.mhus.lib.core.io;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import de.mhus.lib.core.MFile;
import de.mhus.lib.core.MString;
import de.mhus.lib.core.parser.StringPropertyReplacer;

/**
 * Rewrite the incoming stream using the string replacer. The
 * rewriter need to load the full content of the stream in memory twice.
 * 
 * @author mikehummel
 *
 */
public class StringPropertyRewriter implements StreamRewriter {

	private StringPropertyReplacer replacer;

	public StringPropertyRewriter(StringPropertyReplacer replacer) {
		this.replacer = replacer;
	}

	@Override
	public InputStream rewriteContent(InputStream in) {
		String content = MFile.readFile(in);
		content = replacer.process(content);
		return new ByteArrayInputStream(MString.toBytes(content));
	}

}
