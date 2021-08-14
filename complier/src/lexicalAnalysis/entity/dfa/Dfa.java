package lexicalAnalysis.entity.dfa;

import java.util.HashSet;
import java.util.Set;

/**
 * @Package: lexicalAnalysis.entity
 * @ClassName: Dfa
 * @Author: panhengyu
 * @CreateTime: 2021-03-21 19:28
 * @Description:确定有限状态机对象
 */
public class Dfa {
    /**
     * DFA确定有限状态五元组
     */
    private String title;


    private Set<Integer> stateSet;//状态集合
    private Set<Character> symbolTable;//输入符号表
    private Set<DfaFunction> functions;//转换函数
    private int beginState;//开始状态
    private Set<Integer> endStateSet;//终止状态集合
    private Set<DfaNode> dfaNodes;//确定有限状态机的节点

    public Dfa(Set<Integer> stateSet, Set<Character> symbolTable, Set<DfaFunction> functions, int beginState, Set<Integer> endStateSet, Set<DfaNode> dfaNodes) {
        this.stateSet = stateSet;
        this.symbolTable = symbolTable;
        this.functions = functions;
        this.beginState = beginState;
        this.endStateSet = endStateSet;
        this.dfaNodes = dfaNodes;
    }


    public Dfa(String title) {
        stateSet=new HashSet<Integer>();
        symbolTable=new HashSet<Character>();
        functions=new HashSet<DfaFunction>();
        endStateSet=new HashSet<Integer>();
        dfaNodes=new HashSet<DfaNode>();
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "Dfa{" +
                "title='" + title + '\'' +
                ", stateSet=" + stateSet +
                ", symbolTable=" + symbolTable +
                ", functions=" + functions +
                ", beginState=" + beginState +
                ", endStateSet=" + endStateSet +
                ", dfaNodes=" + dfaNodes +
                '}';
    }

    public Set<Integer> getStateSet() {
        return stateSet;
    }

    public void setStateSet(Set<Integer> stateSet) {
        this.stateSet = stateSet;
    }

    public Set<Character> getSymbolTable() {
        return symbolTable;
    }

    public void setSymbolTable(Set<Character> symbolTable) {
        this.symbolTable = symbolTable;
    }

    public Set<DfaFunction> getFunctions() {
        return functions;
    }

    public void setFunctions(Set<DfaFunction> functions) {
        this.functions = functions;
    }

    public int getBeginState() {
        return beginState;
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

    public Set<DfaNode> getDfaNodes() {
        return dfaNodes;
    }

    public void setDfaNodes(Set<DfaNode> dfaNodes) {
        this.dfaNodes = dfaNodes;
    }
}
