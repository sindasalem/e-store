package com.quest.etna.repositories;

import com.quest.etna.model.OrderProduct;
import com.quest.etna.model.OrderProductPK;
import org.springframework.data.repository.CrudRepository;

public interface OrderProductRepository extends CrudRepository<OrderProduct, OrderProductPK> {
}
