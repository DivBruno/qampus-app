import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Logout } from './logout';
import { AuthService } from '../auth-service';
import { Router } from '@angular/router';
import { vi } from 'vitest';

describe('Logout', () => {
  let component: Logout;
  let fixture: ComponentFixture<Logout>;

  let authServiceMock: {
    logout: ReturnType<typeof vi.fn>;
  };

  let routerMock: {
    navigate: ReturnType<typeof vi.fn>;
  };

  beforeEach(async () => {
    authServiceMock = {
      logout: vi.fn(),
    };

    routerMock = {
      navigate: vi.fn(),
    };

    await TestBed.configureTestingModule({
      imports: [Logout],
      providers: [
        {
          provide: AuthService,
          useValue: authServiceMock,
        },
        {
          provide: Router,
          useValue: routerMock,
        },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(Logout);
    component = fixture.componentInstance;

    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should call AuthService logout', async () => {
    authServiceMock.logout.mockResolvedValue(true);

    await component.logout();

    expect(authServiceMock.logout).toHaveBeenCalled();
  });

  it('should navigate to login when logout is successful', async () => {
    authServiceMock.logout.mockResolvedValue(true);

    await component.logout();

    expect(routerMock.navigate).toHaveBeenCalledWith(['/login']);
  });

  it('should show an alert when logout fails', async () => {
    authServiceMock.logout.mockResolvedValue(false);

    const alertMock = vi
      .spyOn(window, 'alert')
      .mockImplementation(() => {});

    await component.logout();

    expect(alertMock).toHaveBeenCalledWith('Erro ao realizar o logout');

    alertMock.mockRestore();
  });
});