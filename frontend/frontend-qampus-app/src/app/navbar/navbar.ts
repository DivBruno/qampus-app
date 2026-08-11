import { Component } from '@angular/core';
import { AuthService } from '../auth/auth-service';
import { Router } from '@angular/router';
import { HostListener } from '@angular/core';

@Component({
  selector: 'app-navbar',
  imports: [],
  templateUrl: './navbar.html',
  styleUrl: './navbar.css',
})

export class Navbar {
  constructor(
    private authService: AuthService,
    private router: Router
  ){}

  menuAberto = false;
  
  toggleMenu(){
    this.menuAberto = !this.menuAberto;
  }

  @HostListener('document:click', ['$event'])
  onDocumentClick(event: MouseEvent){
    const target = event.target as HTMLElement;
    if(!target.closest('.user-menu')){
      this.menuAberto = false;
    }
  }

  async logout(){
    const resposta = await this.authService.logout();
    if(resposta){
      this.router.navigate(['/login']);
    }else{
      alert("Erro ao realizar o logout");
    }
  }

  goTo(rota: string){
    this.router.navigate([rota]);
  }
}
