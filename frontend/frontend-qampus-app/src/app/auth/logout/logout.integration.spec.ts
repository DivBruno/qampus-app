import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Logout } from './logout';
import { AuthService } from '../auth-service';
import { Router } from '@angular/router';
import { vi } from 'vitest';

describe('Logout + AuthService Integration', () => {
  let component: Logout;
  let fixture: ComponentFixture<Logout>;

  let routerMock: {
    navigate: ReturnType<typeof vi.fn>;
  };

  beforeEach(async () => {
    routerMock = {
      navigate: vi.fn(),
    };

    await TestBed.configureTestingModule({
      imports: [Logout],
      providers: [
        AuthService,
        {
          provide: Router,
          useValue: routerMock,
        },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(Logout);
    component = fixture.componentInstance;

    await fixture.whenStable();

    localStorage.clear();
    vi.restoreAllMocks();
  });

  it('should logout through AuthService and navigate to login', async () => {
    localStorage.setItem('token', 'token-integracao');

    const fetchMock = vi.spyOn(globalThis, 'fetch').mockResolvedValue({
      ok: true,
    } as Response);

    await component.logout();

    expect(fetchMock).toHaveBeenCalledWith(
      expect.stringContaining('/logout'),
      expect.objectContaining({
        method: 'POST',
        headers: expect.objectContaining({
          Authorization: 'Bearer token-integracao',
        }),
      })
    );

    expect(localStorage.getItem('token')).toBeNull();

    expect(routerMock.navigate).toHaveBeenCalledWith(['/login']);
  });

  it('should keep the token and show an alert when logout fails', async () => {
    localStorage.setItem('token', 'token-integracao');

    vi.spyOn(globalThis, 'fetch').mockResolvedValue({
      ok: false,
    } as Response);

    const alertMock = vi
      .spyOn(window, 'alert')
      .mockImplementation(() => {});

    await component.logout();

    expect(localStorage.getItem('token')).toBe('token-integracao');

    expect(routerMock.navigate).not.toHaveBeenCalledWith(['/login']);

    expect(alertMock).toHaveBeenCalledWith(
      'Erro ao realizar o logout'
    );

    alertMock.mockRestore();
  });
});