package syntaxAnalysis;

import CONST.constPath;
import fileUtils.inputMain;
import syntaxAnalysis.entity.Production;
import syntaxAnalysis.entity.symbolDefine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @Package: syntaxAnalysis
 * @ClassName: productionManager
 * @Author: panhengyu
 * @CreateTime: 2021-03-29 10:13
 * @Description:控制并记录所有production，并将相同的left的产生式以列表形式存放在一起
 */
public class productionManager {
    private static productionManager self=null;
    //productionMap中的类型ArrayList<Production>表示的是具有相同的产生式左边的产生式集合
    //第一个Integer表示产生式左式编号
    private  HashMap<Integer, ArrayList<Production>> productionMap=new HashMap<Integer,ArrayList<Production>>();
    FirstSetBuilder firstSetBuilder;


    public productionManager() {
        System.out.println("正在初始化productionManager对象!(函数名:productionManager.productionManager)");
        inputMain input=new inputMain();
        StringBuffer buffer=input.getiSys().getText(constPath.GET_SYNTAX_TEXT);

        initProductionManager(buffer);
        firstSetBuilder=new FirstSetBuilder(productionMap);
        firstSetBuilder.runFirstSets();//计算所有语法符号的firstSet集合
        System.out.println("结束初始化productionManager对象!(函数名:productionManager.productionManager)");
        printAllProduction();
    }

    /**
     * console打印出所有的产生式包含大小写
     */
    private void printAllProduction(){
        System.out.println("\n*************************打印出所有的产生式(以数值和项目组形式表示)********************");
        for(int key:productionMap.keySet()){
            System.out.println("非终结符:"+key);
            ArrayList<Production> pros=productionMap.get(key);
            for(int i=0;i<pros.size();i++){
                Production pro=pros.get(i);
                System.out.print("第"+i+"个产生式:");
                System.out.print(pro.getLeft()+" -> ");
                for(int j=0;j<pro.getRight().size();j++){
                    if(j==pro.getDotPos())System.out.print(". ");
                    System.out.print(pro.getRight().get(j)+" ");
                }
                System.out.print("; ");
                for(int k=0;k<pro.getLookAhead().size();k++){
                    System.out.print(pro.getLookAhead().get(k)+" ");
                }
                System.out.println("");
            }
        }
        System.out.println("*************************结束所有的产生式(以数值和项目组形式表示)********************");
    }



    //要确保该类对象在程序运行时候的唯一性
    public static productionManager getProductionManager(){
        if(self==null){
            self=new productionManager();
        }
        return self;
    }


    /**
     * 初始化productionMap
     * @param buffer:testSyntax.txt文件中的二型文法
     */
    public void initProductionManager(StringBuffer buffer){
        System.out.println("正在从testSyntax.txt文件中获取语言描述！(函数名:productionManager.initProductionManager)");
        symbolDefine sydefine=new symbolDefine();
        //读取testGrammar.txt然后初始化productionMap
        int index=0,proFinal=0,productionNum=0,left=0;
        ArrayList<Integer> right=new ArrayList<Integer>();
        while(buffer.charAt(index)!='\0'){
            char input=buffer.charAt(index);
            if(input=='-'||input=='>'){
                index++;
                continue;
            }
            if(input=='\n'){
                ArrayList<Production> pros=productionMap.get(left);
                if(pros==null) pros=new ArrayList<Production>();
                pros.add(new Production(productionNum++,0,left,right));
                productionMap.put(left,pros);
                right=new ArrayList<Integer>();
                proFinal=0;
                index++;
                continue;
            }
            if(proFinal==0){
                left = sydefine.symbolMap.get(input);
                proFinal=1;
                index++;
                continue;
            }
            if(proFinal==1){
                right.add(sydefine.symbolMap.get(input));
                index=index+1;
            }
        }
        System.out.println("结束从testSyntax.txt文件中获取语言描述！(函数名:productionManager.initProductionManager)");
    }

    /**
     * 返回可以计算First集合的对象
     * @return
     */
    public FirstSetBuilder getFirstSetBuilder(){
        return firstSetBuilder;
    }

    /**
     * 将int[]形式的产生式右式转换为ArrayList的形式产生式右式
     * @param arr
     * @return
     */
    public ArrayList<Integer> getProductionRight(int[] arr){
        ArrayList<Integer> right=new ArrayList<Integer>();
        for(int i=0;i<arr.length;i++){
            right.add(arr[i]);
        }
        return right;
    }


    /**
     * 根据production的left放进productionMap的恰当位置
     * @param production
     */
    public void addProduction(Production production){
        ArrayList<Production> proList=productionMap.get(production.getLeft());
        if(proList==null){
            //说明以production.getLeft()为产生式左边的产生式还没有出现过
            proList=new ArrayList<Production>();
            productionMap.put(production.getLeft(),proList);
        }
        if(proList.contains(production)==false){
            proList.add(production);
        }

    }


    /**
     * 根据产生式的左式子获得产生式数组
     * @param left
     * @return
     */
    public ArrayList<Production> getProduction(int left){
            return this.productionMap.get(left);
    }

    /**
     * 根据产生式的编号获得相应的产生式
     * @param index
     * @return
     */
    public Production getProductionByIndex(int index){
        for(Map.Entry<Integer,ArrayList<Production>> item: productionMap.entrySet()){
            ArrayList<Production> productions=item.getValue();
            for(Production pro:productions){
                if(pro.getProductionNum()==index){
                    return pro;
                }
            }
        }
        return null;
    }



    public static void main(String[] args){
        productionManager productionManager= syntaxAnalysis.productionManager.getProductionManager();
    }
}
