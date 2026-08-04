import { Injectable } from '@angular/core';
import { User } from './user';
import { jwtDecode } from 'jwt-decode';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private apiUrl = "http://localhost:8080/auth";
  
  getToken(): string | null{
    return localStorage.getItem('token');
  }

  logout(): void {
    localStorage.removeItem('token');
  }

  isAuthenticated(): boolean{
    return !!this.getToken();
  }

  async register(user: User): Promise<User|null>{
    try{
      const response = await fetch(this.apiUrl, {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(user)
      })
      return await response.json();
    }catch(error){
      console.error('Error registering user: ', error);
      return null;
    }
  }

  async login(email: string, senha: string): Promise<boolean> {
    try {
      const response = await fetch(`${this.apiUrl}/login`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({
          email: email,
          senha: senha
        })
      });

      if (!response.ok) {
        return false;
      }

      const data = await response.json();

      localStorage.setItem('token', data.token);

      return true;

    } catch (error) {
      console.error('Error logging in: ', error);
      return false;
    }
  }

  hasRole(requiredRole: string): boolean{
    const token = this.getToken();
    if(!token){
      return false;
    }
    try{
      const decodedToken: any = jwtDecode(token);
      return decodedToken.role == requiredRole;
    }catch(error){
      console.error('Token error: ', error);
      return false;
    }
  }
}