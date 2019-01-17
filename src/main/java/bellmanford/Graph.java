package bellmanford;


import java.util.Map;


public class Graph extends SparseMatrix {
    int nodeNum; // 图的节点数
    boolean symmetric; // 是否为无向图
    public static final double INF = Double.MAX_VALUE; // 表示不相邻的节点之间的直接距离为无穷大

    public Graph(int n, boolean s) {
        super(n, n); // n x n 的稀疏矩阵
        symmetric = s;
        nodeNum = n;
        setDefaultValue(INF); // 将稀疏矩阵的默认值设成 无穷大
    }

    public int getNodeNum() {
        return nodeNum;
    }

    public void addEdge(int i, int j) {
        try {
            put(i, j, 1);
            if (symmetric) {
                put(j, i, 1);
            }
        } catch (IndexException e) {
            e.printStackTrace();
        } catch (TypeException e) {
            e.printStackTrace();
        }
    }

    public void addEdge(int i, int j, double c) {
        try {
            put(i, j, c);
            if (symmetric) {
                put(j, i, c);
            }
        } catch (SparseMatrix.IndexException  e) {
            e.printStackTrace();
        } catch (TypeException e) {
            e.printStackTrace();
        }
    }

    public void addEdges(double[][] triples) {
        for (int i = 0; i < triples.length; ++i) {
            addEdge((int) triples[i][0], (int) triples[i][1], triples[i][2]);
        }
    }

    public double getEdge(int i, int j) {
        if (i == j) {
            return 0;
        }
        try {
            return (Double) get(i, j);
        } catch (IndexException e) {
            e.printStackTrace();
            return INF;
        }
    }

    // 获取节点 i 的出边集
    public Map<Integer, Object> getOutEdges(int i) {
        return rows.get(i);
    }

    // 展示图的邻接矩阵
    public void show() {
        for (int i = 0; i < nodeNum; ++i) {
            for (int j = 0; j < nodeNum; ++j) {
                String c = getEdge(i, j) < INF ? String.valueOf(getEdge(i, j)) : "inf";
                System.out.print(c + " ");
            }
            System.out.println();
        }
    }
}


