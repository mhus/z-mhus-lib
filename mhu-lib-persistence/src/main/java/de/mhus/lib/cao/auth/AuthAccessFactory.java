package de.mhus.lib.cao.auth;

import de.mhus.lib.cao.CaoAction;
import de.mhus.lib.cao.CaoAspect;
import de.mhus.lib.cao.CaoAspectFactory;
import de.mhus.lib.cao.CaoConnection;
import de.mhus.lib.cao.CaoNode;
import de.mhus.lib.cao.util.MutableActionList;

public class AuthAccessFactory implements CaoAspectFactory<AuthAccess> {

	private AuthConnection con;

	public AuthAccessFactory(AuthConnection con) {
		this.con = con;
	}

	@Override
	public AuthAccess getAspectFor(CaoNode node) {
		AuthNode n = (AuthNode)node;
		synchronized (n) {
			if (n.access != null) return n.access;
			n.access = new Access(con,node);
			return n.access;
		}
	}

	private static class Access implements AuthAccess {

		private AuthConnection con;
		private CaoNode node;

		public Access(AuthConnection con, CaoNode node) {
			this.con = con;
			this.node = node;
		}

		@Override
		public boolean hasReadAccess() {
			return con.hasReadAccess(node);
		}

		@Override
		public boolean hasWriteAccess() {
			return con.hasWriteAccess(node);
		}

		@Override
		public boolean hasContentAccess(String rendition) {
			return con.hasContentAccess(node, rendition);
		}

		@Override
		public boolean hasAspectAccess(Class<? extends CaoAspect> ifc) {
			return con.hasAspectAccess(node, ifc);
		}


	}

	@Override
	public void doInitialize(CaoConnection caoConnection, MutableActionList actionList) {
	}
	
}
