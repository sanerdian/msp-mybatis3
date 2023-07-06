package com.jnetdata.msp.manage.publish.explain.impl.beetl.core;

import org.beetl.core.Transformator;

/**
 * Created by xujian on 2017/12/10.
 */
public class TransformatorExt extends Transformator {
//    char[] cs = null;
//    @Override
//    public Reader transform(Reader orginal) throws IOException, HTMLTagParserException {
//        StringBuilder temp = new StringBuilder();
//        int bufSzie = 1024;
//        cs = new char[bufSzie];
//        boolean var4 = true;
//
//        int len;
//        while((len = orginal.read(this.cs)) != -1) {
//            temp.append(this.cs, 0, len);
//        }
//
//        this.cs = temp.toString().toCharArray();
//        super.findCR();
//        this.checkAppendCR();
//        this.parser();
//        if(this.isSupportHtmlTag && this.htmlTagStack.size() != 0) {
//            String tagName = (String)this.htmlTagStack.peek();
//            GrammarToken token = GrammarToken.createToken(tagName, this.totalLineCount + 1);
//            HTMLTagParserException ex = new HTMLTagParserException("解析html tag 标签出错,未找到匹配结束标签 " + tagName);
//            ex.pushToken(token);
//            ex.line = this.totalLineCount + 1;
//            this.clear();
//            throw ex;
//        } else {
//            orginal.close();
//            return new StringReader(this.sb.toString());
//        }
//    }
}
