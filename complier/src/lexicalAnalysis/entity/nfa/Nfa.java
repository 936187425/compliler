package lexicalAnalysis.entity.nfa;

import java.util.HashSet;
import java.util.Set;

/**
 * @Package: lexicalAnalysis.entity
 * @ClassName: NFA
 * @Author: panhengyu
 * @CreateTime: 2021-03-21 19:27
 * @Description:不确定有限状态机对象
 */
public class Nfa {
    /**
     * Nfa有限状态机是一个五元组
     * 注:为什么NFA的状态用integer表示不用char？
     * 因为考虑到如果在一个非常大的nfa中char具有一定限制表示范围因此使用integer表示更为合理。
     */
    private String title;
    private Set<Integer> stateSet;//状态集合
    private Set<Character> symbolTable;//输入符号表
    private Set<NfaFunction> functions;//转换函数
    private Set<Integer> beginStateSet;//开始状态集合
    private Set<Integer> endStateSet;//终止状态集合
    private Set<NfaNode> nfaNodes;//不确定有限状态机的节点

    public Nfa(Set<Integer> stateSet, Set<Character> symbolTable, Set<NfaFunction> functions, Set<Integer> beginStateSet, Set<Integer> endStateSet, Set<NfaNode> nfaNodes) {
        this.stateSet = stateSet;
        this.symbolTable = symbolTable;
        this.functions = functions;
        this.beginStateSet = beginStateSet;
        this.endStateSet = endStateSet;
        this.nfaNodes = nfaNodes;
    }
    public Nfa() {
        stateSet=new HashSet<Integer>();
        symbolTable=new HashSet<Character>();
        functions=new HashSet<NfaFunction>();
        beginStateSet=new HashSet<Integer>();
        endStateSet=new HashSet<Integer>();
        nfaNodes=new HashSet<NfaNode>();
    }
    @Override
    public String toString() {
        return "Nfa{" +
                "title='" + title + '\'' +
                ", stateSet=" + stateSet +
                ", symbolTable=" + symbolTable +
                ", functions=" + functions +
                ", beginStateSet=" + beginStateSet +
                ", endStateSet=" + endStateSet +
                ", nfaNodes=" + nfaNodes +
                '}';
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public Set<NfaFunction> getFunctions() {
        return functions;
    }

    public void setFunctions(Set<NfaFunction> functions) {
        this.functions = functions;
    }

    public Set<Integer> getBeginStateSet() {
        return beginStateSet;
    }

    public void setBeginStateSet(Set<Integer> beginStateSet) {
        this.beginStateSet = beginStateSet;
    }

    public Set<Integer> getEndStateSet() {
        return endStateSet;
    }

    public void setEndStateSet(Set<Integer> endStateSet) {
        this.endStateSet = endStateSet;
    }

    public Set<NfaNode> getNfaNodes() {
        return nfaNodes;
    }

    public void setNfaNodes(Set<NfaNode> dfaNodes) {
        this.nfaNodes = dfaNodes;
    }
}
