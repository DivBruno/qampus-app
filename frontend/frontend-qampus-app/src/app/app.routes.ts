import { Routes } from '@angular/router';
import { Login } from './auth/login/login';
import { Logout } from './logout/logout';

export const routes: Routes = [
  {
    path: 'login',
    component: Login
  },

  {
    path: 'logout',
    component: Logout
  }
];