package syntaxAnalysis;

import sun.awt.Symbol;
import syntaxAnalysis.entity.GrammarState;
import syntaxAnalysis.entity.Production;
import syntaxAnalysis.entity.symbolDefine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @Package: syntaxAnalysis
 * @ClassName: grammarStateManager
 * @Author: panhengyu
 * @CreateTime: 2021-03-29 16:53
 * @Description:存放LR(1)分析中的项目集组,以及构建LR分析表
 */
public class grammarStateManager {
    private static grammarStateManager self=null;//保证程序运行时只会有一个grammarStateManager对象产生

    private ArrayList<GrammarState> stateList=new ArrayList<GrammarState>();

    //LR(1)项目集和跳转函数
    private HashMap<GrammarState, HashMap<Integer,GrammarState>> transitionMap=new HashMap<GrammarState,HashMap<Integer,GrammarState>>();
    //LR(1)分析表
    private  HashMap<Integer,HashMap<Integer,Integer>> lrStateTbale=new HashMap<Integer,HashMap<Integer,Integer>>();

    public static  grammarStateManager getGrammarStateManager(){
        if(self==null){
            self=new grammarStateManager();
        }
        return self;
    }

   /*********************************建立项目集族*******************************************/
   //执行完buildTransitionStateMachine则初始化完stateList以及transitionMap
   public void buildTransitionStateMachine(){
        System.out.println("开始进行LR(1)分析建立项目集组!(函数名:grammarStateManager.buildTransitionStateMachine)");
        productionManager productionManager= syntaxAnalysis.productionManager.getProductionManager();
        //根据开始非终结符创建初始项目集
        GrammarState state=getGrammarState(productionManager.getProduction(symbolDefine.STMT));

        /**
         * 初始化节点0后，开始构建整个状态机网络，网络的构建类似一种链式反应,节点0生成节点1到5，每个子节点继续生成相应节点
         */
        state.createTransition();
        printAllNode();
        System.out.println("结束进行LR(1)分析建立项目集组!(函数名:grammarStateManager.buildTransitionStateMachine)");
    }

    private void printAllNode(){
            for(GrammarState state:stateList){
                System.out.println("I"+state.getStateNum()+":");
                //打印该项目的流向
                System.out.print("流向:");
                HashMap<Integer,GrammarState> to=transitionMap.get(state);
                if(to!=null) {
                    for (Integer key : to.keySet()) {
                        System.out.print(key + "(" + to.get(key).getStateNum() + ") ");
                    }
                }
                System.out.println("");
                ArrayList<Production> pros=state.getProductions();
                int i=0;
                for(Production pro:pros){
                    System.out.print("("+i+"):");
                    System.out.print(pro.getLeft()+" -> ");
                    for(int j=0;j<pro.getRight().size();j++){
                        if(j==pro.getDotPos())System.out.print(". ");
                        System.out.print(pro.getRight().get(j)+" ");
                    }
                    if(pro.getDotPos()==pro.getRight().size())System.out.print(". ");
                    System.out.print("; ");
                    for(int k=0;k<pro.getLookAhead().size();k++){
                        System.out.print(pro.getLookAhead().get(k)+" ");
                    }
                    System.out.println("");
                    i++;
                }
                System.out.println("");
            }
        }




    /*
        根据production列表新建一个项目(该项目不能与原来项目重合)
     */
    public GrammarState getGrammarState(ArrayList<Production> productionList){
        /**
         * 要生成新的状态节点时，需要查找给定表达式所对应的节点是否已经存在，如果存在就不要构造新的节点
         */
        GrammarState state=new GrammarState(productionList);
        state.makeClosure();//事先makeClosure以下判断是否与stateList中的元素有相等的不然会递归不结束！
        if(stateList.contains(state)==false){
            state=new GrammarState(productionList);
            stateList.add(state);
            GrammarState.increaseStateNum();
            return state;
        }

        for(GrammarState s:stateList){
                if(s.equals(state)){
                    state=s;
                }
        }
        return state;
    }
    /**
     * 添加跳转函数至transitionMap中
     * @param from:开始项目
     * @param to:结束项目
     * @param on:输入字符
     */
    public void addTransition(GrammarState from ,GrammarState to,int on){
        HashMap<Integer,GrammarState> map=transitionMap.get(from);
        if(map==null){
            map=new HashMap<Integer,GrammarState>();
        }
        map.put(on,to);
        transitionMap.put(from,map);
    }


    /***************************************构造LR分析表**********************************/
    public HashMap<Integer,HashMap<Integer,Integer>> getLrStateTable(){
        /**
         * 逻辑：
         * 1)遍历StateList中的所有grammarState记作A转到2
         * 2)根据A从transitionMap中获得相应的跳转函数哈希表B转到2.1
         *      2.1)遍历跳转函数哈希表B获取跳转函数C,根据C的shift信息创建HashMap<Integer,Integer>对象，遍历完跳转到2.2
         *      2.2)根据grammarState对象A中的reduce信息D(调用makeReduce),遍历D创建HashMap<Integer,Integer>对象,遍历完跳转至3
         * 3)完成创建LrStateTable,返回结束
         *
         */

        Iterator<GrammarState> it=stateList.iterator();
        while(it.hasNext()){

            GrammarState state=(GrammarState) it.next();
            HashMap<Integer,GrammarState> transition=transitionMap.get(state);
            HashMap<Integer,Integer> jump=new HashMap<Integer,Integer>();

            //完成lr分析表中shift部分信息
            if(transition!=null){
                for(Map.Entry<Integer,GrammarState> item:transition.entrySet()){
                    jump.put(item.getKey(),item.getValue().getStateNum());
                }

            }

            //完成lr分析表中reduce部分信息
            HashMap<Integer,Integer> reduce=state.makeReduce();
            if(reduce.size()>0){
                for(Map.Entry<Integer,Integer> item:reduce.entrySet()){
                    jump.put(item.getKey(),-(item.getValue()));
                }
            }
            lrStateTbale.put(state.getStateNum(),jump);
        }
        return lrStateTbale;
    }
}
