package gradingTools.shared.testcases.utils;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 *
 * @author Andrew Vitkus
 */
public class RedirectionEnvironment implements AutoCloseable {

    private final CheckedConsumer<PrintStream> setStream;
    private final PrintStream oldStream;
    private final PrintStream newStream;
    private final Path bufferPath;
    private final boolean emptyOnClose;
    private final boolean redirectOnClose;
    private final AtomicBoolean canClose;
    private final AtomicBoolean valid;
    private final AtomicBoolean closed;
    
    private static final Map<String, PrintStream> defaultStreams;
    
    static {
        defaultStreams = new HashMap<>();
        defaultStreams.put("stdout", System.out);
        defaultStreams.put("stderr", System.err);
    }
    
    public static Optional<PrintStream> getDefautFor(String s) {
        if (defaultStreams.containsKey(s)) {
            return Optional.of(defaultStreams.get(s));
        } else {
            return Optional.empty();
        }
    }
    
    public static Optional<PrintStream> setDefaultFor(String s, PrintStream ps) {
        PrintStream old = defaultStreams.put(s, ps);
        if (old != null) {
            return Optional.of(old);
        } else {
            return Optional.empty();
        }
    }

    public static RedirectionEnvironment ofStdOut() throws IOException {
        return ofStdOut(false);
    }

    public static RedirectionEnvironment ofStdOut(boolean redirectOnClose) throws IOException {
        String fileName = "stdout-" + Thread.currentThread().getId()
                + "-" + ThreadLocalRandom.current().nextInt();

        return new RedirectionEnvironment(System.out, fileName, System::setOut, true, redirectOnClose);
    }

    public static RedirectionEnvironment ofStdErr() throws IOException {
        return ofStdErr(false);
    }

    public static RedirectionEnvironment ofStdErr(boolean redirectOnClose) throws IOException {
        String fileName = "stderr-" + Thread.currentThread().getId()
                + "-" + ThreadLocalRandom.current().nextInt();

        return new RedirectionEnvironment(System.err, fileName, System::setErr, true, redirectOnClose);
    }
    
    public static RedirectionEnvironment of(PrintStream oldStream, CheckedConsumer<PrintStream> setStream, boolean emptyOnClose, boolean redirectOnClose) throws IOException {
        String fileName = "printstream-" + oldStream.toString()
                + "-" + Thread.currentThread().getId()
                + "-" + ThreadLocalRandom.current().nextInt();
        return new RedirectionEnvironment(oldStream, fileName, setStream, emptyOnClose, redirectOnClose);
    }
    
    public static Optional<RedirectionEnvironment> redirectOut() {
        return redirectOut(true);
    }

    public static Optional<RedirectionEnvironment> redirectOut(boolean forwardOnClose) {
        try {
            return Optional.of(RedirectionEnvironment.ofStdOut(forwardOnClose));
        } catch (IOException ex) {
            return Optional.empty();
        }
    }
    
    public static Optional<RedirectionEnvironment> redirectErr() {
        return redirectErr(true);
    }
    
    public static Optional<RedirectionEnvironment> redirectErr(boolean forwardOnClose) {
        try {
            return Optional.of(RedirectionEnvironment.ofStdErr(forwardOnClose));
        } catch (IOException ex) {
            return Optional.empty();
        }
    }
    
    public static Optional<String> restore(RedirectionEnvironment re) {
        if (re != null && !re.isClosed()) {
            try {
                return re.closeAndGet();
            } catch (Exception ex) { }
        }
        return Optional.empty();
    }
    
    private RedirectionEnvironment(PrintStream oldStream, String bufferFileName, CheckedConsumer<PrintStream> setStream, boolean emptyOnClose, boolean redirectOnClose) throws IOException {
        this.oldStream = oldStream;
        this.setStream = setStream;
        this.emptyOnClose = emptyOnClose;
        this.redirectOnClose = redirectOnClose;
        bufferPath = Files.createTempFile(bufferFileName, null);
        valid = new AtomicBoolean(true);
        PrintStream ps = null;
        try {
            ps = new PrintStream(bufferPath.toFile());
            setStream.accept(ps);
        } catch (Exception ex) {
            try {
                valid.set(false);
                ps = oldStream;
                setStream.accept(oldStream);
            } catch (Exception ex1) {
            }
        }
        newStream = ps;
        canClose = new AtomicBoolean(true);
        closed = new AtomicBoolean(false);
    }

    public Optional<String> getOutput() {
        if (!isValid() || isClosed()) {
            return Optional.empty();
        }
        canClose.set(false);
        try {
            StringBuilder sb = new StringBuilder();
            try {
                newStream.flush();
                sb.ensureCapacity((int) Files.size(bufferPath) / Character.BYTES);
                /*List<String> lines = */Files.lines(bufferPath)//Files.readAllLines(bufferPath);
                /*lines.stream()*/.forEachOrdered((line) -> sb.append(line).append("\n"));
                if (sb.length() > 0) { 
                    sb.setLength(Math.max(0, sb.length() - 1));
                }
            } catch (Exception ex) {
                valid.set(false);
                ex.printStackTrace(defaultStreams.getOrDefault("stderr", System.err));
                return Optional.empty();
            }
            return Optional.of(sb.toString());
        } finally {
            canClose.set(true);
        }
    }

    public boolean isValid() {
        return valid.get();
    }

    public boolean isClosed() {
        return closed.get();
    }

    public Optional<String> closeAndGet() throws Exception {
        Optional<String> output = close(true);
        if (redirectOnClose && output.isPresent()) {
            oldStream.println(output.get());
            oldStream.flush();
        }
        return output;
    }

    @Override
    public void close() throws Exception {
        Optional<String> output = close(redirectOnClose);
        if (output.isPresent()) {
            oldStream.println(output.get());
            oldStream.flush();
        }
    }

    private Optional<String> close(boolean getOutput) throws Exception {
        if (isClosed()) {
            return Optional.empty();
        }
        Optional<String> ret;
        if (getOutput) {
            ret = getOutput();
        } else {
            ret = Optional.empty();
        }

        closed.set(true);
        setStream.accept(oldStream);
        newStream.close();
        if (emptyOnClose) {
            closeBuffer();
        }
        return ret;
    }

    private void closeBuffer() throws IOException {
        valid.set(false);
        waitToClose();
        newStream.close();
        Files.deleteIfExists(bufferPath);
    }

    private void waitToClose() {
        long end = System.currentTimeMillis() + 100;
        while (canClose.get() == false && System.currentTimeMillis() < end); 
    }
}
