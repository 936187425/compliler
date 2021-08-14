package lexicalAnalysis;

import lexicalAnalysis.entity.FormalGrammar;
import lexicalAnalysis.entity.NfaTransitDfa;
import lexicalAnalysis.entity.dfa.Dfa;
import lexicalAnalysis.entity.dfa.DfaFunction;
import lexicalAnalysis.entity.dfa.DfaNode;
import lexicalAnalysis.entity.nfa.Nfa;
import lexicalAnalysis.entity.nfa.NfaFunction;
import lexicalAnalysis.entity.nfa.NfaNode;
import fileUtils.inputMain;
import CONST.constPath;
import java.util.HashSet;
import java.util.Set;

/**
 * @Package: lexicalAnalysis
 * @ClassName: lexicalAnalysisUtils
 * @Author: 潘恒宇
 * @CreateTime: 2021-03-21 19:33
 * @Description:词法分析的相关工具类：比如正规文法转化为NFA，NFA转化为DFA，最小DFA
 */
public class lexicalAnalysisUtils {
     private inputMain input=new inputMain();

     /**
      *将文件内的正规文法字符串转换为FormalGrammar集合
      **/
     public Set<FormalGrammar> tLexical2FGrammar(){
          Set<FormalGrammar> grammars=new HashSet<FormalGrammar>();
          StringBuffer buffer=input.getiSys().getText(constPath.GET_LEXICAL_TEXT);
          StringBuffer titleBuf=new StringBuffer();
          /*
          字符串转换为文法的算法：
            初始值:num表示换行出现的次数
            if '+' num置为0;
            if '\n' num++;
            if '\0' 表示字符串结束
            if num==1 表示在下一个字符串出现前是title和开始符号信息
            if num>1    表示接下来的产生式信息
           */
         FormalGrammar grammar=null;
         int index=0,firstFlag=1,num=0;
         int left=0,right=0;//左括号、右括号、产生式中阔折号出现次数
         while(buffer.charAt(index)!='\0'){
             char nowChar=buffer.charAt(index);
             if(nowChar=='+'){
                 num=0;
                 if(firstFlag==1)firstFlag=0;
                 else grammars.add(grammar);
                 grammar=new FormalGrammar();
                 index++;
                 continue;
             }
             if(nowChar=='\n') {
                 num++;
                 index++;
                 continue;
             }
             if(num==1){
                 //此时的字符为形如P[S]的那一行
                 if(nowChar=='['){
                     left++;
                     grammar.setTitle(titleBuf.toString());
                     titleBuf.delete(0,titleBuf.length());
                 }
                 if(nowChar==']')right++;
                 if(left==0) titleBuf.append(nowChar);
                 if(left==1&&right==0) grammar.setBeginSymbol(nowChar);
                 if(left==1&&right==1)left=right=0;
             }else{
                //此时的字符为产生式行,此时的处理为一个字符串进行处理
                 StringBuffer b=new StringBuffer();
                 while(buffer.charAt(index)!='\n'){
                     char innerChar=buffer.charAt(index);
                     b.append(innerChar);
                     if(innerChar!='-'){
                         if(Character.isUpperCase(innerChar)&&!grammar.getNoterminalSet().contains(innerChar)){
                                grammar.getNoterminalSet().add(innerChar);
                         }
                         if(Character.isLowerCase(innerChar)&&!grammar.getTerminatorSet().contains(innerChar)){
                             grammar.getTerminatorSet().add(innerChar);
                         }
                     }else{
                         index++;//跳过“->”
                         b.append(buffer.charAt(index));//加入'>'
                     }
                     index++;
                 }
                 grammar.getProduceSet().add(b.toString());
                 index--;
             }
             index++;
         }
         grammars.add(grammar);
         return grammars;

     }



