import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../auth/auth-service';
import { PostService, Post } from '../post/post-service';

@Component({
  selector: 'app-home',
  imports: [],
  templateUrl: './home.html',
  styleUrl: './home.css',
})
export class Home {

  duvidas: Post[] = [];

  constructor(
    private authService: AuthService,
    private router: Router,
    private postService: PostService
  ) {}

  async ngOnInit() {
    try {
      this.duvidas = await this.postService.findAll();
    } catch (error) {
      console.error('Erro ao carregar dúvidas:', error);
      alert('Erro ao carregar dúvidas');
    }
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }

  fazerPergunta(): void {
    this.router.navigate(['/criar-duvida']);
  }

  visualizarDuvida(id: string): void {
    this.router.navigate(['/duvida', id]);
  }
}