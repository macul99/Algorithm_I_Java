import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Board {

    private final int dim;
    private int[][] myBlocks;

    public Board(int[][] blocks)           // construct a board from an n-by-n array of blocks
    {
        dim = blocks.length;
        myBlocks = new int[dim][dim];

        for (int i = 0; i < dim; ++i) {
            System.arraycopy(blocks[i], 0, myBlocks[i], 0, dim);
        }
    }

    // (where blocks[i][j] = block in row i, column j)
    public int dimension()                 // board dimension n
    {
        return dim;
    }


    public int hamming()                   // number of blocks out of place
    {
        int count = 0;

        for (int i = 0; i < dim; ++i) {
            for (int j = 0; j < dim; ++j) {
                if ((myBlocks[i][j] != i * dim + j + 1) && (myBlocks[i][j] != 0)) count++;
            }
        }

        return count;
    }

    public int manhattan()                 // sum of Manhattan distances between blocks and goal
    {
        int steps = 0;

        for (int i = 0; i < dim; ++i) {
            for (int j = 0; j < dim; ++j) {
                if (myBlocks[i][j] != 0) {
                    steps += java.lang.Math.abs(((myBlocks[i][j] - 1) / dim) - i);
                    steps += java.lang.Math.abs(((myBlocks[i][j] - 1) % dim) - j);
                }
            }
        }

        return steps;
    }

    public boolean isGoal()                // is this board the goal board?
    {
        if (myBlocks[dim - 1][dim - 1] != 0) return false;

        for (int i = 0; i < dim; ++i) {
            for (int j = 0; j < dim; ++j) {
                if (i == dim - 1 && j == dim - 1) {
                    if (myBlocks[i][j] != 0) return false;
                } else {
                    if (myBlocks[i][j] != i * dim + j + 1) return false;
                }
            }
        }

        return true;
    }

    public Board twin()                    // a board that is obtained by exchanging any pair of blocks
    {
        int[][] tmpBlocks = new int[dim][dim];

        for (int i = 0; i < dim; ++i) {
            for (int j = 0; j < dim; ++j) {
                tmpBlocks[i][j] = myBlocks[i][j];
            }
        }

        boolean flag = false;

        for (int i = 0; i < dim; ++i) {
            for (int j = 0; j < dim - 1; j += 2) {
                if ((tmpBlocks[i][j] != 0) && (tmpBlocks[i][j + 1] != 0)) {
                    tmpBlocks[i][j] = myBlocks[i][j + 1];
                    tmpBlocks[i][j + 1] = myBlocks[i][j];
                    flag = true;
                    break;
                }
            }
            if (flag) break;
        }

        return new Board(tmpBlocks);
    }

    public boolean equals(Object y)        // does this board equal y?
    {
        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;

        Board that = (Board) y;

        if (dim != that.dimension()) return false;

        for (int i = 0; i < dim; ++i) {
            for (int j = 0; j < dim; ++j) {
                if (myBlocks[i][j] != that.myBlocks[i][j]) return false;
            }
        }

        return true;
    }

    public Iterable<Board> neighbors()     // all neighboring boards
    {
        Stack<Board> ngbItr = new Stack<Board>();
        boolean flag = false;
        int tmp = -1;
        for (int i = 0; i < dim; ++i) {
            for (int j = 0; j < dim; ++j) {
                if (myBlocks[i][j] == 0) {
                    flag = true;
                    if (i > 0) {
                        tmp = myBlocks[i - 1][j];
                        myBlocks[i - 1][j] = 0;
                        myBlocks[i][j] = tmp;
                        ngbItr.push(new Board(myBlocks));
                        myBlocks[i - 1][j] = tmp;
                        myBlocks[i][j] = 0;
                    }

                    if (i < dim - 1) {
                        tmp = myBlocks[i + 1][j];
                        myBlocks[i + 1][j] = 0;
                        myBlocks[i][j] = tmp;
                        ngbItr.push(new Board(myBlocks));
                        myBlocks[i + 1][j] = tmp;
                        myBlocks[i][j] = 0;
                    }

                    if (j < dim - 1) {
                        tmp = myBlocks[i][j + 1];
                        myBlocks[i][j + 1] = 0;
                        myBlocks[i][j] = tmp;
                        ngbItr.push(new Board(myBlocks));
                        myBlocks[i][j + 1] = tmp;
                        myBlocks[i][j] = 0;
                    }

                    if (j > 0) {
                        tmp = myBlocks[i][j - 1];
                        myBlocks[i][j - 1] = 0;
                        myBlocks[i][j] = tmp;
                        ngbItr.push(new Board(myBlocks));
                        myBlocks[i][j - 1] = tmp;
                        myBlocks[i][j] = 0;
                    }

                    break;
                }
            }
            if (flag) break;
        }

        return ngbItr;
    }

    public String toString()               // string representation of this board (in the output format specified below)
    {
        StringBuilder s = new StringBuilder();
        s.append(dim + "\n");
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                s.append(String.format("%2d ", myBlocks[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    public static void main(String[] args) // unit tests (not graded)
    {
        int[][] test = {{8, 1, 3}, {4, 0, 2}, {7, 6, 5}};
        int[][] test1 = {{8, 1, 3}, {4, 0, 2}, {7, 5, 6}};
        Board myBoard = new Board(test);
        Board myBoard1 = new Board(test);
        Board myBoard2 = new Board(test1);
        StdOut.println(myBoard.dimension());
        StdOut.println(myBoard.hamming());
        StdOut.println(myBoard.toString());
        StdOut.println(myBoard.equals(myBoard1));
        StdOut.println(myBoard.equals(myBoard2));
        StdOut.println(myBoard.twin().toString());
        StdOut.println(myBoard.toString());

        Iterable<Board> ngbItr = myBoard.neighbors();

        for (Board bd : ngbItr) {
            StdOut.println("neighbours:");
            StdOut.println(bd.toString());
        }
    }
}
