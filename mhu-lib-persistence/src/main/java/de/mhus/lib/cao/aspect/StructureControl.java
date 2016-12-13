package de.mhus.lib.cao.aspect;

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
	CaoNode createChildNode(IProperties properties);
	boolean createRendition(String name, InputStream data);
	CaoNode copyTo(CaoNode parent, boolean recursive);
}
