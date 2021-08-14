package syntaxAnalysis.entity;

import syntaxAnalysis.grammarStateManager;
import syntaxAnalysis.productionManager;

import javax.security.auth.login.CredentialException;
import javax.swing.text.html.HTMLDocument;
import java.util.*;

/**
 * @Package: syntaxAnalysis.entity
 * @ClassName: GrammarState
 * @Author: panhengyu
 * @CreateTime: 2021-03-29 13:52
 * @Description: LR文法的每个项目集对象
 * 每一个grammarState都包含有shift,reduce信息
 * */
public class GrammarState {
    public static int stateNumCount=0;//项目集数量
    private int stateNum;//该项目集的编号
    private boolean transitionDone=false;//该项目集的进行跳转是否完成

    private productionManager productionManager= syntaxAnalysis.productionManager.getProductionManager();
    private syntaxAnalysis.grammarStateManager grammarStateManager= syntaxAnalysis.grammarStateManager.getGrammarStateManager();

    //GrammarState对象最终存放的信息
    private ArrayList<Production> productions=new ArrayList<Production>();//项目集中的production(left,right,dotPos,lookAhead)
    //项目集的跳转函数,第一个参数Integer表示终结符或者非终结符的编号,GrammarState到重点项目集
    private HashMap<Integer,GrammarState> transition=new HashMap<Integer,GrammarState>();


    //在建立项目集中用到的数据结构
    private ArrayList<Production> closureSet=new ArrayList<Production>();
    //第一个参数表示Integer表达式中逗号符号右边的符号的编号,第二个参数表示包含的产生式
    private HashMap<Integer,ArrayList<Production>> partition=new HashMap<Integer,ArrayList<Production>>();

   // private ArrayList<Production> mergedProduction=new ArrayList<Production>();

    /**
     * 自增项目集数量
     */
    public static void increaseStateNum(){
        stateNumCount++;
    }
    public boolean isTransitionDone(){
        return transitionDone;
    }

    public GrammarState(ArrayList<Production> productions) {
        this.productions = productions;
        this.stateNum=stateNumCount;
        this.closureSet.addAll(productions);
    }

    /**
     * 以该项目集为出发进行makeClosure(求闭包操作),partition(对该项目集进行分区操作),makeTransition(生成新的项目集)三个操作
     */
    public void createTransition(){
        if(transitionDone==true){//已经生成新的项目集了
            return ;
        }

        transitionDone=true;
        makeClosure();

        partition();
        makeTransition();

    }

    /******************************************makeClosure部分(创建闭包)************************************/
    /**
     * 对现在的项目集进行求闭包操作拓展该项目集
     */
    public  void makeClosure(){
        /**
         * 方法就是要有一个stack存放表达式,另外一个closureSet存放当前的闭包集
         */
        Stack<Production> productionStack=new Stack<Production>();

        //1:把该项目集初始的表达式放入productionStack中(为什么不要把表达式放入closureSet中因为在构造函数中已经存放了)
        for(Production production:productions){
            productionStack.add(production);
        }

        /**
         * 2:若栈不空，则先将栈顶元素弹出，否则转至3)
         *  2.1)判断表达式圆点右边的元素是否是非终结符
         *  2.2)true:则计算该表达式的FirstSet(betaC)集组合成具有firstset集的表达式然后判断该表达式是否在closureSet中存在或者覆盖->转至2.3
         *      false:则重复执行2
         *  2.3)如果可以覆盖则closure_se加入该表达式且栈也加入该表达式与此同时在set要去除被覆盖的表达式
         *      如果不可以覆盖且不存在closureSet,则将该表达式加入closureSet和栈中
         *      如果不可以覆盖且存在ClosureSet,则重复2
         * 3:退出
         */

        while(productionStack.empty()==false){
            Production production=productionStack.pop();
            if(symbolDefine.isSymbolTerminals(production.getDotRightSymbol())){
                continue;
            }

            int symbol=production.getDotRightSymbol();

            if(symbol==symbolDefine.UNKNOWN_SYMBOL) continue;//debug!! 此时当dot到达右式的末尾时

            ArrayList<Production> closures=productionManager.getProduction(symbol);//获得表达式左式为symbol的表达式
            ArrayList<Integer> lookAhead=production.computeFirstOfBetaAndC();//计算栈顶表达式的First集合

            Iterator it=closures.iterator();
            //实现:生成含lookAhead集合的表达式并判断该表达式是否在该closureaSet中
            while(it.hasNext()){
                Production preProduction=(Production)it.next();
                Production newProduction=preProduction.cloneSelf();//注意此处一定要新建(clone)production对象元素克隆即内存地址空间发生变化了
                newProduction.setLookAhead(lookAhead);

                //判断ClosureSet是否包含该表达式
                if(closureSet.contains(newProduction)==false){//注意在java中contains本质是调用equals方法，因此如果是比较元素中值相等则需要改写equals
                    closureSet.add(newProduction);
                    productionStack.push(newProduction);

                    //此时需要进行去除冗余表达式操作(即存在表达式覆盖操作)
                    removeRedundantProduction(newProduction);
                }

            }
            productions=this.closureSet;
        }
    }

