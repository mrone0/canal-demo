package com.mrone.service.impl;

import co.elastic.clients.elasticsearch.sql.QueryRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.mrone.entity.User;
import com.mrone.service.EsService;
import jakarta.annotation.PostConstruct;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.GetIndexResponse;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.elasticsearch.index.reindex.UpdateByQueryRequest;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author Mr.One
 * @program canal-demo
 * @description
 * @create 2024-06-20 20:44
 **/
@Service
public class EsServiceImpl implements EsService {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    private Gson gson = new Gson();


    @PostConstruct
    private void init() {
        try {
            GetIndexRequest getIndexRequest = new GetIndexRequest("user");
            boolean indexExists = restHighLevelClient.indices().exists(getIndexRequest, RequestOptions.DEFAULT);
            if (indexExists) return;
            CreateIndexRequest request = new CreateIndexRequest("user");

            XContentBuilder mapping = XContentFactory.jsonBuilder()
                    .startObject()
                    .startObject("properties")
                    .startObject("id")
                    .field("type", "keyword")
                    .endObject()
                    .startObject("name")
                    .field("type", "keyword")
                    .endObject()
                    .startObject("age")
                    .field("type", "integer")
                    .endObject()
                    .startObject("phone")
                    .field("type", "keyword")
                    .endObject()
                    .endObject()
                    .endObject();
            request.mapping(mapping);
            CreateIndexResponse response = restHighLevelClient.indices().create(request, RequestOptions.DEFAULT);
            if (response.isAcknowledged()) {
                restHighLevelClient.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void insert(User user) {
        ObjectMapper mapper = new ObjectMapper();
        IndexRequest indexRequest = new IndexRequest();
        try {
            new IndexRequest().index("user")
                    .source(mapper.writeValueAsString(user), XContentType.JSON);
            restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void initIndex(List<User> userList) {
        BulkRequest bulkRequest = new BulkRequest();
        userList.forEach(user -> {
            bulkRequest.add(new IndexRequest("user")
                    .source(gson.toJson(user), XContentType.JSON));
        });

        BulkResponse bulk = null;
        try {
            bulk = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
            System.out.println(!bulk.hasFailures());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(User user) {
        UpdateByQueryRequest request = new UpdateByQueryRequest("user");
        request.setQuery(QueryBuilders.matchQuery("id", user.getId()));
        Script script = new Script(ScriptType.INLINE, "painless",
                "ctx._source.name = params.name;ctx._source.age = params.age;ctx._source.phone = params.phone",
                Map.of("name", user.getName(), "age", user.getAge(), "phone", user.getPhone()));
        request.setScript(script);
        try {
            BulkByScrollResponse response = restHighLevelClient.updateByQuery(request, RequestOptions.DEFAULT);
            long updatedDocuments = response.getUpdated();
            System.out.println("Number of documents updated: " + updatedDocuments);
        } catch (Exception e) {
            System.out.println("fail");
        }

    }

    @Override
    public void delete(User user) {
        DeleteByQueryRequest request = new DeleteByQueryRequest("user");
        request.setQuery(QueryBuilders.termQuery("id", user.getId()));
        try {
            BulkByScrollResponse response = restHighLevelClient.deleteByQuery(request, RequestOptions.DEFAULT);
            long deletedDocuments = response.getDeleted();
            System.out.println("Number of documents deleted: " + deletedDocuments);
        } catch (IOException e) {
            System.out.println("fail");
        }

    }
}
