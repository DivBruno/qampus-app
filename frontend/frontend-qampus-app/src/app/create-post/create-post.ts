import { Component } from '@angular/core';
import { AuthService } from '../auth/auth-service';
import { Router } from '@angular/router';
import { HostListener } from '@angular/core';
import { FormsModule } from '@angular/forms';


@Component({
  selector: 'app-create-post',
  imports: [FormsModule],
  templateUrl: './create-post.html',
  styleUrl: './create-post.css',
})
export class CreatePost {
  constructor(
    private authService: AuthService,
    private router: Router
  ){}

  menuAberto = false;
  novaTag = false;
  nomeTag = '';
  quantidadeTags = 0;
  tags: string[] = [];
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

  abrirNovaTag() {
    this.novaTag = true;
  }

  fecharNovaTag() {
    this.novaTag = false;
  }

  adicionarTag() {
      if (this.nomeTag.trim() !== '') {
          this.tags.push(this.nomeTag.trim());
          console.log(this.nomeTag);
          this.nomeTag = '';
          this.novaTag = false;
      }
  }
}
