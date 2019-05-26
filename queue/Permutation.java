import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;

public class Permutation {
    public static void main(String[] args) {
        int n = 0;
        if (args.length == 1) n = Integer.parseInt(args[0]);

        RandomizedQueue<String> RQ = new RandomizedQueue<String>();

        while (n-- > 0) {
            RQ.enqueue(StdIn.readString());
        }

        Iterator<String> i = RQ.iterator();
        while (i.hasNext()) {
            String s = i.next();
            StdOut.println(s);
        }
    }
}
