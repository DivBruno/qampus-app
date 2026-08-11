import { TestBed } from '@angular/core/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { CanActivateFn } from '@angular/router';
import { AuthService } from './auth-service';
import { authGuard } from './auth-guard';

describe('authGuard', () => {
  let authService: {
    isAuthenticated: ReturnType<typeof vi.fn>;
    hasRole: ReturnType<typeof vi.fn>;
  }
  let router: {
    createUrlTree: ReturnType<typeof vi.fn>;
  };
  const executeGuard: CanActivateFn = (...guardParameters) =>
    TestBed.runInInjectionContext(() => authGuard(...guardParameters));

  beforeEach(() => {
    authService = {
      isAuthenticated: vi.fn(),
      hasRole: vi.fn()
    };

    router = {
      createUrlTree: vi.fn()
    };
    TestBed.configureTestingModule({
      providers: [
        {
          provide: AuthService,
          useValue: authService
        },
        {
          provide: Router,
          useValue: router
        }
      ]
    });
  });

  it('should redirect to login when not authenticated', () => {
    authService.isAuthenticated.mockReturnValue(false);

    const urlTree = {} as any;
    router.createUrlTree.mockReturnValue(urlTree);

    const route = {
      data: {}
    } as ActivatedRouteSnapshot;
    const result = TestBed.runInInjectionContext(()=>
      authGuard(route, {} as any)
    )

    expect(result).toBe(urlTree);
    expect(router.createUrlTree).toHaveBeenCalledWith(['/login']);
  });
  
  it('should redirect to unauthorized when user has the wrong role', ()=>{
    authService.isAuthenticated.mockReturnValue(true);
    authService.hasRole.mockReturnValue(false);

    const urlTree = {} as any;
    router.createUrlTree.mockReturnValue(urlTree);

    const route = {
      data: {
        role: "STUDENT"
      }
    } as unknown as ActivatedRouteSnapshot;
    const result = TestBed.runInInjectionContext(()=>
      authGuard(route, {} as any)
    )

    expect(result).toBe(urlTree);
    expect(authService.hasRole).toHaveBeenCalledWith('STUDENT');
    expect(router.createUrlTree).toHaveBeenCalledWith(['/unauthorized']);
  })

  it('should return true when authenticated and role is the required', ()=>{
    authService.isAuthenticated.mockReturnValue(true);
    authService.hasRole.mockReturnValue(true);

     const route = {
      data: {
        role: "STUDENT"
      }

    } as unknown as ActivatedRouteSnapshot;
    const result = TestBed.runInInjectionContext(()=>
      authGuard(route, {} as any)
    )

    expect(result).toBe(true);
    expect(authService.hasRole).toHaveBeenCalledWith('STUDENT');
  })
});
