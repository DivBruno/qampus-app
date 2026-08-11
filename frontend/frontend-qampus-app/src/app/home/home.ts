import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { Navbar } from "../navbar/navbar";

interface Duvida {
  id: number;
  titulo: string;
  votos: number;
  respostas: number;
  tags: string[];
}

@Component({
  selector: 'app-home',
  imports: [Navbar],
  templateUrl: './home.html',
  styleUrl: './home.css',
})
export class Home {

  duvidas: Duvida[] = [
    {
      id: 1,
      titulo: 'Como funciona a matrícula nas disciplinas?',
      votos: 12,
      respostas: 4,
      tags: ['CURSO', 'TURMA 1']
    },
    {
      id: 2,
      titulo: 'Quando começam as aulas do próximo período?',
      votos: 8,
      respostas: 2,
      tags: ['CURSO', 'TURMA 2']
    },
    {
      id: 3,
      titulo: 'Como acessar o material das disciplinas?',
      votos: 15,
      respostas: 6,
      tags: ['CURSO', 'TURMA 1']
    }
  ];

  constructor(
    private router: Router
  ) {}

  fazerPergunta(): void {
    this.router.navigate(['/duvida/criar']);
  }

  visualizarDuvida(id: number): void {
    this.router.navigate(['/duvida', id]);
  }
}