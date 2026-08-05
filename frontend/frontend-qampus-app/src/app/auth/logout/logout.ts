import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../auth/auth-service';

@Component({
  selector: 'app-logout',
  imports: [],
  templateUrl: './logout.html',
  styleUrl: './logout.css',
})
export class Logout {

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  async logout() {
    const resposta = await this.authService.logout();
    if(resposta){
      this.router.navigate(['/login']);
    }else{
      alert("Erro ao realizar o logout");
    }
  }
}