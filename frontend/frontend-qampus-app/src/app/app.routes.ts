import { Routes } from '@angular/router';
import { Login } from './auth/login/login';
import { Logout } from './logout/logout';
import { Register } from './auth/register/register';
import { Unauthorized } from './auth/unauthorized/unauthorized';

export const routes: Routes = [
  {
    path: 'login',
    component: Login
  },
  {
    path: 'logout',
    component: Logout
  },
  {
    path: 'registrar',
    component: Register
  },
  {
    path: 'unauthorized',
    component: Unauthorized
  }
];