    /**
     * fGrammar2Nfa:正规文法转化为NFA
     * mapping:将正规文法中非终结符(char)映射到nfa的状态(int)
     */
     public Set<Nfa> fGrammar2Nfa(Set<FormalGrammar> grammars){
          Set<Nfa> nfas=new HashSet<Nfa>();
         /**
          * 正规文法G转换为NFA(M)的算法为:
          * 1)M的字母表与G的终结符集合相同
          * 2)G中的每个非终结符生成M的一个状态，G的开始符号是M的开始状态S
          * 3)增加一个新状态z,作为M的终态
          * 4)对G中形如A->tB的规则，则转换为函数f(A,t)=B
          * 5)对G中形如A->t的规则，则转换为函数f(A)=Z
          */
         for(FormalGrammar grammar:grammars){
             Nfa nfa=new Nfa();
             nfa.setTitle(grammar.getTitle());//设置title
             nfa.setSymbolTable(grammar.getTerminatorSet());//设置输入字母表
             nfa.getBeginStateSet().add(mapping(grammar.getBeginSymbol()));
             for(Character c:grammar.getNoterminalSet()){
                 nfa.getStateSet().add(mapping(c));
             }
             int endstate=nfa.getStateSet().size();
             nfa.getStateSet().add(endstate);//加入终态
             nfa.getEndStateSet().add(endstate);


             //生成转移函数一个转移函数的键值为beginstate和inputSymbol即beginstate、inputSymbol决定一个独立的转移函数
             Set<NfaFunction> nfaFunctions=nfa.getFunctions();//表示一个nfa上的所有的转移函数集合
             for(String f:grammar.getProduceSet()){
                //根据string的长度决定是A->a类型的还是A->tB类型的
                 int len=f.length();
                 boolean innerflag=true;
                 for(NfaFunction nf:nfaFunctions){
                     int bState=nf.getBeginState();
                     int inSymbol=nf.getInputSymbol();
                     //判断nfa中是否加入开始状态为f.charAt(0)结束状态为f.charAt(3)的nfa转换函数
                     if(bState==mapping(f.charAt(0))&&inSymbol==f.charAt(3)) {
                         innerflag=false;
                         if(len==5)nf.getEndStateSet().add(mapping(f.charAt(4)));//把A->tB类型的B节点加进去
                         //当len==4时即A->t(t为任意字符包括*)
                         if(len==4&&!nf.getEndStateSet().contains(endstate))nf.getEndStateSet().add(endstate);
                         break;
                     }
                 }
                 if(innerflag) {//如果nfa转移函数中没有存在
                     Set<Integer> set=new HashSet<Integer>();
                     set.add(len==4?endstate:mapping(f.charAt(4)));
                     nfaFunctions.add(
                             new NfaFunction(
                                     mapping(f.charAt(0)),
                                     set,
                                     f.charAt(3)
                             )
                     );
                 }
             }
             nfa.setFunctions(nfaFunctions);

             //生成nfaNode
             for(int node:nfa.getStateSet()){
                //要排除掉终状态没有转移函数
                if(nfa.getEndStateSet().contains(node))continue;
                 NfaNode nfaNode=new NfaNode(node);
                 for(NfaFunction nf:nfaFunctions){
                     if(nf.getBeginState()==node) nfaNode.getFunctionSet().add(nf);
                 }
                 nfa.getNfaNodes().add(nfaNode);
             }
             nfas.add(nfa);
         }
         return nfas;
     }
     public int mapping(char in){
         return in-'A';
    }




