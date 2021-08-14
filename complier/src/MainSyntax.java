
import lexicalAnalysis.entity.constLexiKeyMap;
import lexicalAnalysis.entity.FormalGrammar;
import lexicalAnalysis.entity.Token;
import lexicalAnalysis.entity.dfa.Dfa;
import lexicalAnalysis.entity.dfa.DfaFunction;
import lexicalAnalysis.entity.dfa.DfaNode;
import lexicalAnalysis.entity.nfa.Nfa;
import fileUtils.inputMain;
import lexicalAnalysis.lexicalAnalysisUtils;
import sun.applet.Main;
import syntaxAnalysis.LRStateTableParser;
import syntaxAnalysis.entity.symbolDefine;
import syntaxAnalysis.grammarStateManager;
import syntaxAnalysis.productionManager;

import java.util.*;

/**
 * @Package: lexicalAnalysis
 * @ClassName: mainLexical
 * @Author: 潘恒宇
 * @CreateTime: 2021-03-21 13:08
 * @Description:
 */
public class MainSyntax {
    private inputMain input;
    lexicalAnalysisUtils lAUtils=new lexicalAnalysisUtils();

    public MainSyntax() {
        input=new inputMain();
    }

    /*
    词法分析器的入口函数
     */
    public Queue<Token> getTokens() {
        /*
        就绪工作：先把用户输入的正规文法转化NFA,然后转化为DFA
        procession：从头到尾，从左到右依次扫描输入的源代码,通过状态机自动识别和查表等方法,最后生成token序列组
         */
        Set<FormalGrammar> FGrammars = lAUtils.tLexical2FGrammar();
        Set<Nfa> nfas=lAUtils.fGrammar2Nfa(FGrammars);
        Set<Dfa> dfas=lAUtils.nfa2Dfa(nfas);
        Queue<Token> tokens=new  LinkedList<Token>();


        //遍历获得以换行符和空格为分隔符的单词单元(不一定是最终tokens获得单词单元)
        Token nowWord=divideWord();
        int endNum=0;
        while(!nowWord.getContent().equals("end")) {
            input.i_mark_start();
            nowWord=divideWord();
            isTypeofWord(nowWord,dfas,tokens);
            input.i_mark_end();
            input.i_mark_prev();
        }


        return tokens;

    }

    /**
     * 获取一个单词(单词之间以空格或换行符为分隔符)
     * @param
     * */
    public Token divideWord(){
        byte c;
        StringBuffer out=new StringBuffer();
        while((c=input.i_advance())!=' '){//空格作为单词之间的分割线 还有换行符
            if(c=='\n') break;
            byte[] Byte=new byte[1];
            Byte[0]=c;
            out.append(new String(Byte));
        }
        Token token=new Token();
        token.setContent(out.toString());
        token.setRow(c=='\n'?input.i_Nlineno()-1:input.i_Nlineno());
        return token;
    }


    /**
     * 对于输入的token(一个token)进行分析
     * 分析的内容:
     * if token.content为空说明该行为空 then return
     * if token.content是表达式 then
     * else
     * @param token
     * @param dfas
     * @param tokens
     */
    public void isTypeofWord(Token token,Set<Dfa> dfas,Queue<Token>tokens){
        //作为判断关键字、限定符以及运算符的索引表
        constLexiKeyMap keyMap=new constLexiKeyMap();
        if(token.getContent().equals("")) return ;
        Dfa[] d=new Dfa[2];
        for(Dfa dfa:dfas){
            if(dfa.getTitle().equals("isIdentier")) d[0]=dfa;
            if(dfa.getTitle().equals("isConst")) d[1]=dfa;
        }
        Boolean[] flag=new Boolean[5];
        for(int i=0;i<5;i++) flag[i]=false;
        //判断关键字
        flag[0]=keyMap.getKeySet().contains(token.getContent());
        //判断限定符
        flag[1]=keyMap.getRestrictSet().contains(token.getContent());
        //判断运算符
        flag[2]=keyMap.getOperatorSet().contains(token.getContent());
        //判断常数
        flag[3]=isAcceptConst(token.getContent(),d[1]);
        //判断标识符
        flag[4]=isAcceptIdentiter(token.getContent(),d[0]);

        if(flag[0]) {//token是关键字
                token.setType("key");
                tokens.add(token);
                return ;
        }
        if(flag[1]){//token是限定符
                    token.setType("restriction");
                    tokens.add(token);
                    return;
        }
        if(flag[2]){//token是操作符
                token.setType("operator");
                tokens.add(token);
                return ;
        }
        if(flag[3]){//token是常数
                token.setType("const");
                tokens.add(token);
                return ;
        }
        if(flag[4]){//token是标识符
            token.setType("identiter");
            tokens.add(token);
            return ;
        }else{
            char first=token.getContent().charAt(0);
            if(first<='9'&&first>='0'){
                System.out.println("!Error line:"+token.getRow()+" "+token.getContent()+" cause:标识符首字母不能为数字!");
                }else{
                System.out.println("!Error line:"+token.getRow()+" "+token.getContent()+" cause:标识符存在非法字符!");
            }
            return ;
        }

    }

