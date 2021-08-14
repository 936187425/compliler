package lexicalAnalysis.entity;

import java.util.HashSet;
import java.util.Set;

/**
 * @Package: CONST
 * @ClassName: constKeyMap
 * @Author: panhengyu
 * @CreateTime: 2021-03-26 13:59
 * @Description:存放该语言相关的限定符和关键字的对象
 */
public class constLexiKeyMap {
    private Set<String> keySet;//语言关键字集合
    private Set<String> restrictSet;//语言限定符集合
    private Set<String> operatorSet;//运算符集合

    public Set<String> getKeySet() {
        return keySet;
    }

    public void setKeySet(Set<String> keySet) {
        this.keySet = keySet;
    }

    public Set<String> getRestrictSet() {
        return restrictSet;
    }

    public void setRestrictSet(Set<String> restrictSet) {
        this.restrictSet = restrictSet;
    }

    public Set<String> getOperatorSet() {
        return operatorSet;
    }

    public void setOperatorSet(Set<String> operatorSet) {
        this.operatorSet = operatorSet;
    }

    public constLexiKeyMap() {
        //初始化关键字
        keySet=new HashSet<String>();
        keySet.add("int");
        keySet.add("float");
        keySet.add("boolean");
        keySet.add("cin");
        keySet.add("cout");
        keySet.add("if");
        keySet.add("then");
        keySet.add("else");
        keySet.add("begin");
        keySet.add("end");
        keySet.add("do");
        keySet.add("while");
        keySet.add("Const");
        //初始化限定符
        restrictSet=new HashSet<String>();
        restrictSet.add(";");
        restrictSet.add(",");
        restrictSet.add("(");
        restrictSet.add(")");
        restrictSet.add("{");
        restrictSet.add("}");
        //初始化运算集合
        operatorSet=new HashSet<String>();
        operatorSet.add("+");
        operatorSet.add("-");
        operatorSet.add("*");
        operatorSet.add("/");
        operatorSet.add("%");
        operatorSet.add("=");
        operatorSet.add("==");
        operatorSet.add("!=");
        operatorSet.add("<");
        operatorSet.add(">");
        operatorSet.add("<=");
        operatorSet.add(">=");

    }
}

