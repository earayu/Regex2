package cn.eovie.re;

/**
 * Created by earayu on 2017/6/16.
 */
import cn.eovie.fa.DFA;
import cn.eovie.fa.NFA;
import cn.eovie.util.Utils;

import java.util.Stack;
import java.util.StringTokenizer;

/**
 * ����������ʽ
 * ʵ����.*|() ��ת��
 * ����\s \w . + ?
 * @author earayu
 * //TODO 待重构
 *
 */
public class Regex {

    private String re;
    private DFA dfa;
    private String escape = "A.*|()\\+?";//Ϊ����ת��ĵ�һ���ַ�����Ϊ1�����ϸ�A��A��ת�⣬��������ν����

    public Regex(String s)
    {
        re = s;
        makeDFA();
    }

    public String getRE()
    {
        return re;
    }

    private boolean isCharacter(char c) {
        char[] alp = Utils.alphetbet1.toCharArray();
        for(int j=0;j<alp.length;++j)
        {
            if(alp[j]==c)
                return true;
        }
        return false;
    }

    /**
     * ��������ʽת�塣
     * @param s
     * @return
     */
    private String escapeRE(String s)
    {
        for(int i=1;i<s.length();++i)
        {
            if(s.charAt(i-1)=='\\' &&
                    (	s.charAt(i)=='.'|| s.charAt(i)=='|' || s.charAt(i)=='*'
                            ||	s.charAt(i)=='(' || s.charAt(i)==')' || s.charAt(i)=='\\'
                            //||  s.charAt(i)=='+' || s.charAt(i)=='?'
                    )
                    )
            {
                String h = s.substring(0,i-1);
                String m = String.valueOf( (char)escape.indexOf( s.charAt(i) ) );
                String t = s.substring(i+1,s.length());
                s = h + m + t;
            }
        }
        return s;
    }

    private String addDot(String re) {
        StringBuffer sb = new StringBuffer();
        sb.append(re.charAt(0));
        for(int i=1; i<re.length(); i++) {
            //������������(.)
            boolean flag = isCharacter(re.charAt(i)) && (re.charAt(i-1) == '*' || re.charAt(i-1) == ')' || isCharacter(re.charAt(i-1)))
                    || (re.charAt(i) == '(' && (isCharacter(re.charAt(i-1)) || re.charAt(i-1)==')' || re.charAt(i-1)=='*'));
            if(flag)
                sb.append('.');
            sb.append(re.charAt(i));
        }
        return sb.toString();
    }


    private String infixToPostfix(String expression) {
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



    private NFA evaluateExpression(String postfix) {
        Stack<NFA> operandStack = new Stack<>();
        for(int i=0;i<postfix.length();++i) {
            char c = postfix.charAt(i);
            if(c == '*') {
                NFA nfa = operandStack.pop();
                nfa.closure();
                operandStack.push(nfa);
            } else if(c == '|' || c == '.') {
                processAnOperator(operandStack, c);
            }
            else {
                operandStack.push(NFA.ins(c));
            }
        }
        return operandStack.pop();
    }

    //����һ��˫Ŀ���������
    private void processAnOperator(Stack<NFA> operandStack, char c) {
        NFA op1 = operandStack.pop();	//������1
        NFA op2 = operandStack.pop();	//������2
        if(c == '|') {			//connect����
            op2.parallel(op1);
            operandStack.push(op2);
        } else if(c == '.') {	//concatenation����
            op2.connect(op1);
            operandStack.push(op2);
        }
    }

    /*
     * Ԥ����������ʽ��ת��,�����﷨�ǣ�δʵ�֣�,������ӷ�,ת�ɺ�׺ʽ��.
     */
    private String pre()
    {
        return infixToPostfix(addDot(/*candy*/(escapeRE(re))));
    }

    private String candy(String s)
    {
        //.
        for(int i=0;i<s.length();++i)
            if(s.charAt(i)=='.')
                s = s.substring(0,i) + "( |!|\"|#|$|%|&|'|,|-|/|0|1|2|3|4|5|6|7|8|9|:|;|<|=|>|@|A|B|C|D|E|F|G|H|I|J|K|L|M|N|O|P|Q|R|S|T|U|V|W|X|Y|Z|[|]|^|_|`|a|b|c|d|e|f|g|h|i|j|k|l|m|n|o|p|q|r|s|t|u|v|w|x|y|z|{|}|~|||||||)"
                        + s.substring(i+1,s.length());
                //+
            else if(s.charAt(i)=='+')//TODO �������žʹ��� (a|b)+
            {
                if(i==0)
                    try {
                        throw new Exception("������ʽ�﷨����: '+' ��������λ");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                s = s.substring(0,i) + s.charAt(i-1) + "*" + s.substring(i+1,s.length());
            }
            //?
            else if(s.charAt(i)=='?')//TODO
            {
                if(i==0)
                    try {
                        throw new Exception("������ʽ�﷨����: '?' ��������λ");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                s = s.substring(0,i-1) + "(" + s.charAt(i-1) + "|" + (char)0 + ")" + s.substring(i+1,s.length());
            }
            else if(s.charAt(i)=='s')
            {
                if(i-1>=0 && s.charAt(i-1)=='\\')
                    s = s.substring(0,i-1) + "( |	|" + (char)10+ "|" + (char)13 +")" + s.substring(i+1,s.length());
            }
            else if(s.charAt(i)=='w')
            {
                if(i-1>=0 && s.charAt(i-1)=='\\')
                    s = s.substring(0,i-1) + "(0|1|2|3|4|5|6|7|8|9|A|B|C|D|E|F|G|H|I|J|K|L|M|N|O|P|Q|R|S|T|U|V|W|X|Y|Z|_|a|b|c|d|e|f|g|h|i|j|k|l|m|n|o|p|q|r|s|t|u|v|w|x|y|z)" + s.substring(i+1,s.length());
            }

        //[a-zA-z0-9]

        //{m,n}

        return s;
    }

    //���ݺ�׺ʽ����NFA, Ȼ������DFA
    private void makeDFA()
    {
        String preRE = pre();
        long t0 = System.currentTimeMillis();
        NFA nfa = this.evaluateExpression(preRE);
        long t1 = System.currentTimeMillis();
        this.dfa = new DFA(nfa);
        long t2 = System.currentTimeMillis();
        System.out.println("nfa:" + (t1-t0) + "ms");
        System.out.println("dfa:" + (t2-t1) + "ms");
    }

    /**
     * Ҫƥ��ı��ʽû�бհ������Ӻͻ����㣬����������Щ�ַ�ֱ��ת�壬Ȼ��ģ��DFA����ƥ�䡣
     */
    public boolean match(String s)
    {
        for(int i=0;i<s.length();++i)
        {
            if(s.charAt(i)=='.'|| s.charAt(i)=='|' || s.charAt(i)=='*'
                    || s.charAt(i)=='(' || s.charAt(i)==')' || s.charAt(i)=='\\'
                //|| s.charAt(i)=='+' || s.charAt(i)=='?'
                    )
            {
                String h = s.substring(0,i);
                String m = String.valueOf((char)escape.indexOf(s.charAt(i)));
                String t = s.substring(i+1,s.length());
                s = h + m + t;
            }
        }
        return dfa.match(s);
    }

    public boolean contains(String s)
    {
        return dfa.contains(s);
    }

    public int search(String s)
    {
        return dfa.search(s);
    }
}
