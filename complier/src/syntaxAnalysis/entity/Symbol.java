package syntaxAnalysis.entity;

import com.sun.org.apache.bcel.internal.generic.VariableLengthInstruction;

import java.util.ArrayList;

/**
 * @Package: syntaxAnalysis.entity
 * @ClassName: Symbol
 * @Author: panhengyu
 * @CreateTime: 2021-04-04 16:10
 * @Description:终结符号与非终结符号在语法分析中的编号
 * function:语法符号数字化
 */
public class Symbol {
    public int value;//语法符号的编号
    public ArrayList<int[]> productions;//语法符号的表达式右式符号化
    public boolean isNullable;//Nullable的定义:如果一个非终结符,它可以推导出空集，则称这样的非终结符为nullable
    public ArrayList<Integer> firstSet=new ArrayList<Integer>();

    public Symbol(int value, ArrayList<int[]> productions, boolean isNullable) {
        this.value = value;
        this.productions = productions;
        this.isNullable = isNullable;

        if(value>=256){//规定大于等于256的符号为终结符号
            firstSet.add(value);
        }
    }

    @Override
    public String toString() {
        return "Symbol{" +
                "value=" + value +
                ", productions=" + productions +
                ", isNullable=" + isNullable +
                ", firstSet=" + firstSet +
                '}';
    }
}
