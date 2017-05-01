package com.liuhaozzu.lucene;

import com.liuhaozzu.lucene.po.HTMLBean;
import com.liuhaozzu.lucene.util.HTMLBeanUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.RAMDirectory;
import org.springframework.stereotype.Service;
import org.testng.annotations.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Collection;

/**
 * Created by Administrator on 2017/4/30 0030.
 */
@Service
public class CreateIndex {

    public static final String indexDir = "D:\\workspaces\\intellij\\lucene\\index";
    public static final String dataDir = "D:/www.bjsxt.com";

    @Test
    public void createIndex() {
        IndexWriter writer = null;
        IndexWriter ramWriter = null;
        try {
            Directory dir = FSDirectory.open(Paths.get(indexDir));
            Analyzer analyzer = new IKAnalyzer();
            IndexWriterConfig config = new IndexWriterConfig(analyzer);
            config.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
            writer = new IndexWriter(dir, config);
            File file = new File(dataDir);

            RAMDirectory ramDir = new RAMDirectory();
            Analyzer analyzer1 = new StandardAnalyzer();
            IndexWriterConfig config1 = new IndexWriterConfig(analyzer1);
            ramWriter = new IndexWriter(ramDir, config1);


            Collection<File> files = FileUtils.listFiles(file, TrueFileFilter.TRUE, TrueFileFilter.TRUE);
            int count = 0;
            for (File f : files) {

                HTMLBean htmlBean = HTMLBeanUtil.parseHTML(f);
                Document doc = new Document();

                doc.add(new StringField("title", htmlBean.getTitle() != null ? htmlBean.getTitle() : "title", Field.Store.YES));
                doc.add(new TextField("content", htmlBean.getContent(), Field.Store.YES));
                doc.add(new StringField("url", htmlBean.getUrl(), Field.Store.YES));
                //writer.addDocument(doc);
                ramWriter.addDocument(doc);
                count++;
                if (count == 50) {
                    ramWriter.close();
                    writer.addIndexes(ramDir);
                    ramDir = new RAMDirectory();
                    Analyzer analyzer2 = new StandardAnalyzer();
                    IndexWriterConfig config2 = new IndexWriterConfig(analyzer2);

                    ramWriter = new IndexWriter(ramDir, config2);
                    count = 0;
                    System.out.println("times:" + count);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
