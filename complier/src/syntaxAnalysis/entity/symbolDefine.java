package syntaxAnalysis.entity;

import java.util.HashMap;

/**
 * @Package: CONST
 * @ClassName: symbolDefine
 * @Author: panhengyu
 * @CreateTime: 2021-03-29 10:44
 * @Description: 终结符和非终结符(char)->int的映射或者是int->char的映射
 */
public class symbolDefine {
    public static int UNKNOWN_SYMBOL=-2;//不合法的字符
    public static int EOI=-1;//LOOK head集合中默认的元素,相当于#
    public static int STMT=0;//二型文法的开始符号
    public static String getSymbolStr(int symbol){ return ""; }

    //define 终结符和终结符所对应的数值
    //第一个参数表示终结符符和非终结符在输入时的符号，
    //第二参数表示的终结符号与非终结符在程序中的编号
    public  HashMap<Character,Integer>symbolMap;
    //define token在系统中所对应的值
    public HashMap<String,Integer> tokensMap;
    public symbolDefine(){
        symbolMap=new HashMap<Character,Integer>();
        tokensMap=new HashMap<String,Integer>();

        //test 1

        /*symbolMap.put('S',0);
        symbolMap.put('A',1);
        symbolMap.put('B',2);
        symbolMap.put('a',256);
        symbolMap.put('b',257);
*/
        //test 2
        /*symbolMap.put('S',0);
        symbolMap.put('A',1);
        symbolMap.put('B',2);
        symbolMap.put('a',256);
        symbolMap.put('b',257);
        symbolMap.put('c',258);
        symbolMap.put('d',259);
        symbolMap.put('e',260);
*/
        //test3
        /*symbolMap.put('S',0);
        symbolMap.put('A',1);
        symbolMap.put('B',2);
        symbolMap.put('a',256);
        symbolMap.put('(',257);
        symbolMap.put(')',258);
        symbolMap.put('*',259);//*表示空符号
        */

        //设定编号小于256的都是非终结符
        //非终结符初始化,
        symbolMap.put('S',0);   //<开始符号>
        symbolMap.put('A',1);   //<程序>
        symbolMap.put('B',2);   //<语句>
        symbolMap.put('C',3);   //<常量说明>
        symbolMap.put('D',4);   //<变量说明>
        symbolMap.put('E',5);   //<常量定义Ⅰ>
        symbolMap.put('F',6);   //<常量定义Ⅱ>
        symbolMap.put('G',7);   //<标识符Ⅰ>
        symbolMap.put('H',8);   //<赋值语句>
        symbolMap.put('I',9);   //<表达式>
        symbolMap.put('J',10);   //<条件语句>
        symbolMap.put('K',11);  //<条件>
        symbolMap.put('L',12);  //<当循环语句>
        symbolMap.put('M',13);  //<读入语句>
        symbolMap.put('N',14);  //<输出语句>
        symbolMap.put('O',15);  //<复合语句>
        symbolMap.put('P',16);  //<语句Ⅰ>
        symbolMap.put('Q',17);  //<表达式串>


        //终结符初始化
        symbolMap.put('a',256);tokensMap.put("Const",256);//Const
        symbolMap.put('b',257);tokensMap.put("identiter",257);//<标识符>
        symbolMap.put('c',258);tokensMap.put("const",258);//<常量>
        symbolMap.put('d',259);tokensMap.put("=",259);//=
        symbolMap.put('e',260);tokensMap.put("int",260);tokensMap.put("float",260);tokensMap.put("boolean",260);//int|float|boolean
        symbolMap.put('f',261);tokensMap.put(";",261);  //;
        symbolMap.put('g',262);tokensMap.put(",",262);  //,
        symbolMap.put('h',263);tokensMap.put("if",263);  //if
        symbolMap.put('i',264);tokensMap.put("then",264);  //then
        symbolMap.put('j',265);tokensMap.put("else",265);  //else
        symbolMap.put('k',266);tokensMap.put("==",266);tokensMap.put("!=",266);tokensMap.put(">",266);tokensMap.put(">=",266);tokensMap.put("<",266);tokensMap.put("<=",266);  //<关系运算符>
        symbolMap.put('l',267);tokensMap.put("while",267);  //while
        symbolMap.put('m',268);tokensMap.put("do",268);  //do
        symbolMap.put('n',269);tokensMap.put("cin",269);  //cin
        symbolMap.put('o',270);tokensMap.put("(",270);  //(
        symbolMap.put('p',271);tokensMap.put(")",271);  //)
        symbolMap.put('q',272);tokensMap.put("cout",272);  //cout
        symbolMap.put('r',273);tokensMap.put("begin",273);  //begin
        symbolMap.put('s',274);tokensMap.put("end",274);  //end
        symbolMap.put('t',275);tokensMap.put("+",275);tokensMap.put("-",275);  //<加法运算符>
        symbolMap.put('u',276);tokensMap.put("*",276);tokensMap.put("/",276);  //<乘法运算符>
        symbolMap.put('v',277);tokensMap.put("{",277);//{
        symbolMap.put('w',278);tokensMap.put("}",278);//}
    }

    /**
     * 判断该符号是否是终结符
     * @param symbol
     * @return
     */
    public static boolean isSymbolTerminals(int symbol){
        if(symbol<256) return false;
        else return true;
    }

}
