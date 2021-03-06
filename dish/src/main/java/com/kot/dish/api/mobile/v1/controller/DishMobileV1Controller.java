package com.kot.dish.api.mobile.v1.controller;

import java.util.List;
import java.util.stream.Collectors;
import com.kot.dish.api.ApiInfo;
import com.kot.dish.api.mobile.v1.dto.DishMobileV1Request;
import com.kot.dish.api.mobile.v1.dto.DishMobileV1Response;
import com.kot.dish.api.mobile.v1.mapper.DishMobileV1APIMapper;
import com.kot.dish.bll.model.Dish;
import com.kot.dish.bll.service.DishService;
import com.kot.dish.intercomm.category.CategoryClient;
import com.kot.dish.intercomm.category.CategoryResponseModel;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(DishMobileV1Controller.API_URL)
@Tag(name = ApiInfo.DISH_MOBILE_API)
public class DishMobileV1Controller {

	public static final String API_URL = ApiInfo.API_PREFIX + ApiInfo.MOBILE_API_VERSION_V1 + ApiInfo.DISH_ENDPOINT;

	@Autowired
	private DishService dishService;

	@Autowired
	private DishMobileV1APIMapper dishAPIMapper;

	@Autowired
	private CategoryClient categoryClient;

	@GetMapping("/status")
	public ResponseEntity<?> getStatus() {
		return new ResponseEntity<>("Works!", HttpStatus.OK);
	}

	@GetMapping("/categories")
	public ResponseEntity<?> getCategories() {
		List<CategoryResponseModel> categories = categoryClient.getCategories();
		return new ResponseEntity<>(categories, HttpStatus.OK);
	}

	@PostMapping("/")
	public ResponseEntity<DishMobileV1Response> create(@RequestBody DishMobileV1Request request) {
		Dish model = dishService.save(dishAPIMapper.dtoToModel(request));
		return new ResponseEntity<>(dishAPIMapper.modelToDto(model), HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<DishMobileV1Response> getById(@PathVariable Long id) {
		Dish model = dishService.findById(id);
		return new ResponseEntity<>(dishAPIMapper.modelToDto(model), HttpStatus.OK);
	}

	@GetMapping("/")
	public ResponseEntity<List<DishMobileV1Response>> getAll() {
		List<Dish> dishEntities = dishService.findAll().getContent();
		List<DishMobileV1Response> dishResponses = dishEntities
				.stream()
				.map(model -> dishAPIMapper.modelToDto(model))
				.collect(Collectors.toList());
		return new ResponseEntity<>(dishResponses, HttpStatus.OK);
	}

}
