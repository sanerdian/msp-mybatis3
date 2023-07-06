package com.jnetdata.msp.core.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jnetdata.msp.core.dbsource.model.DbSource;
import com.jnetdata.msp.core.dbsource.service.DbSourceService;
import com.jnetdata.msp.core.es.EsFieldInfo;
import com.jnetdata.msp.core.util.MapUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.shiro.authc.AuthenticationException;
import org.elasticsearch.action.admin.indices.alias.get.GetAliasesRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.*;
import org.elasticsearch.client.indices.*;
import org.elasticsearch.cluster.metadata.AliasMetaData;
import org.elasticsearch.cluster.metadata.MappingMetaData;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.thenicesys.data.api.EntityId;
import org.thenicesys.data.api.Pager;
import org.thenicesys.data.api.Pair;
import org.thenicesys.web.vo.PageVo1;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
public class EsCommonService {

    /**
     * es客户端连接服务器信息
     * @author 王树彬
     * @date 2021/6/19
     */
    @Autowired
    EsProperties esProperties;

    @Autowired
    DbSourceService dbSourceService;

    private Map<Long,RestHighLevelClient> clients = new HashMap();

    @PostConstruct
    public void init() {
        try {
            List<DbSource> list = dbSourceService.allByType("ES");
            for (DbSource dbSource : list) {
                createClient(dbSource);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    /**
     * 获取客户端连接
     * @param dbSource
     * @author 王树彬
     * @date 2022/12/7
     * @return
     */
    private RestHighLevelClient getClient(DbSource dbSource){
        if(clients.containsKey(dbSource.getId()) && clients.get(dbSource.getId())!=null){
            return clients.get(dbSource.getId());
        }
        return createClient(dbSource);
    }

    public void flashClient(Long dbSourceId){
        createClient(dbSourceService.getById(dbSourceId));
    }

    public void flashClient(DbSource dbSource){
        createClient(dbSource);
    }

    public void delClient(Long dbSourceId){
        clients.remove(dbSourceId);
    }

    private RestHighLevelClient createClient(DbSource dbSource){
        RestClientBuilder builder = RestClient.builder(new HttpHost(dbSource.getIp(), dbSource.getPort(), "http"));
        if(!StringUtils.isEmpty(dbSource.getAcount())){
            CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
            credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(dbSource.getAcount(),dbSource.getPw()));
            builder.setHttpClientConfigCallback(f -> f.setDefaultCredentialsProvider(credentialsProvider));
        }
        RestHighLevelClient restHighLevelClient = new RestHighLevelClient(builder);
        clients.put(dbSource.getId(),restHighLevelClient);
        return restHighLevelClient;
    }

    private RestHighLevelClient getClient(){
        return this.getClient((Long)null);
    }

    /**
     * 获取客户端连接
     * @param dbSourceId
     * @author 王树彬
     * @date 2021/6/29
     * @return
     */
    private RestHighLevelClient getClient(Long dbSourceId){
        if(dbSourceId==null){
            if(clients.isEmpty()){
                DbSource dbSource = dbSourceService.list().get(0);
                return createClient(dbSource);
            }else {
                return clients.get(clients.keySet().iterator().next());
            }
        }
        if(clients.containsKey(dbSourceId) && clients.get(dbSourceId)!=null){
            return clients.get(dbSourceId);
        }
        DbSource byId = dbSourceService.getById(dbSourceId);
        return createClient(byId);
    }

    /**
     * es简单查询
     * @param query
     * @param indices
     * @author 王树彬
     * @date 2021/6/29
     * @return
     */
    public SearchHit[] simpleSearch(QueryBuilder query, String... indices){
        return this.commonSearch(0, 1000, null, true, query, indices);
    }

    public <T> Pager<T> search(String index, QueryBuilder builder, PageVo1 pager, Class<T> c, List<String> highlightFields) {
        return search(index, builder, pager, c, highlightFields, highlightFields);
    }
    public <T> Pager<T> search(Long dataSourceId, String index, QueryBuilder builder, PageVo1 pager, Class<T> c, List<String> highlightFields) {
        return search(dataSourceId,index,builder,pager,c,highlightFields,highlightFields);
    }

    public <T> Pager<T> search(String index, QueryBuilder builder, PageVo1 pager, Class<T> c, List<String> highlightFields, List<String> highlightInfoFields) {
        return search(getClient(),index,builder,pager,c,highlightFields,highlightInfoFields);
    }

    public <T> Pager<T> search(Long dataSourceId,String index, QueryBuilder builder, PageVo1 pager, Class<T> c, List<String> highlightFields, List<String> highlightInfoFields) {
        return search(getClient(dataSourceId),index,builder,pager,c,highlightFields,highlightInfoFields);
    }

    public <T> Pager<T> search(RestHighLevelClient client,String index, QueryBuilder builder, PageVo1 pager, Class<T> c, List<String> highlightFields, List<String> highlightInfoFields) {
//        ScriptScoreFunctionBuilder scoreFunctionBuilder = ScoreFunctionBuilders.scriptFunction(new Script("return doc ['updatetime'].value.millis/1000000000000L"));
//        FunctionScoreQueryBuilder functionScoreQueryBuilder = QueryBuilders.functionScoreQuery(builder, scoreFunctionBuilder)
//                .scoreMode(FunctionScoreQuery.ScoreMode.SUM).boostMode(CombineFunction.SUM);

        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
//        sourceBuilder.query(functionScoreQueryBuilder);
        sourceBuilder.query(builder);
        sourceBuilder.from((pager.getCurrent() -1) * pager.getSize()).size(pager.getSize());
        sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));
        sourceBuilder.trackTotalHits(true);
        sourceBuilder.explain(true);
        HighlightBuilder hiBuilder = new HighlightBuilder();
        hiBuilder.preTags("<font color=red>");
        hiBuilder.postTags("</font>");
        hiBuilder.highlighterType("unified");
        hiBuilder.fragmentSize(100000); //最大高亮分片数
        if(highlightFields != null && !highlightFields.isEmpty()) {
            for (String field : highlightFields) {
                hiBuilder.field(field);
            }
        }else{
            List<Map<String, Object>> fields = getFields(client,index);
            for (Map<String, Object> field : fields) {
                hiBuilder.field(String.valueOf(field.get("key")));
            }
        }
        sourceBuilder.highlighter(hiBuilder);
        if(!pager.getSortProps().isEmpty()){
            for (Pair<String, Boolean> sortProp : (List<Pair<String, Boolean>>)pager.getSortProps()) {
                sourceBuilder.sort(SortBuilders.fieldSort(sortProp.getKey()).order(sortProp.getValue() ? SortOrder.ASC : SortOrder.DESC));
            }
        }
        SortBuilders.fieldSort("updatetime").order(SortOrder.DESC);
        SearchRequest request = new SearchRequest(index);
        request.source(sourceBuilder);
        SearchResponse response = null;
        try {
            response = client.search(request, RequestOptions.DEFAULT);
            // 打印请求体
            System.out.println("es查询:" + request.source().toString());

            if(response.getSuccessfulShards() != 5){
                throw new AuthenticationException(response.getShardFailures()[0].reason());
            }
            long count = response.getHits().getTotalHits().value;
            SearchHit[] hits = response.getHits().getHits();
            List<T> res = new ArrayList<>(hits.length);
            for (SearchHit hit : hits) {
                JSONObject jsonObject = JSON.parseObject(hit.getSourceAsString());
//                //处理高亮片段
                Map<String, HighlightField> highlightFieldsOfResult = hit.getHighlightFields();
                List<String> highlightInfos = new ArrayList<>();
                Map<String, Object> hightlightFields = new HashMap<>();
                for (Map.Entry<String, HighlightField> objectEntry : highlightFieldsOfResult.entrySet()) {
                    HighlightField nameField = objectEntry.getValue();
                    if (nameField != null) {
                        Text[] fragments = nameField.fragments();
                        StringBuilder nameTmp = new StringBuilder();
                        for (Text text : fragments) {
                            nameTmp.append(text);
                            if(highlightInfoFields==null||highlightInfoFields.isEmpty()||highlightInfoFields.indexOf(objectEntry.getKey())>=0){
                                String hf = text.toString();
                                hf = hf.replace("\n",",").replace("\\n","，");
                                String[] strs = hf.split("(?![/（）\\\\])[\\p{P}]" );
                                List<String> collect = Arrays.stream(strs).filter(m -> m.indexOf("<font") >= 0).distinct().collect(Collectors.toList());
                                highlightInfos.addAll(collect);
                            }
                        }
                        //将高亮片段组装到结果中去
                        String nameTmpStr = nameTmp.toString();
                        hightlightFields.put(objectEntry.getKey(),nameTmpStr);
                    }
                }
                jsonObject.put("hightlightFields",hightlightFields);
                jsonObject.put("highlightInfos",highlightInfos);
                jsonObject.put("id",hit.getId());
                jsonObject.put("esscore",hit.getScore());
                T t = MapUtil.map2Obj(jsonObject, c);
                res.add(t);
            }
            Pager pager1 = pager.toPager();
            pager1.setTotal((int)count);
            pager1.setRecords(res);
            return pager1;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> Pager<T> search(String index, BoolQueryBuilder builder, PageVo1 pager, Class<T> c) {
        return search(index,builder,pager,c,null,null);
    }

    /**
     * es查询
     * @param start 开始
     * @param end 结束
     * @param sortField 排序字段
     * @param order 是否顺序
     * @param query 查询体
     * @param indices 索引
     * @author 王树彬
     * @date 2021/6/29
     */
    public SearchHit[] commonSearch(int start, int end, String sortField, boolean order, QueryBuilder query, String... indices){
        try {
            //配置查询数量、超时时间
            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
            sourceBuilder.query(query).from(start).size(end);
            sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));
            if(!StringUtils.isEmpty(sortField)){
                sourceBuilder.sort(SortBuilders.fieldSort(sortField).order(order ? SortOrder.ASC : SortOrder.DESC));
            }
            //查询
            SearchRequest searchRequest = new SearchRequest();
            searchRequest.indices(indices);
            searchRequest.source(sourceBuilder);
            SearchResponse response = clients.get(0).search(searchRequest, RequestOptions.DEFAULT);
            return response.getHits().getHits();
        }catch (Exception e){
            log.error("es查询异常：{}", e.getMessage());
        }
        return null;
    }

