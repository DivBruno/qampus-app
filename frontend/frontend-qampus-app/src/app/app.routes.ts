import { Routes } from '@angular/router';
import { Register } from './auth/register/register';
import { Unauthorized } from './auth/unauthorized/unauthorized';

export const routes: Routes = [
    {
        path: 'registrar',
        component: Register
    },
    {
        path: 'unauthorized',
        component: Unauthorized
    }
];