    /**
     * 从ClosureSet中去除掉可以被覆盖的元素
     * @param pro
     */
    private void removeRedundantProduction(Production pro){
        boolean removeHappended=true;

        while(removeHappended){
            removeHappended=false;
            //此时要遍历closure-set
            Iterator it=closureSet.iterator();
            while(it.hasNext()){
                Production cloProduction=(Production) it.next();
                if(pro.coveUp(cloProduction)){//判断新创建的productin能否覆盖closureSet中原有的production
                    closureSet.remove(cloProduction);
                    removeHappended=true;
                    break;
                }
            }
        }
    }


    /***********************************************partition部分即分区部分**********************************/
    /**
     * 把表达式的原点右边第一个字符相等的表达式归为一个区即成为下一个新的项目集
     */
    private void partition(){
        for(int i=0;i<closureSet.size();i++){
            int symbol=closureSet.get(i).getDotRightSymbol();
            if(symbol==symbolDefine.UNKNOWN_SYMBOL){
                continue;
            }

            //此时将该表达式加到全局变量的partition的恰当分量上
            ArrayList<Production> productionList=partition.get(symbol);
            if(productionList==null){
                productionList=new ArrayList<Production>();
                partition.put(symbol,productionList);
            }
            if(productionList.contains(closureSet.get(i))==false){
                productionList.add(closureSet.get(i));
            }
        }
    }



    /***********************************************建立转移函数部分(fun:1、建立新的项目集,2、与此完成递归建立整个项目集组)*****************************************/
    /**
     * 建立转移函数
     */
    private void makeTransition(){
        for(Map.Entry<Integer,ArrayList<Production>>entry:partition.entrySet()){
            GrammarState nextState=makeNextGrammarState(entry.getKey());//创建下一个节点?
            transition.put(entry.getKey(),nextState);
            grammarStateManager.addTransition(this,nextState,entry.getKey());

        }

        extendFollowingTransition();
    }

    /**
     * 创建下一个项目集()
     * @param left
     * @return
     */
    private GrammarState makeNextGrammarState(int left){
        /**
         * 将原有项目中经过分区后获得某区的表达式集合productionList
         * 然后对productionList中每个元素--表达式的右式逗号符号向右移一位(注意要new一个对象而不是原有的对象上修改)
         * 将productionList作为项目的初始表达式集合新建一个grammarstate
         */
            ArrayList<Production> productionList=partition.get(left);
            ArrayList<Production> newProductionList=new ArrayList<Production>();

            for(int i=0;i<productionList.size();i++){
                Production production=productionList.get(i);
                newProductionList.add(production.dotForward());//dotForward中是新建一个对象
            }
            return grammarStateManager.getGrammarState(newProductionList);//建立新的项目集
    }

    /**
     *递归完成创建项目集
     */
    private void extendFollowingTransition(){
        for(Map.Entry<Integer,GrammarState> entry:transition.entrySet()){
            GrammarState state=entry.getValue();
            if(state. isTransitionDone()==false){//每一个项目在新建过程中都会
                state.createTransition();
            }
        }
    }

    /**********************************define grammarState相等为：productions元素相等***************************************************/
    @Override
    public boolean equals(Object obj) {
        return checkProductionEqual(obj,false);
    }

    public boolean checkProductionEqual(Object obj,boolean isPartial){
        GrammarState state=(GrammarState) obj;
        if(state.productions.size()!=this.productions.size()){
            return false;
        }
        int equalCount=0;
        for(int i=0;i<state.productions.size();i++){
            for(int j=0;j<this.productions.size();j++){
                if(isPartial==false){
                    if(state.productions.get(i).equals(this.productions.get(j))){
                        equalCount++;
                        break;
                    }
                }else{
                    if(state.productions.get(i).productionEquals(this.productions.get(j))==true){
                        equalCount++;
                        break;
                    }
                }
            }
        }
        return equalCount==state.productions.size();
    }



    /*************************************该节点能否进行规约(reduce)*********************************************/
    public HashMap<Integer,Integer> makeReduce(){
        HashMap<Integer,Integer> map=new HashMap<Integer,Integer>();
        reduce(map,this.productions);
        //reduce(map,this.mergedProduction);
        return map;
    }
    public void reduce(HashMap<Integer,Integer> map,ArrayList<Production> productions){
        /**
         * 1)遍历productions获得某个production 如果该表达式的逗号在右式末尾则可以进行规约转到2),否则重复1直到遍历完
         * 2)遍历该表达式的lookAhead集合,创建<Integer,Integer>的键值对加入map然后转向1)
         */
        for(Production pro:productions){
            if(pro.canBeReduce()){
                ArrayList<Integer> lookAhead=pro.getLookAhead();
                for(int i=0;i<lookAhead.size();i++){
                    map.put(lookAhead.get(i),pro.getProductionNum());
                }
            }
        }
    }


    public int getStateNum() {
        return stateNum;
    }

    public void setStateNum(int stateNum) {
        this.stateNum = stateNum;
    }

    public ArrayList<Production> getProductions() {
        return productions;
    }

    public void setProductions(ArrayList<Production> productions) {
        this.productions = productions;
    }
}
