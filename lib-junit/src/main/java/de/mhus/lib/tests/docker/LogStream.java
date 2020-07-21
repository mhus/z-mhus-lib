package de.mhus.lib.tests.docker;

import java.io.IOException;
import java.util.LinkedList;

import com.github.dockerjava.api.model.Frame;

import de.mhus.lib.core.MCast;
import de.mhus.lib.core.MString;
import de.mhus.lib.core.io.PipedStream;

public class LogStream extends com.github.dockerjava.api.async.ResultCallback.Adapter<Frame> {

    private PipedStream output = new PipedStream();
    private DockerContainer cont;
    private boolean print = true;
    private boolean closed = false;
    
    public LogStream(DockerContainer cont) {
        this.cont = cont;
    }

    public PipedStream getOutput() {
        return output;
    }

    @Override
    public void onComplete() {
       // output.close();
       // if (input != null)
       //     input.close();
    }
    
    @Override
    public void onNext(Frame item) {
        try {
            if (print)
                System.out.print(new String(item.getPayload(), MString.CHARSET_CHARSET_UTF_8));
//                System.out.print("(" + step + ")" + new String(item.getPayload(), MString.CHARSET_CHARSET_UTF_8));
            output.getOut().write(item.getPayload());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * No UTF8 encoding supported !!!!
     * Use: 
     * LinkedList<Byte> logArray = logStream.readLine();
     * logArray.removeIf(v -> v == 0);
     * String logStr = new String(MCast.toByteArray(logArray), MString.CHARSET_CHARSET_UTF_8);
     * 
     * @return The next line until and inclusive LF \n, also inclusive 0 characters and CR \r
     */
    public LinkedList<Byte> readLineRaw() {
        LinkedList<Byte> sb = new LinkedList<>();
        try {
            while (true) {
                int c = output.getIn().read(); // no utf8 !! - utf8 fails because of a lot of 0 bytes
                if (c < 0) return sb;
                sb.add((byte)c);
                if (c == '\n') {
//                    String out = sb.toString();
//                    System.err.print(step + ": " + sb );
                    return sb;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
//            System.err.print(step + ": " + sb );
            return sb;
        }
    }
    
    public String readLine() {
        LinkedList<Byte> array = readLineRaw();
        array.removeIf(v -> v == 0);
        String logStr = new String(MCast.toByteArray(array), MString.CHARSET_CHARSET_UTF_8);
        return logStr;
    }

    public LinkedList<Byte> readAllRaw() {
        LinkedList<Byte> sb = new LinkedList<>();
        try {
            while (true) {
                int c = output.getIn().read(); // no utf8 !! - utf8 fails because of a lot of 0 bytes
                if (c < 0) return sb;
                sb.add((byte)c);
                if (c == '\n') {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb;
    }

    public String readAll() {
        LinkedList<Byte> array = readAllRaw();
        array.removeIf(v -> v == 0);
        String logStr = new String(MCast.toByteArray(array), MString.CHARSET_CHARSET_UTF_8);
        return logStr;
    }

    @Override
    public void close() throws IOException {
        this.closed = true;
        super.close();
        output.close();
    }
    
    public DockerContainer getContainer() {
        return cont;
    }

    public boolean isPrint() {
        return print;
    }

    public void setPrint(boolean print) {
        this.print = print;
    }

    public boolean isClosed() {
        return closed;
    }

}