    /**
     * 判断输入的input字符串在isConstDfa下是否是可接受状态
     * @param input
     * @param dfa
     * @return
     */
    public boolean isAcceptConst(String input,Dfa dfa){
        int nowState=0;
        /*不可接受的状态是：
            1、输入不合法的字符
            2、输入正确的字符但是没有相应的转换函数
            3、没有字符输入时的状态不是可接受状态
        */
        for(int i=0;i<input.length();i++){

            char mapping=mapConstDfa(input.charAt(i));
            if(mapping=='~') return false;//不合法字符串

            for(DfaNode node:dfa.getDfaNodes()){
                int nodeState=node.getState();
                if(nodeState==nowState){
                    boolean flag=false;
                    for(DfaFunction f:node.getFunctionSets()){
                        if(f.getInputSymbol()==mapping){
                            flag=true;
                            nowState=f.getEndState();
                            break;
                        }
                    }
                    if(!flag) return false;
                    break;
                }
            }
        }
        if(dfa.getEndStateSet().contains(nowState))
            return true;
        else
            return false;
    }

    public char mapConstDfa(char in){
        //a:[0-9],b:. c:e/E d:+ - e:i *:other
        if(in<='9'&&in>='0') return 'a';
        if(in=='.') return 'b';
        if(in=='e'||in=='E') return 'c';
        if(in=='+'||in=='-') return 'd';
        if(in=='i') return'e';
        return '~';
    }

    /**
     * 判断输入的input字符串在isIdentiterDfa下是否是可接受状态
     * @param input
     * @param dfa
     * @return
     */
    public boolean isAcceptIdentiter(String input,Dfa dfa){
       int nowState=0;
        /**
         * 不可接受状态:
         * 1、输入的字符不是合法的
         * 2、输入的字符在相应的状态下没有转换函数
         * 3、字符输完后此时状态是不可接受状态
         */
        for(int i=0;i<input.length();i++){
            char mapping=mapIdentiterDfa(input.charAt(i));
            if(mapping=='~') return false;

            for(DfaNode node:dfa.getDfaNodes()){
                int nodeState=node.getState();
                if(nodeState==nowState){
                    boolean flag=false;
                    for(DfaFunction f:node.getFunctionSets()){
                        if(f.getInputSymbol()==mapping){
                            flag=true;
                            nowState=f.getEndState();
                            break;
                        }
                    }
                    if(!flag) return false;
                    break;
                }
            }
        }
        if(dfa.getEndStateSet().contains(nowState))
            return true;
        else
            return false;
    }

    public char mapIdentiterDfa(char in){
        //a:_ , b:[0-9],c:A-Z;a-z
        if(in=='_') return 'a';
        if(in<='9'&&in>='0') return 'b';
        if((in<='z'&&in>='a')||(in<='Z'&&in>='A')) return 'c';
        return '~';
    }



    /*************************************语法分析*************************************************/

    public  void bottomUpParser(Queue<Token> tokens){

        //在创建productionManager的同时也会创建并初始化FirstSetBuilder
        productionManager  productionManager= syntaxAnalysis.productionManager.getProductionManager();

        grammarStateManager  grammarStateManager= syntaxAnalysis.grammarStateManager.getGrammarStateManager();
        grammarStateManager.buildTransitionStateMachine();//建立项目集组

        //以下就是对token分别进行解析
        //语法分析部分
        LRStateTableParser synataxParser=new LRStateTableParser();
        synataxParser.parse(getInputString(tokens));

    }
    //将词法分析器获得tokens表转换为语法分析器可以识别的输入符号
    private Queue<Integer> getInputString(Queue<Token>tokens){
        Queue<Integer> queue=new LinkedList<Integer>();
        symbolDefine define=new symbolDefine();
        while(!tokens.isEmpty()){
            Token token=tokens.poll();
            if(token.getType()=="const"){
                queue.offer(define.tokensMap.get("const"));
                continue;
            }
            if(token.getType()=="identiter"){
                queue.offer(define.tokensMap.get("identiter"));
                continue;
            }
            queue.offer(define.tokensMap.get(token.getContent()));
        }
        return queue;
    }



   public static void  main(String[] args){
        MainSyntax m=new MainSyntax();
        Queue<Token> tokens= m.getTokens();
        //开始格式化输出词法分析结果
        System.out.println("**词法分析器的token表(from testSource.txt)*******************************************************");
        //开始输出对齐处理
        String column1Format="%-15s";
        String column2Format="%-30s";
        String column3Format="%-4s";
        for(Token token:tokens){
            System.out.format(column1Format+" "+column2Format+" "+column3Format+" ",
                    "LINE:"+token.getRow(),
                    "TOKEN_CONTENT:"+token.getContent(),
                    "TYPE:"+token.getType()
            );
            System.out.println();
        }
        System.out.println("**********************************************************************************************");
        m.bottomUpParser(tokens);
    }




}
