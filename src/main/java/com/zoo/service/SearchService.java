package com.zoo.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zoo.mapper.erp.product.ProductBrandMapper;
import com.zoo.mapper.erp.product.ProductDetailMapper;
import com.zoo.mapper.erp.product.ProductSkuMapper;
import com.zoo.mapper.erp.product.ProductTypeMapper;
import com.zoo.mapper.erp.product.SpecParamMapper;
import com.zoo.model.erp.product.Product;
import com.zoo.model.erp.product.ProductBrand;
import com.zoo.model.erp.product.ProductDetail;
import com.zoo.model.erp.product.ProductSku;
import com.zoo.model.erp.product.ProductType;
import com.zoo.model.erp.product.SpecParam;
import com.zoo.model.search.Goods;
import com.zoo.repository.GoodsRepository;
import com.zoo.service.erp.product.ProductService;
import com.zoo.utils.JsonUtils;
import com.zoo.vo.PageResult;
import com.zoo.vo.SearchRequest;
import com.zoo.vo.SearchResult;

import com.fasterxml.jackson.core.type.TypeReference;

@Service
public class SearchService {

    @Autowired
    private GoodsRepository goodsRepository;
    @Autowired
    ProductService productService;
    @Autowired
    ProductSkuMapper skuMapper;
    @Autowired
    ProductBrandMapper brandMapper;
    @Autowired
    ProductTypeMapper typeMapper;
    @Autowired
    SpecParamMapper paramMapper;
    @Autowired
    ProductDetailMapper detailMapper;
    @Autowired
    ElasticsearchTemplate elasticsearchTemplate;
    private ObjectMapper mapper = new ObjectMapper();
    public PageResult<Goods> search(SearchRequest request) {
        String key = request.getKey();
        // 判断是否有搜索条件，如果没有，直接返回null。不允许搜索全部商品
        if (StringUtils.isBlank(key)) {
            return null;
        }

        // 构建查询条件
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        
        
        
        // 2、通过sourceFilter设置返回的结果字段,我们只需要id、skus、subTitle
        queryBuilder.withSourceFilter(new FetchSourceFilter(
                new String[]{"id","skus","name"}, null));
        //基本搜索条件
        QueryBuilder basicQuery = buildBasicQuery(request);
        // 1、对key进行全文检索查询
        queryBuilder.withQuery(basicQuery);
        // 3、分页
        // 准备分页参数
        int page = request.getPage();
        int size = request.getSize();
        queryBuilder.withPageable(PageRequest.of(page - 1, size));
        //排序
        String sortBy = request.getSortBy();
        Boolean desc = request.getDescending();
        if(StringUtils.isNotBlank(sortBy)) {
        	queryBuilder.withSort(SortBuilders.fieldSort(sortBy).order(desc?SortOrder.DESC:SortOrder.ASC));
        }
        //聚合
        String brandAggName = "brand";//品牌聚合名称
        String typeAggName = "type";//分类聚合名称
        //对品牌进行聚合
        queryBuilder.addAggregation(AggregationBuilders.terms(brandAggName).field("brandId.keyword"));
        //对分类进行聚合
        queryBuilder.addAggregation(AggregationBuilders.terms(typeAggName).field("typeId.keyword"));
        // 4、查询，获取结果
        AggregatedPage<Goods>  pageInfo = (AggregatedPage<Goods>) this.goodsRepository.search(queryBuilder.build());
        //品牌的聚合结果
        List<ProductBrand> brands=getBrandAggResult(pageInfo.getAggregation(brandAggName));
        //分类聚合结果
        List<ProductType> types = getTypeAggResult(pageInfo.getAggregation(typeAggName));
        List<Map<String,Object>> specs = new ArrayList<>();
        //根据产品分类判断是否需要聚合
        if(types.size()==1) {
        	//如果产品分类只有一个才进行聚合，并根据分类与基本查询条件聚合
        	specs = getSpec(types.get(0).getId(),basicQuery);
        }
        // 封装结果并返回
        return new SearchResult(pageInfo.getTotalElements(), pageInfo.getTotalPages(), pageInfo.getContent(),brands,types,specs);
    }
    /**
     * 聚合出规格参数
     * @param id
     * @param basicQuery
     * @return
     */
    private List<Map<String, Object>> getSpec(String typeId, QueryBuilder query) {
		List<SpecParam> params = paramMapper.getSpecParams(typeId);
		List<Map<String,Object>> specs = new ArrayList<>();
		NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
		queryBuilder.withQuery(query);
		//聚合规格参数
		params.forEach(p->{
			String key = p.getName();
			queryBuilder.addAggregation(AggregationBuilders.terms(key).field("specs."+key+".keyword"));
		});
		//查询
		Map<String,Aggregation> aggs = this.elasticsearchTemplate.query(queryBuilder.build(), SearchResponse::getAggregations).asMap();
		//解析聚合结果
		params.forEach(param->{
			Map<String,Object> spec = new HashMap<>();
			String key = param.getName();
			spec.put("k", key);
			StringTerms terms = (StringTerms)aggs.get(key);
			spec.put("options", terms.getBuckets().stream().map(StringTerms.Bucket::getKeyAsString));
			specs.add(spec);
		});
		return specs;
	}
	private QueryBuilder buildBasicQuery(SearchRequest request) {
    	//构建布尔查询
        BoolQueryBuilder basicQuery = QueryBuilders.boolQuery();
        //搜索条件
        basicQuery.must(QueryBuilders.matchQuery("all", request.getKey()).operator(Operator.AND));
     // 过滤条件构建器
        BoolQueryBuilder filterQueryBuilder = QueryBuilders.boolQuery();
        //过滤条件
        Map<String, String> filterMap = request.getFilter();

        if (!CollectionUtils.isEmpty(filterMap)) {
            for (Map.Entry<String, String> entry : filterMap.entrySet()) {
                String key = entry.getKey();
                //判断key是否是分类或者品牌过滤条件
                if (!"typeId".equals(key) && !"brandId".equals(key)) {
                    key = "specs." + key + ".keyword";
                }
                //过滤条件
                String value = entry.getValue();
                //因为是keyword类型，使用terms查询
                filterQueryBuilder.filter(QueryBuilders.termQuery(key, value));
            }
        }
     // 添加过滤条件
        basicQuery.filter(filterQueryBuilder);
        return basicQuery;
	}
	private List<ProductType> getTypeAggResult(Aggregation aggregation) {
		
		StringTerms typeAgg = (StringTerms) aggregation;
		List<String> tids = new ArrayList<>();
		for(StringTerms.Bucket bucket:typeAgg.getBuckets()) {
			tids.add(bucket.getKeyAsString());
		}
		List<ProductType> types = new ArrayList<ProductType>();
		if(tids.size()>0) {
			types = typeMapper.getTypeByIds(tids);
		}
		
		return types;
	}
	//解析品牌聚合结果
	private List<ProductBrand> getBrandAggResult(Aggregation aggregation) {
		StringTerms brandAgg = (StringTerms) aggregation;
		List<String> bids = new ArrayList<>();
		for(StringTerms.Bucket bucket:brandAgg.getBuckets()) {
			bids.add(bucket.getKeyAsString());
		}
		
		List<ProductBrand> brands = new ArrayList<ProductBrand>();
		if(bids.size()>0) {
			brands = brandMapper.getBrandByIds(bids);
		}
		return brands;
	}

