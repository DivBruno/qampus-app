import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../auth/auth-service';

interface Duvida {
  id: number;
  titulo: string;
  respostas: number;
  tags: string[];
}

@Component({
  selector: 'app-home',
  imports: [],
  templateUrl: './home.html',
  styleUrl: './home.css',
})
export class Home {

  duvidas: Duvida[] = [
    {
      id: 1,
      titulo: 'Como funciona a matrícula nas disciplinas?',
      respostas: 4,
      tags: ['CURSO', 'TURMA 1']
    },
    {
      id: 2,
      titulo: 'Quando começam as aulas do próximo período?',
      respostas: 2,
      tags: ['CURSO', 'TURMA 2']
    },
    {
      id: 3,
      titulo: 'Como acessar o material das disciplinas?',
      respostas: 6,
      tags: ['CURSO', 'TURMA 1']
    }
  ];

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }

  fazerPergunta(): void {
    this.router.navigate(['/criar-duvida']);
  }

  visualizarDuvida(id: number): void {
    this.router.navigate(['/duvida', id]);
  }
}