    /**
     * 获取最新数据（虚拟机、宿主机、云物理主机、物理存储、负载均衡（SLB））
     * @param indexName 索引名称
     * @param idName id字段名
     * @param idValue id字段值
     * @param cloudName 云租户字段名
     * @param cloudValue 云租户字段值
     * @param order 排序字段名
     * @author 王树彬
     * @date 2021/6/29
     */
    public JSONObject getLatestData(String indexName, String idName, String idValue, String cloudName, String cloudValue, String order){
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy.MM");
            String hostIndex = indexName + "_" + format.format(new Date());
            BoolQueryBuilder query = QueryBuilders.boolQuery()
                    .must(QueryBuilders.prefixQuery(idName, idValue.trim()))
                    .must(QueryBuilders.prefixQuery(cloudName, cloudValue.trim()));
            SearchHit[] hits = this.commonSearch(0, 1, order, false, query, hostIndex);
            if(hits != null && hits.length > 0){
                return JSON.parseObject(hits[0].getSourceAsString());
            }
        }catch (Exception e){
            log.error("[索引名称：{}]从es中获取宿主机信息异常：{}", indexName, e.getMessage());
        }
        return null;
    }

    public boolean addData(String index, Map map) {
        return addData(getClient(),index,map);
    }

    public boolean addData(Long dbSourceId,String index, Map map) {
        return addData(getClient(dbSourceId),index,map);
    }

    public boolean addData(RestHighLevelClient client, String index, Map map){
        try {
            IndexRequest request = new IndexRequest(index).source(map);
            if(map.containsKey("_id") && map.get("_id")!=null) request.id((String)map.get("_id"));
            IndexResponse response = client.index(request, RequestOptions.DEFAULT);
            if(response.getResult().name().equalsIgnoreCase("created")){
                return true;
            }else {
                return false;
            }
        }catch (Exception e){
            log.error("es查询异常：{}", e.getMessage());
            return false;
        }
    }

    public <T extends EntityId<String>> boolean addData(Long dbSourceId,String index, T entity){
        return addData(getClient(dbSourceId),index,entity);
    }

    public <T extends EntityId<String>> boolean addData(RestHighLevelClient client, String index, T entity){
        Map map = MapUtil.toMapValueForEs(entity);
        try {
            IndexRequest request = new IndexRequest(index).source(map);
            if(entity.getId() != null) request.id(entity.getId());
            IndexResponse response = client.index(request, RequestOptions.DEFAULT);
            if(response.getResult().name().equalsIgnoreCase("created")){
                entity.setId(response.getId());
                return true;
            }else {
                return false;
            }
        }catch (Exception e){
            log.error("es查询异常：{}", e.getMessage());
            return false;
        }
    }


    public boolean updateData(String index,String id, Map<String,Object> map){
        return updateData(getClient(),index,id,map);
    }

    public boolean updateData(Long dbSourceId,String index,String id, Map<String,Object> map){
        return updateData(getClient(dbSourceId),index,id,map);
    }

    public boolean updateData(RestHighLevelClient client,String index,String id, Map<String,Object> map){
        try {
            UpdateRequest request = new UpdateRequest(index, id).doc(map);
            client.update(request,RequestOptions.DEFAULT);
            return true;
        }catch (Exception e){
            log.error("es查询异常：{}", e.getMessage());
            return false;
        }
    }

    public boolean delData(String index, String id){
        return delData(getClient(),index,id);
    }

    public boolean delData(Long dbSourceId,String index, String id){
        return delData(getClient(dbSourceId),index,id);
    }

    public boolean delData(RestHighLevelClient client,String index, String id){
        try {
            DeleteRequest deleteRequest = new DeleteRequest(index, id);
            DeleteResponse response = client.delete(deleteRequest, RequestOptions.DEFAULT);
            if(response.getResult().name().equalsIgnoreCase("deleted")){
                return true;
            }else {
                return false;
            }
        }catch (Exception e){
            log.error("es查询异常：{}", e.getMessage());
            return false;
        }
    }

    public <T> T getById(Long dbSourceId,String index,String id, Class<T> cz) {
        return getById(getClient(dbSourceId),index,id,cz);
    }

    public <T> T getById(RestHighLevelClient client,String index,String id, Class<T> cz) {
        GetRequest request = new GetRequest(index, id);
        try {
            GetResponse response = client.get(request, RequestOptions.DEFAULT);
            String result = response.getSourceAsString();
            return JSON.parseObject(result, cz);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Map<String,Object> getById(String index,String id) {
        return getById(getClient(),index,id);
    }

    public Map<String,Object> getById(Long dbSourceId,String index,String id) {
        return getById(getClient(dbSourceId),index,id);
    }

    public Map<String,Object> getById(RestHighLevelClient client,String index,String id) {
        GetRequest request = new GetRequest(index, id);
        try {
            GetResponse response = client.get(request, RequestOptions.DEFAULT);
            String result = response.getSourceAsString();
            if(result == null) return null;
            JSONObject jsonObject = JSON.parseObject(result);
            jsonObject.put("id",id);
            return jsonObject.getInnerMap();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取全部索引
     * @param dbSource
     * @author 王树彬
     * @date 2022/12/6
     * @return
     */
    public Set<String> getAllIndices(DbSource dbSource) {
        RestHighLevelClient client = getClient(dbSource);
        try {
            GetAliasesRequest request = new GetAliasesRequest();
//            GetSettingsRequest request1 = new GetSettingsRequest();
            GetAliasesResponse getAliasesResponse = client.indices().getAlias(request, RequestOptions.DEFAULT);
//            GetSettingsResponse settings = client.indices().getSettings(request1, RequestOptions.DEFAULT);
//            ImmutableOpenMap<String, Settings> indexToSettings = settings.getIndexToSettings();
            Map<String, Set<AliasMetaData>> map = getAliasesResponse.getAliases();
            Set<String> indices = map.keySet().stream().filter(m -> !m.startsWith(".")).collect(Collectors.toSet());
            return indices;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new HashSet<>();
    }

    public Set<String> getAllIndices(Long dbSourceId) {
        return getAllIndices(getClient(dbSourceId));
    }

    public Set<String> getAllIndices(RestHighLevelClient client) {
        try {
            GetAliasesRequest request = new GetAliasesRequest();
//            GetSettingsRequest request1 = new GetSettingsRequest();
            GetAliasesResponse getAliasesResponse = client.indices().getAlias(request, RequestOptions.DEFAULT);
//            GetSettingsResponse settings = client.indices().getSettings(request1, RequestOptions.DEFAULT);
//            ImmutableOpenMap<String, Settings> indexToSettings = settings.getIndexToSettings();
            Map<String, Set<AliasMetaData>> map = getAliasesResponse.getAliases();
            Set<String> indices = map.keySet().stream().filter(m -> !m.startsWith(".")).collect(Collectors.toSet());
            return indices;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new HashSet<>();
    }

    public List<Map<String, Object>> getFields(DbSource dbSource,String index) {
        RestHighLevelClient client = getClient(dbSource);
        return getFields(client,index);
    }

    /**
     * 获取路径
     * @param dbSourceId
     * @param index
     * @author 王树彬
     * @date 2022/12/7
     * @return
     */
    public List<Map<String, Object>> getFields(Long dbSourceId,String index) {
        RestHighLevelClient client = getClient(dbSourceId);
        return getFields(client,index);
    }

    public List<EsFieldInfo> getEsFields(Long dbSourceId,String index) {
        RestHighLevelClient client = getClient(dbSourceId);
        List<Map<String, Object>> fields = getFields(client, index);
        List<EsFieldInfo> list = new ArrayList<>();
        for (Map<String, Object> field : fields) {
            EsFieldInfo esFieldInfo = MapUtil.map2Obj(field, EsFieldInfo.class);
            list.add(esFieldInfo);
        }
        return list;
    }

    public List<Map<String, Object>> getFields(RestHighLevelClient client,String index) {
        List<Map<String, Object>> mapList = new ArrayList<>();
        try {
            //指定索引
            GetMappingsRequest getMappings = new GetMappingsRequest().indices(index);
            //调用获取
            GetMappingsResponse getMappingResponse = client.indices().getMapping(getMappings, RequestOptions.DEFAULT);


            //处理数据
            Map<String, MappingMetaData> allMappings = getMappingResponse.mappings();
            Map<String, Object> mapping1 = allMappings.get(index).sourceAsMap();
            Map<String, Object> properties = new HashMap<>();
            if(mapping1.containsKey("properties")){
                properties = (Map<String, Object>) mapping1.get("properties");
            }else{
                for (Map.Entry<String, Object> ObjectEntry : mapping1.entrySet()) {
                    Map<String, Object> properties1 = (Map<String, Object>) ObjectEntry.getValue();
                    if(properties1.containsKey("properties")){
                        properties = (Map<String, Object>) properties1.get("properties");
                        break;
                    }
                }
            }
            for (Map.Entry<String, Object> ObjectEntry : properties.entrySet()) {
                String key = ObjectEntry.getKey();
                if(key.toLowerCase().equals("id")) continue;
                Map<String, Object> map = new HashMap<>();
                Map<String, Object> value1 = (Map<String, Object>) ObjectEntry.getValue();
                map.put("key",key);
                map.put("type", value1.get("type"));
                mapList.add(map);

                if(value1.containsKey("fields")){
                    Map<String, Object> fields = (Map<String, Object>)value1.get("fields");
                    if(fields.containsKey("keyword")){
                        map = new HashMap<>();
                        map.put("key",key+".keyword");
                        map.put("type", "keyword");
                        mapList.add(map);
                    }
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mapList;
    }

    public void createIndices(Long dbSourceId,String index) {
        createIndices(getClient(dbSourceId),index);
    }

    public void createIndices(RestHighLevelClient client,String index) {
        if(StringUtils.isEmpty(index)){
            throw new RuntimeException("索引名称不能为空");
        }
        //创建索引对象
        CreateIndexRequest createIndexRequest = new CreateIndexRequest(index);
        //设置参数
        createIndexRequest.settings(Settings.builder().put("number_of_shards", "5").put("number_of_replicas", "0").put("analysis.analyzer.default.type","ik_max_word"));

        //设置别名
//        createIndexRequest.alias(new Alias("index"));

        // 额外参数
        //设置超时时间
//        createIndexRequest.setTimeout(TimeValue.timeValueMinutes(2));
//        //设置主节点超时时间
//        createIndexRequest.setMasterTimeout(TimeValue.timeValueMinutes(1));
//        //在创建索引API返回响应之前等待的活动分片副本的数量，以int形式表示
//        createIndexRequest.waitForActiveShards(ActiveShardCount.from(2));
//        createIndexRequest.waitForActiveShards(ActiveShardCount.DEFAULT);

        //操作索引的客户端
        IndicesClient indices = client.indices();
        //执行创建索引库
        CreateIndexResponse createIndexResponse = null;
        try {
            createIndexResponse = indices.create(createIndexRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
                e.printStackTrace();
        }

        //得到响应（全部）
        boolean acknowledged = createIndexResponse.isAcknowledged();
        //得到响应 指示是否在超时前为索引中的每个分片启动了所需数量的碎片副本
        boolean shardsAcknowledged = createIndexResponse.isShardsAcknowledged();

        System.out.println("acknowledged:" + acknowledged);
        System.out.println("shardsAcknowledged:" + shardsAcknowledged);
    }

    /**
     * 删除索引
     * @param esLinkId
     * @param index
     * @author 王树彬
     * @date 2022/12/5
     */
    public void deleteIndices(Long esLinkId,String index) {
        deleteIndices(getClient(esLinkId),index);
    }

    /**
     * 删除索引
     * @param client
     * @param index
     * @author 王树彬
     * @date 2022/12/5
     */
    public void deleteIndices(RestHighLevelClient client,String index) {
        if(StringUtils.isEmpty(index)){
            throw new RuntimeException("索引名称不能为空");
        }
        //创建删除索引请求
        DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest(index);
        //        执行
        AcknowledgedResponse delete = null;
        try {
            delete = client.indices().delete(deleteIndexRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //得到相应
        boolean acknowledged = delete.isAcknowledged();
    }

    public void addField(EsFieldInfo entity) {
        RestHighLevelClient client = getClient(entity.getDbSourceId());
        PutMappingRequest putMappingRequest = new PutMappingRequest(entity.getIndex());
        try {
            putMappingRequest.source("{\n" +
                            "  \"properties\": {\n" +
                            "    \""+entity.getKey()+"\": {\n" +
                            "      \"type\": \""+entity.getType()+"\"\n" +
                            "    }\n" +
                            "  }\n" +
                            "}",
                    XContentType.JSON);
            client.indices().putMapping(putMappingRequest,RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    public void close() {
//        try {
//            client.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}
