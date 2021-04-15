import {Injectable} from '@angular/core';
import {BASE_API, CURRENT_USER, USER} from '../_globals/vars';
import {HttpClient} from '@angular/common/http';
import {Router} from '@angular/router';
import {BehaviorSubject} from 'rxjs';

@Injectable({providedIn: 'root'})
export class UserService {

  constructor(private http: HttpClient) {
  }
  getAllUsers() {
    return this.http.get(BASE_API + USER);
  }
  getCurrentUser() {
    return this.http.get(BASE_API + CURRENT_USER);
  }

  deleteUser(userId: number) {
    return this.http.delete(BASE_API + USER + '/' + userId);
  }
}
