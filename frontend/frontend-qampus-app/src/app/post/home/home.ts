import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../auth/auth-service';
import { PostService, Post } from '../post-service';
import { Navbar } from "../../navbar/navbar";
import { ChangeDetectorRef } from '@angular/core';

@Component({
  selector: 'app-home',
  imports: [Navbar],
  templateUrl: './home.html',
  styleUrl: './home.css',
})
export class Home implements OnInit{

  duvidas: Post[] = [];

  constructor(
    private authService: AuthService,
    private router: Router,
    private postService: PostService,
    private cdr: ChangeDetectorRef
  ) {}

  async ngOnInit() {
    try {
      this.duvidas = await this.postService.findAll();
      this.cdr.detectChanges();
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
    this.router.navigate(['/post/criar']);
  }

  visualizarDuvida(id: string): void {
    this.router.navigate(['/post', id]);
  }
}