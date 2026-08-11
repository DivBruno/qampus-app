import { Routes } from '@angular/router';
import { Login } from './auth/login/login';
import { Register } from './auth/register/register';
import { Unauthorized } from './auth/unauthorized/unauthorized';
import { authGuard } from './auth/auth-guard';
import { CreatePost } from './create-post/create-post';
import { Home } from './home/home';
import { Duvida } from './duvida/duvida';

export const routes: Routes = [
  {
    path: 'login',
    component: Login
  },
  {
    path: 'home',
    component: Home,
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
  },
  {
    path: 'duvida/criar',
    component: CreatePost,
    canActivate: [authGuard],
//    data: {role: 'STUDENT'}
  },
  {
    path: 'duvida/:id',
    component: Duvida,
    canActivate: [authGuard],
  },

];