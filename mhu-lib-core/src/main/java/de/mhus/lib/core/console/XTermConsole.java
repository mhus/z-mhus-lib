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
package de.mhus.lib.core.console;

import java.io.IOException;

import de.mhus.lib.core.MCast;
import de.mhus.lib.core.MString;
import de.mhus.lib.core.MSystem;

public class XTermConsole extends ANSIConsole {

	private long lastUpdate;

	public XTermConsole() {
		super();
	}

	public XTermConsole(boolean supportColor) {
		super(supportColor);
	}

	@Override
	public void resetTerminal() {
		try {
//			MSystem.execute("/bin/sh","-c","clear < /dev/tty");
			Runtime.getRuntime().exec("clear");
		} catch (IOException e) {
		}
	}

	/*
speed 9600 baud; 62 rows; 238 columns;
lflags: icanon isig iexten echo echoe -echok echoke -echonl echoctl
	-echoprt -altwerase -noflsh -tostop -flusho pendin -nokerninfo
	-extproc
iflags: -istrip icrnl -inlcr -igncr ixon -ixoff ixany imaxbel iutf8
	-ignbrk brkint -inpck -ignpar -parmrk
oflags: opost onlcr -oxtabs -onocr -onlret
cflags: cread cs8 -parenb -parodd hupcl -clocal -cstopb -crtscts -dsrflow
	-dtrflow -mdmbuf
cchars: discard = ^O; dsusp = ^Y; eof = ^D; eol = <undef>;
	eol2 = <undef>; erase = ^?; intr = ^C; kill = ^U; lnext = ^V;
	min = 1; quit = ^\; reprint = ^R; start = ^Q; status = ^T;
	stop = ^S; susp = ^Z; time = 0; werase = ^W;
	
	
speed 9600 baud; rows 60; columns 238; line = 0;
intr = ^C; quit = ^\; erase = ^?; kill = ^U; eof = ^D; eol = M-^?; eol2 = M-^?; swtch = <undef>; start = ^Q; stop = ^S; susp = ^Z; rprnt = ^R; werase = ^W; lnext = ^V; flush = ^O; min = 1; time = 0;
-parenb -parodd -cmspar cs8 -hupcl -cstopb cread -clocal -crtscts
-ignbrk -brkint -ignpar -parmrk -inpck -istrip -inlcr -igncr icrnl ixon -ixoff -iuclc ixany imaxbel -iutf8
opost -olcuc -ocrnl onlcr -onocr -onlret -ofill -ofdel nl0 cr0 tab0 bs0 vt0 ff0
isig icanon iexten echo echoe -echok -echonl -noflsh -xcase -tostop -echoprt echoctl echoke
	
	 */
	
//	public String[] getRawSettings() throws IOException {
//		String[] ret = MSystem.execute("/bin/sh","-c","stty -a < /dev/tty");
//		return ret;
//	}
	
//	@Override
//	public void loadSettings() {
//		
//		try {
//			String[] ret = getRawSettings();
//			String[] parts = ret[0].split("\n");
//			if (parts.length > 0) {
//				String[] parts2 = parts[0].split(";");
//				for (String p : parts2) {
//					p = p.trim();
//					if (p.endsWith(" rows"))
//						height = MCast.toint(MString.beforeIndex(p, ' '), DEFAULT_HEIGHT);
//					else
//					if (p.endsWith(" columns"))
//						width = MCast.toint(MString.beforeIndex(p, ' '), DEFAULT_WIDTH);
//					else
//					if (p.startsWith("rows "))
//						height = MCast.toint(MString.afterIndex(p, ' '), DEFAULT_HEIGHT);
//					else
//					if (p.startsWith("columns "))
//						width = MCast.toint(MString.afterIndex(p, ' '), DEFAULT_WIDTH);
//				}
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
	
	/*
/bin/sh -c "echo $COLUMNS $LINES $TERM"
238 29 xterm-color
	 */
	public String[] getRawSettings() throws IOException {
		String[] ret = MSystem.execute("/bin/sh","-c","echo $COLUMNS $LINES $TERM");
		return ret;
	}
	
	@Override
	public void loadSettings() {
		
//		try {
//			String[] ret = getRawSettings();
//			String[] parts = ret[0].split(" ");
//			width = MCast.toint(parts[0], DEFAULT_WIDTH);
//			height = MCast.toint(parts[1], DEFAULT_HEIGHT);
		updateSize();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}

	@Override
	public int getWidth() {
		updateSize();
		return width;
	}

	private void updateSize() {
		if (System.currentTimeMillis() - lastUpdate < 10000) return;
		lastUpdate = System.currentTimeMillis();
		try {
			String w = MSystem.execute("tput","cols")[0];
			System.out.println("["+w+"]");
			if (w.equals("80")) {
				for (String part : getRawSettings()[0].split("\\;")) {
					part = part.toLowerCase().trim();
					if (part.endsWith(" columns")) {
						width = MCast.toint(MString.beforeIndex(part, ' '), DEFAULT_WIDTH);
						break;
					} else
					if (part.startsWith("columns ")) {
						width = MCast.toint(MString.afterIndex(part, ' '), DEFAULT_WIDTH);
						break;
					}
				}
			} else
				width = MCast.toint(w, DEFAULT_WIDTH);
		} catch (IOException e) {
			width = DEFAULT_WIDTH;
		}
		try {
			String h = MSystem.execute("tput","lines")[0];
			System.out.println("["+h+"]");
			if ( h.equals("40")) {
				for (String part : getRawSettings()[0].split("\\;")) {
					part = part.toLowerCase().trim();
					if (part.endsWith(" rows")) {
						height = MCast.toint(MString.beforeIndex(part, ' '), DEFAULT_HEIGHT);
						break;
					} else
					if (part.startsWith("rows ")) {
						height = MCast.toint(MString.afterIndex(part, ' '), DEFAULT_HEIGHT);
						break;
					}
				}
			} else
				height = MCast.toint(h, DEFAULT_HEIGHT);
		} catch (IOException e) {
			height = DEFAULT_HEIGHT;
		}
	}

	@Override
	public int getHeight() {
		updateSize();
		return height;
	}

}
