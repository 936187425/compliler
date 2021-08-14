package fileUtils;

import CONST.constPath;

import java.util.Arrays;

/**
 * @Package: lexicalAnalysis.entity.input
 * @ClassName: input
 * @Author: panhengyu
 * @CreateTime: 2021-03-23 16:47
 * @Description:描述的是从磁盘读取数据的缓冲区对象包含词法分析文本、语法分析文本以及语义分析文本的open读取函数。
 */
public class inputMain {
    private inputSystem iSys;//inputSystem

    private final int MAXLOOK=16;//look head最多字符数
    private final int MAXLEN=1024;//分词后字符串的最大长度
    private final int BUFSIZE=(MAXLEN*3)+(2*MAXLOOK);//缓冲区大小
    private int End_buf=BUFSIZE;//缓冲区的逻辑结束地址
    private final int DANGER=(End_buf-MAXLOOK);//需要发生flush的界限
    private final int END=BUFSIZE;
    private final byte[] Start_buf=new byte[BUFSIZE];//创建缓冲区的大小



    //上一个被词法分析器解析的字符串相关属性
    private int pMark=END;//上一个被词法分析器解析的字符串的起始地址
    private int pLineno=0;//上一个被词法分析器解析的字符串的所在的行号(Token列表中一个属性)
    private int pLength=0;//上一个被词法分析器解析的字符串的长度

    //当前被词法分析器解析的字符串相关属性
    private int sMark=END;//当前解析的字符串的起始地址
    private int eMark=END;//当前解析的字符串的结束地址
    private int NEXT=END;//指向当前要读入的字符位置
    private int Lineno=1;//当前被词法分析器解析的字符串的行号

    private int Mline=1;
    //标记输入流的情况
    private boolean Eof_read=false;//Eof_read==true表示当前输入流(输入流与缓冲区不同概念)再也没有信息可读


    public inputMain() {
        iSys=new inputSystem();
        i_openTestSource();//源代码文件输入流
        i_openTestLexical();//词法分析中文法的输入流
        i_openTestSyntax();
        i_openTestGrammar();
        //初始化
        Eof_read=false;
        NEXT=END;
        sMark=END;
        eMark=END;
        End_buf=END;
        Lineno=1;
        Mline=1;
    }

    /**打开源代码文本文件testSource.txt
     */
    public void i_openTestSource(){
        iSys.openText(constPath.GET_SOURCE_TEXT);
    }
    /**
     * 打开词法分析的测试文本testLexical.txt,返回StringBuffer
     * @ return testLexical.txt的字符串
     */
    public void i_openTestLexical(){
        iSys.openText(constPath.GET_LEXICAL_TEXT);
    }
    /**
     * 打开语法分析的文本testSyntax.txt
    */
    public void i_openTestSyntax(){
        iSys.openText(constPath.GET_SYNTAX_TEXT);
    }
    /**
     *打开语义分析的文本testGrammar.txt
     */
    public void i_openTestGrammar(){
        iSys.openText(constPath.GET_GRAMMAR_TEXT);
    }

    public inputSystem getiSys() {
        return iSys;
    }

    public void setiSys(inputSystem iSys) {
        this.iSys = iSys;
    }


    /**
     * 判断当前输入流和缓冲区是否有数据可读取
     * @return true==没有字符
     */
    boolean noMoreChars(){

        return (Eof_read&&NEXT>=End_buf);
    }

    /**
     * 返回当前被词法解析器解析的字符串长度
     * @return 字符串长度
     */
    public int i_Nlength(){
        return eMark-sMark;
    }

    /**
     * 返回当前被词法解析器解析的字符串内容
     * @return 字符串内容
     */
    public String i_Ntext(){
        byte[] out= Arrays.copyOfRange(Start_buf,sMark,sMark+i_Nlength());
        return out.toString();
    }

    /**
     * 返回当前被词法解析器解析的字符串的行号
     * @return 行号
     */
    public int i_Nlineno(){
        return this.Lineno;
    }


    /**
     * 返回上一个被词法解析器解析的字符串的长度
     * @return 长度
     */
    public int i_Plength(){
        return this.pLength;
    }

    /**
     * 返回上一个被词法解析器解析的字符串的行号
     * @return 行号
     */
    public int i_Plineno(){
        return this.pLineno;
    }

    /**
     * 返回上一个被词法解析器解析的字符串内容
     * @return  内容
     */
    public String i_Ptext(){
        byte[] out=Arrays.copyOfRange(Start_buf,pMark,pMark+i_Plength());
        return out.toString();
    }