	public void loadData() {
		this.elasticsearchTemplate.createIndex(Goods.class);
		this.elasticsearchTemplate.putMapping(Goods.class);

		List<Product> products = productService.getProductByPage(null,null);
		List<Goods> goodsList = new ArrayList<Goods>();
		for(Product product:products) {
			try {
				Goods goods = this.buildGoods(product);
				goodsList.add(goods);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		this.goodsRepository.saveAll(goodsList);
	}

	public Goods buildGoods(Product product) throws IOException {
		Goods goods = new Goods();
		// 查询详情
		
		//查询sku
		List<ProductSku> skus = skuMapper.getSkuByProductId(product.getId());
		
		List<Map<String,Object>> skuList = new ArrayList<>();
		skus.forEach(sku->{
			Map<String,Object> skuMap = new HashMap<String,Object>();
			skuMap.put("id",sku.getId());
			skuMap.put("name", sku.getName());
			skuMap.put("image", StringUtils.isBlank(sku.getImages()) ? "" : StringUtils.split(sku.getImages(), ",")[0]);
			skuList.add(skuMap);
		});
		
		//查询规格参数，规格参数中分为通用规格参数和特有规格参数
		List<SpecParam> params = paramMapper.getSpecParams(product.getTypeId());
		
		//查询商品详情
        ProductDetail productDetail = detailMapper.getProductDetailById(product.getId());
		//获取通用规格参数
        Map<String, String> genericSpec = new HashMap<String,String>();
        if(StringUtils.isNotBlank(productDetail.getGenericSpec()))
        genericSpec = JsonUtils.toMap(productDetail.getGenericSpec(), String.class, String.class);
        //获取特有规格参数
        Map<String, List<String>> specialSpec = new HashMap<String,List<String>>();
        if(StringUtils.isNotBlank(productDetail.getSpecialSpec()))
        specialSpec = JsonUtils.nativeRead(productDetail.getSpecialSpec(), new TypeReference<Map<String, List<String>>>() {
        });
		//定义spec 对应map
		Map<String,Object> specsMap = new HashMap<String,Object>();
		//对规格进行遍历，并封装spec，其中spec的key是规格参数的名称，值是商品详情中的值
		for(SpecParam param:params) {
			String key = param.getName();
			Object value="";
			if(param.isGeneric()) {
				//参数是通用属性，通过规格参数的ID从商品详情存储的规格参数中查出值
				value = genericSpec.get(param.getId());
				if (param.isNumeric()) {
                    //参数是数值类型，处理成段，方便后期对数值类型进行范围过滤
                    value = chooseSegment(value.toString(), param);
                }
			}else {
				//参数不是通用类型
                value = specialSpec.get(param.getId());
			}
			value = (value == null ? "其他" : value);
			specsMap.put(key, value);
		}
		goods.setId(product.getId());
		goods.setName(product.getName());
		goods.setBrandId(product.getProductBrand().getId());
		goods.setTypeId(product.getTypeId());
		goods.setAll(product.getName());
		goods.setSkus(mapper.writeValueAsString(skuList));
		goods.setSpecs(specsMap);
		return goods;
	}
	/**
     * 将规格参数为数值型的参数划分为段
     *
     * @param value
     * @param p
     * @return
     */
    private String chooseSegment(String value, SpecParam p) {
        double val = NumberUtils.toDouble(value);
        String result = "其它";
        // 保存数值段
        for (String segment : p.getSegments().split(",")) {
            String[] segs = segment.split("-");
            // 获取数值范围
            double begin = NumberUtils.toDouble(segs[0]);
            double end = Double.MAX_VALUE;
            if (segs.length == 2) {
                end = NumberUtils.toDouble(segs[1]);
            }
            // 判断是否在范围内
            if (val >= begin && val < end) {
                if (segs.length == 1) {
                    result = segs[0] + p.getUnit() + "以上";
                } else if (begin == 0) {
                    result = segs[1] + p.getUnit() + "以下";
                } else {
                    result = segment + p.getUnit();
                }
                break;
            }
        }
        return result;
    }
}
