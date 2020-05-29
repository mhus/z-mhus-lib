/**
 * Copyright 2018 Mike Hummel
 *
 * <p>Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 * <p>http://www.apache.org/licenses/LICENSE-2.0
 *
 * <p>Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.mhus.lib.core.console;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.LinkedList;

import org.jline.reader.LineReader;
import org.jline.reader.impl.LineReaderImpl;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import de.mhus.lib.core.MString;
import de.mhus.lib.core.logging.MLogUtil;

// http://ascii-table.com/ansi-escape-sequences.php

public class ANSIConsole extends Console {

    public static final int KEY_SPECIAL_LEFT = 68;
    public static final int KEY_SPECIAL_UP = 65;
    public static final int KEY_SPECIAL_DOWN = 66;
    public static final int KEY_SPECIAL_RIGHT = 67;
    public static final int KEY_ENTER = 13;

    public static final int KEY_q = 113;
    public static final int KEY_a = 97;
    public static final int KEY_z = 112;
    public static final int KEY_A = 65;
    public static final int KEY_Z = 90;
    public static final int KEY_0 = 48;
    public static final int KEY_9 = 57;

    public static final int KEY_TAB = 9;
    public static final int KEY_ESC = 27;
    public static final int KEY_CTRL_A = 1;
    public static final int KEY_CTRL_Z = 26;
    public static final int KEY_FUNCTION = 79;
    public static final int KEY_F1 = 80;
    public static final int KEY_F2 = 81;
    public static final int KEY_F3 = 82;
    public static final int KEY_F4 = 83;
    //	public static final int KEY_F5=84;
    //	public static final int KEY_F6=85;
    //	public static final int KEY_F7=86;
    //	public static final int KEY_F8=87;
    //	public static final int KEY_F9=88;
    //	public static final int KEY_F10=89;
    //	public static final int KEY_F11=90;

    protected COLOR foreground;
    protected COLOR background;
    protected boolean blink;
    protected boolean bold;
    //	protected int width = DEFAULT_WIDTH;
    //	protected int height = DEFAULT_HEIGHT;
    protected LineReaderImpl reader;

    protected int width = 0;
    protected int height = 0;
    protected boolean supportSize;
    private String term;

    public ANSIConsole() throws IOException {
        this(new LineReaderImpl(TerminalBuilder.builder().build()));
    }

    public ANSIConsole(LineReaderImpl reader) throws IOException {
        super();
        this.reader = reader;
        loadSettings();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T adaptTo(Class<? extends T> clazz) {
        if (clazz == LineReader.class || clazz == LineReaderImpl.class) return (T) reader;
        return super.adaptTo(clazz);
    }

    protected void loadSettings() {
        term = System.getenv("TERM");
        int w = reader.getTerminal().getWidth();
        int h = reader.getTerminal().getHeight();
        if (w == 80 && h == 24) { // default size if size can't be recognized
            width = DEFAULT_WIDTH;
            height = DEFAULT_HEIGHT;
            supportSize = false;
        } else {
            width = 0;
            height = 0;
            supportSize = true;
        }
    }

    public ANSIConsole(InputStream in, PrintStream out, boolean flush, String charset)
            throws IOException {
        super(out, flush, charset);
        reader = new LineReaderImpl(TerminalBuilder.builder().streams(in, out).build());
        loadSettings();
    }

    public ANSIConsole(InputStream in, PrintStream out) throws IOException {
        super(out);
        reader = new LineReaderImpl(TerminalBuilder.builder().streams(in, out).build());
        loadSettings();
    }

    @Override
    public String readPassword() throws IOException {
        return reader.readLine('*');
    }

    @Override
    public String readLine(LinkedList<String> history) {
        try {
        	reader.getHistory().purge();
        	if (history != null)
        		history.forEach(i -> reader.getHistory().add(i));
            String ret = reader.readLine();
            if (history != null && MString.isSetTrim(ret) && !ret.startsWith(" ")) {
            	if (history.size() == 0 || !history.getLast().equals(ret))
            		history.add(ret);
            }
            return ret;
        } catch (Exception e) {
            MLogUtil.log().t(e);
        }
        return null;
        //		return System.console().readLine();
    }

    @Override
    public int read() {
        try {
            return reader.readCharacter();
        } catch (Exception e) {
            MLogUtil.log().t(e);
        }
        return -1;
    }

    @Override
    public ConsoleKey readKey() {
        while (true) {
            int first = read();
            if (first == 1091) {
                int second = read();
                //				if (second == )

                switch (second) {
                    case KEY_SPECIAL_DOWN:
                    case KEY_SPECIAL_LEFT:
                    case KEY_SPECIAL_RIGHT:
                    case KEY_SPECIAL_UP:
                        return new ConsoleKey((byte) 0, true, (char) second);
                    case KEY_F1:
                    case KEY_F2:
                    case KEY_F3:
                    case KEY_F4:
                        return new ConsoleKey((byte) 0, true, (char) (second - 79));
                    default:
                }
                continue;
            }
            return new ConsoleKey((byte) 0, false, (char) first);
        }
    }

    @Override
    public boolean isSupportSize() {
        return supportSize;
    }

    @Override
    public int getWidth() {
        if (width > 0) return width;
        return reader.getTerminal().getWidth();
    }

    @Override
    public int getHeight() {
        if (height > 0) return height;
        return reader.getTerminal().getHeight();
    }

    @Override
    public boolean isSupportCursor() {
        return true;
    }

    @Override
    public void setCursor(int x, int y) {
        print(ansiSetCursor(x, y));
    }

    public static String ansiSetCursor(int x, int y) {
        return (char) 27 + "[" + y + ";" + x + "H";
    }

    @Override
    public int getCursorX() {
        return -1;
    }

    @Override
    public int getCursorY() {
        return -1;
    }

    @Override
    public boolean isSupportColor() {
        // method not found return reader.getTerminal().getAnsiSupport();
        boolean ret = reader.getTerminal().getType().equals("ansi");
        if (ret) return true;
        return term.contains("color");
    }

    @Override
    public void setColor(COLOR foreground, COLOR background) {
        this.foreground = foreground;
        this.background = background;

        if (isSupportColor()) {
            if (foreground != null && foreground != COLOR.UNKNOWN)
                print(ansiForeground(foreground));
            if (background != null && background != COLOR.UNKNOWN)
                print(ansiBackground(background));
        }
    }

    public static String ansiForeground(COLOR color) {
        return (char) 27 + "[" + ansiFGColorValue(color) + "m";
    }

    public static String ansiBackground(COLOR color) {
        return (char) 27 + "[" + ansiBGColorValue(color) + "m";
    }

    @Override
    public COLOR getForegroundColor() {
        return foreground;
    }

    @Override
    public COLOR getBackgroundColor() {
        return background;
    }

    @Override
    public boolean isSupportBlink() {
        return isSupportColor();
    }

    @Override
    public void setBlink(boolean blink) {
        this.blink = blink;
        updateAttributes();
    }

    private void updateAttributes() {
        if (isSupportColor()) print(ansiAttributes(blink, bold));
    }

    public static String ansiAttributes(boolean blink, boolean bold) {
        return (char) 27 + "[0" + (blink ? ";5" : "") + (bold ? ";1" : "") + "m";
    }

    @Override
    public boolean isBlink() {
        return blink;
    }

    @Override
    public boolean isSupportBold() {
        return isSupportColor();
    }

    @Override
    public void setBold(boolean bold) {
        this.bold = bold;
        updateAttributes();
    }

    @Override
    public boolean isBold() {
        return bold;
    }

    public static String ansiFGColorValue(COLOR col) {
        switch (col) {
            case BLACK:
                return "30";
            case BLUE:
                return "34";
            case GREEN:
                return "32";
            case RED:
                return "31";
            case WHITE:
                return "37";
            case YELLOW:
                return "33";
            case CYAN:
                return "36";
            case MAGENTA:
                return "35";
                
            case BRIGHT_BLACK:
                return "90";
            case BRIGHT_BLUE:
                return "94";
            case BRIGHT_GREEN:
                return "92";
            case BRIGHT_RED:
                return "91";
            case BRIGHT_WHITE:
                return "97";
            case BRIGHT_YELLOW:
                return "93";
            case BRIGHT_CYAN:
                return "96";
            case BRIGHT_MAGENTA:
                return "95";
                
            default:
                return "37";
        }
    }
    
    public static String ansiBGColorValue(COLOR col) {
        switch (col) {
            case BLACK:
                return "40";
            case BLUE:
                return "44";
            case GREEN:
                return "42";
            case RED:
                return "41";
            case WHITE:
                return "47";
            case YELLOW:
                return "43";
            case CYAN:
                return "46";
            case MAGENTA:
                return "45";

            case BRIGHT_BLACK:
                return "100";
            case BRIGHT_BLUE:
                return "104";
            case BRIGHT_GREEN:
                return "102";
            case BRIGHT_RED:
                return "101";
            case BRIGHT_WHITE:
                return "107";
            case BRIGHT_YELLOW:
                return "103";
            case BRIGHT_CYAN:
                return "106";
            case BRIGHT_MAGENTA:
                return "105";
                
            default:
                return "47";
        }
    }
    

    @Override
    public void cleanup() {
        bold = false;
        blink = false;
        foreground = COLOR.UNKNOWN;
        background = COLOR.UNKNOWN;
        print(ansiCleanup());
    }

    public static String ansiCleanup() {
        return (char) 27 + "[0m";
    }

    @Override
    public void resetTerminal() {
        // \033c
        //		print("\033[H\033[2J");
        print(ansiReset());
    }

    public static String ansiReset() {
        return (char) 27 + "c";
    }

    @Override
    public boolean isAnsi() {
        return true;
    }

    public static String[] getRawAnsiSettings() throws IOException {
        Terminal terminal = TerminalBuilder.terminal();
        return new String[] {
            "Width: " + terminal.getWidth(),
            "Height: " + terminal.getHeight(),
            "Ansi: " + terminal.getType().equals("ansi"),
            "Echo: " + terminal.echo()
        };
    }

    @Override
    public void setWidth(int w) {
        this.width = w;
    }

    @Override
    public void setHeight(int h) {
        this.height = h;
    }
}
