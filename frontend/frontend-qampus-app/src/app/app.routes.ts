import { Routes } from '@angular/router';
import { Login } from './auth/login/login';
import { Logout } from './auth/logout/logout';
import { Register } from './auth/register/register';
import { Unauthorized } from './auth/unauthorized/unauthorized';
import { authGuard } from './auth/auth-guard';

export const routes: Routes = [
  {
    path: 'login',
    component: Login
  },
  {
    path: 'home',
    component: Logout,
    canActivate: [authGuard],
  },
  {
    path: 'registrar',
    component: Register
  },
  {
    path: 'unauthorized',
    component: Unauthorized
  },
  {
    path: '',
    redirectTo: 'login',
    pathMatch: 'full'
  }
];