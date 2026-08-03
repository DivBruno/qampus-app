import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivateFn, Router } from '@angular/router';
import { AuthService } from './auth-service';

export const authGuard: CanActivateFn = (route: ActivatedRouteSnapshot, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  const requiredRole: string | undefined = route.data?.['role'];

  if(!authService.isAuthenticated()){
    return router.createUrlTree(['/login']);
  }
  if(requiredRole && !authService.hasRole(requiredRole)){
    return router.createUrlTree(['/unauthorized']);
  }
  return true;
};
