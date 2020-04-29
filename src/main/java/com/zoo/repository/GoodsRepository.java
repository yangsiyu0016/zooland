package com.zoo.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.zoo.model.search.Goods;

public interface GoodsRepository extends ElasticsearchRepository<Goods, String> {

}
