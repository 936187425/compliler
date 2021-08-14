package lexicalAnalysis.entity.dfa;

/**
 * @Package: lexicalAnalysis.entity.dfa
 * @ClassName: DfaFunction
 * @Author: panhengyu
 * @CreateTime: 2021-03-21 20:40
 * @Description:DFA转换函数
 */
public class DfaFunction {
    /**
     * 确定有限状态机的转换函数具有确定性即一个状态在一个输入符号下转换到另外一个状态
     */
    private int beginState;//初始状态
    private int endState;//转变后的状态
    private char inputSymbol;//输入符号

    public DfaFunction(int beginState, int endState, char inputSymbol) {
        this.beginState = beginState;
        this.endState = endState;
        this.inputSymbol = inputSymbol;
    }


    @Override
    public String toString() {
        return "DfaFunction{" +
                "beginState=" + beginState +
                ", endState=" + endState +
                ", inputSymbol=" + inputSymbol +
                '}';
    }

    public int getBeginState() {
        return beginState;
    }

    public void setBeginState(int beginState) {
        this.beginState = beginState;
    }

    public int getEndState() {
        return endState;
    }

    public void setEndState(int endState) {
        this.endState = endState;
    }

    public char getInputSymbol() {
        return inputSymbol;
    }

    public void setInputSymbol(char inputSymbol) {
        this.inputSymbol = inputSymbol;
    }
}
