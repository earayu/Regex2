package cn.eovie.fa;

import cn.eovie.fa.NFA2Node;

/**
 * Created by earayu on 2017/6/26.
 */
public class Edge {

    char c;
    NFA2Node src;
    NFA2Node des;

    public Edge(NFA2Node src, char c, NFA2Node des)
    {
        this.c = c;
        this.src = src;
        this.des = des;
    }

    public char getChar()
    {
        return c;
    }

}
