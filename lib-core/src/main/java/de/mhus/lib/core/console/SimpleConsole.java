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

import java.io.InputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;

import de.mhus.lib.core.io.TextReader;

public class SimpleConsole extends Console {

    private TextReader reader;
    private COLOR foreground;
    private COLOR background;
    private boolean blink;
    private boolean bold;
    protected int width = DEFAULT_WIDTH;
    protected int height = DEFAULT_HEIGHT;

    public SimpleConsole() {
        super();
        reader = new TextReader(System.in);
    }

    public SimpleConsole(InputStream in, PrintStream out, boolean flush, String charset)
            throws UnsupportedEncodingException {
        super(out, flush, charset);
        reader = new TextReader(in);
    }

    public SimpleConsole(InputStream in, PrintStream out) {
        super(out);
        reader = new TextReader(in);
    }

    @Override
    public String readLine(String prompt, LinkedList<String> history) {
        if (prompt != null) print(prompt);
        return reader.readLine();
        //		return System.console().readLine();
    }

    @Override
    public int read() {
        return reader.readChar();
    }

    @Override
    public ConsoleKey readKey() {
        int key = read();
        return new ConsoleKey((byte) 0, false, (char) key);
    }

    @Override
    public boolean isSupportSize() {
        return false;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public boolean isSupportCursor() {
        return false;
    }

    @Override
    public void setCursor(int x, int y) {}

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
        return false;
    }

    @Override
    public void setColor(COLOR foreground, COLOR background) {
        this.foreground = foreground;
        this.background = background;
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
        return false;
    }

    @Override
    public void setBlink(boolean blink) {
        this.blink = blink;
    }

    @Override
    public boolean isBlink() {
        return blink;
    }

    @Override
    public boolean isSupportBold() {
        return false;
    }

    @Override
    public void setBold(boolean bold) {
        this.bold = bold;
    }

    @Override
    public boolean isBold() {
        return bold;
    }

    @Override
    public void cleanup() {
        bold = false;
        blink = false;
        foreground = COLOR.WHITE;
        background = COLOR.BLACK;
    }
}
