package de.mhus.lib.core.io;

import java.io.File;

import de.mhus.lib.core.MFile;
import de.mhus.lib.core.MLog;
import de.mhus.lib.core.MString;

/**
 * For simple pdf content checking.
 * 
 * @author markus hahn
 *
 */
public class PdfChecker extends MLog {

	/**
	 * checks for suspicious contents
	 * @param pdfFile
	 * @return 0 = not suspicious, 1 = little suspicious, 2-3 = suspicious, 4-5 = highly suspicious
	 */
	
	public FileChecker.SUSPICIOUS isSuspicious(File pdfFile) {
		String content = MFile.readFile(pdfFile);
		int score = isSuspicious(content);
		if (score == 0) return FileChecker.SUSPICIOUS.NO;
		if (score == 1) return FileChecker.SUSPICIOUS.MAYBE;
		return FileChecker.SUSPICIOUS.YES;
	}

	/**
	 * checks for suspicious contents
	 * @param content
	 * @return 0 = not suspicious, 1 = little suspicious, 2-3 = suspicious, 4-5 = highly suspicious
	 */
	public int isSuspicious(String content) {
		String[] lines = MString.split(content, "\n");
		return isSuspicious(lines);
	}

	/**
	 * checks for suspicious contents
	 * @param lines
	 * @return 0 = not suspicious, 1 = little suspicious, 2-3 = suspicious, 4-5 = highly suspicious
	 */
	public int isSuspicious(String[] lines) {
		
		int score = 0;
		int jsCnt = 0;
		int javaScriptCnt = 0;
		int aaCnt = 0;
		int openActionCnt = 0;
		int pageCnt = 0;
		/*
		 * possible tags:
		 * obj
		 * endobj
		 * stream
		 * endstream
		 * xref
		 * trailer
		 * startxref
		 * /Page
		 * /Encrypt
		 * /ObjStm
		 * /JS
		 * /JavaScript
		 * /AA
		 * /OpenAction
		 * /JBIG2Decode
		 * /RichMedia
		 * /Launch
		 * /XFA
		 * 
		 * suspicious tags:
		 * /JS and /JavaScript (contain scripts)
		 * /AA and /OpenAction (auto execution of scripts at startup is very suspicious)
		 * /Page BUT NOT /Pages (if there's only 1 page, only in combination with other tags)
		 * /ObjStm (can contain/obfuscate other objects)
		 */
		for (String line : lines) {
			// only evaluate type descriptors
			if (!line.startsWith("<<"))
				continue;
			
			if (line.contains("/JS")) {
				jsCnt++;
			}
			if (line.contains("/JavaScript")) {
				javaScriptCnt++;
			}
			if (line.contains("/AA")) {
				aaCnt++;
			}
			if (line.contains("/OpenAction")) {
				openActionCnt++;
			}
			if (line.contains("/Page") && !line.contains("/Pages")) {
				pageCnt++;
			}
		}
		
		if (aaCnt > 0 || openActionCnt > 0) {
			if (score < 3) score += 2;
			else if (score < 5) score++;
		}
		if (jsCnt > 0 || javaScriptCnt > 0) {
			if (score < 3) score += 2;
			else if (score < 5) score++;
		}
		if (pageCnt == 1) {
			if (score > 0 && score < 5) score++;
		}
		
		return score;
	}
	
}
