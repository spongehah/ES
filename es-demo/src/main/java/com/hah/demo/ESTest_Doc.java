package com.hah.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHost;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;

import java.io.IOException;

public class ESTest_Doc {

    public static void main(String[] args) throws IOException {
        //创建ES客户端
        RestHighLevelClient esClient = new RestHighLevelClient(
                RestClient.builder(new HttpHost("localhost", 9200, "http")));

        //插入数据
        IndexRequest request = new IndexRequest();
        request.index("user").id("1001");
        User user = new User("张三", "男", 30);
        ObjectMapper mapper = new ObjectMapper();
        String userJson = mapper.writeValueAsString(user);
        request.source(userJson, XContentType.JSON);
        IndexResponse response = esClient.index(request, RequestOptions.DEFAULT);
        //返回结果
        System.out.println(response.getResult());


        //修改数据
        UpdateRequest updateRequest = new UpdateRequest();
        updateRequest.index("user").id("1001");
        updateRequest.doc(XContentType.JSON, "sex", "女");
        UpdateResponse updateResponse = esClient.update(updateRequest, RequestOptions.DEFAULT);
        System.out.println(updateResponse.getResult());


        //查询数据
        GetRequest getRequest = new GetRequest();
        getRequest.index("user").id("1001");
        GetResponse getResponse = esClient.get(getRequest, RequestOptions.DEFAULT);
        System.out.println(getResponse.getSourceAsString());


        //删除数据
        DeleteRequest deleteRequest = new DeleteRequest();
        deleteRequest.index("user").id("1001");
        DeleteResponse deleteResponse = esClient.delete(deleteRequest, RequestOptions.DEFAULT);
        System.out.println(deleteResponse.getResult());

        //关闭ES客户端
        esClient.close();
    }
}
