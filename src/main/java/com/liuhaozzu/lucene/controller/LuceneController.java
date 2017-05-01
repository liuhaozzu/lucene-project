package com.liuhaozzu.lucene.controller;

import com.liuhaozzu.lucene.CreateIndex;
import com.liuhaozzu.lucene.po.HTMLBean;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/5/1 0001.
 */
@Controller
public class LuceneController {

    @Autowired
    private CreateIndex createIndex;

    @RequestMapping("/create.do")
    public String createIndex() {
        File file = new File(CreateIndex.indexDir);
        if (file.exists()) {
            file.delete();
            file.mkdirs();
        }
        long start = System.currentTimeMillis();
        createIndex.createIndex();
        long end = System.currentTimeMillis();
        //mav.addObject("costTime", end - start);
        System.out.println("cost:::" + (end - start));
        return "create.jsp";
    }

    @RequestMapping("/index.do")
    public String search(String keywords, Model model) {
        long start = System.currentTimeMillis();
        Directory dir = null;
        try {
            dir = FSDirectory.open(Paths.get(CreateIndex.indexDir));
            IndexReader reader = DirectoryReader.open(dir);
            IndexSearcher searcher = new IndexSearcher(reader);
            //QueryParser qp = new QueryParser("content", new StandardAnalyzer());
            //Query query = qp.parse("java");
            Analyzer analyzer = new StandardAnalyzer();
            MultiFieldQueryParser mfqp = new MultiFieldQueryParser(new String[]{"title", "content"}, analyzer);
            Query query = mfqp.parse(keywords);
            TopDocs search = searcher.search(query, 10);
            long end = System.currentTimeMillis();
            ScoreDoc[] scoreDocs = search.scoreDocs;
            //System.out.println(search.totalHits + ">>>" + (end - start));
            List<HTMLBean> results = new ArrayList<>();
            for (ScoreDoc sd : scoreDocs) {
                Document document = reader.document(sd.doc);
                SimpleHTMLFormatter sf = new SimpleHTMLFormatter("<font color=\"red\">", "</font>");
                QueryScorer qs = new QueryScorer(query, "title");
                Highlighter hl = new Highlighter(sf, qs);
                String title = hl.getBestFragment(new StandardAnalyzer(), "title", document.get("title"));

                String content = hl.getBestFragment(new StandardAnalyzer().tokenStream("content", document.get("content")), document.get("content"));

                String url = document.get("url");
                HTMLBean hb = new HTMLBean();
                hb.setContent(content);
                hb.setTitle(title);
                hb.setUrl(url);
                results.add(hb);
            }
            model.addAttribute("results", results);
            model.addAttribute("totalHits", search.totalHits);
            model.addAttribute("keywords", keywords);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (InvalidTokenOffsetsException e) {
            e.printStackTrace();
        }
        return "index.jsp";
    }
}
