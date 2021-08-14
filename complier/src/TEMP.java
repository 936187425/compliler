/**
 * @Package: PACKAGE_NAME
 * @ClassName: TEMP
 * @Author: panhengyu
 * @CreateTime: 2021-03-26 18:57
 * @Description:\
 */
/*public class TEMP*/
        /*public void  isTypeOfWord(Token nowWord, Set<Dfa> dfas, Set<Token> tokens){

            Boolean[] flags=new Boolean[5];
            for(Dfa dfa:dfas){
                if(dfa.getTitle().equals("isIdentier")) {
                    flags[0]=isIdentier(nowWord.getContent(),dfa);
                    if(flags[0]) {
                        nowWord.setType("Identier");
                        tokens.add(nowWord);
                    }else{
                        System.out.println("Error:the word line"+nowWord.getRow()+" "
                                +nowWord.getContent()+"is not right identier");
                    }
                }
                if(dfa.getTitle().equals("isKey")){
                    flags[1]=isKey(nowWord.getContent(),dfa);
                    if(flags[1]) {
                        nowWord.setType("Key");
                        tokens.add(nowWord);
                    }
                }
            /*TODO
            if(dfa.getTitle().equals("isConst")){}
            if(dfa.getTitle().equals("isOperator")){}
            if(dfa.getTitle().equals("isQualifier")){}
            */
      /*      }

        }
*/
        /**
         * 判断单词是否是标识符
         * @param nowWord
         * @param dfa
         * @return
         */
       /* public boolean isIdentier(String nowWord,Dfa dfa){
            int index=0;
            int nowState=0;
            Set<Integer> endStates=new HashSet<Integer>();
            //字符串一个一个字符的扫描
            while(index<nowWord.length()){
                char c=nowWord.charAt(index);
                for(DfaNode node:dfa.getDfaNodes()){
                    //取出beginState为nowState的dfaNode
                    if(node.getState()==nowState) {
                        boolean flag=false;

                        //map(c)=>a/b/c a:下划线,b:[0-9],c:A-Z,a-z
                        char finalChar;
                        if(c<='9'&&c>='0') finalChar='b';
                        else if((c<='z'&&c>='a')||(c<='Z'&&c>='a')) finalChar='c';
                        else finalChar='a';

                        //遍历该dfaNode的dfaFunction
                        for (DfaFunction dfaFunction : node.getFunctionSets()) {
                            if(dfaFunction.getInputSymbol()==finalChar){
                                nowState=dfaFunction.getEndState();
                                flag=true;
                                break;
                            }
                        }
                        if(flag) break;
                        else return false;//此时没有找到合适的转换函数即直接return false
                    }
                }
                index++;
            }

            if(dfa.getEndStateSet().contains(nowState))return true;//最后状态为可接受状态就输出true
            else return false;//最后状态不为可接受状态就输出false
        }

        /**
         * 关键字的判断函数
         */
        /*public boolean isKey(String nowWord,Dfa dfa){
            int index=0;
            int nowState=0;
            Set<Integer> endStates=new HashSet<Integer>();
            //字符串一个一个字符的扫描
            while(index<nowWord.length()){
                char c=nowWord.charAt(index);
                for(DfaNode node:dfa.getDfaNodes()){
                    //取出beginState为nowState的dfaNode
                    if(node.getState()==nowState) {
                        boolean flag=false;
                        //遍历该dfaNode的dfaFunction
                        for (DfaFunction dfaFunction : node.getFunctionSets()) {
                            if(dfaFunction.getInputSymbol()==c){
                                nowState=dfaFunction.getEndState();
                                flag=true;
                                break;
                            }
                        }
                        if(flag) break;
                        else return false;//此时没有找到合适的转换函数即直接return false
                    }
                }
                index++;
            }
            if(dfa.getEndStateSet().contains(nowState))return true;//最后状态为可接受状态就输出true
            else return false;//最后状态不为可接受状态就输出false
        }

/**
 * 算术符的判断函数
 */
/**
 * 限定符的判断函数
 */
/**
 * 常数的判断函数
 */
/*{
}
*/