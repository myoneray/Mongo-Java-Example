package regexmatches;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 如何从一个给定的字符串中找到数字串：
 * 
 * @author upsmart
 *
 */
public class RegexMatches {
    // Pattern类：
    // pattern对象是一个正则表达式的编译表示。
    // Pattern类没有公共构造方法。要创建一个Pattern对象，你必须首先调用其公共静态编译方法，它返回一个Pattern对象。
    // 该方法接受一个正则表达式作为它的第一个参数。
    // 按指定模式在字符串查找

    // Matcher类：
    // Matcher对象是对输入字符串进行解释和匹配操作的引擎。
    // 与Pattern类一样，Matcher也没有公共构造方法。
    // 你需要调用Pattern对象的matcher方法来获得一个Matcher对象。

    // PatternSyntaxException：
    // PatternSyntaxException是一个非强制异常类，它表示一个正则表达式模式中的语法错误。
    public static void main(String args[]) {
        Pattern pattern = Pattern.compile("\\bcat\\b");
        Matcher matcher = pattern.matcher("cat cat cat cattie cat");
        int count = 0;

        while(matcher.find()) {
          count++;
          System.out.println("Match number "+count);
          System.out.println("start(): "+matcher.start());
          System.out.println("end(): "+matcher.end());
       }
    }
}
