import org.junit.Test;

import java.util.Stack;
import java.util.StringTokenizer;

/**
 * Created by earayu on 2017/6/26.
 */
public class test {

    @Test
    public void test1()
    {
        System.out.println(infix2Pos("(a.b)|c"));
    }

    public static String infix2Pos(String exp)
    {
        StringBuffer postfix = new StringBuffer();
        //�洢��������ջ
        Stack<Character> operatorStack = new Stack<Character>();
        // ����������������ֿ�
        StringTokenizer tokens =
                new StringTokenizer(exp, "()*|.", true);
        // �׶�1: ɨ����Ŵ�
        while(tokens.hasMoreTokens()) {
            String token = tokens.nextToken();
            if(token.charAt(0) == '|') {
                // Process all * , . in the top of the operator stack
                while(!operatorStack.isEmpty()
                        && (operatorStack.peek() == '*' || operatorStack.peek() == '.')) {
                    postfix.append(operatorStack.pop());
                }
                // Push the | operator into the operator stack
                operatorStack.push(token.charAt(0));

            } else if(token.charAt(0) == '.') {
                // Process all . in the top of the operator stack
                while (!operatorStack.isEmpty() && operatorStack.peek().equals('.')) {
                    postfix.append(operatorStack.pop());
                }
                // Push the . operator into the operator stack
                operatorStack.push(token.charAt(0));

            } else if(token.charAt(0) == '*') {
                postfix.append(token.charAt(0));
            } else if(token.charAt(0) == '(') {
                operatorStack.push(new Character('(')); // Push '(' to stack
            } else if(token.charAt(0) == ')') {
                // Process all the operators in the stack until seeing '('
                while (!operatorStack.peek().equals('(')) {
                    postfix.append(operatorStack.pop());
                }
                operatorStack.pop();

            } else {
                postfix.append(token);
            }
        }

        // �׶� 2: process all the remaining operators in the stack
        while(!operatorStack.isEmpty()) {
            postfix.append(operatorStack.pop());
        }
        return postfix.toString();
    }

    @Test
    public void testChar()
    {
        for(int i=0;i<255;i++)
        {
            System.out.print((char) i);
        }
    }

}
