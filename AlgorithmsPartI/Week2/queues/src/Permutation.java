import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class Permutation {
    public static void main(String[] args) {
        int k = Integer.parseInt(args[0]);
        RandomizedQueue<String> randomizedQueue = new RandomizedQueue<String>();

        int n = 0;
        while (!StdIn.isEmpty()) {
            n++;
            String s = StdIn.readString();
            if (n > k && StdRandom.uniform(n) < k)  {
                randomizedQueue.dequeue();
            } else if (n > k) { continue; }
            randomizedQueue.enqueue(s);
        }
        for (int i = 0; i < k; i++) {
            StdOut.println(randomizedQueue.dequeue());
        }
    }
}