package com.hah.demo;

import org.apache.http.HttpHost;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;

import java.io.IOException;

public class ESTest_Doc_Batch {

    public static void main(String[] args) throws IOException {
        RestHighLevelClient esClient = new RestHighLevelClient(RestClient.builder(
                new HttpHost("localhost", 9200, "http")
        ));

        //批量插入doc
        BulkRequest bulkRequest = new BulkRequest();
        bulkRequest.add(new IndexRequest().index("user").id("1001").source(XContentType.JSON, "name", "zhangsan", "sex", "男", "age", 30));
        bulkRequest.add(new IndexRequest().index("user").id("1002").source(XContentType.JSON, "name", "lisi", "sex", "女", "age", 30));
        bulkRequest.add(new IndexRequest().index("user").id("1003").source(XContentType.JSON, "name", "wangwu", "sex", "男", "age", 40));
        bulkRequest.add(new IndexRequest().index("user").id("1004").source(XContentType.JSON, "name", "wangwu1", "sex", "女", "age", 40));
        bulkRequest.add(new IndexRequest().index("user").id("1005").source(XContentType.JSON, "name", "wangwu2", "sex", "男", "age", 50));
        bulkRequest.add(new IndexRequest().index("user").id("1006").source(XContentType.JSON, "name", "wangwu3", "sex", "男", "age", 50));

        BulkResponse bulkResponse = esClient.bulk(bulkRequest, RequestOptions.DEFAULT);
        System.out.println(bulkResponse.getTook());//花费的时间
        System.out.println(bulkResponse.getItems());

        //批量删除
//        bulkRequest = new BulkRequest();
//        bulkRequest.add(new DeleteRequest().index("user").id("1001"));
//        bulkRequest.add(new DeleteRequest().index("user").id("1002"));
//        bulkRequest.add(new DeleteRequest().index("user").id("1003"));
//        BulkResponse response = esClient.bulk(bulkRequest, RequestOptions.DEFAULT);
//        System.out.println(response);

        esClient.close();
    }
}
