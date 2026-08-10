import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { Navbar } from "../navbar/navbar";

interface Resposta {
  id: number;
  texto: string;
}

interface DuvidaRelacionada {
  id: number;
  titulo: string;
  votos: number;
}

@Component({
  selector: 'app-duvida',
  imports: [Navbar],
  templateUrl: './duvida.html',
  styleUrl: './duvida.css',
})
export class Duvida {

  titulo = 'Dúvida 1';

  descricao = `
    Lorem ipsum dolor sit amet. Vel error officiis ut internos accusamus 
    omnis odio ea praesentium dolor et deleniti esse et perspiciatis 
    voluptates. Ut illum consequatur qui similique ducimus rem nihil 
    excepturi a nostrum harum in nulla ipsam eos dolore molestias sit 
    vero sint. Non voluptas dignissimos ut doloremque velit ab commodi 
    dolores est delectus omnis id fugit fugiat rem sapiente facilis ea 
    numquam numquam.
  `;

  votos = 0;

  tags = ['Tag', 'Tag'];

  respostas: Resposta[] = [
    {
      id: 1,
      texto: 'Lorem ipsum dolor sit amet. Vel error officiis ut internos accusamus sed omnis odio ea praesentium dolor et deleniti esse et perspiciatis voluptates.'
    },
    {
      id: 2,
      texto: 'Lorem ipsum dolor sit amet. Vel error officiis ut internos accusamus sed omnis odio ea praesentium dolor et deleniti esse et perspiciatis voluptates.'
    },
    {
      id: 3,
      texto: 'Lorem ipsum dolor sit amet. Vel error officiis ut internos accusamus sed omnis odio ea praesentium dolor et deleniti esse et perspiciatis voluptates.'
    }
  ];

  relacionadas: DuvidaRelacionada[] = [
    {
      id: 2,
      titulo: 'Dúvida relacionada à atual',
      votos: 4
    },
    {
      id: 3,
      titulo: 'Dúvida relacionada à atual',
      votos: 7
    },
    {
      id: 4,
      titulo: 'Dúvida relacionada à atual',
      votos: 2
    },
    {
      id: 5,
      titulo: 'Dúvida relacionada à atual',
      votos: 5
    }
  ];

  constructor(
    private router: Router
  ) {}

  votar(valor: number): void {
    this.votos += valor;
  }

  visualizarRelacionada(id: number): void {
    this.router.navigate(['/duvida', id]);
  }
}