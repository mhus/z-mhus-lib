package de.mhus.lib.core.io;

import java.io.InputStream;

public interface StreamRewriter {

	/**
	 * Load the incoming stream and return a new stream for rewritten content.
	 * The rewriter can consume the incoming stream fully until EOF.
	 * 
	 * @param in Incoming content
	 * @return Replacement stream e.g the incoming stream
	 */
	InputStream rewriteContent(InputStream in);
	
}
