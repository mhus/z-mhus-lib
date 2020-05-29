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
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;

import de.mhus.lib.annotations.activator.DefaultImplementation;
import de.mhus.lib.basics.Adaptable;
import de.mhus.lib.core.M;
import de.mhus.lib.errors.NotSupportedException;

@DefaultImplementation(SimpleConsole.class)
public abstract class Console extends PrintStream implements Adaptable {

    // https://en.wikipedia.org/wiki/ANSI_escape_code
    public enum COLOR {
        UNKNOWN,
        WHITE,
        BLACK,
        RED,
        GREEN,
        BLUE,
        YELLOW,
        MAGENTA,
        CYAN,
        BRIGHT_WHITE,
        BRIGHT_BLACK,
        BRIGHT_RED,
        BRIGHT_GREEN,
        BRIGHT_BLUE,
        BRIGHT_YELLOW,
        BRIGHT_MAGENTA,
        BRIGHT_CYAN
    };

    public static int DEFAULT_WIDTH = 200;
    public static int DEFAULT_HEIGHT = 25;

    private static ThreadLocal<Console> consoles = new ThreadLocal<>();

    public Console() {
        this(System.out);
    }

    public Console(PrintStream out) {
        super(out);
    }

    public Console(PrintStream out, boolean flush, String charset)
            throws UnsupportedEncodingException {
        super(out, flush, charset);
    }

    /**
     * Factory to return the correct implementation of console.
     *
     * @return a new console object
     */
    public static Console create() {

        Console console = consoles.get();
        if (console == null) {
            ConsoleFactory factory = M.l(ConsoleFactory.class);
            console = factory.create();
            consoles.set(console);
        }
        return console;
    }

    public String readLine() {
        return readLine(null);
    }

    public abstract String readLine(LinkedList<String> history);

    /**
     * Returns the next character from input stream of the console. In some cases check for ALT key
     * pressed and return character + 1000, see JLine ConsoleReader In case of error a value lesser
     * 0 will be returned.
     *
     * @return pressed key
     */
    public abstract int read();

    public abstract ConsoleKey readKey();

    public String readPassword() throws IOException {
        return new String(System.console().readPassword());
    }

    public void cr() {
        print('\r');
    }

    /**
     * Return true if the console knows about the current size of the terminal.
     *
     * @return True if support size
     */
    public abstract boolean isSupportSize();

    public abstract int getWidth();

    public abstract int getHeight();

    public abstract boolean isSupportCursor();

    public abstract void setCursor(int x, int y);

    public abstract int getCursorX();

    public abstract int getCursorY();

    public ConsoleProgressBar createProgressBar() {
        return new ConsoleProgressBar(this);
    }

    public abstract boolean isSupportColor();

    public abstract void setColor(COLOR foreground, COLOR background);

    public abstract COLOR getForegroundColor();

    public abstract COLOR getBackgroundColor();

    public abstract boolean isSupportBlink();

    public abstract void setBlink(boolean blink);

    public abstract boolean isBlink();

    public abstract boolean isSupportBold();

    public abstract void setBold(boolean bold);

    public abstract boolean isBold();

    public abstract void cleanup();

    public void printLine() {
        printLine('-');
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T adaptTo(Class<? extends T> clazz) {
        if (clazz == OutputStream.class) return (T) this;
        if (clazz == InputStream.class)
            return (T)
                    new InputStream() {
                        @Override
                        public int read() throws IOException {
                            return Console.this.read();
                        }
                    };
        throw new NotSupportedException("Can't adapt to", clazz);
    }

    public void printLine(char c) {
        for (int i = 0; i < getWidth(); i++) print(c);
        println();
    }

    public void resetTerminal() {}

    public static void resetConsole() {
        consoles.remove();
    }

    public static synchronized Console get() {
        Console console = consoles.get();
        if (console == null) {
            create();
            console = consoles.get();
        }
        return console;
    }

    public static synchronized void set(Console console) {
        if (console == null) consoles.remove();
        else consoles.set(console);
    }

    public static boolean isInitialized() {
        Console console = consoles.get();
        return console != null;
    }

    public boolean isAnsi() {
        return false;
    }

    public void setWidth(int w) {}

    public void setHeight(int h) {}

    public static char askQuestion(
            String question, char[] answers, boolean toLower, boolean acceptEnter)
            throws IOException {
        if (answers == null || answers.length == 0) return '\0';
        while (true) {
            get().print(question);
            get().print(" (");
            boolean first = true;
            for (char x : answers) {
                if (!first) get().print('/');
                get().print(x);
                first = false;
            }
            get().print(") ");
            get().flush();
            //            String line = get().readLine();
            //            if (line == null) throw new IOException("Can't read from console");
            //            if (line.length() == 0) {
            //                if (acceptEnter) return '\n';
            //                continue;
            //            }
            //            char c = line.charAt(0);
            int key = get().read();
            if (key < 0) throw new IOException("Can't read from console");
            get().println((char) key);
            if (key == '\r') {
                if (acceptEnter) return '\r';
                continue;
            }
            char c = (char) key;
            if (toLower) c = Character.toLowerCase(c);
            for (char x : answers) {
                if (toLower) x = Character.toLowerCase(x);
                if (c == x) return c;
            }
        }
    }
}