    /**
     * 把NEXT所指的缓冲区作为刷新的开始即作为StartBuf
     * @return NEXT值
     */
    public int i_mark_start(){
        Mline=Lineno;
        eMark=sMark=NEXT;
        return sMark;
    }

    /**
     * 作用就是当读完一个被词法分析解析后的完整单词后，将eMark设为Next说明当前单词已经解析完
     * @return eMark=NEXT
     */
    public int i_mark_end(){
        Mline=Lineno;
        eMark=NEXT;
        return eMark;
    }

    /**
     * 上一个被词法解析器解析的字符串将无法在缓冲区中找到,指针往后移动
     */
    public int i_mark_prev(){
        pMark=sMark;
        pLineno=Lineno;
        pLength=eMark-sMark;
        return pMark;
    }



    /****************************实现输入系统(分词系统)的主要方法****************************************/

    public static final int NO_MORE_CHARS_TO_READ=0;
    public static final int FLUSH_OK=1;
    public static final int FLUSH_ERROR=-1;

    /**
     * i_advance是真正的读取字符函数，它将字符从数据流中存入缓冲区中，并返回缓冲区要读取的字符
     * 并将NEXT+1,并指向下一个字符,如果NEXT的位置距离缓冲区的End_buff不到MAXLOOK时即在越过danger后就需执行刷新flush操作
     * @return NEXT指针在Startbuff缓冲区中读取的字符
     */
    public byte i_advance(){

        if(noMoreChars()){
            return NO_MORE_CHARS_TO_READ;
        }

        if(Eof_read==false&&i_flush(false)<0){
            return FLUSH_ERROR;
        }

        if(Start_buf[NEXT]=='\n'){
            Lineno++;//当前行号加1
        }
        return Start_buf[NEXT++];
    }

    /**
     * 对Start_buff进行刷新操作
     * @param force==false表示没有强制一定要进行刷新操作,==true则表示一定要进行刷新操作
     * @return  FLUSH_OK=1 or FLUSH_ERROR=-1
     */
    private int i_flush(boolean force){

        if(noMoreChars()){
            return NO_MORE_CHARS_TO_READ;
        }
        if(Eof_read){
            /*
            表示缓冲区已经没有数据可读了但是输入流中还有数据
             */
            return FLUSH_OK;
        }

        int shift_dis,copy_dis,left_edge;
        if(NEXT>DANGER||force) {
            /*
             *开始进行刷新操作：
             *即将缓冲区从min(pMark,sMark)到End_buf中的数据整体向右平移min(pMark,sMark)个单位,然后在整体移动的数据后
             *从输入流中读入数据至End_buf处。
             * shift_dis整体移动的距离
             * copy_dis整体移动的数据缓冲区区域
             * left_edge整体移动的数据缓冲区左边界
             */
            left_edge = pMark < sMark ? pMark : sMark;
            shift_dis = left_edge;

            if (shift_dis < MAXLEN) {
                if (!force) return FLUSH_ERROR;
                //强制刷新
                left_edge = i_mark_start();
                i_mark_prev();
                shift_dis = left_edge;
            }
            copy_dis = End_buf - left_edge;
            //移动缓冲区
            System.arraycopy(Start_buf, 0, Start_buf, left_edge, copy_dis);

            //填充缓冲区
            if (i_fillBuff(copy_dis) == 0) {
                System.out.println("location:i_flush\n cause:Buffer full ");
            }

            if (pMark != 0) pMark -= shift_dis;
            sMark -= shift_dis;
            eMark -= shift_dis;
            NEXT -= shift_dis;
        }
        return FLUSH_OK;
    }


    /**
     * 从输入流中读取信息,填充缓冲区平移后的可用空间,可用空间的长度是从starting_at到End_buf
     * 每次从数据流中读取的数据长度是MAXLEN的整数倍
     * @param starting_at
     * @return 是否成功填充缓冲区的情况
     */
    private int i_fillBuff(int starting_at){
        int need=((END-starting_at)/MAXLEN)*MAXLEN;//需要从输入流中读取的数据长度
        int got=0;//实际从输入流中读取的数据长度
        if(need<0){
            System.out.println("location:i_fillBuff\n cause:starting_at is error");
        }
        if(need==0) return 0;
        if((got=iSys.read(Start_buf,starting_at,need,constPath.GET_SOURCE_TEXT))==-1){
            System.out.println("location:i_fillBuff\n cause:Can't read input file");
        }
        End_buf=starting_at+got;
        if(got<need){
            //输入流已经到末尾
            Eof_read=true;
        }
        return got;
    }


}
