import cn.eovie.re.Regex;
import com.sun.org.apache.regexp.internal.RE;
import org.junit.Test;

/**
 * Created by earayu on 2017/6/16.
 */
public class testRegex {

    @Test
    public void test1()
    {
        Regex re = new Regex("a.a*");
        assert !re.match("");
        assert re.match("a");
        assert re.match("aa");
        assert re.match("aaa");
        assert re.match("aaaa");
    }

    @Test
    public void test2()
    {
        char a = '你';
        Regex re = new Regex("a|b");
        assert re.match("a");
        assert re.match("b");
        assert !re.match("ab");
    }

    @Test
    public void test3()
    {
        Regex re = new Regex("a*");
        assert re.match("");
        assert re.match("a");
        assert re.match("aaaa");
        assert !re.match("b");
    }

    @Test
    public void test4()
    {
        Regex re = new Regex("(a|b)*");
        assert re.match("");
        assert re.match("a");
        assert re.match("b");
        assert re.match("aaa");
        assert re.match("bbb");
        assert re.match("abab");
    }

    @Test
    public void test5()
    {
        Regex re = new Regex("(a|b)*|(c|d)*");
        assert re.match("");
        assert re.match("a");
        assert re.match("b");
        assert re.match("c");
        assert re.match("d");
        assert re.match("ababaaaabbab");
        assert re.match("cccccccccdcddd");
    }

    @Test
    public void test6()
    {
        Regex re = new Regex("(a|b)|(c|d)");
        System.out.println(re.match("ac"));
    }


}
