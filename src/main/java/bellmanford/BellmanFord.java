package bellmanford;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by Administrator on 2018/12/29 0029.
 */
public class BellmanFord {

    // 返回 o 到 d 的最短路程
    static double Dijkstra(Graph g, int origin, int destination) {
        return Dijkstra(g, origin).distances[destination];
    }

    // 返回 o 到所有节点的最短路程
    static ShortestPath Dijkstra(Graph g, int origin) {
        int nodeNum = g.getNodeNum();

        // 初始化前向节点
        int pre[] = new int[nodeNum];
        pre[origin] = origin;

        // 初始化距离向量
        double[] distances = new double[nodeNum];
        for (int i = 0; i < nodeNum; ++i) {
            distances[i] = Graph.INF;
        }
        distances[origin] = 0;

        // 初始化候选顶点集，即为起始节点
        Set<Integer> V = new HashSet<Integer>();
        V.add(Integer.valueOf(origin));

        while (!V.isEmpty()) {

            // 选出 V 中距离起点最近的节点，从V中删除
            Integer toRemove = null;

            double minDisInV = Graph.INF;
            for (Integer i : V) {
                if (distances[i] <= minDisInV) {
                    minDisInV = distances[i];
                    toRemove = i;
                }
            }
            V.remove(toRemove);

            // 在与toRemove的出边相连的节点中，选择节点 j
            Map<Integer, Object> outEdges = g.getOutEdges(toRemove);
            if (outEdges != null) {
                for (Integer j : outEdges.keySet()) {
                    // 如果节点 j 可以使得距离向量更小，把 j 加入 V
                    if (distances[j] > distances[toRemove] + (Double) outEdges.get(j)) {
                        distances[j] = distances[toRemove] + (Double) outEdges.get(j);
                        pre[j] = toRemove;
                        if (!V.contains(j)) {
                            V.add(j);
                        }
                    }
                }
            }
        }

        return new ShortestPath(origin, distances, pre, false);
    }

    // 返回 o 到所有节点的最短路程
    static ShortestPath BellmanFord(Graph g, int origin) {
        int nodeNum = g.getNodeNum();
        boolean hasNLoop = false;

        // 初始化前向节点
        int pre[] = new int[nodeNum];
        pre[origin] = origin;

        // 初始化距离向量
        double[] distances = new double[nodeNum];
        for (int i = 0; i < nodeNum; ++i) {
            distances[i] = Graph.INF;
        }
        distances[origin] = 0;

        int iteration = nodeNum;
        while (iteration-- > 0) {
            boolean update = false;
            // 遍历所有的边
            for (int i = 0; i < nodeNum; ++i) {
                Map<Integer, Object> outEdges = g.getOutEdges(i);
                if (outEdges != null) {
                    for (Integer j : outEdges.keySet()) {
                        // 如果节点 j 可以使得距离向量更小，把 j 加入 V
                        if (distances[j] > distances[i] + (Double) outEdges.get(j)) {
                            update = true;
                            if (iteration == 0) {
                                hasNLoop = true;
                                break;
                            }
                            distances[j] = distances[i] + (Double) outEdges.get(j);
                            pre[j] = i;
                        }
                    }
                }
            }
            // 如果这次遍历没有任何更新，那么下一次也不会更新，提前退出循环
            if (!update) {
                break;
            }
        }
        return new ShortestPath(origin, distances, pre, hasNLoop);
    }

    public static class ShortestPath {

        int origin;
        int[] preNodes;
        double[] distances;
        boolean hasNegativeLoop;

        public ShortestPath(int o, double[] dis, int[] pre, boolean has) {
            origin = o;
            preNodes = pre;
            distances = dis;
            hasNegativeLoop = has;
        }


        public void analyse() {
            System.out.println("Negative loops exist: " + hasNegativeLoop);
            for (int i = 0; i < distances.length; ++i) {
                String c = distances[i] < Graph.INF ? String.valueOf(distances[i]) : "inf";
                System.out.println("Distance from " + origin + " to " + i + ": " + c);
                if (distances[i] < Graph.INF) {
                    int j = i;
                    while (j != origin) {
                        System.out.print(j + " <= ");
                        j = preNodes[j];
                    }
                    System.out.println(j);
                }
            }
        }
    }
}
