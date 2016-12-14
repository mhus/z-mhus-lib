package de.mhus.lib.cao.util;

import java.io.File;
import java.io.InputStream;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import de.mhus.lib.cao.CaoAction;
import de.mhus.lib.cao.CaoAspectFactory;
import de.mhus.lib.cao.CaoCore;
import de.mhus.lib.cao.CaoNode;
import de.mhus.lib.cao.action.CaoConfiguration;
import de.mhus.lib.cao.action.CopyConfiguration;
import de.mhus.lib.cao.action.CreateConfiguration;
import de.mhus.lib.cao.action.DeleteConfiguration;
import de.mhus.lib.cao.action.MoveConfiguration;
import de.mhus.lib.cao.action.RenameConfiguration;
import de.mhus.lib.cao.action.UploadRenditionConfiguration;
import de.mhus.lib.cao.aspect.StructureControl;
import de.mhus.lib.core.IProperties;
import de.mhus.lib.core.MLog;
import de.mhus.lib.core.MSystem;
import de.mhus.lib.core.strategy.OperationResult;
import de.mhus.lib.errors.MException;

public class DefaultStructureControl extends MLog implements CaoAspectFactory<StructureControl> {

	private SortAttribute sortAttribute;

	public DefaultStructureControl(SortAttribute sortAttribute) {
		this.sortAttribute = sortAttribute;
	}
	
	@Override
	public StructureControl getAspectFor(CaoNode node) {
		return new Aspect(node);
	}

	@Override
	public void doInitialize(CaoCore core, MutableActionList actionList) {
		
	}

	private class Aspect implements StructureControl {

		private CaoNode node;

		public Aspect(CaoNode node) {
			this.node = node;
		}

		private WritableNode findMyself(List<WritableNode> all) {
			for (WritableNode n : all)
				if (n.equals(node)) return n;
			return null;
		}

		private void sortAll(List<WritableNode> all) {
			all.sort(sortAttribute);
		}
		
		private void repairAll(List<WritableNode> all) {
			// repair order of the nodes - do not remove gaps
			HashSet<Object> index = new HashSet<>();
			LinkedList<WritableNode> rest = new LinkedList<>();
			Object max = null;
			for (WritableNode n :all) {
				Object pos = sortAttribute.getPosition(n);
				if (pos == null) {
					rest.add(n);
				} else {
					if (index.contains(pos)) {
						while (index.contains(pos)) pos = sortAttribute.inc(pos);
						sortAttribute.setPosition(n, pos);
					}
					index.add(pos);
					max = sortAttribute.max(max,pos);
				}
			}
			for (WritableNode n : rest) {
				max = sortAttribute.inc(max);
				sortAttribute.setPosition(n, max);
			}
		}

		private boolean saveAll(List<WritableNode> all) {
			boolean changed = false;
			for (WritableNode n : all)
				if (n.isChanged()) {
					try {
						n.save();
						changed = true;
					} catch (MException e) {
					}
				}
			return changed;
		}

		private LinkedList<WritableNode> loadAll() {
			
			LinkedList<WritableNode> out = sortAttribute.loadAll(node);
			if (out != null) return out;
			
			out = new LinkedList<WritableNode>();
			for (CaoNode n : node.getParent().getNodes())
				out.add(new WritableNode(n));
			return out;
		}

		private void fillGaps(List<WritableNode> all) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public boolean moveUp() {
			// switch place with note before
			List<WritableNode> all = loadAll();
			repairAll(all);
			sortAll(all);
			WritableNode last = null;
			for (WritableNode n : all) {
				if (n.equals(node)) {
					if (last != null) {
						// switch pos
						Object lastPos = sortAttribute.getPosition(last);
						Object myPos = sortAttribute.getPosition(n);
						sortAttribute.setPosition(last, myPos);
						sortAttribute.setPosition(n, lastPos);
					}
					break;
				}
				last = n;
			}
			
			return saveAll(all);
		}
		
