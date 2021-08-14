package lexicalAnalysis.entity;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @Package: lexicalAnalysis.entity
 * @ClassName: FormalGrammar
 * @Author: panhengyu
 * @CreateTime: 2021-03-21 19:41
 * @Description:正规文法
 *
 * */


public class FormalGrammar {
    /**
     * 正规文法是一个四元组(Vn,Vt,P,S),每个正规文法都应该有一个title来辨别与其他正规文法的区别
     */
    private String title;//文法的类型即作用决定了类型
    private Set<Character> noterminalSet;//非终结符集合
    private Set<Character> terminatorSet;//终结符集合
    private Set<String>   produceSet;//产生式集合
    private char beginSymbol;//开始符

    public FormalGrammar(String title, Set<Character> noterminalSet, Set<Character> terminatorSet, Set<String> produceSet, char beginSymbol) {
        this.title = title;
        this.noterminalSet = noterminalSet;
        this.terminatorSet = terminatorSet;
        this.produceSet = produceSet;
        this.beginSymbol = beginSymbol;
    }

    public FormalGrammar() {
        noterminalSet=new HashSet<Character>();
        terminatorSet=new HashSet<Character>();
        produceSet=new HashSet<String>();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Set<Character> getNoterminalSet() {
        return noterminalSet;
    }


    public void setNoterminalSet(Set<Character> noterminalSet) {
        this.noterminalSet = noterminalSet;
    }

    public Set<Character> getTerminatorSet() {
        return terminatorSet;
    }

    public void setTerminatorSet(Set<Character> terminatorSet) {
        this.terminatorSet = terminatorSet;
    }

    public Set<String> getProduceSet() {
        return produceSet;
    }

    public void setProduceSet(Set<String> produceSet) {
        this.produceSet = produceSet;
    }

    public char getBeginSymbol() {
        return beginSymbol;
    }

    public void setBeginSymbol(char beginSymbol) {
        this.beginSymbol = beginSymbol;
    }

    @Override
    public String toString() {
        return "FormalGrammar{" +
                "title='" + title + '\'' +
                ", noterminalSet=" + noterminalSet +
                ", terminatorSet=" + terminatorSet +
                ", produceSet=" + produceSet +
                ", beginSymbol=" + beginSymbol +
                '}';
    }
}
