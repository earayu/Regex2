package cn.eovie.util;

/**
 * Created by earayu on 2017/6/16.
 */

import cn.eovie.fa.NFA;
import cn.eovie.fa.NFA.NFANode;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;


//TODO 待重构
public class Utils {

    //ע�⣺.*|()\��Щ��Ҫת����ַ������ܼ���alphetbet��Ҫ������ת����ֵ���룡��
    //��Ϊ����Ҫ�����Ǳ����׺ʽ���������Ǳ������ǡ��ַ��������������Ǻ��ַ�֮�����'.'
    //���磺��a*������׺ʽ���ǡ�a*�������ǲ�����a��*֮�����'.'����a.*���Ǵ���ġ�
    //����a\\*������׺ʽΪ��a.����  ������ֵΪ��\����RE����escape�ַ����е�����λ��.
    public static String alphetbet = (char)10+(char)13+ new String(" 	!\"#$%&',-/0123456789:;<=>@ABCDEFGHIJKLMNOPQRSTUVWXYZ[]^_`abcdefghijklmnopqrstuvwxyz{}~");
    public static String alphetbet1 = new String(" !\"#$%&'+,-/0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[]^_`abcdefghijklmnopqrstuvwxyz{}~?");
    public static char[] alphetbet2 = {' ','!','\"','#','$','%','&','\'','+',',','-','/','0','1','2','3','4','5','6','7','8','9',':',';','<','=','>','?','@','A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z','[',']','^','_','`','a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z','{','}','~','','?','','','','','','',};



    /**
     * ����NFA��һ��״̬�ıհ��������һ�����ϣ������һ�����Ա��з��ء�
     */
    public static HashSet<NFA.NFANode> eClosure(NFA.NFANode n)
    {
        Queue<NFA.NFANode> que = new LinkedList<>();
        HashSet<NFA.NFANode> dstates = new HashSet<>();
        dstates.add(n);
        que.offer(n);
        while(!que.isEmpty())
        {
            NFA.NFANode node = que.poll();
            for(int i=0;i<node.edge.size();++i)
            {
                if(node.edge.get(i).equals('\0'))
                {
                    //�жϸ�Ԫ����û�б����������û�г�����dstates�У�˵����Ԫ���ǵ�һ�α����ʡ�����
                    //Ҫ������У���Ȼ��������У����ⷢ��ѭ������(a*)*�������
                    if(!dstates.contains(node.desNode.get(i)))
                    {
                        dstates.add(node.desNode.get(i));
                        que.offer(node.desNode.get(i));
                    }
                }
            }
        }
        return dstates;
    }


    /**
     * ����һ������������״̬�ıհ�
     */
    public static HashSet<NFANode> eClosure(HashSet<NFANode> T)//600ms
    {

        HashSet<NFANode> result = new HashSet<>();
        for(NFANode node:T)
        {
            //��T�е�ÿһ���ڵ㶼ʹ��eClosure(NFANode n)����
            HashSet<NFANode> r = eClosure(node);
            //Ȼ�󽫽ڵ㲻�ظ��ؼ���result
            result.addAll(r);
        }

        return result;
    }
    /**
     * ���ݡ��Ӽ����취����д�ķ���������һ�������ڵ�״̬����ĳ���ַ���Ľ����
     */
    public static HashSet<NFANode> move(HashSet<NFANode> T, Character s)
    {

        HashSet<NFANode> dstates = new HashSet<>();

        for(NFANode node:T)
            for(int i=0;i<node.edge.size();++i)//
                if(node.edge.get(i).equals(s))
                    dstates.add(node.desNode.get(i));

        return dstates;
    }


    public static boolean inResult(HashSet<HashSet<NFANode>> result, HashSet<NFANode> T)
    {
        return result.contains(T);
    }

}