		@Override
		public boolean moveDown() {
			// switch place with next node
			List<WritableNode> all = loadAll();
			repairAll(all);
			sortAll(all);
			WritableNode last = null;
			for (WritableNode n : all) {
				if (last != null) {
					// switch pos
					Object lastPos = sortAttribute.getPosition(last);
					Object myPos = sortAttribute.getPosition(n);
					sortAttribute.setPosition(last, myPos);
					sortAttribute.setPosition(n, lastPos);
					break;
				}
				if (n.equals(node)) last = n;
			}
			return saveAll(all);
		}

		@Override
		public boolean moveToTop() {
			// move all one down up to me
			List<WritableNode> all = loadAll();
			repairAll(all);
			sortAll(all);
			Object firstPos = null;
			WritableNode last = null;
			WritableNode me = null;
			Object max = null;
			for (WritableNode n : all) {
				Object pos = sortAttribute.getPosition(n);
				max = pos;
				if (n.equals(node)) {
					me = n;
					continue;
				}
				if (firstPos == null)
					firstPos = pos;
				if (last != null)
					sortAttribute.setPosition(last, pos);
				last = n;
			}
			max = sortAttribute.inc(max);
			sortAttribute.setPosition(last, max);
			
			if (firstPos != null)
				sortAttribute.setPosition(me, firstPos);

			return saveAll(all);
		}

		@Override
		public boolean moveToBottom() {
			List<WritableNode> all = loadAll();
			repairAll(all);
			sortAll(all);
			Object max = null;
			for (WritableNode n :all) {
				Object pos = sortAttribute.getPosition(n);
				max = sortAttribute.max(max, pos);
			}
			if (max == null) return false;
			WritableNode me = findMyself(all);
			Object pos = sortAttribute.getPosition(me);
			if (sortAttribute.equals(max, pos)) return false;
			max = sortAttribute.inc(max);
			sortAttribute.setPosition(me, max);
			fillGaps(all);
			return saveAll(all);
		}

		@Override
		public boolean moveAfter(CaoNode predecessor) {
			List<WritableNode> all = loadAll();
			repairAll(all);
			sortAll(all);
			boolean found = false;
			WritableNode me = null;
			Object myPos = null;
			Object lastPos = null;
			for (WritableNode n :all) {

				if (n.equals(node)) {
					me = n;
					continue;
				}
				
				if (found) {
					Object pos = sortAttribute.getPosition(n);
					if (lastPos == null) {
						myPos = pos;
					} else {
						sortAttribute.setPosition(n, lastPos);
					}
					lastPos = pos;
				} else
				if (n.equals(predecessor)) {
					found = true;
				}
			}
			if (me != null && myPos != null)
				sortAttribute.setPosition(me, myPos);
			
			return saveAll(all);
		}

		@Override
		public int getPositionIndex() {
			List<WritableNode> all = loadAll();
			repairAll(all);
			sortAll(all);
			int cnt = 0;
			for (WritableNode n :all) {
				if (n.equals(node)) return cnt;
				cnt++;
			}
			return -1;
		}

		@Override
		public boolean isAtTop() {
			LinkedList<WritableNode> all = loadAll();
			if (all.size() == 0) return false;
			repairAll(all);
			sortAll(all);
			return all.getFirst().equals(node);
		}

		@Override
		public boolean isAtBottom() {
			LinkedList<WritableNode> all = loadAll();
			if (all.size() == 0) return false;
			repairAll(all);
			sortAll(all);
			return all.getLast().equals(node);
		}

		@Override
		public boolean moveTo(CaoNode parent) {
			try {
				CaoAction action = node.getConnection().getAction(CaoAction.MOVE);
				if (action == null) return false;
				MoveConfiguration configuration = (MoveConfiguration) action.createConfiguration(node, null);
				configuration.setNewParent(parent);
				OperationResult ret = action.doExecute(configuration, null);
				log().d("result",ret);
				return ret != null && ret.isSuccessful();
			} catch (Exception e) {
				log().e("moveTo",node,parent,e);
			}
			return false;
		}

		@Override
		public boolean delete(boolean recursive) {
			try {
				CaoAction action = node.getConnection().getAction(CaoAction.DELETE);
				if (action == null) return false;
				DeleteConfiguration configuration = (DeleteConfiguration) action.createConfiguration(node, null);
				configuration.setRecursive(recursive);
				OperationResult ret = action.doExecute(configuration, null);
				return ret != null && ret.isSuccessful();
			} catch (Exception e) {
				log().e("delete",node,recursive,e);
			}
			return false;
		}

