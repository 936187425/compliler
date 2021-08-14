package lexicalAnalysis.entity;

/**
 * @Package: lexicalAnalysis.entity
 * @ClassName: token
 * @Author: panhengyu
 * @CreateTime: 2021-03-21 19:27
 * @Description:token对象，getTokenList类output的类对象
 */
public class Token {
    private int row;
    private String type;//保留字、标识符、算符、界符以及常数值
    private String content;

    public Token(int row, String type, String content) {
        this.row = row;
        this.type = type;
        this.content = content;
    }

    public Token() {
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Token{" +
                "row=" + row +
                ", type='" + type + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
