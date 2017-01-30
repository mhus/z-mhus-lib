package de.mhus.lib.cao.util;

import java.util.LinkedList;

import de.mhus.lib.cao.CaoAspectFactory;
import de.mhus.lib.cao.CaoCore;
import de.mhus.lib.cao.CaoNode;
import de.mhus.lib.cao.aspect.Changes;
import de.mhus.lib.core.MLog;

public class DefaultChangesQueue  extends MLog implements CaoAspectFactory<Changes> {

	public enum EVENT {DELETED,CREATED, MODIFIED, MOVED, UNLINK, LINK, BIG_CHANGE}

	private static final int MAX_QUEUE_SIZE = 30;
	
	private LinkedList<Change> queue = new LinkedList<>();

	private String name;
	
	@Override
	public Changes getAspectFor(CaoNode node) {
		return new Aspect(node);
	}

	@Override
	public void doInitialize(CaoCore core, MutableActionList actionList) {
		name = core.getName();
	}

	public Change[] clearEventQueue() {
		synchronized (queue) {
			Change[] out = queue.toArray(new Change[queue.size()]);
			queue.clear();
			return out;
		}
	}
	
	public int getEventQueueSize() {
		return queue.size();
	}
	
	public class Change {

		public EVENT event;
		private String node;
		private String parent;
		
		public Change(EVENT event, String node, String parent) {
			this.event = event;
			this.node = node;
			this.parent = parent;
		}
		
		public EVENT getEvent() {
			return event;
		}

		public String getNode() {
			return node;
		}

		public String getParent() {
			return parent;
		}
		
	}
	
	private class Aspect implements Changes {

		private CaoNode node;

		public Aspect(CaoNode node) {
			this.node = node;
		}

		@Override
		public void deleted() {
			addEvent(EVENT.DELETED, node, node.getParent());
		}

		@Override
		public void modified() {
			addEvent(EVENT.MODIFIED, node, node.getParent());
		}

		@Override
		public void created() {
			addEvent(EVENT.CREATED, node, node.getParent());
		}

		@Override
		public void moved() {
			addEvent(EVENT.MOVED, node, node.getParent());
		}

		@Override
		public void movedFrom(CaoNode oldParent) {
			addEvent(EVENT.UNLINK, node, oldParent);
			addEvent(EVENT.LINK, node, node.getParent());
		}
		
	}

	public void addEvent(EVENT event, CaoNode node, CaoNode parent) {
		synchronized (queue) {
			if (queue.size() == 1 && queue.get(0).event == EVENT.BIG_CHANGE) {
				log().t(name,"Queue is already full");
				return;
			}
			if (queue.size() > MAX_QUEUE_SIZE) {
				queue.clear();
				queue.add(new Change(EVENT.BIG_CHANGE,null,null));
				log().d(name,"Event queue is full");
				return;
			}
			log().t(name, event,node,parent);
			queue.add(new Change(event, node.getId(), parent.getId()));
		}
	}
	
}
