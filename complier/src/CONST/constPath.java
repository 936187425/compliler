package CONST;


/**
 * @Package: PACKAGE_NAME
 * @ClassName: constPath
 * @Author: panhengyu
 * @CreateTime: 2021-03-21 13:55
 * @Description:项目常用的参数比如文件路径等
 */
public class constPath {
    public  static final int GET_SOURCE_TEXT=0;
    public  static final int GET_LEXICAL_TEXT=1;
    public  static final int GET_SYNTAX_TEXT=2;
    public  static final int GET_GRAMMAR_TEXT=3;

    public  static final String INPUT_SOURCE_PATH= "src/testSource.txt";//测试源代码路径
    public  static final String INPUT_LEXICAL_PATH= "src/testLexical.txt";//词法分析的3型文法文件路径
    public  static  final String INPUT_SYNTAX_PATH= "src/testSyntax.txt";//语法分析的2型文法文件路径
    public  static final String INPUT_GRAMMAR_PATH= "src/testGrammar.txt";//语义分析的上下文无关文法+属性文法的路径
}
