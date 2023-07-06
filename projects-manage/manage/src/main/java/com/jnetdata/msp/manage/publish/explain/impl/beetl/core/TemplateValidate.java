package com.jnetdata.msp.manage.publish.explain.impl.beetl.core;

import org.beetl.core.GroupTemplate;
import org.beetl.core.Resource;
import org.beetl.core.Transformator;
import org.beetl.core.statement.ErrorGrammarProgram;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Reader;

/**
 * Created by xujian on 2017/12/10.
 */
@Service
public class TemplateValidate {
    @Autowired
    GroupTemplate groupTemplate;

    public void loadTemplate(Resource res) {
        Transformator sf = null;

        ErrorGrammarProgram ep;
        try {
            Reader reader = res.openReader();
            sf = new Transformator(this.groupTemplate.getConf().getPlaceholderStart(),
                    this.groupTemplate.getConf().getPlaceholderEnd(), this.groupTemplate.getConf().getStatementStart(),
                    this.groupTemplate.getConf().getStatementEnd());

            if (this.groupTemplate.getConf().isHtmlTagSupport()) {
                sf.enableHtmlTagSupport(this.groupTemplate.getConf().getHtmlTagStart(), this.groupTemplate.getConf().getHtmlTagEnd(), this.groupTemplate.getConf().getHtmlTagBindingAttribute());
            }

            Reader scriptReader = sf.transform(reader);
//            Program program = this.groupTemplate.getE.createProgram(res, scriptReader, sf.textMap, sf.lineSeparator, this);
//            return program;
        } catch (Exception e) {

        }
    }
}
