package lexicalAnalysis.entity.nfa;

import java.util.HashSet;
import java.util.Set;

/**
 * @Package: lexicalAnalysis.entity.nfa
 * @ClassName: NfaFunction
 * @Author: panhengyu
 * @CreateTime: 2021-03-21 20:39
 * @Description:NFA转换函数
 */
public class NfaFunction {
    /**
     * NFA转换函数即在一个状态在一个输入符号下可能转移的状态
     */
    private int beginState;//初始状态
    private char inputSymbol;//输入符号
    private Set<Integer> endStateSet;//转移后的状态集合


    public NfaFunction(int beginState, Set<Integer> endStateSet, char inputSymbol) {
        this.beginState = beginState;
        this.endStateSet = endStateSet;
        this.inputSymbol = inputSymbol;
    }

    public NfaFunction() {
        endStateSet=new HashSet<Integer>();
    }

    public int getBeginState() {
        return beginState;
    }

    @Override
    public String toString() {
        return "NfaFunction{" +
                "beginState=" + beginState +
                ", inputSymbol=" + inputSymbol +
                ", endStateSet=" + endStateSet +
                '}';
    }

    public void setBeginState(int beginState) {
        this.beginState = beginState;
    }

    public Set<Integer> getEndStateSet() {
        return endStateSet;
    }

    public void setEndStateSet(Set<Integer> endStateSet) {
        this.endStateSet = endStateSet;
    }

    public char getInputSymbol() {
        return inputSymbol;
    }

    public void setInputSymbol(char inputSymbol) {
        this.inputSymbol = inputSymbol;
    }
}
