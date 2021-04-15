import { Component, OnInit } from '@angular/core';
import {Router} from '@angular/router';
import {UserService} from '../_services/user.service';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit {
  userName: string;
  connected: boolean;
  currentUser: any;
  isAdmin: boolean;

  constructor(private router: Router,
              private userService: UserService) { }

  ngOnInit() {
    this.getCurrentUser();
    if (localStorage.getItem('currentUser') === null) {
      this.connected = false;
    } else {

      this.userName = JSON.parse(localStorage.getItem('currentUser')).username;
      this.connected = true;
    }
  }

  SignOut() {
    localStorage.removeItem('currentUser');
    this.router.navigate(['/sign-in']);
  }

  getCurrentUser() {
    this.userService.getCurrentUser().subscribe(
      (currentUser: any) => {
        this.currentUser = currentUser;
      }, (error) => {
        console.log(error);
      }, () => {

      }
    );
  }
}
