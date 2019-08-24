package grader.basics.util;

import grader.basics.execution.BasicProjectExecution;
import grader.basics.project.BasicProjectIntrospection;
import util.trace.Tracer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created with IntelliJ IDEA.
 * User: josh
 * Date: 11/7/13
 * Time: 11:27 AM
 * To change this template use File | Settings | File Templates.
 */
public class TimedProcess {

    private static final ExecutorService THREAD_POOL = Executors.newCachedThreadPool();

    private Process process;
    private ProcessBuilder processBuilder;
    private long timeout;

    public TimedProcess(ProcessBuilder builder, long timeout) {
        processBuilder = builder;
        this.timeout = timeout;
    }

    public Process start() throws IOException {
//    	processBuilder.redirectError(processBuilder.redirectOutput());
        process = processBuilder.start();
        return process;
    }
    
    public void destroy() {
    	process.destroy();
    }

    public InputStream getInputStream() {
        return process.getInputStream();
    }

    public OutputStream getOutputStream() {
        return process.getOutputStream();
    }
    
    public InputStream getErrorStream() {
    	return process.getErrorStream();
    }

    public Process getProcess() {
        return process;
    }

    public int waitFor() throws InterruptedException, ExecutionException, TimeoutException {
        // Don't timeout if the timeout is -1
    	if (!BasicProjectExecution.isWaitForMethodConstructorsAndProcesses()) {
    		return 0; // do not wait in this case, for infinite or finite time
    	}
        if (timeout == -1 || !BasicProjectExecution.isUseMethodAndConstructorTimeOut())
            return process.waitFor();

        return timedCall(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
//            	Tracer.info(this, "Waiting for process to finish within seconds:" + timeout);
                return process.waitFor();
            }
        }, timeout, TimeUnit.SECONDS);
    }
    private static <T> T timedCall(Callable<T> c, long timeout, TimeUnit timeUnit)
            throws InterruptedException, ExecutionException, TimeoutException {
        FutureTask<T> task = new FutureTask<T>(c);
        THREAD_POOL.execute(task);
        return task.get(timeout, timeUnit);
    }

}
