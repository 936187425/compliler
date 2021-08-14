package lexicalAnalysis.entity.dfa;

import java.util.HashSet;
import java.util.Set;

/**
 * @Package: lexicalAnalysis.entity.dfa
 * @ClassName: DfaNode
 * @Author: panhengyu
 * @CreateTime: 2021-03-21 20:28
 * @Description:DFA确定有限状态机的节点
 */
public class DfaNode {
    /**
     * 确定有限状态机的节点信息：状态,以该状态为初始状态的转换函数
     */
    private int state;//状态名
    private Set<DfaFunction> functionSets;//转换函数

    public DfaNode(int state, Set<DfaFunction> functionSets) {
        this.state = state;
        this.functionSets = functionSets;
    }

    public DfaNode(int state) {
        this.state = state;
        functionSets=new HashSet<DfaFunction>();
    }

    @Override
    public String toString() {
        return "DfaNode{" +
                "state=" + state +
                ", functionSets=" + functionSets +
                '}';
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Set<DfaFunction> getFunctionSets() {
        return functionSets;
    }

    public void setFunctionSets(Set<DfaFunction> functionSets) {
        this.functionSets = functionSets;
    }
}
