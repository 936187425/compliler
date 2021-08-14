package syntaxAnalysis;

import syntaxAnalysis.entity.Production;
import syntaxAnalysis.entity.Symbol;
import syntaxAnalysis.entity.symbolDefine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * @Package: syntaxAnalysis
 * @ClassName: FirstSetBuilder
 * @Author: panhengyu
 * @CreateTime: 2021-03-29 16:28
 * @Description: 计算字符的First集合
 */
public class FirstSetBuilder {


    private HashMap<Integer, Symbol> symbolMap=new HashMap<Integer,Symbol>();
    //在testSyntax.txt文件中出现过的所有非终结符和终结符字符
    private ArrayList<Symbol> symbolsArray=new ArrayList<Symbol>();
    private boolean runFirstSetPass=true;


    public FirstSetBuilder(HashMap<Integer, ArrayList<Production>> productionMap){
        System.out.println("正在初始化FirstSetBuilder对象!(函数名FirstSetBuilder.FirstSetBuilder)");
        //初始化symbolArrays
        symbolDefine symDefine=new symbolDefine();
        HashMap<Character,Integer> symbolMap=symDefine.symbolMap;
        for(Character key:symbolMap.keySet()){
            int value=symbolMap.get(key);
            if(!isSymbolTerminals(value)){//非终结符
                ArrayList<Production> pros=productionMap.get(value);
                if(pros!=null){
                    ArrayList<int[]> rights=new ArrayList<int[]>();
                    for(Production pro:pros){
                        //ArrayList<Integer> -> int[]
                       rights.add(pro.getRight().stream().mapToInt(k->k).toArray());
                    }
                    //todo
                    //自己添加能否推出nullable
                    symbolsArray.add(new Symbol(value,rights,value==16||value==18?true:false));
                }
            }else{//终结符
                symbolsArray.add(new Symbol(value,null,false));
            }
        }
        //初始化symbolMap
        for(Symbol s:symbolsArray){
            this.symbolMap.put(s.value,s);
        }

        System.out.println("结束初始化FirstSetBuilder对象!(函数名:FirstSetBuilder.FirstSetBuilder)");
    }


    /**
     * 计算每个语法符号的FirstSet集合
     */
    public void runFirstSets(){
            System.out.println("正在计算每个语法符号的FirstSet集合!(函数名:FirstSetBuilder.runFirstSets)");

            while(runFirstSetPass){
                runFirstSetPass=false;

                Iterator<Symbol> it=symbolsArray.iterator();
                while(it.hasNext()){
                    Symbol symbol=it.next();
                    addSymbolFirstSet(symbol);
                }
             }
           printAllFirstSet();
           System.out.println("结束计算每个语法符号的FirstSet集合!(函数名:FirstSetBuilder.runFirstSets)");
    }
    private void addSymbolFirstSet(Symbol symbol){
        /**
         * 1)判断symbol是否是终结符号:true结束;false转到2)
         * 2)遍历symbol的所有产生式，获得每个产生式的右式A,转到2.1
         *    2.1)从左向右遍历右式A获得语法符号a,如果a为终结符号则转到2.2;如果a不为终结符号则并上a符号的First集合
         *    2.2)把该终结符号加入该symbol的FirstSet
         *
         */
        if(this.isSymbolTerminals(symbol.value)){

            if(!symbol.firstSet.contains(symbol.value))
                symbol.firstSet.add(symbol.value);
            return ;
        }

        for(int i=0;i<symbol.productions.size();i++){
            //以symbol.value为左式的右式序列
            int[] rightForm=symbol.productions.get(i);
            if(rightForm.length==0) continue;
            if(isSymbolTerminals(rightForm[0])&&!symbol.firstSet.contains(rightForm[0])){
                runFirstSetPass = true;
                symbol.firstSet.add(rightForm[0]);

            }else if(!isSymbolTerminals(rightForm[0])){
                int pos=0;
                Symbol curSymbol=null;
                do{
                    curSymbol=symbolMap.get(rightForm[pos]);
                    if(!symbol.firstSet.containsAll(curSymbol.firstSet)){
                        runFirstSetPass=true;

                        //将curSymbol.firstSet中元素全加入symbol.firstSet中
                        for(int j=0;j<curSymbol.firstSet.size();j++){
                            if(!symbol.firstSet.contains(curSymbol.firstSet.get(j))){
                                symbol.firstSet.add(curSymbol.firstSet.get(j));
                            }
                        }

                    }
                    pos++;

                }while(pos<rightForm.length&&curSymbol.isNullable);
            }
        }
    }
    //打印所有的firstSet集合
    private void printAllFirstSet(){
        System.out.println("\n*********************打印所有字符(非终结符和终结符)的FirstSet集合******************************");
        int i=0;
        for(Symbol symbol:symbolsArray){
            System.out.println("("+i+") "+(isSymbolTerminals(symbol.value)?"终结符":"非终结符")+symbol.value+" firstSet:"+symbol.firstSet);
            i++;
        }
        System.out.println("\n*********************结束所有字符(非终结符和终结符)的FirstSet集合******************************");
    }



    /***判断语法符号是否是终结符*******/
    private boolean isSymbolTerminals(int symbol) {
        return symbol>=256?true:false;
    }



    /**
     * 获得特定语法符号的FirstSet集合
     * @param sym
     * @return
     */
    public ArrayList<Integer> getFirstSet(int sym){
        Iterator<Symbol> it=symbolsArray.iterator();
        while(it.hasNext()){
            Symbol symbol=it.next();
            if(symbol.value==sym){
                return symbol.firstSet;
            }
        }
        return null;
    }

    public boolean isSymbolNullable(int sym){
        Symbol symbol=symbolMap.get(sym);
        if(symbol==null) return false;
        return symbol.isNullable;
    }
}
