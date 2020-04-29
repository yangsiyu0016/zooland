package com.zoo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zoo.model.search.Goods;
import com.zoo.service.SearchService;
import com.zoo.vo.PageResult;
import com.zoo.vo.SearchRequest;

@RestController
@RequestMapping("/search")
public class SearchController {
	@Autowired
	SearchService searchService;
	@PostMapping("page")
    public ResponseEntity<PageResult<Goods>> search(@RequestBody SearchRequest request) {
        PageResult<Goods> result = this.searchService.search(request);
        if (result == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(result);
    }
	/**
	 * 导入数据
	 */
	@PostMapping("loadData")
	public void loadData() {
		searchService.loadData();
	}
	
}
