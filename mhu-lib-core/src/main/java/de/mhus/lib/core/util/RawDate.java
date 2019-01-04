/**
 * Copyright 2018 Mike Hummel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.mhus.lib.core.util;

public class RawDate {

	private long time;

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}
	
	public int getMillies() {
		return (int)(time % 1000);
	}
	
	public int getSecond() {
		return (int)(time / 1000 % 60);
	}

	public int getMinute() {
		return (int)(time / 1000 / 60 % 60);
	}

	public int getHour() {
		return (int)(time / 1000 / 60 / 60 % 24);
	}

	public long getDaysSince1970() {
		return time / 1000 / 60 / 60 / 24;
	}

}
