package lexicalAnalysis.entity.nfa;

import java.util.HashSet;
import java.util.Set;

/**
 * @Package: lexicalAnalysis.entity.nfa
 * @ClassName: NfaNode
 * @Author: panhengyu
 * @CreateTime: 2021-03-21 20:29
 * @Description:Nfa不确定有限状态机的节点
 */
public class NfaNode {
    /*
    NfaNode节点集合
     */
    private int state;//状态
    private Set<NfaFunction> functionSet;//转移函数集合(已state为转移前状态的集合)

    public NfaNode(int state) {
        this.state = state;
        this.functionSet=new HashSet<NfaFunction>();
    }

    public NfaNode(int state, Set<NfaFunction> functionSet) {
        this.state = state;
        this.functionSet = functionSet;
    }

    @Override
    public String toString() {
        return "NfaNode{" +
                "state=" + state +
                ", functionSet=" + functionSet +
                '}';
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Set<NfaFunction> getFunctionSet() {
        return functionSet;
    }

    public void setFunctionSet(Set<NfaFunction> functionSet) {
        this.functionSet = functionSet;
    }
}
