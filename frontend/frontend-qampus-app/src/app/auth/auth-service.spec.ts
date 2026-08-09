import { TestBed } from '@angular/core/testing';
import { AuthService } from './auth-service';
import { vi } from 'vitest';

describe('AuthService', () => {
  let service: AuthService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AuthService);

    localStorage.clear();
    vi.restoreAllMocks();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should return the token stored in localStorage', () => {
    localStorage.setItem('token', 'token-teste');

    expect(service.getToken()).toBe('token-teste');
  });

  it('should return null when there is no token', () => {
    expect(service.getToken()).toBeNull();
  });

  it('should return true when the user is authenticated', () => {
    localStorage.setItem('token', 'token-teste');

    expect(service.isAuthenticated()).toBe(true);
  });

  it('should return false when the user is not authenticated', () => {
    expect(service.isAuthenticated()).toBe(false);
  });

  it('should remove the token when logout is successful', async () => {
    localStorage.setItem('token', 'token-teste');

    vi.spyOn(globalThis, 'fetch').mockResolvedValue(
      new Response(null, { status: 200 })
    );

    const result = await service.logout();

    expect(result).toBe(true);
    expect(localStorage.getItem('token')).toBeNull();
  });

  it('should not remove the token when logout fails', async () => {
    localStorage.setItem('token', 'token-teste');

    vi.spyOn(globalThis, 'fetch').mockResolvedValue(
      new Response(null, { status: 401 })
    );

    const result = await service.logout();

    expect(result).toBe(false);
    expect(localStorage.getItem('token')).toBe('token-teste');
  });

  it('should login successfully and store the token', async () => {
    const token = 'token-teste';

    vi.spyOn(globalThis, 'fetch').mockResolvedValue(
      new Response(
        JSON.stringify({ token }),
        {
          status: 200,
          headers: { 'Content-Type': 'application/json' }
        }
      )
    );

    const result = await service.login('teste@email.com', '123456');

    expect(result).toBe(true);
    expect(localStorage.getItem('token')).toBe(token);
  });

  it('should return false when login fails', async () => {
    vi.spyOn(globalThis, 'fetch').mockResolvedValue(
      new Response(null, { status: 401 })
    );

    const result = await service.login('teste@email.com', 'senha-errada');

    expect(result).toBe(false);
    expect(localStorage.getItem('token')).toBeNull();
  });

  it('should return false when login request throws an error', async () => {
    vi.spyOn(globalThis, 'fetch').mockRejectedValue(
      new Error('Erro de conexão')
    );

    const result = await service.login('teste@email.com', '123456');

    expect(result).toBe(false);
    expect(localStorage.getItem('token')).toBeNull();
  });
});
