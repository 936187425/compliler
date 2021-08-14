package fileUtils;
import java.io.*;
import CONST.*;


/**
 * @Package: utils
 * @ClassName: inputSystem
 * @Author: 潘恒宇
 * @CreateTime: 2021-03-21 13:38
 * @Description:
 * 实现对testGrammar.txt,testLexical.txt,testSource.txt,testSyntax.txt文件:
 * open:打开文件并把文件内容存入该对象的StringBuffer中
 * close:关闭文件
 * read:读取StringBuffer特定位置的连续长度数据
 */
public class inputSystem {

    private StringBuffer sourceText;
    private StringBuffer lexicalText;
    private StringBuffer syntaxText;
    private StringBuffer grammarText;
    private int curPos_source;
    private int curPos_lexical;
    private int curPos_syntax;
    private int curPos_grammar;

    private StringBuffer getStringBuffer(final int FLAG){
        StringBuffer output=null;
        switch (FLAG){
            case constPath.GET_SOURCE_TEXT:output=sourceText;break;
            case constPath.GET_LEXICAL_TEXT:output=lexicalText;break;
            case constPath.GET_SYNTAX_TEXT:output=syntaxText;break;
            case constPath.GET_GRAMMAR_TEXT:output=grammarText;break;
        }
        return output;
    }
    private String getPath(final int FLAG){
        String path=null;
        switch (FLAG){
            case constPath.GET_SOURCE_TEXT:
                path=constPath.INPUT_SOURCE_PATH;
                break;
            case constPath.GET_LEXICAL_TEXT:
                path=constPath.INPUT_LEXICAL_PATH;
                break;
            case constPath.GET_SYNTAX_TEXT:
                path=constPath.INPUT_SYNTAX_PATH;
                break;
            case constPath.GET_GRAMMAR_TEXT:
                path=constPath.INPUT_GRAMMAR_PATH;
                break;
        }
        return path;
    }
    private int getCurPos(final int FLAG){
        int curPos=-1;
        switch (FLAG){
            case constPath.GET_SOURCE_TEXT:
                curPos=curPos_source;
                break;
            case constPath.GET_LEXICAL_TEXT:
                curPos=curPos_lexical;
                break;
            case constPath.GET_SYNTAX_TEXT:
                curPos=curPos_syntax;
                break;
            case constPath.GET_GRAMMAR_TEXT:
                curPos=curPos_grammar;
                break;
        }
        return curPos;
    }
    private void setCurPos(final int FLAG,int curPos){
        switch (FLAG){
            case constPath.GET_SOURCE_TEXT:
                curPos_source=curPos;
                break;
            case constPath.GET_LEXICAL_TEXT:
                curPos_lexical=curPos;
                break;
            case constPath.GET_SYNTAX_TEXT:
                curPos_syntax=curPos;
                break;
            case constPath.GET_GRAMMAR_TEXT:
                curPos_grammar=curPos;
                break;
        }
    }


    public inputSystem() {
        sourceText=new StringBuffer();
        lexicalText=new StringBuffer();
        syntaxText=new StringBuffer();
        grammarText=new StringBuffer();
        curPos_source=0;
        curPos_lexical=0;
        curPos_syntax=0;
        curPos_grammar=0;
    }

    public void openText(final  int FLAG) {
        StringBuffer buffer=getStringBuffer(FLAG);
        File file=new File(getPath(FLAG));
        try {
            String encoding="UTF-8";
            InputStreamReader read=new InputStreamReader(
                    new FileInputStream(file),encoding
            );
            BufferedReader bufferedReader=new BufferedReader(read);
            String lineTxt;
            while((lineTxt=bufferedReader.readLine())!=null){
                buffer.append(lineTxt);
                buffer.append('\n');
            }
            buffer.append('\0');
            bufferedReader.close();
            read.close();
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("读取文件出错了");
        }
    }


    public int close() {
        return 0;
    }


    public int read(byte[] buff, int begin, int len,final int FLAG) {
        /*
        获取缓冲区指定大小的数据装入buff[begin,begin+len)
        return:返回buff数组中实际元素个数
         */
        int curPos=getCurPos(FLAG);
        StringBuffer nowText=getStringBuffer(FLAG);

        if(curPos>=nowText.length())
            return 0;
        int readCount=0;

        while(curPos+readCount<nowText.length()&&readCount<len){
            buff[begin+readCount]=nowText.toString().getBytes()[curPos+readCount];
            readCount++;
        }
        curPos+=readCount;
        setCurPos(FLAG,curPos);
        return readCount;
    }


    public StringBuffer getText(final int FLAG) {
        return getStringBuffer(FLAG);
    }


    /*
    test
     */
   /* public static void main(String[] args){
        inputSystem in=new inputSystem();
        in.openText(constPath.GET_SOURCE_TEXT);
        in.openText(constPath.GET_GRAMMAR_TEXT);
        in.openText(constPath.GET_SYNTAX_TEXT);
        in.openText(constPath.GET_LEXICAL_TEXT);
       System.out.println(in.lexicalText);
       System.out.println(in.sourceText);
    }*/


}
