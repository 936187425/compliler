package syntaxAnalysis.entity;

import syntaxAnalysis.productionManager;

import java.util.ArrayList;

/**
 * @Package: syntaxAnalysis.entity
 * @ClassName: Production
 * @Author: panhengyu
 * @CreateTime: 2021-03-29 10:12
 * @Description:描述产生式对象
 */
public class Production {
    private int dotPos;//dotPos的取值范围为[0,right.size()]
    private int left;//产生式左边
    private ArrayList<Integer> right;//产生式右边
    private ArrayList<Integer> lookAhead=new ArrayList<Integer>();   //LookAhead集合,存放的是非终结符的集合
    private  int productionNum=-1;//产生式在产生式集合中的编号

    public Production(int productionNum,int dot,int left,ArrayList<Integer> right){
        this.left=left;
        this.right=right;
        this.productionNum=productionNum;
        this.lookAhead.add(symbolDefine.EOI);
        if(dot>right.size()){
            dot=right.size();
        }
        this.dotPos=dot;
    }

    /**
     * 创建一个新对象,此对象中dot位置向后移了一位,此时LookAhead集合并不会发生变化
     * 时机:初始化新的项目集时，没有计算新项目集的closure_set之前
     * @return
     */
    public Production dotForward(){
        Production production=new Production(productionNum,dotPos+1,left,right);
        production.lookAhead=new ArrayList<Integer>();
        for(int i=0;i<this.lookAhead.size();i++){
            production.lookAhead.add(this.lookAhead.get(i));
        }
        return production;
    }

    /**
     * 创建一个新对象此时的新对象中的元素等于旧对象的元素
     * @return
     */
    public Production cloneSelf(){
        Production production=new Production(this.productionNum,this.dotPos,this.left,this.right);
        production.lookAhead=new ArrayList<Integer>();

        for(int i=0;i<this.lookAhead.size();i++){
            production.lookAhead.add(this.lookAhead.get(i));
        }
        return production;
    }



    /**
     * 计算First集合即FIRST(betaC)
     * @return
     */
    public ArrayList<Integer> computeFirstOfBetaAndC(){
        /*
            计算First集合的算法：对于产生式A->α.Bβ,C
            此时先将β和C相连然后计算βC的First集合，如果β中的每一个元素都是nullable，
            那么First(βC)就是First(β)并上First(C)(为每个C中每一个元素)
         */

        //先创建可能的fisrtSet集合
        ArrayList<Integer> set=new ArrayList<Integer>();
        //把β和C相连以数组的形式表达出来
        for(int i=dotPos+1;i<right.size();i++){
            set.add(right.get(i));
        }

        //set.addAll(lookAhead);

        //productionManager在整个程序运行过程中只有一个对象
        productionManager manager=productionManager.getProductionManager();
        ArrayList<Integer> firsetSet=new ArrayList<Integer>();
        if(set.size()>0){
            for(int i=0;i<set.size();i++){
                //先计算一个字符的First集合
                ArrayList<Integer> lookAhead=manager.getFirstSetBuilder().getFirstSet(set.get(i));

                for(int j=0;j<lookAhead.size();j++) {
                    if (firsetSet.contains(lookAhead.get(j)) == false) {//保证不会重复加入FirstSet
                        firsetSet.add(lookAhead.get(j));
                    }
                }

                //判断该字符的first集合中是否存在空字符
                if(manager.getFirstSetBuilder().isSymbolNullable(set.get(i))==false){
                    break;
                }

                if(i==set.size()-1){
                    firsetSet.addAll(lookAhead);
                }
            }
        }else{
            firsetSet.addAll(lookAhead);
        }
        return firsetSet;

    }


    /**
     * 返回right列表位置在dotPos的symbol
     * 即A->a.Bb此时返回的就是‘B’
     * @return
     */
    public int getDotRightSymbol(){
        if(dotPos>=right.size()){
            return symbolDefine.UNKNOWN_SYMBOL;
        }
        return right.get(dotPos);
    }


