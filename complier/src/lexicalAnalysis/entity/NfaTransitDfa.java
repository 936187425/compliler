package lexicalAnalysis.entity;

import java.util.HashSet;
import java.util.Set;

/**
 * @Package: lexicalAnalysis.entity
 * @ClassName: NfaTransitDfa
 * @Author: panhengyu
 * @CreateTime: 2021-03-22 19:48
 * @Description:Nfa对象转换到Dfa对象的中间状态对象
 */
public class NfaTransitDfa {
    private int DfaKey;//表示该状态在Dfa中该表示的状态
    private boolean isSign;//isSign==true表示已经标记
    private boolean isEndState;//判断该状态是否是终止状态

    private Set<Integer> NfaStates;//表示Nfa状态经过e_closure(radian_move)后的Nfa的状态集合


    public NfaTransitDfa() {
        NfaStates=new HashSet<Integer>();
        isSign=false;
        isEndState=false;
    }

    public NfaTransitDfa(int dfaKey, boolean isSign, Set<Integer> nfaStates,boolean isEndState) {
        DfaKey = dfaKey;
        this.isEndState=isEndState;
        this.isSign = isSign;
        NfaStates = nfaStates;

    }

    public int getDfaKey() {
        return DfaKey;
    }

    public boolean isEndState() {
        return isEndState;
    }

    public void setEndState(boolean endState) {
        isEndState = endState;
    }

    public void setDfaKey(int dfaKey) {
        DfaKey = dfaKey;
    }

    public boolean isSign() {
        return isSign;
    }

    public void setSign(boolean sign) {
        isSign = sign;
    }

    public Set<Integer> getNfaStates() {
        return NfaStates;
    }

    public void setNfaStates(Set<Integer> nfaStates) {
        NfaStates = nfaStates;
    }
}
