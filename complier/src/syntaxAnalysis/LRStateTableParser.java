package syntaxAnalysis;

import lexicalAnalysis.entity.Token;
import syntaxAnalysis.entity.Production;
import syntaxAnalysis.entity.Symbol;
import syntaxAnalysis.entity.symbolDefine;

import java.util.*;

/**
 * @Package: syntaxAnalysis
 * @ClassName: LRStateTableParser
 * @Author: panhengyu
 * @CreateTime: 2021-04-04 19:35
 * @Description: LR分析表的翻译解释部分,LR分析表存放在grammarStateManager中
 */
public class LRStateTableParser {
    private HashMap<Integer, HashMap<Integer, Integer>> lrStateTable;//lr分析表
    //LR(1)分析过程需要两个堆栈分别为文法符号栈和状态栈
    private Stack<Integer>statusStack=new Stack<Integer>();//Integer为项目编号,转换栈
    private Stack<Integer>parseStack=new Stack<Integer>();//文法符号栈
    private Stack<Object> valueStack=new Stack<Object>();

    public LRStateTableParser() {
        System.out.println("正在建立LR(1)语法分析表!(函数名:LRStateTableParser.LRStateTableParser)");
        //初始化分析表
        lrStateTable=grammarStateManager.getGrammarStateManager().getLrStateTable();
        printLrStateTable();
        valueStack.push(null);
        System.out.println("结束建立LR(1)语法分析表!(函数名:LRStateTableParser.LRStateTableParser)");
    }
    /**
     * 打印LR(1)分析表
     */
    private void printLrStateTable(){
        System.out.println("\nLR(1)分析表如下:*******************************************************");
        String column1Format="%-30s";
        String column2Format="%-30s";
        String column3Format="%-4s";
        for(int key:lrStateTable.keySet()){
            HashMap<Integer,Integer> curRow=lrStateTable.get(key);
            for(int key1:curRow.keySet()) {
                System.out.format(column1Format+" "+column2Format+" "+column3Format+" ",
                        "FROM:"+key,
                        "INPUT:"+key1,
                        "TO:"+((curRow.get(key1)<0)?"R"+(-1*curRow.get(key1)):"S"+curRow.get(key1))
                );
                System.out.println();
            }
        }
        System.out.println("***********************************************************************");
    }



    /***************************************分析输入的token的***************************************/
    //根据当前节点以及输入符号获得跳转的项目集编号
    private Integer getAction(Integer currentState,Integer input){
        HashMap<Integer,Integer> jump=lrStateTable.get(currentState);
        if(jump!=null){
            Integer next=jump.get(input);
            return next;
        }
        return null;
    }

    public void parse(Queue<Integer> tokens){
        //step1:初始化部分,ParseStack中push -1,statusStack push 0,token push_back -1
        tokens.offer(-1);
        statusStack.push(0);
        parseStack.push(-1);
        int currentInput=tokens.peek();
        while(true){

            Integer action=getAction(statusStack.peek(),currentInput);
            if(action==null){
                System.out.println("the input is denied");
                return;
            }
            if(action==0){ //表示输入的字符串可以被接收
                System.out.println("the input can be accepted");
                return ;
            }
            if(action>0){
                /**
                 * 当action>0时进行shift操作,把下一个项目编号加入栈中,把输入符号currentInput加入堆栈中,把action也加入堆栈中
                 * 注意:由于规约时没有把GOTO下一个项目编号加入statusStack中所以要判断
                 */
                statusStack.add(action);
                parseStack.add(currentInput);

                //currentInput如果是终结符那么就是来自输入串,如果是非终结符，那么就是来自规约
                if(symbolDefine.isSymbolTerminals(currentInput)){
                   //非终结符,则tokens出队头元素
                    tokens.poll();
                }
                currentInput=tokens.peek();
            }
            if(action<0){
                /**
                 * 进行reduce操作:
                 * 找到进行规约的产生式,根据右式的符号的数量n来对输入符号栈和项目栈进行弹栈次数n次,
                 * 然后把产生式的左式的非终结符加入输入符号栈中,然后根据左式查询
                 */
                int reduceProductin=-action;
                Production production=productionManager.getProductionManager().getProductionByIndex(reduceProductin);
                int rightSize=production.getRight().size();

                //将输入符号栈和项目栈弹出
                while(rightSize>0){
                    parseStack.pop();
                    statusStack.pop();
                    rightSize--;
                }
                currentInput=production.getLeft();
                parseStack.push(currentInput);//加入产生式的左式语法符号的编号

            }
        }
    }
}
