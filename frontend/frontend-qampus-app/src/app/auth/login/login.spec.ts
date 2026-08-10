import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Login } from './login';
import { AuthService } from '../auth-service';
import { Router } from '@angular/router';
import { vi } from 'vitest';

describe('Login', () => {
  let component: Login;
  let fixture: ComponentFixture<Login>;

  let authServiceMock: {
    login: ReturnType<typeof vi.fn>;
  };

  let routerMock: {
    navigate: ReturnType<typeof vi.fn>;
  };

  beforeEach(async () => {
    authServiceMock = {
      login: vi.fn(),
    };

    routerMock = {
      navigate: vi.fn(),
    };

    await TestBed.configureTestingModule({
      imports: [Login],
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

    fixture = TestBed.createComponent(Login);
    component = fixture.componentInstance;

    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should call AuthService login with email and password', async () => {
    authServiceMock.login.mockResolvedValue(true);

    component.email = 'teste@email.com';
    component.password = '123456';

    await component.onSubmit();

    expect(authServiceMock.login).toHaveBeenCalledWith(
      'teste@email.com',
      '123456'
    );
  });

  it('should navigate to home when login is successful', async () => {
    authServiceMock.login.mockResolvedValue(true);

    component.email = 'teste@email.com';
    component.password = '123456';

    await component.onSubmit();

    expect(routerMock.navigate).toHaveBeenCalledWith(['home']);
  });

  it('should show an alert when login fails', async () => {
    authServiceMock.login.mockResolvedValue(false);

    const alertMock = vi
      .spyOn(window, 'alert')
      .mockImplementation(() => {});

    component.email = 'teste@email.com';
    component.password = 'senha-errada';

    await component.onSubmit();

    expect(alertMock).toHaveBeenCalledWith('Email ou Senha Inválidos');

    alertMock.mockRestore();
  });

  it('should navigate to the specified route', () => {
    component.goTo('registrar');

    expect(routerMock.navigate).toHaveBeenCalledWith(['registrar']);
  });
});