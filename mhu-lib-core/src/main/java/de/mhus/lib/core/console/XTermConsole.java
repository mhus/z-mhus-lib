package de.mhus.lib.core.console;

import java.io.IOException;

import de.mhus.lib.core.MCast;
import de.mhus.lib.core.MString;
import de.mhus.lib.core.MSystem;

public class XTermConsole extends ANSIConsole {

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
	 */
	public void loadSettings() {
		
		try {
			String[] ret = MSystem.execute("/bin/sh","-c","stty -a < /dev/tty");
			String[] parts = ret[0].split("\n");
			if (parts.length > 0) {
				String[] parts2 = parts[0].split(";");
				for (String p : parts2) {
					if (p.endsWith(" rows"))
						height = MCast.toint(MString.beforeIndex(p.trim(), ' '), DEFAULT_HEIGHT);
					else
					if (p.endsWith(" columns"))
						width = MCast.toint(MString.beforeIndex(p.trim(), ' '), DEFAULT_WIDTH);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
