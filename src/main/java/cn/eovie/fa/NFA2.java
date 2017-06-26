package cn.eovie.fa;

import java.util.ArrayList;
import java.util.Stack;
import java.util.StringTokenizer;

/**
 * Created by earayu on 2017/6/26.
 */
public class NFA2 {

    private NFA2Node startNode;
    private ArrayList<NFA2Node> endNode;//TODO 改成set？


    public NFA2(char c)
    {
        endNode = new ArrayList<>();
        NFA2Node s = NFA2Node.newStartNode();
        NFA2Node e = NFA2Node.newEndNode();
        s.addEdge(c, e);
        this.startNode = s;
        this.endNode.add(e);
    }

    public NFA2 connect(final NFA2 target)
    {
        this.endNode.stream().forEach(e->{e.edges.addAll(target.startNode.edges);e.end=false;});
        this.endNode = target.endNode;
        return this;
    }

    public NFA2 parallel(NFA2 target)
    {
        this.startNode.edges.addAll(target.startNode.edges);
        this.endNode.addAll(target.endNode);
        return this;
    }

    public NFA2 closure()
    {
        this.startNode.changeAllEdgeDes(this.startNode);
        this.endNode.clear();
        this.endNode.add(this.startNode);
        this.startNode.end = true;
        return this;
    }

    //其实就是一个DFS算法
    public boolean match(String s)
    {
        NFA2Node start = startNode;
        for(Character c:s.toCharArray())
        {
            NFA2Node node = start.hasPath(c);
            if(node!=null)
                start = node;
            else
                return false;
        }
        if(start.end==true)//end节点
            return true;
        else
            return false;
    }


    public static NFA2 evaluateExpression(String postfix) {
        Stack<NFA2> stack = new Stack<>();

        for(char c:postfix.toCharArray())
        {
            switch (c)
            {
                case '*':
                    stack.push(stack.pop().closure());
                    break;
                case '|': {
                    NFA2 n1 = stack.pop();
                    NFA2 n2 = stack.pop();
                    stack.push(n2.parallel(n1));
                }
                break;
                case '.': {
                    NFA2 n1 = stack.pop();
                    NFA2 n2 = stack.pop();
                    stack.push(n2.connect(n1));
                }
                    break;
                default:
                    stack.push(new NFA2(c));
            }
        }

        return stack.pop();
    }

    private static String infixToPostfix(String expression) {
        StringBuffer postfix = new StringBuffer();
        Stack<Character> operatorStack = new Stack<Character>();
        StringTokenizer tokens =
                new StringTokenizer(expression, "()*|.", true);
        while(tokens.hasMoreTokens()) {
            String token = tokens.nextToken();
            if(token.charAt(0) == '|') {
                while(!operatorStack.isEmpty()
                        && (operatorStack.peek() == '*' || operatorStack.peek() == '.')) {
                    postfix.append(operatorStack.pop());
                }
                operatorStack.push(token.charAt(0));

            } else if(token.charAt(0) == '.') {
                while (!operatorStack.isEmpty() && operatorStack.peek().equals('.')) {
                    postfix.append(operatorStack.pop());
                }
                operatorStack.push(token.charAt(0));

            } else if(token.charAt(0) == '*') {
                postfix.append(token.charAt(0));
            } else if(token.charAt(0) == '(') {
                operatorStack.push(new Character('('));
            } else if(token.charAt(0) == ')') {
                while (!operatorStack.peek().equals('(')) {
                    postfix.append(operatorStack.pop());
                }
                operatorStack.pop();
            } else {
                postfix.append(token);
            }
        }

        while(!operatorStack.isEmpty()) {
            postfix.append(operatorStack.pop());
        }
        return postfix.toString();
    }

    public static void main(String[] args) {
        NFA2 nfa1 = new NFA2('a');
        NFA2 nfa2 = new NFA2('b');
        NFA2 nfa3 = new NFA2('c');
        NFA2 nfa4 = new NFA2('d');

        String regex = "(a|b)*|c*.d";
        System.out.println(infixToPostfix(regex));
        NFA2 nfa = NFA2.evaluateExpression(infixToPostfix(regex));

        System.out.println(nfa.match("abbababababababa"));
        System.out.println(nfa.match(""));
        System.out.println(nfa.match("d"));
        System.out.println(nfa.match("cd"));
        System.out.println(nfa.match("cccccd"));
    }

}
