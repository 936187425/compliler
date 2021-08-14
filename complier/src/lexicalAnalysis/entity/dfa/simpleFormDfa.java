package lexicalAnalysis.entity.dfa;

import java.util.Arrays;

/**
 * @Package: lexicalAnalysis.entity.dfa
 * @ClassName: simpleFormDfa
 * @Author: panhengyu
 * @CreateTime: 2021-03-24 14:16
 * @Description:
 */
public class simpleFormDfa {
    String title;
    int[][] transFunc;//元素非-1表示转换到的状态名，-1表示非法转换函数
    int[] acceptState;//0表示非可接受状态,1表示可接受状态

    public simpleFormDfa(Dfa dfa) {

        this.title = dfa.getTitle();
        int stateLen=dfa.getStateSet().size();
        //int symbolLen=dfa.getSymbolTable().size();
        transFunc=new int[stateLen][26];
        acceptState=new int[stateLen];

        //初始化可接受数组和转移函数数组
        for(int i:dfa.getEndStateSet()) acceptState[i]=1;
        for(int i=0;i<stateLen;i++){
            for(int j=0;j<26;j++){
                transFunc[i][j]=-1;
            }
        }
        for(DfaNode dfaNode:dfa.getDfaNodes()){
            for(DfaFunction dfaFunction:dfaNode.getFunctionSets()){
                int beginState=dfaFunction.getBeginState();
                int endState=dfaFunction.getEndState();
                int inputsymbol=dfaFunction.getInputSymbol()-'a';
                transFunc[beginState][inputsymbol]=endState;
            }
        }
    }

    @Override
    public String toString() {
        System.out.println("==================simpleFormDfa:==================================");
        for(int i=0;i<transFunc.length;i++){
            for(int j=0;j<transFunc[0].length;j++){
                System.out.print(transFunc[i][j]+" ");
            }
            System.out.println("");
        }
        System.out.println("");
        return "simpleFormDfa{" +
                "title='" + title + '\'' +
                ", acceptState=" + Arrays.toString(acceptState) +
                '}';
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int[][] getTransFunc() {
        return transFunc;
    }

    public void setTransFunc(int[][] transFunc) {
        this.transFunc = transFunc;
    }

    public int[] getAcceptState() {
        return acceptState;
    }

    public void setAcceptState(int[] acceptState) {
        this.acceptState = acceptState;
    }
}
