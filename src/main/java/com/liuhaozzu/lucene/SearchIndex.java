package com.liuhaozzu.lucene;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.file.Paths;

/**
 * Created by Administrator on 2017/4/30 0030.
 */
public class SearchIndex {
    public static final String indexDir = "D:\\workspaces\\intellij\\lucene\\index";
    public static final String dataDir = "D:\\workspaces\\intellij\\lucene\\data";

    @Test
    public void search() {
        try {
            Directory dir = FSDirectory.open(Paths.get(CreateIndex.indexDir));
            IndexReader reader = DirectoryReader.open(dir);
            IndexSearcher searcher = new IndexSearcher(reader);
            QueryParser qp = new QueryParser("content", new StandardAnalyzer());

            Query query = qp.parse("java");
            TopDocs search = searcher.search(query, 10);
            ScoreDoc[] scoreDocs = search.scoreDocs;
            for (ScoreDoc sc : scoreDocs) {
                int docId = sc.doc;
                Document doc = reader.document(docId);
                System.out.println(doc.get("filename"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }
}
