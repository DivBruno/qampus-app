import { TestBed } from '@angular/core/testing';
import { AuthService } from './auth-service';
import { vi } from 'vitest';
import { User } from './user';
import * as jwtDecodeModule from 'jwt-decode';

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

  it('should register successfully and store the token', async () => {
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

    const user: User = {
        name: "Nome de Teste",
        email: "teste@email.com",
        password: "senha123",
        role: "STUDENT"
      }

    const result = await service.register(user);

    expect(result).toBe(true);
    expect(localStorage.getItem('token')).toBe(token);
  });

  it('should return false when register fails', async()=>{
    vi.spyOn(globalThis, 'fetch').mockResolvedValue(
      new Response(null, {status: 401})
    );
    const user: User = {
        name: "Nome de Teste",
        email: "teste@email.com",
        password: "senha123",
        role: "STUDENT"
      }
    const result = await service.register(user);
    expect(result).toBe(false);
    expect(localStorage.getItem('token')).toBeNull();
  })

  it('should return false when register request throws an error', async () => {
    vi.spyOn(globalThis, 'fetch').mockRejectedValue(
      new Error('Erro de conexão')
    );
    const user: User = {
        name: "Nome de Teste",
        email: "teste@email.com",
        password: "senha123",
        role: "STUDENT"
      }
    const result = await service.register(user);

    expect(result).toBe(false);
    expect(localStorage.getItem('token')).toBeNull();
  });

  it('should return true when the token has the required role', ()=>{
    const token = createFakeToken({
      role: 'STUDENT'
    });
    localStorage.setItem('token', token);

    expect(service.hasRole('STUDENT')).toBe(true);
    localStorage.removeItem('token');
  })

  it('should return false when the role is not the required', ()=>{
    const token = createFakeToken({
      role: 'TEACHER'
    });
    localStorage.setItem('token', token);

    expect(service.hasRole('STUDENT')).toBe(false);
    localStorage.removeItem('token');
  })

  it('should return false when there is no token', ()=>{
    expect(service.hasRole('STUDENT')).toBe(false);
  })

  it('should return false when the token is invalid', ()=>{
    vi.spyOn(service, 'getToken').mockReturnValue('token-invalido');
    expect(service.hasRole('STUDENT')).toBe(false);
  })
});

function createFakeToken(payload: any): string{
  const header = btoa(JSON.stringify({
    alg: 'none',
    typ: 'JWT'
  }));
  const encodedPayload = btoa(JSON.stringify(payload));
  return `${header}.${encodedPayload}.fake`; 
}