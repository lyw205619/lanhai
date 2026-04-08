package com.liyiwei.lanhai.model.entity.product;

import com.liyiwei.lanhai.model.entity.base.BaseEntity;
import lombok.Data;

@Data
public class ProductDetails extends BaseEntity {

	private Long productId;
	private String imageUrls;

}