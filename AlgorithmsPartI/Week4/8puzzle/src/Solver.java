import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.Comparator;
import java.util.Iterator;

public class Solver {

    private class SearchNode {
        Board board;
        int moves;
        SearchNode previous;
        int manhattan;

        public SearchNode(Board board, int moves, SearchNode previous) {
            this.board = board;
            this.moves = moves;
            this.previous = previous;
            // Even though it is cached in Board itself, still the grader fails, hence caching it here as well.
            this.manhattan = board.manhattan();
        }
    }

    private SearchNode solutionSearchNode = null;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException();

        MinPQ<SearchNode> minPQOriginal = new MinPQ<>(new Comparator<SearchNode>() {
            @Override
            public int compare(SearchNode searchNode1, SearchNode searchNode2) {
                return searchNode1.manhattan + searchNode1.moves - searchNode2.manhattan - searchNode2.moves;
            }
        });
        MinPQ<SearchNode> minPQTwin = new MinPQ<>(new Comparator<SearchNode>() {
            @Override
            public int compare(SearchNode searchNode1, SearchNode searchNode2) {
                return searchNode1.manhattan + searchNode1.moves - searchNode2.manhattan - searchNode2.moves;
            }
        });

        SearchNode initialSearchNode = new SearchNode(initial, 0, null);
        minPQOriginal.insert(initialSearchNode);

        SearchNode twinSearchNode = new SearchNode(initial.twin(), 0, null);
        minPQTwin.insert(twinSearchNode);

        while (!minPQOriginal.isEmpty()) {
            SearchNode searchNodeOriginal = minPQOriginal.delMin();
            SearchNode searchNodeTwin = minPQTwin.delMin();
            if (searchNodeOriginal.board.isGoal()) {
                solutionSearchNode = searchNodeOriginal;
                break;
            }
            if (searchNodeTwin.board.isGoal()) {
                break;
            }

            for (Board neighbor: searchNodeOriginal.board.neighbors()) {
                if (searchNodeOriginal.previous == null || !neighbor.equals(searchNodeOriginal.previous.board))
                    minPQOriginal.insert(new SearchNode(neighbor, searchNodeOriginal.moves + 1, searchNodeOriginal));
            }
            for (Board neighbor: searchNodeTwin.board.neighbors()) {
                if (searchNodeTwin.previous == null || !neighbor.equals(searchNodeTwin.previous.board))
                    minPQTwin.insert(new SearchNode(neighbor, searchNodeTwin.moves + 1, searchNodeTwin));
            }
        }

    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return solutionSearchNode != null;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (!isSolvable()) return -1;
        return solutionSearchNode.moves;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (!isSolvable()) return null;

        Board[] sequence = new Board[solutionSearchNode.moves + 1];
        SearchNode temp = solutionSearchNode;
        for (int i = solutionSearchNode.moves; i >= 0; i--) {
            sequence[i] = temp.board;
            temp = temp.previous;
        }
        return new Iterable<Board>() {
            @Override
            public Iterator<Board> iterator() {
                return new Iterator<Board>() {
                    int current = 0;

                    @Override
                    public boolean hasNext() {
                        return current <= solutionSearchNode.moves;
                    }

                    @Override
                    public Board next() {
                        return sequence[current++];
                    }
                };
            }
        };
    }

    // test client (see below)
    public static void main(String[] args) {

        /*
         1  2  3
         4  6  5
         7  8  0
         */
        Board board = new Board(new int[][]{new int[]{1, 2, 3}, new int[]{4, 6, 5}, new int[]{7, 8, 0}});
        //Board board = new Board(new int[][]{new int[]{0, 1}, new int[]{2, 3}});
        Solver solver = new Solver(board);

        StdOut.println(solver.isSolvable());
        if (solver.isSolvable()) {
            for (Board node : solver.solution()) {
                StdOut.println(node);
            }
        }
    }

}