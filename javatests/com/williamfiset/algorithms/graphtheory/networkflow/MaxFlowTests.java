package javatests.com.williamfiset.algorithms.graphtheory.networkflow;

import static com.google.common.truth.Truth.assertThat;

import com.williamfiset.algorithms.graphtheory.networkflow.NetworkFlowSolverBase;
import com.williamfiset.algorithms.graphtheory.networkflow.CapacityScalingSolverAdjacencyList;
import com.williamfiset.algorithms.graphtheory.networkflow.Dinics;
import com.williamfiset.algorithms.graphtheory.networkflow.EdmondsKarpAdjacencyList;
import com.williamfiset.algorithms.graphtheory.networkflow.FordFulkersonDfsSolverAdjacencyList;

import com.google.common.collect.ImmutableList;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;
import org.junit.*;

public class MaxFlowTests {

  List<NetworkFlowSolverBase> solvers;

  @Before
  public void setUp() {
    solvers = new ArrayList<>();
  }

  void createAllSolvers(int n, int s, int t) {
    solvers.add(new CapacityScalingSolverAdjacencyList(n + 2, s, t));
    solvers.add(new Dinics(n + 2, s, t));
    solvers.add(new EdmondsKarpAdjacencyList(n + 2, s, t));
    solvers.add(new FordFulkersonDfsSolverAdjacencyList(n + 2, s, t));
  }

  void addEdge(int f, int t, int c) {
    for (NetworkFlowSolverBase solver : solvers) {
      solver.addEdge(f, t, c);
    }
  }

  void assertFlow(long flow) {
    for (NetworkFlowSolverBase solver : solvers) {
      assertThat(solver.getMaxFlow()).isEqualTo(flow);
    }
  }

  @Test
  public void lineGraphTest() {
    int n = 2, s = n, t = n+1;
    createAllSolvers(n, s, t);
    
    addEdge(s, 0, 5);
    addEdge(0, 1, 3);
    addEdge(1, t, 7);

    assertFlow(3);
  }

  @Test
  public void testDisconnectedGraph() {
    int n = 2, s = n, t = n+1;
    createAllSolvers(n, s, t);
    
    // There's no edge connecting 0 and 1
    addEdge(s, 0, 9);
    addEdge(1, t, 9);

    assertFlow(0);
  }

  // Testing graph from:
  // http://crypto.cs.mcgill.ca/~crepeau/COMP251/KeyNoteSlides/07demo-maxflowCS-C.pdf
  @Test
  public void testSmallFlowGraph() {
    int n = 4, s = n, t = n+1;
    createAllSolvers(n, s, t);

    // Source edges
    addEdge(s, 0, 10);
    addEdge(s, 1, 10);

    // Sink edges
    addEdge(2, t, 10);
    addEdge(3, t, 10);

    // Middle edges
    addEdge(0, 1, 2);
    addEdge(0, 2, 4);
    addEdge(0, 3, 8);
    addEdge(1, 3, 9);
    addEdge(3, 2, 6);

    assertFlow(19);
  }

  @Test
  public void classicNetwork() {
    int n = 2, s = n, t = n+1;
    final int k = 10000;
    createAllSolvers(n, s, t);

    addEdge(s, 0, k);
    addEdge(s, 1, k);
    addEdge(0, t, k);
    addEdge(1, t, k);
    addEdge(0, 1, 1);

    assertFlow(2*k);
  }

  @Test
  public void evilNetwork1() {
    int n = 4, s = n, t = n+1;
    final int k = 100000;
    createAllSolvers(2*k, s, t);

    addEdge(s, 0, k);
    for (int i = 0; i < k - 1; i++)
      addEdge(i, i + 1, k);
    for (int i = 0; i < k; i++) {
      addEdge(k-1, k + i, 1);
      addEdge(k + i, t, 1);
    }

    assertFlow(k);
  }

}













