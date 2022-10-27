import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.*;

class SumThread implements Callable<Long> {
    long[] mas;
    int index;
    int size;

    SumThread(long[] curr_mas, int curr_index, int curr_size) {
        mas = curr_mas;
        index = curr_index;
        size = curr_size;
    }

    public Long call() {
        return mas[index] + mas[size - 1 - index];
    }
}

class Main {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Main main = new Main();
        main.starter();
    }

    public void starter() throws ExecutionException, InterruptedException {
        int size = 100000;
        long SumMas = 0;

        long[] mas = new long[size];

        for (int i = 0; i < size; i++)
            mas[i] = i;

        for (int i = 0; i < size; i++)
            SumMas = SumMas + mas[i];

        System.out.println("Сума елементів масиву:");
        System.out.println(SumMas);

        ExecutorService executorService = Executors.newFixedThreadPool(10);

        System.out.println("Сума в багатопоточному режимі:");
        System.out.println(findArraySum(mas, executorService));
    }

    public long findArraySum(long[] mas, ExecutorService executorService) throws ExecutionException, InterruptedException {
        int size = mas.length;

        List<Future<Long>> list = new ArrayList<>();

        do {
            list.clear();

            for (int i = 0; i < size / 2; i++) {
                list.add(executorService.submit(new SumThread(mas, i, size)));
            }

            for (int i = 0; i < size / 2; i++) {
                mas[i] = list.get(i).get();
            }

            size = size / 2 + size % 2;
        } while (size > 1);

        executorService.shutdownNow();

        return mas[0];
    }
}