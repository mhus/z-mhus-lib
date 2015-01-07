package de.mhus.lib.core.strategy;

public abstract class AbstractOperation implements Operation {

	private Object owner;

	@Override
	public boolean isBusy() {
		synchronized (this) {
			return owner != null;
		}
	}

	@Override
	public boolean setBusy(Object owner) {
		synchronized (this) {
			if (this.owner != null) return false;
			this.owner = owner;
		}
		return true;
	}

	@Override
	public boolean releaseBusy(Object owner) {
		synchronized (this) {
			if (this.owner == null) return true;
//			if (!this.owner.equals(owner)) return false;
			if (this.owner != owner) return false;
			this.owner = null;
		}
		return true;
	}

}
