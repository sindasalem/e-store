import { Component, OnInit } from '@angular/core';
import {OrderService} from '../_services/order.service';
import {ActivatedRoute} from '@angular/router';
import {BASE_API, PICTURES} from '../_globals/vars';

@Component({
  selector: 'app-order-details',
  templateUrl: './order-details.component.html',
  styleUrls: ['./order-details.component.css']
})
export class OrderDetailsComponent implements OnInit {

  products = [];
  BASE_API = BASE_API;
  PICTURES = PICTURES;
  orderId;
  constructor(private orderService: OrderService,
              private route: ActivatedRoute) { }

  ngOnInit() {
    this.route.params.subscribe(params => {
      console.log(params.id);
      this.orderId = params.id;
      this.getAllProductsByOrderId(params.id);
    });
  }

  getAllProductsByOrderId(orderId) {
    this.orderService.getAllProductsByOrderId(orderId).subscribe(
      (products: any) => {
        this.products = products;
        console.log(products);

      }, (error) => {
        console.log(error);
      }
    );
  }
}
