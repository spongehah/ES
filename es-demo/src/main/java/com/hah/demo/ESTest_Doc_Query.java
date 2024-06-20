package com.hah.demo;

import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.FuzzyQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortOrder;

import java.io.IOException;

public class ESTest_Doc_Query {

    public static void main(String[] args) throws IOException {

        //创建ES客户端
        RestHighLevelClient esClient = new RestHighLevelClient(
                RestClient.builder(new HttpHost("localhost", 9200, "http")));

        //1 全量查询
        SearchRequest request = new SearchRequest();
        request.indices("user");
        request.source(new SearchSourceBuilder().query(QueryBuilders.matchAllQuery()));
        SearchResponse response = esClient.search(request, RequestOptions.DEFAULT);
        SearchHits hits = response.getHits();
        System.out.println(hits.getTotalHits());
        System.out.println(response.getTook());

        for (SearchHit hit : hits) {
            System.out.println(hit.getSourceAsString());
        }
        System.out.println("#############################");

        //2 条件查询
        request = new SearchRequest();
        request.indices("user");
        request.source(new SearchSourceBuilder().query(QueryBuilders.termQuery("age", 30)));
        response = esClient.search(request, RequestOptions.DEFAULT);
        hits = response.getHits();
        System.out.println(hits.getTotalHits());
        System.out.println(response.getTook());

        for (SearchHit hit : hits) {
            System.out.println(hit.getSourceAsString());
        }
        System.out.println("#############################");

        //3 分页查询
        request = new SearchRequest();
        request.indices("user");
        SearchSourceBuilder builder = new SearchSourceBuilder().query(QueryBuilders.matchAllQuery());
        builder.from(0).size(2);
        request.source(builder);
        response = esClient.search(request, RequestOptions.DEFAULT);
        hits = response.getHits();
        System.out.println(hits.getTotalHits());
        System.out.println(response.getTook());

        for (SearchHit hit : hits) {
            System.out.println(hit.getSourceAsString());
        }
        System.out.println("#############################");

        //4 排序
        request = new SearchRequest();
        request.indices("user");
        builder = new SearchSourceBuilder().query(QueryBuilders.matchAllQuery());
        builder.sort("age", SortOrder.DESC);
        request.source(builder);
        response = esClient.search(request, RequestOptions.DEFAULT);
        hits = response.getHits();
        System.out.println(hits.getTotalHits());
        System.out.println(response.getTook());

        for (SearchHit hit : hits) {
            System.out.println(hit.getSourceAsString());
        }
        System.out.println("#############################");

        //5 字段过滤
        request = new SearchRequest();
        request.indices("user");
        builder = new SearchSourceBuilder().query(QueryBuilders.matchAllQuery());
        String[] includes = {"name"};
        builder.fetchSource(includes, new String[]{});
        request.source(builder);
        response = esClient.search(request, RequestOptions.DEFAULT);
        hits = response.getHits();
        System.out.println(hits.getTotalHits());
        System.out.println(response.getTook());

        for (SearchHit hit : hits) {
            System.out.println(hit.getSourceAsString());
        }
        System.out.println("#############################");

        //6 组合查询
        request = new SearchRequest();
        request.indices("user");
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.matchQuery("age", 30));
        boolQueryBuilder.must(QueryBuilders.matchQuery("sex", "男"));
        builder = new SearchSourceBuilder().query(boolQueryBuilder);
        request.source(builder);
        response = esClient.search(request, RequestOptions.DEFAULT);
        hits = response.getHits();
        System.out.println(hits.getTotalHits());
        System.out.println(response.getTook());

        for (SearchHit hit : hits) {
            System.out.println(hit.getSourceAsString());
        }
        System.out.println("#############################");

        //7 范围查询
        request = new SearchRequest();
        request.indices("user");
        RangeQueryBuilder rangeQuery = QueryBuilders.rangeQuery("age");
        rangeQuery.gte(30);
        rangeQuery.lte(40);
        builder = new SearchSourceBuilder().query(rangeQuery);
        request.source(builder);
        response = esClient.search(request, RequestOptions.DEFAULT);
        hits = response.getHits();
        System.out.println(hits.getTotalHits());
        System.out.println(response.getTook());

        for (SearchHit hit : hits) {
            System.out.println(hit.getSourceAsString());
        }
        System.out.println("#############################");

        //8 模糊查询
        request = new SearchRequest();
        request.indices("user");
        //差多少个字符能查出来
        builder = new SearchSourceBuilder().query(QueryBuilders.fuzzyQuery("name", "wangwu").fuzziness(Fuzziness.ONE));
        request.source(builder);
        response = esClient.search(request, RequestOptions.DEFAULT);
        hits = response.getHits();
        System.out.println(hits.getTotalHits());
        System.out.println(response.getTook());

        for (SearchHit hit : hits) {
            System.out.println(hit.getSourceAsString());
        }
        System.out.println("#############################");

        //9 高亮查询
        request = new SearchRequest();
        request.indices("user");
        builder = new SearchSourceBuilder();
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.preTags("<font color='red'>");
        highlightBuilder.postTags("</font>");
        highlightBuilder.field("name");
        builder.highlighter(highlightBuilder);

        builder.query(QueryBuilders.termQuery("name", "zhangsan"));

        request.source(builder);
        response = esClient.search(request, RequestOptions.DEFAULT);
        hits = response.getHits();
        System.out.println(hits.getTotalHits());
        System.out.println(response.getTook());

        for (SearchHit hit : hits) {
            System.out.println(hit.getSourceAsString());
        }

        System.out.println("#############################");

        //10 聚合查询
        request = new SearchRequest();
        request.indices("user");
        builder = new SearchSourceBuilder();

        //最大值
        // AggregationBuilder aggregationBuilder = AggregationBuilders.max("maxAge").field("age");
        //分组
        TermsAggregationBuilder aggregationBuilder = AggregationBuilders.terms("ageGroup").field("age");
        builder.aggregation(aggregationBuilder);

        request.source(builder);
        response = esClient.search(request, RequestOptions.DEFAULT);
        hits = response.getHits();
        System.out.println(hits.getTotalHits());
        System.out.println(response.getTook());

        for (SearchHit hit : hits) {
            System.out.println(hit.getSourceAsString());
        }

        System.out.println("#############################");

        //关闭ES客户端
        esClient.close();
    }
}