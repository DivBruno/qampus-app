import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { Navbar } from '../../navbar/navbar';
import { Post, PostService, Answer } from '../post-service';

@Component({
  selector: 'app-duvida',
  imports: [Navbar, FormsModule],
  templateUrl: './duvida.html',
  styleUrl: './duvida.css',
})
export class Duvida implements OnInit {

  post: Post | null = null;

  novaResposta = '';

  respostas: Answer[] = [];

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private postService: PostService,
    private cdr: ChangeDetectorRef
  ) {}

  async ngOnInit() {
    const id = this.route.snapshot.paramMap.get('id');

    if (!id) {
      return;
    }

    try {
      this.post = await this.postService.findById(id);
      this.cdr.detectChanges();
    } catch (error) {
      console.error('Erro ao carregar dúvida:', error);
    }
  }

  async votar(valor: number): Promise<void> {
    if (!this.post) {
      return;
    }

    try {
      const postAtualizado =
        valor > 0
          ? await this.postService.upvotePost(this.post.id)
          : await this.postService.downvotePost(this.post.id);

      this.post = postAtualizado;
      this.cdr.detectChanges();
    } catch (error) {
      console.error('Erro ao votar na dúvida:', error);
    }
  }

  async responder(): Promise<void> {
    if (!this.novaResposta.trim()) {
      alert('O conteúdo da resposta é obrigatório.');
      return;
    }

    if (!this.post) {
      return;
    }

    try {
      const resposta = await this.postService.createAnswer(
        this.post.id,
        this.novaResposta
      );

      this.respostas.push(resposta);
      this.novaResposta = '';
    } catch (error) {
      console.error('Erro ao responder dúvida:', error);
      alert('Erro ao enviar resposta.');
    }
  }

  async votarResposta(resposta: Answer, valor: number): Promise<void> {
    if (!this.post) {
      return;
    }

    try {
      const respostaAtualizada =
        valor > 0
          ? await this.postService.upvoteAnswer(this.post.id, resposta.id)
          : await this.postService.downvoteAnswer(this.post.id, resposta.id);

      const index = this.respostas.findIndex(
        r => r.id === resposta.id
      );

      if (index !== -1) {
        this.respostas[index] = respostaAtualizada;
      }
    } catch (error) {
      console.error('Erro ao votar na resposta:', error);
    }
  }

  editarDuvida(): void {
    this.router.navigate(['/post/editar', this.post?.id]);
  }

  visualizarRelacionada(id: string): void {
    this.router.navigate(['/duvida', id]);
  }
}