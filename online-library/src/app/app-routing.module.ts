import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {HomeComponent} from './home/home.component';
import {LoginRegisterComponent} from './login-register/login-register.component';
import {ListBooksComponent} from './list-books/list-books.component';
import {ShoppingBagComponent} from './shopping-bag/shopping-bag.component';
import {UserListComponent} from './user/user-list/user-list.component';
import {SignUpComponent} from './sign-up/sign-up.component';
import {AuthGuard} from './_guards/auth.guard';
import {UserAddComponent} from './user/user-add/user-add.component';
import {AddBookComponent} from './add-book/add-book.component';
import {OrderListComponent} from './order-list/order-list.component';
import {OrderDetailsComponent} from './order-details/order-details.component';


const routes: Routes = [
  {
    path: '',
    canActivate: [AuthGuard],
    component: HomeComponent,
  },
  {
    path: 'sign-in',
    component: LoginRegisterComponent,
  },
  {
    path: 'sign-up',
    component: SignUpComponent,
  },
  {
    path: 'user-list',
    canActivate: [AuthGuard],
    component: UserListComponent,
  },
  {
    path: 'user-add',
    component: UserAddComponent,
  },
  {
    path: 'order-list',
    component: OrderListComponent,
  },
  {
    path: 'order-details/:id',
    component: OrderDetailsComponent,
  },
  {
    path: 'book-add',
    component: AddBookComponent ,
  },
  {
    path: 'list-books',
    canActivate: [AuthGuard],
    component: ListBooksComponent,
  },
  {
    path: 'shopping-bag',
    canActivate: [AuthGuard],
    component: ShoppingBagComponent,
  },

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
