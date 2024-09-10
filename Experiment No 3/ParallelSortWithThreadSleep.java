import java.util.Arrays;
import java.time.LocalTime;

public class ParallelSortWithThreadSleep {
    private static final int ARRAY_SIZE = 100;
    private static final int NUM_THREADS = 4;

    public static void main(String[] args) {
        int[] array = createArray();
        System.out.println("Before sorting: " + Arrays.toString(array));

        Thread[] threads = new Thread[NUM_THREADS];
        int segmentSize = ARRAY_SIZE / NUM_THREADS;

        for (int i = 0; i < NUM_THREADS; i++) {
            int startIndex = i * segmentSize;
            int endIndex = (i == NUM_THREADS - 1) ? ARRAY_SIZE - 1 : (startIndex + segmentSize - 1);
            threads[i] = new Thread(new SortTask(array, startIndex, endIndex));
            threads[i].start();
        }

        for (Thread thread : threads) {
            try {
                thread.join(); // Ensure all threads complete before moving forward
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Perform final merge sort to combine the sorted segments
        mergeSort(array, 0, ARRAY_SIZE - 1);

        System.out.println("After sorting: " + Arrays.toString(array));
    }

    // Function to create a random array
    private static int[] createArray() {
        int[] array = new int[ARRAY_SIZE];
        for (int i = 0; i < ARRAY_SIZE; i++) {
            array[i] = (int) (Math.random() * 400); // Random numbers between 0 and 400
        }
        return array;
    }

    // Merge Sort function
    private static void mergeSort(int[] array, int left, int right) {
        if (left < right) {
            int mid = (left + right) / 2;
            mergeSort(array, left, mid);
            mergeSort(array, mid + 1, right);
            merge(array, left, mid, right);
        }
    }

    // Merge function to combine two sorted halves
    private static void merge(int[] array, int left, int mid, int right) {
        int[] temp = new int[right - left + 1];
        int i = left, j = mid + 1, k = 0;

        while (i <= mid && j <= right) {
            if (array[i] <= array[j]) {
                temp[k++] = array[i++];
            } else {
                temp[k++] = array[j++];
            }
        }

        while (i <= mid) {
            temp[k++] = array[i++];
        }

        while (j <= right) {
            temp[k++] = array[j++];
        }

        System.arraycopy(temp, 0, array, left, temp.length);
    }

    // Task class for sorting each segment
    static class SortTask implements Runnable {
        private int[] array;
        private int startIndex;
        private int endIndex;

        public SortTask(int[] array, int startIndex, int endIndex) {
            this.array = array;
            this.startIndex = startIndex;
            this.endIndex = endIndex;
        }

        @Override
        public void run() {
            try {
                // Add sleep to simulate delay
                System.out.println("[" + LocalTime.now() + "] Sorting segment: " + startIndex + " to " + endIndex);
                Thread.sleep(2000); // Sleep for 2 seconds before sorting
                Arrays.sort(array, startIndex, endIndex + 1);
                System.out.println("[" + LocalTime.now() + "] Sorted segment: " + startIndex + " to " + endIndex);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
