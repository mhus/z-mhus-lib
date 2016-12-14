package de.mhus.lib.cao.aspect;

import java.io.File;
import java.io.InputStream;

import de.mhus.lib.cao.CaoAspect;
import de.mhus.lib.cao.CaoNode;
import de.mhus.lib.core.IProperties;

public interface StructureControl extends CaoAspect {
	boolean moveUp();
	boolean moveDown();
	boolean moveToTop();
	boolean moveToBottom();
	boolean moveAfter(CaoNode predecessor);
	int getPositionIndex();
	boolean isAtTop();
	boolean isAtBottom();
	boolean moveTo(CaoNode parent);
	boolean delete(boolean recursive);
	CaoNode createChildNode(String name, IProperties properties);
	boolean uploadRendition(String name, File file);
	CaoNode copyTo(CaoNode parent, boolean recursive);
	boolean rename(String name);
}
