package callable;

import java.util.concurrent.*;

/**
 * @Author: Henry Yi
 * @Date: 7/9/2020 - 14:06
 * @Description: callable
 * @Version: 1.0
 */
public class CallableAndFuture {
    public static void main(String[] args) throws Exception {
        ExecutorService service = Executors.newCachedThreadPool();
        Future<Long> future = service.submit(new Task());

        System.out.println(future.get());
        System.out.println("go on");

        service.shutdown();
    }
}

class Task implements Callable<Long> {

    public static void main(String[] args) throws Exception {
        FutureTask<Long> ft = new FutureTask<>(new Task());
        new Thread(ft).start();
        Long l = ft.get();
        System.out.println(l);
    }

    @Override
    public Long call() throws Exception {
        long r = 0L;
        for (int i = 0; i < 10; i++) {
            r += i;
            Thread.sleep(500);
        }
        return r;
    }
}



