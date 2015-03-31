package de.mhus.lib.test;

public class MJsonObject {

		public String string;

		public MJsonObject() {
			
		}
		public MJsonObject(String string) {
			this.string = string;
		}
		
		@Override
		public String toString() {
			return string;
		}

}
