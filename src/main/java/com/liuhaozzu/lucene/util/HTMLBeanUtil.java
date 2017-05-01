package com.liuhaozzu.lucene.util;

import com.liuhaozzu.lucene.po.HTMLBean;
import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.Source;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;

/**
 * Created by Administrator on 2017/5/1 0001.
 */
public class HTMLBeanUtil {
    @Test
    public static HTMLBean parseHTML(File file) {
        try {
            Source sc = new Source(file);
            Element title = sc.getFirstElement(HTMLElementName.TITLE);
            String content = sc.getTextExtractor().toString();
            HTMLBean hb = new HTMLBean();
            hb.setContent(content);
            if (title != null) {
                hb.setTitle(title.getTextExtractor().toString());
            }
            String path = file.getAbsolutePath();
            hb.setUrl("http://" + path);
            return hb;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