    /***********************************重新改写equal**************************************************/

    /**
     * 重写equal(在java中如果没有重写equal那么比较的是两个对象的内存地址)
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        Production pro=(Production) o;
        //两个产生式相等即满足3个条件(产生式左边、产生式右边、逗号相等、以及LookAhead集合必须相等)
        //比如:A->a.和A->.a认为两个不同的产生式前者可以进行归约(reduce)操作后者进行移进(shift)操作
        if(productionEquals(pro)&&lookAheadSetComparing(pro)==0)
            return true;
        else
            return false;
    }
    /**
     * 判断两个表达式是否相等(不包含LookAhead集合的比较)
     * @param p
     * @return
     */
    public boolean productionEquals(Production p){
        if(this.left!=p.getLeft())return false;
        if(this.dotPos!=p.getDotPos())return false;
        if(!this.right.equals(p.getRight()))return false;//注意此时的equals比较的hash地址相等而不是元素值相等哦
        return true;
    }



    /**
     * 比较两个lookAhead集合是否相等
     * @param production
     * @return
     */
    public int lookAheadSetComparing(Production production){
        if(this.lookAhead.size()<production.lookAhead.size()) return -1;
        if(this.lookAhead.size()>production.lookAhead.size()) {
            //该判断条件用在coveUp上
            for(int i=0;i<production.lookAhead.size();i++){
                if(!this.lookAhead.contains(production.lookAhead.get(i))) return -1;
            }
            return 1;
        }
        if(this.lookAhead.size()==production.lookAhead.size()){
            /*for(int i=0;i<lookAhead.size();i++){
                if(this.lookAhead.get(i)!=production.lookAhead.get(i)){
                    return -1;
                }
            }*/
            for(int i=0;i<lookAhead.size();i++){
                if(!production.lookAhead.contains(this.lookAhead.get(i)))return -1;//Integer类中的equals比较的是数值
            }
        }
        return 0;
    }


    /**
     * 判断新的产生式能否覆盖原来的表达式
     * pro1.coveUp(pro2) 如果return true那么pro1则可以覆盖pro2 反之pro1则不可以覆盖pro2
     * @return
     */
    public boolean coveUp(Production pro){
        if(productionEquals(pro)&&lookAheadSetComparing(pro)>0)
            //后者的判断条件要大于0因为在GrammarState中的removeRedundantProduction方法是在新表达式先加入后才判断是否覆盖
           return true;
        else
            return false;
    }

    /***************************************************************************************/

    /**
     * 判断是否可以进行规约操作即reduce
     * 根据圆点的位置是否在产生式右边的末尾
     * @return
     */
    public boolean canBeReduce(){
        return this.dotPos>=this.right.size();
    }

    /**
     * printProduction的方法作用在于打印各个产生式的信息，用来debug
     * @return
     */
    public void printProduction(){
        System.out.println(symbolDefine.getSymbolStr(left)+"->");
        for(int i=0;i<right.size();i++){
            if(i==dotPos){
                System.out.print("."+symbolDefine.getSymbolStr(this.right.get(i)));
            }else{
                System.out.print(symbolDefine.getSymbolStr(this.right.get(i)));
            }
        }
        if(dotPos==right.size())
            System.out.print(".");
        System.out.println("");
    }



    public int getDotPos() {
        return dotPos;
    }

    public void setDotPos(int dotPos) {
        this.dotPos = dotPos;
    }

    public int getLeft() {
        return left;
    }

    public void setLeft(int left) {
        this.left = left;
    }

    public ArrayList<Integer> getRight() {
        return right;
    }

    public void setRight(ArrayList<Integer> right) {
        this.right = right;
    }

    public ArrayList<Integer> getLookAhead() {
        return lookAhead;
    }

    public void setLookAhead(ArrayList<Integer> lookAhead) {
        this.lookAhead = lookAhead;
    }

    public int getProductionNum() {
        return productionNum;
    }

    public void setProductionNum(int productionNum) {
        this.productionNum = productionNum;
    }


}
