package com.hah.demo;

import org.apache.http.HttpHost;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.GetIndexResponse;

import java.io.IOException;

public class ESTest_Index {

    public static void main(String[] args) throws IOException {
        //创建ES客户端
        RestHighLevelClient esClient = new RestHighLevelClient(
                RestClient.builder(new HttpHost("localhost", 9200, "http")));

        //创建索引
        CreateIndexResponse response = esClient.indices().create(new CreateIndexRequest("user"), RequestOptions.DEFAULT);

        //响应内容
        boolean acknowledged = response.isAcknowledged();
        System.out.println("索引操作：" + acknowledged);

        //查询索引
        GetIndexResponse response2 = esClient.indices().get(new GetIndexRequest("user"), RequestOptions.DEFAULT);

        //响应内容
        System.out.println(response2.getAliases());
        System.out.println(response2.getMappings());
        System.out.println(response2.getSettings());

        //删除索引
        AcknowledgedResponse response3 = esClient.indices().delete(new DeleteIndexRequest("user"), RequestOptions.DEFAULT);
        System.out.println(response3.isAcknowledged());

        //关闭ES客户端
        esClient.close();
    }
}
