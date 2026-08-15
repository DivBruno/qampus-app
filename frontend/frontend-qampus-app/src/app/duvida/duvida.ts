import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { Navbar } from '../navbar/navbar';
import { Post, PostService } from '../post/post-service';

@Component({
  selector: 'app-duvida',
  imports: [Navbar, FormsModule],
  templateUrl: './duvida.html',
  styleUrl: './duvida.css',
})
export class Duvida implements OnInit {

  post: Post | null = null;

  novaResposta = '';

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private postService: PostService
  ) {}

  async ngOnInit() {
    const id = this.route.snapshot.paramMap.get('id');

    if (!id) {
      return;
    }

    try {
      this.post = await this.postService.findById(id);
    } catch (error) {
      console.error('Erro ao carregar dúvida:', error);
    }
  }

  votar(valor: number): void {
    if (!this.post) {
      return;
    }

    if (valor > 0) {
      this.post.upVotes++;
    } else {
      this.post.downVotes++;
    }
  }

  responder(): void {
    if (!this.novaResposta.trim()) {
      alert('O conteúdo da resposta é obrigatório.');
      return;
    }

    console.log('Resposta:', this.novaResposta);

    this.novaResposta = '';
  }

  visualizarRelacionada(id: string): void {
    this.router.navigate(['/duvida', id]);
  }
}