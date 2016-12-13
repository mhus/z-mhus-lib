package de.mhus.lib.cao.util;

import java.io.InputStream;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import de.mhus.lib.cao.CaoAspectFactory;
import de.mhus.lib.cao.CaoCore;
import de.mhus.lib.cao.CaoNode;
import de.mhus.lib.cao.aspect.StructureControl;
import de.mhus.lib.core.IProperties;
import de.mhus.lib.errors.MException;

public class DefaultStructureControl implements CaoAspectFactory<StructureControl> {

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
				if (n.getId().equals(node.getId())) return n;
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

		private List<WritableNode> loadAll() {
			LinkedList<WritableNode> out = new LinkedList<WritableNode>();
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
				if (n.getId().equals(node.getId())) {
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
				if (n.getId().equals(node.getId())) last = n;
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
			for (WritableNode n : all) {
				Object pos = sortAttribute.getPosition(n);
				if (n.getId().equals(node.getId())) {
					if (firstPos != null)
						sortAttribute.setPosition(n, firstPos);
					break;
				}
				if (firstPos == null)
					firstPos = pos;
				if (last != null)
					sortAttribute.setPosition(last, pos);
				last = n;
			}
			return false;
		}

		@Override
		public boolean moveToBottom() {
			List<WritableNode> all = loadAll();
			repairAll(all);
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
			sortAttribute.setPosition(node, max);
			fillGaps(all);
			return saveAll(all);
		}

		@Override
		public boolean moveAfter(CaoNode predecessor) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public int getPositionIndex() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public boolean isAtTop() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean isAtBottom() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean moveTo(CaoNode parent) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean delete(boolean recursive) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public CaoNode createChildNode(IProperties properties) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean createRendition(String name, InputStream data) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public CaoNode copyTo(CaoNode parent, boolean recursive) {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
	
	public static interface SortAttribute extends Comparator<CaoNode> {
		Object getPosition(CaoNode node);
		Object max(Object max, Object pos);
		Object inc(Object pos);
		void setPosition(CaoNode node, Object pos);
		boolean equals(Object p1, Object p2);
	}
}