    /**
     *将NFA转化为DFA,其中有两个重要函数
     * 1、e_closure() 闭包函数
     * 2、radian_move() 弧度转移函数
     * 然后利用子集法实现NFA转换为DFA
     */
     public Set<Dfa> nfa2Dfa(Set<Nfa> nfas){

          Set<Dfa> dfas=new HashSet<Dfa>();
          for(Nfa nfa:nfas){
              //Set:dfa中title
              //System.out.println(nfa.getTitle());
              Dfa dfa=new Dfa(nfa.getTitle());
              //Set:dfa中SymbolTbale，要去除e(空字符)_debug
              dfa.setSymbolTable(nfa.getSymbolTable());
              dfa.getSymbolTable().remove('*');

              Set<NfaTransitDfa> C=new HashSet<NfaTransitDfa>();
              //Set:dfa的beginState
              dfa.setBeginState(0);
              NfaTransitDfa beginC=new NfaTransitDfa();
              beginC.setDfaKey(0);
              beginC.setNfaStates(e_closure(nfa.getBeginStateSet(),nfa));
              C.add(beginC);

              //unSignedNum表示C中未被标记的集合,keyNum表示Dfa的状态数,symbolTable:输入的字母表
              int unSignedNum=1,keyNum=1;
              Set<Character> symbolTable=dfa.getSymbolTable();
              while(unSignedNum>0){
                  //找到C中未被标记的元素T
                  Set<Integer> T=null;
                  int tDfaKey=0;//表示元素T在Dfa中的状态名
                  for(NfaTransitDfa nfaTransitDfa:C){
                      if(!nfaTransitDfa.isSign()){
                          //将该状态设置为标记
                          nfaTransitDfa.setSign(true);
                          T=nfaTransitDfa.getNfaStates();
                          tDfaKey=nfaTransitDfa.getDfaKey();
                          unSignedNum--;
                          break;
                      }
                  }

                  //遍历每个输入字母从而对未被标记的元素建立转换函数
                  for(char symbol : symbolTable){

                      Set<Integer> U=e_closure(radian_move(T,nfa,symbol),nfa);
                      //System.out.println(U.toString()+" "+T.toString()+" "+symbol);
                      //如果U集合不在C集合中，将作为未被标记的子集加入C中,uDfaKey:表示集合U在dfa中的状态名
                      boolean flag=true;//flag=false说明在状态U已经在C中
                      int uDfaKey=0;
                      for(NfaTransitDfa nfaTransitDfa:C){
                          if(isSetEquals(U,nfaTransitDfa.getNfaStates())) {
                              flag=false;
                              uDfaKey=nfaTransitDfa.getDfaKey();
                              break;
                          }
                      }
                      if(flag) {
                          //判断该状态是否可能是dfa的结束状态
                          //判断依据:只要转换后的nfa状态集中存在一个终止状态该nfa状态集就可以认为是dfa的一个终止状态
                          boolean isEndState=false;
                          for(Integer val:U){
                              if(nfa.getEndStateSet().contains(val)) {
                                  isEndState=true;
                                  break;
                              }
                          }

                          //加入新建的dfa状态(此时要判断U不为空因为U为空则说明没有该状态)
                          if(U.size()>0) {
                              uDfaKey = keyNum;
                              C.add(new NfaTransitDfa(keyNum++, false, U, isEndState));
                              unSignedNum++;
                          }
                      }
                      //Set:DFA中的dfaFunctions(此时要判断U不为空因为U为空则说明没有状态)
                      if(U.size()>0) {
                          DfaFunction dfaFunction = new DfaFunction(tDfaKey, uDfaKey, symbol);
                          dfa.getFunctions().add(dfaFunction);
                      }
                  }
              }

              //此时形成了keyNum个DFA状态
              for(NfaTransitDfa transitState:C){
                  //Set:dfa的StateSet
                  dfa.getStateSet().add(transitState.getDfaKey());
                  //Set:dfa的endStateSet
                  if(transitState.isEndState()) {
                      dfa.getEndStateSet().add(transitState.getDfaKey());
                  }
                  //Set:dfa的dfaNodes
                  DfaNode dfaNode=new DfaNode(transitState.getDfaKey());
                  for(DfaFunction dfaFunction:dfa.getFunctions()){
                      if(dfaFunction.getBeginState()==dfaNode.getState()){
                          dfaNode.getFunctionSets().add(dfaFunction);
                      }
                  }
                  dfa.getDfaNodes().add(dfaNode);
              }
              dfas.add(dfa);
          }
          return dfas;
     }

     private Set<Integer> e_closure(Set<Integer> stateSet, Nfa nfa){
           /*e_closure意思是说stateSet中经过输入字符e转换的状态集合
             param:stateSet:为状态集合
             return:经过输入字符*所转换的状态集合
           */
          Set<Integer> outStates=new HashSet<Integer>();
          for(Integer state:stateSet){
            for(NfaNode node:nfa.getNfaNodes()){
                if(node.getState()==state){//找到nfa中正确的nfaNode
                    Set<NfaFunction> nfs=node.getFunctionSet();
                    for(NfaFunction nf:nfs){
                        if(nf.getInputSymbol()=='*') outStates.addAll(nf.getEndStateSet());
                    }
                }
            }
          }
         //任何状态经过空字符串*都能转换到自身状态
          outStates.addAll(stateSet);
          return outStates;
     }

     private Set<Integer>radian_move(Set<Integer> stateSet,Nfa nfa,char symbol){
          /*radian_move表示的是stateSet中的状态经过symbol转换的状态集合
          param:stateSet:状态集合;     symbol:输入符号
          return: 状态集合
           */
          Set<Integer> outStates=new HashSet<Integer>();
          for(Integer state:stateSet){
             for(NfaNode node:nfa.getNfaNodes()){
                 if(node.getState()==state){
                     Set<NfaFunction> nfs=node.getFunctionSet();
                     for(NfaFunction nf:nfs){
                         if(nf.getInputSymbol()==symbol) outStates.addAll(nf.getEndStateSet());
                     }
                 }
             }
         }

         return outStates;
     }

     /**比较两个集合是否相等**/
    public boolean isSetEquals(Set<Integer> p,Set<Integer>q){
        if(p.size()!=q.size())return false;
        int num=0;
        for(Integer p1:p){
            for(Integer q1:q){
                if(p1==q1){
                    num++;
                    break;
                }
            }
        }
        return num==p.size()?true:false;
    }



    /**
     test tLexical2Grammar()
     */
    public static void main(String[] args){
        lexicalAnalysisUtils tmp=new lexicalAnalysisUtils();
        Set<FormalGrammar> grammars=tmp.tLexical2FGrammar();
        Set<Nfa>nfa=tmp.fGrammar2Nfa(grammars);



    }

}
