import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;


public class Solver {

    private final Stack<Node> results;

    private boolean solvedFlag;

    public Solver(Board initial)           // find a solution to the initial board (using the A* algorithm)
    {
        if (initial == null) throw new java.lang.IllegalArgumentException();

        solvedFlag = false;
        results = new Stack<Node>();

        Board twin = initial.twin();

        MinPQ<Node> pQ = new MinPQ<Node>();
        MinPQ<Node> pQTwin = new MinPQ<Node>();

        pQ.insert(new Node(initial, 0, null));
        pQTwin.insert(new Node(twin, 0, null));
        Node tmpNode = null;
        Node tmpNodeTwin = null;

        while (true) {
            tmpNode = pQ.delMin();
            tmpNodeTwin = pQTwin.delMin();

            results.push(tmpNode);

            if (tmpNode.board.isGoal()) {
                solvedFlag = true;
                break;
            }

            if (tmpNodeTwin.board.isGoal()) {
                break;
            }

            // StdOut.println("current board:");
            // StdOut.println(tmpNode.board.toString());
            for (Board bd : tmpNode.board.neighbors()) {
                if (tmpNode.predecessor == null || !tmpNode.predecessor.board.equals(bd)) {
                    pQ.insert(new Node(bd, tmpNode.moves + 1, tmpNode));
                    // StdOut.println("neighbours:");
                    // StdOut.println(bd.toString());
                    // StdOut.println("moves:");
                    // StdOut.println(tmpNode.moves + 1);
                }
            }

            for (Board bd : tmpNodeTwin.board.neighbors()) {
                if (tmpNode.predecessor == null || !tmpNodeTwin.predecessor.board.equals(bd)) {
                    pQTwin.insert(new Node(bd, tmpNodeTwin.moves + 1, tmpNodeTwin));
                }
            }
        }
    }

    public boolean isSolvable()            // is the initial board solvable?
    {
        return solvedFlag;
    }

    public int moves()                     // min number of moves to solve initial board; -1 if unsolvable
    {
        if (isSolvable()) return results.peek().moves;

        return -1;
    }

    public Iterable<Board> solution()      // sequence of boards in a shortest solution; null if unsolvable
    {
        if (isSolvable()) {
            Stack<Board> rst = new Stack<Board>();
            Iterator<Node> itr = results.iterator();
            Node curNode = itr.next();

            rst.push(curNode.board);

            while (curNode.moves != 0) {
                curNode = curNode.predecessor;
                rst.push(curNode.board);
            }
            return rst;
        }

        return null;
    }

    private class Node implements Comparable<Node> {
        Board board;
        int moves;
        Node predecessor;
        int priority;
        int manha;

        public Node(Board bd, int mv, Node predc) {
            board = bd;
            moves = mv;
            predecessor = predc;
            manha = board.manhattan();
            // priority = moves + board.hamming();
            priority = moves + manha;
        }

        public int compareTo(Node that) {
            /* YOUR CODE HERE */
            if (this.priority < that.priority)
                return -1;
            if (this.priority > that.priority)
                return 1;
            if (this.moves > that.moves)
                return -1;
            if (this.moves < that.moves)
                return 1;
            return 0;
        }
    }

    public static void main(String[] args) // solve a slider puzzle (given below)
    {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
        // StdOut.println(solver.level);
    }
}

