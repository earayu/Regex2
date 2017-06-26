package cn.eovie.fa;

import java.util.ArrayList;

/**
 * Created by earayu on 2017/6/26.
 */
public class NFA2Node {

    boolean start;
    boolean end;
    ArrayList<Edge> edges;//TODO 改成set？

    public NFA2Node()
    {
        edges = new ArrayList<>();
    }

    public static NFA2Node newStartNode()
    {
        NFA2Node node = new NFA2Node();
        node.start = true;
        return node;
    }

    public static NFA2Node newEndNode()
    {
        NFA2Node node = new NFA2Node();
        node.end = true;
        return node;
    }

    public void addEdge(char c, NFA2Node des)
    {
        Edge edge = new Edge(this, c, des);
        edges.add(edge);
    }

    public void changeAllEdgeDes(NFA2Node des)
    {
        edges.stream().forEach(e->e.des = des);
    }

    public NFA2Node hasPath(char c)
    {
        for(Edge edge:edges)
        {
            if(edge.c==c)
            {
                return edge.des;
            }
        }
        return null;
    }
}
