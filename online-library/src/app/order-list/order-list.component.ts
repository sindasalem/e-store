import { Component, OnInit } from '@angular/core';
import {OrderService} from '../_services/order.service';

@Component({
  selector: 'app-order-list',
  templateUrl: './order-list.component.html',
  styleUrls: ['./order-list.component.css']
})
export class OrderListComponent implements OnInit {

  orders = [];
  constructor(private orderService: OrderService) { }

  ngOnInit() {
    this.getOrders();
  }

  getOrders() {
    this.orderService.getAllOrders().subscribe(
      (orders: any) => {
        this.orders = orders;
        console.log(orders);
      }, (error) => {
        console.log(error);
      }, () => {

      }
    );
  }

}
