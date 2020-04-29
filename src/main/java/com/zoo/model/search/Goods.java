package com.zoo.model.search;

import java.util.Map;

import javax.persistence.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import lombok.Data;
@Data
@Document(indexName = "goods", type = "docs", shards = 1, replicas = 0)
public class Goods {
    @Id
    private String id; // spuId
    @Field(type = FieldType.text, analyzer = "ik_max_word")
    private String all; // 所有需要被搜索的信息，包含标题，分类，甚至品牌
    @Field(type = FieldType.keyword, index = false)
    private String name;// 卖点
    @Field(type = FieldType.text, index = true)
    private String brandId;// 品牌id
    @Field(type = FieldType.text, index = true)
    private String typeId;
    @Field(type = FieldType.keyword, index = false)
    private String skus;// sku信息的json结构
    private Map<String, Object> specs;// 可搜索的规格参数，key是参数名，值是参数值
}