		@Override
		public CaoNode createChildNode(String name, IProperties properties) {
			try {
				CaoAction action = node.getConnection().getAction(CaoAction.CREATE);
				if (action == null) return null;
				CreateConfiguration configuration = (CreateConfiguration) action.createConfiguration(node, null);
				configuration.setName(name);
				configuration.getProperties().putAll(properties);
				OperationResult ret = action.doExecute(configuration, null);
				if (ret != null && ret.isSuccessful())
					return ret.getResultAs();
			} catch (Exception e) {
				log().e("createChildNode",node,name,e);
			}
			return null;
		}

		@Override
		public boolean uploadRendition(String name, File file) {
			try {
				CaoAction action = node.getConnection().getAction(CaoAction.UPLOAD_RENDITION);
				if (action == null) return false;
				UploadRenditionConfiguration configuration = (UploadRenditionConfiguration) action.createConfiguration(node, null);
				configuration.setRendition(name);
				configuration.setFile(file);
				OperationResult ret = action.doExecute(configuration, null);
				return ret != null && ret.isSuccessful();
			} catch (Exception e) {
				log().e("uploadRendition",node,name,e);
			}
			return false;
		}

		@Override
		public CaoNode copyTo(CaoNode parent, boolean recursive) {
			try {
				CaoAction action = node.getConnection().getAction(CaoAction.COPY);
				if (action == null) return null;
				CopyConfiguration configuration = (CopyConfiguration) action.createConfiguration(node, null);
				configuration.setNewParent(parent);
				configuration.setRecursive(recursive);
				OperationResult ret = action.doExecute(configuration, null);
				if (ret != null && ret.isSuccessful())
					return ret.getResultAs();
			} catch (Exception e) {
				log().e("copyTo",node,recursive,e);
			}
			return null;
		}

		@Override
		public boolean rename(String name) {
			try {
				CaoAction action = node.getConnection().getAction(CaoAction.RENAME);
				if (action == null) return false;
				RenameConfiguration configuration = (RenameConfiguration) action.createConfiguration(node, null);
				configuration.setName(name);
				OperationResult ret = action.doExecute(configuration, null);
				return ret != null && ret.isSuccessful();
			} catch (Exception e) {
				log().e("rename",node,name,e);
			}
			return false;
		}
		
	}
	
	public static interface SortAttribute<S> extends Comparator<CaoNode> {
		S getPosition(CaoNode node);
		LinkedList<WritableNode> loadAll(CaoNode node);
		S max(S max, S pos);
		S inc(S pos);
		void setPosition(CaoNode node, S pos);
		boolean equals(S p1, S p2);
	}
	
	public static class DefaultSortAttribute implements SortAttribute<Integer> {

		private String attributeName;

		public DefaultSortAttribute(String attributeName) {
			this.attributeName = attributeName;
		}
		
		@Override
		public int compare(CaoNode o1, CaoNode o2) {
			Integer p1 = getPosition(o1);
			Integer p2 = getPosition(o2);
			
			return comparePositions(p1, p2);
		}

		private int comparePositions(Integer p1, Integer p2) {
			if (p1 == null) return -1;
			return p1.compareTo(p2);
		}

		@Override
		public Integer getPosition(CaoNode node) {
			int ret = node.getInt(attributeName, -1);
			if (ret < 0) return null;
			return ret;
		}

		@Override
		public LinkedList<WritableNode> loadAll(CaoNode node) {
			return null;
		}

		@Override
		public Integer max(Integer a, Integer b) {
			if (a == null && b == null) return 0;
			if (a == null) return b;
			if (b == null) return a;
			return Math.max(a, b);
		}

		@Override
		public Integer inc(Integer pos) {
			if (pos == null) return 0;
			return pos+1;
		}

		@Override
		public void setPosition(CaoNode node, Integer pos) {
			if (pos == null)
				node.remove(attributeName);
			else
				node.setInt(attributeName, pos);
		}

		@Override
		public boolean equals(Integer p1, Integer p2) {
			return MSystem.equals(p1, p2);
		}
		
	}
	
}
