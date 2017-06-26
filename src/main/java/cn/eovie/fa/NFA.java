package cn.eovie.fa;

/**
 * Created by earayu on 2017/6/16.
 */

import java.util.ArrayList;

/**
 * ʵ�ֵ�NFA,��Щ�ط�û�д����⡣
 * ���Կ���һ�ż�Ȩ����ͼ
 * @author earayu
 * //TODO 待重构
 *
 */
public class NFA{

    private ArrayList<NFANode> nfa;

    public int size()
    {
        return nfa.size();
    }

    /**
     * Լ��������Ϊi�ıߵ�������Ϊi�Ľڵ�
     * @author earayu
     *
     */
    public class NFANode implements Comparable
    {
        public String state;//״̬��
        public ArrayList<Character> edge;
        public ArrayList<NFANode> desNode;
        public boolean start;//true��Ϊ��ʼ״̬
        public boolean end;

        public NFANode(String state)
        {
            this.state = state;
            edge = new ArrayList<>();
            desNode = new ArrayList<>();
            start = false;
            end = false;
        }

        public void addEdge(Character s, NFANode d)
        {
            edge.add(s);
            desNode.add(d);
        }

        public boolean hasPath(char c)
        {
            for(Character x:edge)
            {
                if(x.equals(c))
                    return true;
            }
            return false;
        }

        @Override
        public int compareTo(Object o) {
            if(o instanceof NFANode)
            {
                NFANode node = (NFANode) o;
                return this.state.compareTo(node.state);
            }
            throw new ClassCastException("Cannot compare Pair with "
                    + o.getClass().getName());
        }
    }


    public NFA()
    {
        nfa = new ArrayList<>();
    }

    /**
     * ��ӿ�ʼ״̬
     */
    private void addStart(String s)
    {
        if(nfa==null)
            return;
        getNode(s).start = true;
    }

    private void addEnd(String s)
    {
        if(nfa==null)
            return;
        getNode(s).end = true;
    }

    /**
     * ���״̬��Ϊs��״̬֮NFA
     */
    private void addNodeToNFA(String s)
    {
        if(nfa==null)
            return;
        nfa.add(new NFANode(s));
    }

    private void addNodeToNFA(NFANode node)
    {
        if(nfa==null)
            return;
        nfa.add(node);
    }

    private void addEdgeToState(String state, Character edge, String des)
    {
        if(this.nfa == null)
            return;
        NFANode from = getNode(state);
        NFANode to = getNode(des);
        from.addEdge(edge, to);
    }

    /**
     * ��ȡ״̬��Ϊstate�Ľڵ�
     */
    private NFANode getNode(String state)
    {
        if(nfa == null)
            return null;
        for(NFANode n:this.nfa)
            if(n.state.equals(state))
                return n;
        return null;
    }

    /**
     * ��ȡ��ʼ״̬
     */
    public NFANode getStart()
    {
        if(nfa==null)
            return null;
        for(NFANode n:nfa)
            if(n.start)
                return n;
        return null;
    }

    /**
     * ��ȡ�ս�״̬
     */
    public NFANode getEnd()
    {
        if(nfa==null)
            return null;
        for(NFANode n:nfa)
            if(n.end)
                return n;
        return null;
    }

    /**
     * ��Ϊһ�����NFA�����кܶ�״̬��NFA���������С��NFAƴ�Ӷ��ɵġ�
     * ��С��NFA�Ǹ������ɵģ�����ζ�����ǵ�״̬����������
     * �������������NFA��״̬����������
     */
    private void stateSort()
    {
        for(int i=0;i<nfa.size();++i)
            nfa.get(i).state = String.valueOf(i);
    }

    /**
     * ���Ӳ���
     * @param n2
     */
    public void connect(NFA n2)
    {
        if(n2.nfa.size()==0)
            return;
        if(nfa.size()==0)
        {
            this.nfa = n2.nfa;
            return;
        }
        NFANode n = getEnd();
        n.end = false;
        n.addEdge('\0', n2.getStart());
        n2.getStart().start = false;
        for(NFANode node:n2.nfa)
            this.addNodeToNFA(node);
        this.stateSort();
    }

    /**
     * ������
     * @param n2
     */
    public void parallel(NFA n2)//TODO
    {
        NFA n0 = new NFA();
        n0.addNodeToNFA("S");
        n0.addStart("S");
        NFANode s1 = this.getStart();
        NFANode s2 = n2.getStart();
        n0.getStart().addEdge('\0', s1);
        n0.getStart().addEdge('\0', s2);
        s1.start = false;
        s2.start = false;
        n0.addNodeToNFA("F");
        n0.addEnd("F");
        NFANode e1 = this.getEnd();
        NFANode e2 = n2.getEnd();
        e1.addEdge('\0', n0.getEnd());
        e2.addEdge('\0', n0.getEnd());
        e1.end = false;
        e2.end = false;
        for(NFANode node:this.nfa)
            n0.addNodeToNFA(node);
        for(NFANode node:n2.nfa)
            n0.addNodeToNFA(node);
        n0.stateSort();
        this.nfa = n0.nfa;
    }

    /**
     * Ϊһ��NFA��ӱհ�
     */
    public void closure()//TODO
    {
        NFA n = new NFA();
        n.addNodeToNFA("S");
        n.addNodeToNFA("E");
        n.addStart("S");
        n.addEnd("E");
        NFANode s = this.getStart();
        NFANode e = this.getEnd();
        s.start = false;
        e.end = false;
        n.getStart().addEdge('\0', s);
        e.addEdge('\0', s);
        e.addEdge('\0', n.getEnd());
        n.getStart().addEdge('\0', n.getEnd());
        for(NFANode node:this.nfa)
            n.addNodeToNFA(node);
        n.stateSort();
        nfa = n.nfa;
    }

    /**
     * ����һ��NFAʵ��������һ����ʼ״̬��һ���ս�״̬��һ�������ַ�Ϊc�ıߡ�
     */
    public static NFA ins(Character c)
    {
        NFA n = new NFA();
        n.addNodeToNFA("S");
        n.addStart("S");
        n.addNodeToNFA("E");
        n.addEnd("E");
        n.addEdgeToState("S", c, "E");
        n.stateSort();
        return n;
    }

    /**
     * ģ��NFA��ƥ���ַ���s�������������ת����DFA��
     */
    public boolean match(String s)//TODO �����������
    {
        if(nfa==null)
            return false;
        NFANode startNode = null;
        for(NFANode n:nfa)
            if(n.start==true)
                startNode = n;

        for(Character c:s.toCharArray())
        {
            if(startNode.hasPath(c))
                startNode = startNode.desNode.get(startNode.edge.indexOf(c));
            else
                return false;
        }
        if(startNode.end==true)
            return true;
        else
            return false;
    }

    public static void main(String[] args) {
        char c = (char)2;
        NFA n1 = NFA.ins(c);
        NFANode node = n1.getStart();

        System.out.println(node.hasPath(c));
        System.out.println(n1.match(String.valueOf(c)));
    }


}

